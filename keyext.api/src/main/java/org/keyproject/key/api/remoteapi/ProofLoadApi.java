package org.keyproject.key.api.remoteapi;

import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.keyproject.key.api.data.KeyIdentifications;
import org.keyproject.key.api.data.KeyIdentifications.EnvironmentId;
import org.keyproject.key.api.data.KeyIdentifications.ProofId;
import org.keyproject.key.api.data.LoadParams;
import org.keyproject.key.api.data.ProblemDefinition;

import java.util.concurrent.CompletableFuture;

/**
 * Functionalities for loading proofs either from a built-in example, or from a set of files.
 *
 * @author Alexander Weigl
 * @since v1
 */
@JsonSegment("loading")
public interface ProofLoadApi {
    /**
     * I am not sure whether this is helpful. Mainly a feature for testing?!
     * @param id
     * @return
     */
    @JsonRequest
    CompletableFuture<ProofId> loadExample(String id);

    /**
     *
     */
    @JsonRequest
    CompletableFuture<ProofId> loadProblem(ProblemDefinition problem);

    /**
     *
     */
    @JsonRequest
    CompletableFuture<ProofId> loadKey(String content);

    @JsonRequest
    CompletableFuture<ProofId> loadTerm(String term);

    /**
     * Test!
     *
     * @param params parameters for loading
     * @return
     * @throws ProblemLoaderException if something went wrong
     */
    @JsonRequest
    CompletableFuture<Either<EnvironmentId,ProofId>> load(LoadParams params) throws ProblemLoaderException;
}