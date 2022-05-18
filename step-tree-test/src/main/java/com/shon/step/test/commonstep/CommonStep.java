package com.shon.step.test.commonstep;

import com.shon.step.core.anotation.StepErrorCode;
import com.shon.step.core.template.AbstractStep;
import com.shon.step.core.template.StepContext;
import com.shon.step.test.demo.ErrorCodeEnum;

/**
 * @author: shiye.ys
 * @date: 2022/5/18
 */
@StepErrorCode(errorCodeEnum = "ErrorCodeEnum")
public class CommonStep extends AbstractStep {
    @Override
    protected void execute(StepContext context) {
        System.out.println(ErrorCodeEnum.TEST_ERROR_CODE2);
    }
}
