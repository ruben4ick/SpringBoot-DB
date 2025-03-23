package ua.ukma.warehouse.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "secondary.datasource")
@Getter
@Setter
@Profile("testRouting")
public class DataSourceSecondaryConfig {
    private String url;
    private String username;
    private String password;
}
