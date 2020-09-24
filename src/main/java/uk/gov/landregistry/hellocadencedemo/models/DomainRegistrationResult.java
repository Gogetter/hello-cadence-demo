package uk.gov.landregistry.hellocadencedemo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainRegistrationResult {
    private final boolean successful;
    private final String message;
}
