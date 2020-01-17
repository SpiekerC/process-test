package com.dvelop.bpm.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessNPETest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @Deployment(resources = "npe-test.bpmn")
    public void testWithIo() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("npe-test");

        ExecutorService executors = Executors.newFixedThreadPool(2);
        ArrayList<Future<?>> futures = new ArrayList<>();

        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(instance.getId())
                .activityId("receive")
                .singleResult();
        
        assertNotNull(execution);

        futures.add(executors.submit(() -> {
            runtimeService.signal(execution.getId());
        }));
        
        futures.add(executors.submit(() -> {
           runtimeService.deleteProcessInstance(instance.getId(), "Test ended");
        }));
        
        futures.forEach(t -> {
            try {
                t.get();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
        });

    }

}
