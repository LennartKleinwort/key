package de.uka.ilkd.key.java.transformations;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.uka.ilkd.key.java.transformations.pipeline.*;

import com.github.javaparser.ast.CompilationUnit;

/**
 * @author Alexander Weigl
 * @version 1 (11/7/21)
 */
public class KeYJavaPipeline {
    private final TransformationPipelineServices pipelineServices;
    private final List<JavaTransformer> steps = new LinkedList<>();

    public KeYJavaPipeline(TransformationPipelineServices pipelineServices) {
        this.pipelineServices = pipelineServices;
    }

    public TransformationPipelineServices getPipelineServices() {
        return pipelineServices;
    }

    public List<JavaTransformer> getSteps() {
        return steps;
    }

    public static KeYJavaPipeline createDefault(TransformationPipelineServices pipelineServices) {
        KeYJavaPipeline p = new KeYJavaPipeline(pipelineServices);
        // new EnumClassBuilder(pipelineServices),
        p.add(new JMLCommentTransformer(pipelineServices));
        p.add(new JMLTransformer(pipelineServices));
        p.add(new ImplicitFieldAdder(pipelineServices));
        p.add(new InstanceAllocationMethodBuilder(pipelineServices));
        p.add(new ConstructorNormalformBuilder(pipelineServices));
        p.add(new ClassPreparationMethodBuilder(pipelineServices));
        p.add(new ClassInitializeMethodBuilder(pipelineServices));
        p.add(new PrepareObjectBuilder(pipelineServices));
        p.add(new CreateBuilder(pipelineServices));
        p.add(new CreateObjectBuilder(pipelineServices));
        p.add(new LocalClassTransformation(pipelineServices));
        p.add(new ConstantStringExpressionEvaluator(pipelineServices));
        return p;
    }

    public void add(JavaTransformer step) {
        steps.add(step);
    }

    public void apply() {
        apply(pipelineServices.getCache().getUnits());
    }

    public void apply(Collection<CompilationUnit> compilationUnits) {
        for (JavaTransformer step : steps) {
            for (CompilationUnit compilationUnit : compilationUnits) {
                step.apply(compilationUnit);
            }
        }
    }
}