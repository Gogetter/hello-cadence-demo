package uk.gov.landregistry.hellocadencedemo.services.hello;

import com.uber.cadence.WorkflowExecution;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.worker.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.landregistry.hellocadencedemo.config.CadenceConfig;
import uk.gov.landregistry.hellocadencedemo.models.WorkflowExecutionResult;

import static java.time.Duration.ofHours;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class HelloWorkflowService {
    private final CadenceConfig cadenceConfig;
    public WorkflowExecutionResult execute(final String domainName, final String taskListName) {
        try {
            Worker.Factory workerFactory = new Worker.Factory(domainName);

            Worker worker = workerFactory.newWorker(taskListName);
            worker.registerWorkflowImplementationTypes(HelloWorkflowImpl.class);
            workerFactory.start();

            // Start a workflow execution. Usually this is done from another program.
            WorkflowClient workflowClient = WorkflowClient.newInstance(cadenceConfig.getHost(), cadenceConfig.getPort(), domainName);

            // Get a workflow stub using the same task list the worker uses.
            WorkflowOptions workflowOptions = new WorkflowOptions.Builder()
                .setTaskList(taskListName)
                .setExecutionStartToCloseTimeout(ofMinutes(3))
                .build();
            HelloWorkflow workflow = workflowClient.newWorkflowStub(HelloWorkflow.class, workflowOptions);

            // Start workflow asynchronously -> initiates the workflow execution and immediately returns to the caller
            // As opposed to synchronously -> workflow.getGreeting(greeting) which starts a workflow and waits for it to complete
            WorkflowExecution workflowExecution =
                WorkflowClient.start(workflow::getGreeting, ", its AP's Sprint Review Demo");

            String executionResultMessage = String.format("Started process file workflow with workflowId=%s and runId=%s",
                workflowExecution.getWorkflowId(), workflowExecution.getRunId());

            log.debug("{}", executionResultMessage);

            return WorkflowExecutionResult.builder()
                .failed(false)
                .message(executionResultMessage)
                .build();
        } catch (Exception exc) {
            return WorkflowExecutionResult.builder()
                .failed(true)
                .message(exc.getMessage())
                .build();
        }
    }
}
