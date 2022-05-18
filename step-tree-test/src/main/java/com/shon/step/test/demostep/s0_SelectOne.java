package com.shon.step.test.demostep;

import java.util.List;

import com.shon.step.core.anotation.Step;
import com.shon.step.core.anotation.StepErrorCode;
import com.shon.step.core.template.AbstractStep;
import com.shon.step.test.demo.ErrorCodeEnum;

@Step(desc = "挑选一只大象")
@StepErrorCode(errorCodeEnum = ErrorCodeEnum.class)
public class s0_SelectOne extends AbstractStep<DemoStepContext> {
    @Override
    protected void execute(DemoStepContext context) {
        Integer test = 1;

        System.out.println(ErrorCodeEnum.TEST_ERROR_CODE);
    }
}
