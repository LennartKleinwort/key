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

package de.uka.ilkd.key.java.declaration;

import de.uka.ilkd.key.java.*;
import de.uka.ilkd.key.java.reference.TypeReference;
import de.uka.ilkd.key.java.reference.TypeReferenceContainer;
import org.key_project.util.ExtList;
import org.key_project.util.collection.ImmutableArray;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Inheritance specification.
 *
 * @author <TT>AutoDoc</TT>
 */

public abstract class InheritanceSpecification
        extends JavaNonTerminalProgramElement implements TypeReferenceContainer {

    protected final ImmutableArray<TypeReference> supertypes;

    public InheritanceSpecification(PositionInfo pi, List<Comment> comments, ImmutableArray<TypeReference> supertypes) {
        super(pi, comments);
        this.supertypes = supertypes;
    }

    public InheritanceSpecification() {
        this(null, null, null);
    }

    /**
     * Inheritance specification.
     *
     * @param supertype a type reference.
     */

    public InheritanceSpecification(TypeReference supertype) {
        this(null, null, new ImmutableArray<>(supertype));
    }

    /**
     * Inheritance specification.
     *
     * @param supertypes a type reference mutable list.
     */

    public InheritanceSpecification(TypeReference[] supertypes) {
        this(null, null, new ImmutableArray<>(supertypes));
    }

    /**
     * Inheritance specification.
     *
     * @param children the ExtList may include: a Comment
     *                 several TypeReference (as references to the supertypes)
     *                 a Comment
     */
    protected InheritanceSpecification(ExtList children) {
        super(children);
        this.supertypes = new ImmutableArray<>(children.collect(TypeReference.class));
    }


    @Override
    @Nonnull
    public SourceElement getLastElement() {
        if (supertypes == null) {
            return this;
        }
        return supertypes.get(supertypes.size() - 1);
    }


    /**
     * Returns the number of children of this node.
     *
     * @return an int giving the number of children of this node
     */

    @Override
    public int getChildCount() {
        int result = 0;
        if (supertypes != null) result += supertypes.size();
        return result;
    }

    /**
     * Returns the child at the specified index in this node's "virtual"
     * child array
     *
     * @param index an index into this node's "virtual" child array
     * @return the program element at the given position
     * @throws ArrayIndexOutOfBoundsException if <tt>index</tt> is out
     *                                        of bounds
     */

    @Override
    public ProgramElement getChildAt(int index) {
        if (supertypes != null) {
            return supertypes.get(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Get supertypes.
     *
     * @return the type reference array wrapper.
     */

    public ImmutableArray<TypeReference> getSupertypes() {
        return supertypes;
    }

    /**
     * Get the number of type references in this container.
     *
     * @return the number of type references.
     */

    @Override
    public int getTypeReferenceCount() {
        return (supertypes != null) ? supertypes.size() : 0;
    }

    /*
      Return the type reference at the specified index in this node's
      "virtual" type reference array.
      @param index an index for a type reference.
      @return the type reference with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */

    @Override
    public TypeReference getTypeReferenceAt(int index) {
        if (supertypes != null) {
            return supertypes.get(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}