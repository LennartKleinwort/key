package de.uka.ilkd.key.java.ast;

import de.uka.ilkd.key.java.ast.declaration.Modifier;

import org.key_project.util.collection.ImmutableArray;


/**
 * Declaration. taken from COMPOST and changed to achieve an immutable structure
 */
public interface Declaration extends NonTerminalProgramElement {

    /**
     * Get the modifiers.
     *
     * @return the (original) list of modifiers wrapped .
     */
    ImmutableArray<Modifier> getModifiers();
}