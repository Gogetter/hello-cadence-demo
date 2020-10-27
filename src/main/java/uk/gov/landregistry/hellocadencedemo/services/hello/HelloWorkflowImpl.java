package uk.gov.landregistry.hellocadencedemo.services.hello;

import com.uber.cadence.workflow.Workflow;

import static java.time.Duration.ofHours;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

public class HelloWorkflowImpl implements HelloWorkflow {
    @Override
    public String getGreeting(String name) {
        // Comment out to allow for workflow to complete immediately
        Workflow.sleep(ofSeconds(3));
        return "Hello " + name + "!";
    }
}
