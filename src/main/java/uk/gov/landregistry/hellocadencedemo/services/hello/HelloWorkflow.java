package uk.gov.landregistry.hellocadencedemo.services.hello;

import com.uber.cadence.workflow.WorkflowMethod;

public interface HelloWorkflow {
    @WorkflowMethod
    String getGreeting(String name);
}
