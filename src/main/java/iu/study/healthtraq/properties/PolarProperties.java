package iu.study.healthtraq.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "polar")
public class PolarProperties {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
}
