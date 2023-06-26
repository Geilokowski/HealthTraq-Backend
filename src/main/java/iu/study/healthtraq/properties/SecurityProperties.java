package iu.study.healthtraq.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private final long authTokenValidityDurationHours;
    private final String securityKey;
}
