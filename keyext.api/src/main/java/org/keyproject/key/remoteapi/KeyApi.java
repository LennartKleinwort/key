package org.keyproject.key.remoteapi;

import de.uka.ilkd.key.gui.Example;
import de.uka.ilkd.key.gui.ExampleChooser;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface KeyApi extends KeyExampleApi, KeyMetaApi {
    //region general server management
    /**
     * Shutdown Request (:leftwards_arrow_with_hook:)
     The shutdown request is sent from the client to the server. It asks the server to shut down, but to not exit (otherwise the response might not be delivered correctly to the client). There is a separate exit notification that asks the server to exit. Clients must not send any notifications other than exit or requests to a server to which they have sent a shutdown request. Clients should also wait with sending the exit notification until they have received a response from the shutdown request.

     If a server receives requests after a shutdown request those requests should error with InvalidRequest.

     Request:

     method: ‘shutdown’
     params: none
     Response:

     result: null
     error: code and message set in case an exception happens during shutdown request.
     */
    @JsonRequest
    Void shutdown();

    /**
     * Exit Notification (:arrow_right:)
     * A notification to ask the server to exit its process. The server should exit with success code 0 if the shutdown request has been received before; otherwise with error code 1.
     * <p>
     * Notification:
     * <p>
     * method: ‘exit’
     * params: none
     */
    @JsonNotification
    void exit();



    interface SetTraceParams {
        /**
         * The new value that should be assigned to the trace setting.
         */
        TraceValue value = null;
    }
    /**
     * SetTrace Notification
     * A notification that should be used by the client to modify the trace setting of the server.
     */
    @JsonNotification
    void setTrace(SetTraceParams params);
    //endregion
}