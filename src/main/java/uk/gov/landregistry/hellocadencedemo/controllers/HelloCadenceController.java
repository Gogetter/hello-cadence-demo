package uk.gov.landregistry.hellocadencedemo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.landregistry.hellocadencedemo.models.Domain;
import uk.gov.landregistry.hellocadencedemo.models.DomainRegistrationResult;
import uk.gov.landregistry.hellocadencedemo.models.WorkflowExecutionResult;
import uk.gov.landregistry.hellocadencedemo.services.domain.RegisterDomainService;
import uk.gov.landregistry.hellocadencedemo.services.hello.HelloWorkflowService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/hello/")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class HelloCadenceController {
    private final RegisterDomainService domainService;
    private final HelloWorkflowService helloService;

    @PostMapping(value = "register", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DomainRegistrationResult> registerDomain(@RequestBody Domain domain) {
        return ResponseEntity.ok(domainService.register(domain));
    }

    @PostMapping(value = "execute/{domainName}/{taskName}")
    public ResponseEntity<WorkflowExecutionResult> executeWorkflow(@PathVariable String domainName,
                                                                   @PathVariable String taskName) {
        if (StringUtils.isEmpty(domainName) || StringUtils.isEmpty(taskName)) {
            return new ResponseEntity<>(WorkflowExecutionResult.builder()
                .failed(true)
                .message("Incomplete data received")
                .build(),
                HttpStatus.BAD_REQUEST);
        }

        WorkflowExecutionResult executionResult = helloService.execute(domainName, taskName);
        return ResponseEntity.ok(executionResult);
    }
}
