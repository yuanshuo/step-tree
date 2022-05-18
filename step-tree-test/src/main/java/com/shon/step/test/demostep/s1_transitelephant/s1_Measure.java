package com.shon.step.test.demostep.s1_transitelephant;

import com.shon.step.core.anotation.Step;
import com.shon.step.core.anotation.StepErrorCode;
import com.shon.step.test.demo.ErrorCodeEnum;
import com.shon.step.test.demostep.DemoStepContext;
import com.shon.step.core.template.AbstractStep;
import com.shon.step.test.util.CommonUtil;

@Step(desc = "测量尺寸")
@StepErrorCode(errorCodeEnum = ErrorCodeEnum.class)
public class s1_Measure extends AbstractStep<DemoStepContext> {

    private CommonUtil commonUtil = new CommonUtil();
    @Override
    protected void execute(DemoStepContext context) {
        commonUtil.util();
        System.out.println("s1_Measure");
    }
}
