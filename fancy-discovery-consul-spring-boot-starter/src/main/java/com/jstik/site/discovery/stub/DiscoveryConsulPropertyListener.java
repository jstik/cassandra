package com.jstik.site.discovery.stub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;


public class DiscoveryConsulPropertyListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Value("${spring.cloud.consul.stub.enabled}")
    private boolean stubEnabled;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        String stubEnabled = event.getEnvironment().getProperty("spring.cloud.consul.stub.enabled");
        if (stubEnabled != null && Boolean.valueOf(stubEnabled)) {
            Properties props = new Properties();
            props.put("spring.cloud.consul.enabled", false);
            props.put("spring.cloud.discovery.enabled", false);
            props.put("spring.cloud.bus.enabled", false);
            environment.getPropertySources().addFirst(new PropertiesPropertySource("disable-discovery", props));
        }
    }
}
