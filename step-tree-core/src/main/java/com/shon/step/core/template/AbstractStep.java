package com.shon.step.core.template;

/**
 * 步骤抽象
 */
public abstract class AbstractStep<T extends StepContext> {
    protected abstract void execute(T context);

}
