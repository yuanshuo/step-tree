package com.shon.step.test.demostep;

import com.shon.step.core.anotation.Step;
import com.shon.step.core.anotation.StepErrorCode;
import com.shon.step.core.template.AbstractStep;
import com.shon.step.test.commonstep.CommonStep;
import com.shon.step.test.demo.ErrorCodeEnum;

@Step(desc = "关冰箱门")
@StepErrorCode(errorCodeEnum = ErrorCodeEnum.class)
public class s3_CloseIcebox extends CommonStep {

}
