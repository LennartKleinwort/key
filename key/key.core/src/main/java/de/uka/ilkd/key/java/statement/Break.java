// This file is part of KeY - Integrated Deductive Software Design
//
// Copyright (C) 2001-2011 Universitaet Karlsruhe (TH), Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
// Copyright (C) 2011-2014 Karlsruhe Institute of Technology, Germany
//                         Technical University Darmstadt, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General
// Public License. See LICENSE.TXT for details.
//

package de.uka.ilkd.key.java.statement;

import de.uka.ilkd.key.java.Comment;
import de.uka.ilkd.key.java.Label;
import de.uka.ilkd.key.java.PositionInfo;
import de.uka.ilkd.key.java.PrettyPrinter;
import de.uka.ilkd.key.java.visitor.Visitor;
import org.key_project.util.ExtList;

import java.util.List;

public class Break extends LabelJumpStatement {
    public Break(PositionInfo pi, List<Comment> comments, Label name) {
        super(pi, comments, name);
    }

    public Break() {
        super(null, null, null);
    }

    /**
     * Break.
     *
     * @param label a name for the label.
     */
    public Break(Label label) {
        super(null, null, label);
    }

    /**
     * Constructor for the transformation of COMPOST ASTs to KeY.
     *
     * @param children the children of this AST element as KeY classes.
     *                 May contain: Comments,
     *                 a ProgramElementName (as label of the label jump statement)
     */
    public Break(ExtList children) {
        super(children);
    }

    /**
     * calls the corresponding method of a visitor in order to
     * perform some action/transformation on this element
     *
     * @param v the Visitor
     */
    public void visit(Visitor v) {
        v.performActionOnBreak(this);
    }

    public void prettyPrint(PrettyPrinter p) throws java.io.IOException {
        p.printBreak(this);
    }
}