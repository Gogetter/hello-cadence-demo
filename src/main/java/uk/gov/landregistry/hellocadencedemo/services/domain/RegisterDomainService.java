package uk.gov.landregistry.hellocadencedemo.services.domain;

import com.uber.cadence.BadRequestError;
import com.uber.cadence.ClientVersionNotSupportedError;
import com.uber.cadence.DomainAlreadyExistsError;
import com.uber.cadence.RegisterDomainRequest;
import com.uber.cadence.ServiceBusyError;
import com.uber.cadence.serviceclient.IWorkflowService;
import com.uber.cadence.serviceclient.WorkflowServiceTChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.landregistry.hellocadencedemo.config.CadenceConfig;
import uk.gov.landregistry.hellocadencedemo.models.Domain;
import uk.gov.landregistry.hellocadencedemo.models.DomainRegistrationResult;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RegisterDomainService {
    private final CadenceConfig cadenceConfig;

    public DomainRegistrationResult register(final Domain domain) {
        String message;
        Domain domainToUse = checkDomain(domain);

        IWorkflowService cadenceService = new WorkflowServiceTChannel(cadenceConfig.getHost(), cadenceConfig.getPort());
        RegisterDomainRequest request = new RegisterDomainRequest();
        request.setDescription(domainToUse.description());
        request.setEmitMetric(true);
        request.setName(domainToUse.name());

        int retentionPeriodInDays = 1;

        request.setWorkflowExecutionRetentionPeriodInDays(retentionPeriodInDays);
        try {
            cadenceService.RegisterDomain(request);
            message = String.format("Successfully registered domain %s with retention days %s",
                domainToUse.name(), retentionPeriodInDays);
            log.debug("{}", message);
            return buildDomainRegistrationResult(true, message);
        } catch (DomainAlreadyExistsError e) {
            message = String.format("Domain %s has already been registered", domainToUse.name());
            log.warn("{}", message);
            return buildDomainRegistrationResult(false, message);
        } catch (BadRequestError badRequestError) {
            badRequestError.printStackTrace();
            return buildDomainRegistrationResult(false, badRequestError.getMessage());

        } catch (ClientVersionNotSupportedError clientVersionNotSupportedError) {
            clientVersionNotSupportedError.printStackTrace();
            return buildDomainRegistrationResult(false, clientVersionNotSupportedError.getMessage());

        } catch (ServiceBusyError serviceBusyError) {
            serviceBusyError.printStackTrace();
            return buildDomainRegistrationResult(false, serviceBusyError.getMessage());

        } catch (TException e) {
            e.printStackTrace();
            return buildDomainRegistrationResult(false, e.getMessage());
        }
    }

    private Domain checkDomain(final Domain domain) {
        return Domain.notEmpty(domain) ? domain : Domain.builder()
            .name(cadenceConfig.getName())
            .description(cadenceConfig.getDescription())
            .build();
    }

    private DomainRegistrationResult buildDomainRegistrationResult(final boolean success, final String message) {
        return DomainRegistrationResult.builder()
            .successful(success)
            .message(message)
            .build();
    }
}
