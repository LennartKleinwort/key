package de.uka.ilkd.key.java.loader;


import java.net.URI;
import javax.annotation.Nullable;

import de.uka.ilkd.key.java.Position;

import com.github.javaparser.ast.Node;

/**
 * @author Alexander Weigl
 * @version 1 (16.04.23)
 */
public class JavaBuildingIssue {
    private final Node node;
    private final String message;

    public JavaBuildingIssue(String message, Node node) {
        this.message = message;
        this.node = node;
    }

    public String getMessage() {
        return message;
    }

    public Position getPosition() {
        return Position.fromOneZeroBased(getLine(), getColumn());
    }

    @Nullable
    public URI getPath() {
        return node.findCompilationUnit()
                .flatMap(it -> it.getStorage()).map(it -> it.getPath().toUri()).orElse(null);
    }

    public int getLine() {
        return node.getRange().map(it -> it.begin).map(it -> it.line).orElse(-1);
    }

    public int getColumn() {
        return node.getRange().map(it -> it.begin).map(it -> it.column).orElse(-1);
    }

    @Override
    public String toString() {
        return "JavaBuildingIssue{" +
            "node=" + node +
            "range=" + node.getRange() +
            ", message='" + message + '\'' +
            '}';
    }
}