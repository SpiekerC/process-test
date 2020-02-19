package com.dvelop.bpm.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTransientTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @Deployment(resources = "transient-test.bpmn")
    public void testWithLocal() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("transient-test");

        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(instance.getId())
                .activityId("receive")
                .singleResult();

        assertNotNull(execution);

        TypedValue variable = runtimeService.getVariableLocalTyped(execution.getId(), "transientLocal");
        assertNull("Local variable should be null since it is transient", variable);

        runtimeService.signal(execution.getId());

    }

    @Test
    @Deployment(resources = "transient-test.bpmn")
    public void testWithNonLocal() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("transient-test");

        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(instance.getId())
                .activityId("receive")
                .singleResult();

        assertNotNull(execution);

        TypedValue variable = runtimeService.getVariableTyped(execution.getId(), "transient");
        assertNull("Variable should be null since it is transient", variable);

        runtimeService.signal(execution.getId());

    }
}
