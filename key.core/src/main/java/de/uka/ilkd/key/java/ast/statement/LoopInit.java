/**
 * This class encapsulates initializers of a for loop
 */

package de.uka.ilkd.key.java.ast.statement;

import de.uka.ilkd.key.java.ast.JavaNonTerminalProgramElement;
import de.uka.ilkd.key.java.ast.LoopInitializer;
import de.uka.ilkd.key.java.ast.PositionInfo;
import de.uka.ilkd.key.java.ast.ProgramElement;
import de.uka.ilkd.key.java.ast.Statement;
import de.uka.ilkd.key.java.ast.StatementContainer;
import de.uka.ilkd.key.java.visitor.Visitor;

import org.key_project.util.ExtList;
import org.key_project.util.collection.ImmutableArray;

public class LoopInit extends JavaNonTerminalProgramElement
        implements StatementContainer, ILoopInit {

    final ImmutableArray<LoopInitializer> inits;

    public LoopInit(ImmutableArray<LoopInitializer> exprarr) {
        inits = exprarr;
    }

    public LoopInit(LoopInitializer[] exprarr) {
        inits = new ImmutableArray<>(exprarr);
    }

    public LoopInit(ExtList ups, PositionInfo pos) {
        super(pos);
        final LoopInitializer[] exps = new LoopInitializer[ups.size()];
        for (int i = 0; i < exps.length; i++) {
            exps[i] = (LoopInitializer) ups.get(i);
        }
        inits = new ImmutableArray<>(exps);
    }


    /**
     * Get the number of statements in this container.
     *
     * @return the number of statements.
     */
    public int getStatementCount() {
        return inits.size();
    }

    /*
     * Return the statement at the specified index in this node's "virtual" statement array.
     *
     * @param index an index for an statement.
     *
     * @return the statement with the given index.
     *
     * @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out of bounds.
     */
    public Statement getStatementAt(int index) {
        return inits.get(index);
    }

    public int size() {
        return getStatementCount();
    }

    public ImmutableArray<LoopInitializer> getInits() {
        return inits;
    }

    public void visit(Visitor v) {
        v.performActionOnLoopInit(this);
    }

    public int getChildCount() {
        return getStatementCount();
    }

    public ProgramElement getChildAt(int index) {
        return getStatementAt(index);
    }

}