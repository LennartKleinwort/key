/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package de.uka.ilkd.key.rule;

import de.uka.ilkd.key.java.JavaTools;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.java.SourceElement;
import de.uka.ilkd.key.java.reference.ExecutionContext;
import de.uka.ilkd.key.java.reference.ReferencePrefix;
import de.uka.ilkd.key.java.statement.SetStatement;
import de.uka.ilkd.key.logic.*;
import de.uka.ilkd.key.logic.op.*;
import de.uka.ilkd.key.logic.sort.Sort;
import de.uka.ilkd.key.parser.Location;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.OpReplacer;
import de.uka.ilkd.key.speclang.TermReplacementMap;
import de.uka.ilkd.key.speclang.jml.translation.Context;
import de.uka.ilkd.key.speclang.jml.translation.JMLSpecFactory;
import de.uka.ilkd.key.speclang.jml.translation.ProgramVariableCollection;
import de.uka.ilkd.key.speclang.njml.JmlIO;
import de.uka.ilkd.key.speclang.translation.SLTranslationException;
import org.jspecify.annotations.NonNull;
import org.key_project.util.collection.ImmutableList;

/**
 * A rule for set statements. This unwraps the contained CopyAssignment
 *
 * @author Julian Wiesler
 */
public final class SetStatementRule implements BuiltInRule {

    /**
     * The instance
     */
    public static final SetStatementRule INSTANCE = new SetStatementRule();
    /**
     * The name of this rule
     */
    private static final Name name = new Name("Set Statement");

    private SetStatementRule() {
        // no statements
    }

    @Override
    public boolean isApplicable(Goal goal, PosInOccurrence occurrence) {
        if (AbstractAuxiliaryContractRule.occursNotAtTopLevelInSuccedent(occurrence)) {
            return false;
        }
        // abort if inside of transformer
        if (Transformer.inTransformer(occurrence)) {
            return false;
        }

        Term target = occurrence.subTerm();
        if (target.op() instanceof UpdateApplication) {
            target = UpdateApplication.getTarget(target);
        }
        final SourceElement activeStatement = JavaTools.getActiveStatement(target.javaBlock());
        return activeStatement instanceof SetStatement;
    }

    @Override
    public boolean isApplicableOnSubTerms() {
        return false;
    }

    @Override
    public IBuiltInRuleApp createApp(PosInOccurrence occurrence, TermServices services) {
        return new SetStatementBuiltInRuleApp(this, occurrence);
    }

    @NonNull
    @Override
    public ImmutableList<Goal> apply(Goal goal, Services services, RuleApp ruleApp)
            throws RuleAbortException {
        if (!(ruleApp instanceof SetStatementBuiltInRuleApp)) {
            throw new IllegalArgumentException("Can only apply SetStatementBuiltInRuleApp");
        }

        final TermBuilder tb = services.getTermBuilder();
        final PosInOccurrence occurrence = ruleApp.posInOccurrence();
        final Term formula = occurrence.subTerm();
        assert formula.op() instanceof UpdateApplication :
                "Currently, this can only be applied if there is an update application in front of the modality";

        Term update = UpdateApplication.getUpdate(formula);
        Term target = UpdateApplication.getTarget(formula);

        try {
            var setStatement = (SetStatement) JavaTools.getActiveStatement(target.javaBlock());
            ExecutionContext exCtx = JavaTools.getInnermostExecutionContext(target.javaBlock(), services);
            ReferencePrefix prefix = exCtx.getRuntimeInstance();

            Term self = tb.var((ProgramVariable) prefix);

            IProgramMethod pm = exCtx.getMethodContext();
            var processor = new SetStatementProcessor(setStatement, services, (ProgramMethod) pm, self);

            var lhs = processor.computeTarget();
            var rhs = processor.computeValue();

            Term newUpdate = tb.elementary(lhs, rhs);
            JavaBlock javaBlock = JavaTools.removeActiveStatement(target.javaBlock(), services);
            Term newTerm = tb.apply(update,
                    tb.apply(newUpdate, services.getTermFactory().createTerm(target.op(), target.subs(), target.boundVars(), javaBlock, target.getLabels())));

            ImmutableList<Goal> result = goal.split(1);
            result.head().changeFormula(new SequentFormula(newTerm), occurrence);
            return result;

        } catch (ClassCastException e) {
            throw new RuleAbortException("Not a set statement");
        } catch (SLTranslationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Name name() {
        return name;
    }

    @Override
    public String displayName() {
        return name.toString();
    }

    @Override
    public String toString() {
        return name.toString();
    }
}


class SetStatementProcessor {
    final SetStatement statement;
    private final Term assignee;
    private final Term value;
    private final ProgramVariableCollection vars;
    private final Services services;
    private final OpReplacer replacer;

    SetStatementProcessor(SetStatement statement, Services services, ProgramMethod pm, Term self) throws SLTranslationException {
        this.statement = statement;
        this.services = services;
        var specFac = new JMLSpecFactory(services);
        this.vars = specFac.createProgramVariablesForStatement(statement, pm);

        var setStatementContext = statement.getParserContext();
        var io = new JmlIO(services).context(Context.inMethod(pm, services.getTermBuilder()))
                .selfVar(vars.selfVar)
                .parameters(vars.paramVars)
                .resultVariable(vars.resultVar).exceptionVariable(vars.excVar).atPres(vars.atPres)
                .atBefore(vars.atBefores);
        this.assignee = io.translateTerm(setStatementContext.assignee);
        var value = io.translateTerm(setStatementContext.value);

        if (value.sort() == Sort.FORMULA) {
            value = services.getTermBuilder().convertToBoolean(value);
        }
        this.value = value;

        String error = specFac.checkSetStatementAssignee(assignee);
        if (error != null) {
            throw new SLTranslationException(
                    "Invalid assignment target for set statement: " + error,
                    Location.fromToken(setStatementContext.start));
        }
        this.replacer = getReplacer(self);
    }

    @NonNull
    private OpReplacer getReplacer(Term self) {
        var termFactory = services.getTermFactory();
        var replacementMap = new TermReplacementMap(termFactory);
        if (self != null) {
            replacementMap.replaceSelf(vars.selfVar, self, services);
        }
        replacementMap.replaceRemembranceLocalVariables(vars.atPreVars, vars.atPres, services);
        replacementMap.replaceRemembranceLocalVariables(vars.atBeforeVars, vars.atBefores, services);
        return new OpReplacer(replacementMap, termFactory, services.getProof());
    }


    Term computeValue() {
        return replacer.replace(value);
    }

    Term computeTarget() {
        return replacer.replace(assignee);
    }

}