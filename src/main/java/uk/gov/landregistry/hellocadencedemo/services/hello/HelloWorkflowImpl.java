package uk.gov.landregistry.hellocadencedemo.services.hello;

import com.uber.cadence.workflow.Workflow;

import static java.time.Duration.ofSeconds;

public class HelloWorkflowImpl implements HelloWorkflow {
    @Override
    public String getGreeting(String name) {
        Workflow.sleep(ofSeconds(10));
        return "Hello " + name + "!";
    }
}
