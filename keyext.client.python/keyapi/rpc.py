import enum
import json
import sys
import threading
from io import TextIOWrapper
from typing import Dict

JSON_RPC_REQ_FORMAT = "Content-Length: {json_string_len}\r\n\r\n{json_string}"
LEN_HEADER = "Content-Length: "
TYPE_HEADER = "Content-Type: "


class MyEncoder(json.JSONEncoder):
    """
    Encodes an object in JSON
    """

    def default(self, o):  # pylint: disable=E0202
        return o.__dict__


class ResponseError(Exception):
    def __init__(self, error_code, message):
        super().__init__(message)
        self.error_code = error_code


class ErrorCodes(enum.Enum):
    ParseError = 1


class JsonRpcEndpoint(object):
    '''
    Thread safe JSON RPC endpoint implementation. Responsible to recieve and send JSON RPC messages, as described in the
    protocol. More information can be found: https://www.jsonrpc.org/
    '''

    def __init__(self, stdin, stdout):
        self.stdin = stdin
        self.stdout = stdout
        self.read_lock = threading.Lock()
        self.write_lock = threading.Lock()

    @staticmethod
    def __add_header(json_string):
        '''
        Adds a header for the given json string

        :param str json_string: The string
        :return: the string with the header
        '''
        return JSON_RPC_REQ_FORMAT.format(json_string_len=len(json_string), json_string=json_string)

    def send_request(self, message):
        '''
        Sends the given message.

        :param dict message: The message to send.
        '''
        json_string = json.dumps(message, cls=MyEncoder)
        jsonrpc_req = self.__add_header(json_string)
        with self.write_lock:
            self.stdin.write(jsonrpc_req.encode())
            self.stdin.flush()

    def recv_response(self):
        '''
        Recives a message.

        :return: a message
        '''
        with self.read_lock:
            message_size = None
            while True:
                # read header
                line = self.stdout.readline()
                if not line:
                    # server quit
                    return None
                line = line.decode("utf-8")
                if not line.endswith("\r\n"):
                    raise ResponseError(ErrorCodes.ParseError, "Bad header: missing newline")
                # remove the "\r\n"
                line = line[:-2]
                if line == "":
                    # done with the headers
                    break
                elif line.startswith(LEN_HEADER):
                    line = line[len(LEN_HEADER):]
                    if not line.isdigit():
                        raise ResponseError(ErrorCodes.ParseError,
                                            "Bad header: size is not int")
                    message_size = int(line)
                elif line.startswith(TYPE_HEADER):
                    # nothing todo with type for now.
                    pass
                else:
                    raise ResponseError(ErrorCodes.ParseError, "Bad header: unkown header")
            if not message_size:
                raise ResponseError(ErrorCodes.ParseError, "Bad header: missing size")

            jsonrpc_res = self.stdout.read(message_size).decode("utf-8")
            return json.loads(jsonrpc_res)


class LspEndpoint(threading.Thread):
    def __init__(self, json_rpc_endpoint: JsonRpcEndpoint, method_callbacks=None, notify_callbacks=None, timeout=2):
        super().__init__()
        self.json_rpc_endpoint: JsonRpcEndpoint = json_rpc_endpoint
        self.notify_callbacks: Dict = notify_callbacks or {}
        self.method_callbacks: Dict = method_callbacks or {}
        self.event_dict = {}
        self.response_dict = {}
        self.next_id = 0
        self._timeout = timeout
        self.shutdown_flag = False

    def handle_result(self, rpc_id, result, error):
        self.response_dict[rpc_id] = (result, error)
        cond = self.event_dict[rpc_id]
        cond.acquire()
        cond.notify()
        cond.release()

    def stop(self):
        self.shutdown_flag = True

    def run(self):
        while not self.shutdown_flag:
            try:
                jsonrpc_message = self.json_rpc_endpoint.recv_response()
                if jsonrpc_message is None:
                    print("server quit")
                    break
                method = jsonrpc_message.get("method")
                result = jsonrpc_message.get("result")
                error = jsonrpc_message.get("error")
                rpc_id = jsonrpc_message.get("id")
                params = jsonrpc_message.get("params")

                if method:
                    if rpc_id:
                        # a call for method
                        if method not in self.method_callbacks:
                            raise ResponseError(ErrorCodes.MethodNotFound,
                                                "Method not found: {method}".format(method=method))
                        result = self.method_callbacks[method](params)
                        self.send_response(rpc_id, result, None)
                    else:
                        # a call for notify
                        if method not in self.notify_callbacks:
                            # Have nothing to do with this.
                            print("Notify method not found: {method}.".format(method=method))
                        else:
                            self.notify_callbacks[method](params)
                else:
                    self.handle_result(rpc_id, result, error)
            except ResponseError as e:
                self.send_response(rpc_id, None, e)

    def send_response(self, id, result, error):
        message_dict = {"jsonrpc": "2.0", "id": id}
        if result:
            message_dict["result"] = result
        if error:
            message_dict["error"] = error
        self.json_rpc_endpoint.send_request(message_dict)

    def send_message(self, method_name, params, id=None):
        message_dict = {}
        message_dict["jsonrpc"] = "2.0"
        if id is not None:
            message_dict["id"] = id
        message_dict["method"] = method_name
        message_dict["params"] = params
        self.json_rpc_endpoint.send_request(message_dict)

    def call_method(self, method_name, **kwargs):
        current_id = self.next_id
        self.next_id += 1
        cond = threading.Condition()
        self.event_dict[current_id] = cond

        cond.acquire()
        self.send_message(method_name, kwargs, current_id)
        if self.shutdown_flag:
            return None

        if not cond.wait(timeout=self._timeout):
            raise TimeoutError()
        cond.release()

        self.event_dict.pop(current_id)
        result, error = self.response_dict.pop(current_id)
        if error:
            raise ResponseError(error.get("code"), error.get("message"), error.get("data"))
        return result

    def send_notification(self, method_name, **kwargs):
        self.send_message(method_name, kwargs)