package ca.bc.gov.iamp.bcparis.common.metrics.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MicroMeterConfig implements MeterRegistryCustomizer {
    @Autowired
    ConfigurableApplicationContext appContext;

    @Value("${application.domain:unknown}")
    private String applicationDomain;

    @Value("${application.lob:unknown}")
    private String applicationLineOfBusiness;

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${spring.profiles.active:unknown}")
    private String applicationProfile;

    @Value("${NAMESPACE:unknown}")
    private String namespace;

    @Value("${HOSTNAME:unknown}")
    private String pod;

    @Override
    public void customize(MeterRegistry registry) {
        registry.config().commonTags("domain", applicationDomain);
        registry.config().commonTags("lob", applicationLineOfBusiness);
        registry.config().commonTags("app", applicationName);
        registry.config().commonTags("profile", applicationProfile);
        registry.config().commonTags("namespace", namespace);
        registry.config().commonTags("pod", pod);
    }
}
