package uk.gov.landregistry.hellocadencedemo.services.hello;

import com.uber.cadence.workflow.WorkflowMethod;

public interface HelloWorkflow {
    @WorkflowMethod(executionStartToCloseTimeoutSeconds = 300)
    String getGreeting(String name);
}
