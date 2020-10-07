package uk.gov.landregistry.hellocadencedemo.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "cadence.demo.domain")
public class CadenceConfig {
    private String name;
    private String description;
    private String host;
    private int port;
}
