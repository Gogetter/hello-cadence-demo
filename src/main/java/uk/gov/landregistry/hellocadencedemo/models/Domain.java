package uk.gov.landregistry.hellocadencedemo.models;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import static org.springframework.util.StringUtils.isEmpty;

@Data
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class Domain {
    private String name;
    private String description;

    public static boolean notEmpty(final Domain domain) {
        return (domain != null && !isEmpty(domain.description) && !isEmpty(domain.name));
    }
}
