package de.uka.ilkd.key.java.ast.expression.operator.adt;

import java.util.List;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.java.ast.Comment;
import de.uka.ilkd.key.java.ast.Expression;
import de.uka.ilkd.key.java.ast.PositionInfo;
import de.uka.ilkd.key.java.ast.abstraction.KeYJavaType;
import de.uka.ilkd.key.java.ast.abstraction.PrimitiveType;
import de.uka.ilkd.key.java.ast.expression.Operator;
import de.uka.ilkd.key.java.ast.reference.ExecutionContext;
import de.uka.ilkd.key.java.visitor.Visitor;

import org.key_project.util.ExtList;
import org.key_project.util.collection.ImmutableArray;

public class SeqSub extends Operator {

    public SeqSub(PositionInfo pi, List<Comment> c, Expression... child) {
        super(pi, c, new ImmutableArray<>(child));
    }

    public SeqSub(ExtList changeList) {
        super(changeList);
    }


    @Override
    public int getPrecedence() {
        return 0;
    }


    @Override
    public int getNotation() {
        return PREFIX;
    }


    @Override
    public void visit(Visitor v) {
        v.performActionOnSeqSub(this);
    }


    @Override
    public int getArity() {
        return 3;
    }


    @Override
    public KeYJavaType getKeYJavaType(Services javaServ, ExecutionContext ec) {
        // bugfix, this used to return the join for the the first two arguments'
        // types.
        return javaServ.getJavaInfo().getKeYJavaType(PrimitiveType.JAVA_SEQ);
    }
}