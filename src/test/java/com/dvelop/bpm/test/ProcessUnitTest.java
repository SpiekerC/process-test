package com.dvelop.bpm.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessUnitTest {

    @Rule
    public ProcessEngineRule    processEngineRule      = new ProcessEngineRule();

    @Test
    @Deployment(resources = "base-test-with-io.bpmn")
    public void testWithIo() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("base-test-with-io");
      
        assertThat(runtimeService.getVariable(instance.getId(), "variable"), is("base"));
        Execution execution1 = runtimeService.createExecutionQuery().activityId("Wait-1").singleResult();
        assertThat(runtimeService.getVariable(execution1.getId(), "variable"), is("1"));
        Execution execution2 = runtimeService.createExecutionQuery().activityId("Wait-2").singleResult();
        assertThat(runtimeService.getVariable(execution2.getId(), "variable"), is("2"));
        
        runtimeService.deleteProcessInstance(instance.getId(), "Test ended");
    }
    
    @Test
    @Deployment(resources = "base-test.bpmn")
    public void test() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("base-test");

        assertThat(runtimeService.getVariable(instance.getId(), "variable"), is("base"));
        Execution execution1 = runtimeService.createExecutionQuery().activityId("Wait-1").singleResult();
        assertThat(runtimeService.getVariable(execution1.getId(), "variable"), is("1"));
        Execution execution2 = runtimeService.createExecutionQuery().activityId("Wait-2").singleResult();
        assertThat(runtimeService.getVariable(execution2.getId(), "variable"), is("2"));
        
        runtimeService.deleteProcessInstance(instance.getId(), "Test ended");
    }

    @Test
    @Deployment(resources = "base-test-with-multi-instance-and-io.bpmn")
    public void testWithMultiInstanceAndIo() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("base-test-with-multi-instance-and-io");

        assertThat(runtimeService.getVariable(instance.getId(), "variable"), is("base"));
        List<Execution> executions = runtimeService.createExecutionQuery().activityId("Wait").list();
        executions.stream().forEach((ex) -> {
            Object variable = runtimeService.getVariable(ex.getId(), "instance");
            assertThat(runtimeService.getVariable(ex.getId(), "variable"), is(variable.toString()));            
        }); 
        
        runtimeService.deleteProcessInstance(instance.getId(), "Test ended");
    }
    
    @Test
    @Deployment(resources = "base-test-with-multi-instance.bpmn")
    public void testWithMultiInstance() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("base-test-with-multi-instance");

        assertThat(runtimeService.getVariable(instance.getId(), "variable"), is("base"));
        List<Execution> executions = runtimeService.createExecutionQuery().activityId("Wait").list();
        executions.stream().forEach((ex) -> {
            Object variable = runtimeService.getVariable(ex.getId(), "instance");
            assertThat(runtimeService.getVariable(ex.getId(), "variable"), is(variable.toString()));            
        }); 
        
        runtimeService.deleteProcessInstance(instance.getId(), "Test ended");
    }
}
