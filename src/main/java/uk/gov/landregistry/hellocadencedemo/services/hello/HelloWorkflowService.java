package uk.gov.landregistry.hellocadencedemo.services.hello;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.worker.Worker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.landregistry.hellocadencedemo.models.WorkflowExecutionResult;

@Slf4j
@Service
public class HelloWorkflowService {
    public WorkflowExecutionResult execute(final String domainName, final String taskListName) {
        try {
            Worker.Factory workerFactory = new Worker.Factory(domainName);

            Worker worker = workerFactory.newWorker(taskListName);
            worker.registerWorkflowImplementationTypes(HelloWorkflowImpl.class);
            workerFactory.start();

            // Start a workflow execution. Usually this is done from another program.
            WorkflowClient workflowClient = WorkflowClient.newInstance(domainName);

            // Get a workflow stub using the same task list the worker uses.
            WorkflowOptions workflowOptions = new WorkflowOptions.Builder().setTaskList(taskListName).build();
            HelloWorkflow workflow = workflowClient.newWorkflowStub(HelloWorkflow.class, workflowOptions);

            // Execute a workflow waiting for it to complete.
            String greeting = workflow.getGreeting("World! We just testing");
            log.debug("Greeting returned {}", greeting);

            return WorkflowExecutionResult.builder()
                .failed(false)
                .message(String.format("Greeting returned %s", greeting))
                .build();
        } catch (Exception exc) {
            return WorkflowExecutionResult.builder()
                .failed(true)
                .message(exc.getMessage())
                .build();
        }
    }
}
