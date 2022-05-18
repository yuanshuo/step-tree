package com.shon.step.test.demostep;

import com.shon.step.core.anotation.Step;
import com.shon.step.core.anotation.StepErrorCode;
import com.shon.step.core.template.AbstractStep;

@Step(desc = "关冰箱门")
@StepErrorCode
public class s3_CloseIcebox extends AbstractStep<DemoStepContext> {
    @Override
    protected void execute(DemoStepContext context) {

    }
}
