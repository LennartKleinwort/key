package de.uka.ilkd.key.java.ast.ccatch;

import java.util.List;

import de.uka.ilkd.key.java.ast.Comment;
import de.uka.ilkd.key.java.ast.JavaNonTerminalProgramElement;
import de.uka.ilkd.key.java.ast.PositionInfo;

/**
 * A "non-standard" parameter declaration of a Ccatch clause, e.g., "\Return".
 *
 * @author Dominic Steinhöfel
 */
public abstract class CcatchNonstandardParameterDeclaration extends JavaNonTerminalProgramElement {

    public CcatchNonstandardParameterDeclaration(PositionInfo pi, List<Comment> c) {
        super(pi, c);
    }

    public CcatchNonstandardParameterDeclaration() {

    }
}