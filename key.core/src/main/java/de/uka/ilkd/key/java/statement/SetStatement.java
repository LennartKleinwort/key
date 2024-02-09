/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package de.uka.ilkd.key.java.statement;

import de.uka.ilkd.key.java.PositionInfo;
import de.uka.ilkd.key.java.ProgramElement;
import de.uka.ilkd.key.java.visitor.Visitor;
import de.uka.ilkd.key.speclang.njml.JmlParser;

/**
 * JML set statement
 *
 * @author Julian Wiesler
 */
public class SetStatement extends JavaStatement {
    /**
     * The parser context of the statement produced during parsing.
     */
    private final JmlParser.Set_statementContext context;

    /**
     * Constructor used in recoderext
     */
    public SetStatement(JmlParser.Set_statementContext context, PositionInfo positionInfo) {
        super(positionInfo);
        this.context = context;
    }

    /**
     * Constructor used when cloning
     */
    public SetStatement(SetStatement copyFrom) {
        this.context = copyFrom.context;
    }

    /**
     * Removes the attached parser context from this set statement
     *
     * @return the parser context that was attached
     */
    public JmlParser.Set_statementContext getParserContext() {
        return context;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Visitor v) {
        v.performActionOnSetStatement(this);
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public ProgramElement getChildAt(int index) {
        throw new IndexOutOfBoundsException("SetStatement has no program children");
    }

}
