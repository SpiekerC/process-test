package com.dvelop.bpm.test;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.StringValue;

public class SetTransientVariableDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        StringValue transientVariable = Variables.stringValue("some value", true);
        execution.setVariable("transient", transientVariable);
        execution.setVariableLocal("transientLocal", transientVariable);
    }

}
