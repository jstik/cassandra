package com.jstik.site.discovery.tags;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsulCustomTagsCustomizer implements ConsulRegistrationCustomizer {

    private ObjectProvider<ConsulCustomTagsProperties> tagsPropertiesProvider;

    public ConsulCustomTagsCustomizer(ObjectProvider<ConsulCustomTagsProperties> tagsPropertiesProvider) {
        this.tagsPropertiesProvider = tagsPropertiesProvider;
    }

    @Override
    public void customize(ConsulRegistration registration) {
        if (this.tagsPropertiesProvider == null)
            return;
        ConsulCustomTagsProperties tagsProperties = this.tagsPropertiesProvider.getIfAvailable();
        if (tagsProperties == null)
            return;
        Set<String> tags = tagsProperties.getAdd().entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.toSet());
        if(tags.isEmpty())
            return;
        List<String> registrationTags = registration.getService().getTags();
        if (registrationTags == null) {
            registrationTags = new ArrayList<>();
        }
        registrationTags.addAll(tags);
        registration.getService().setTags(registrationTags);
    }
}
