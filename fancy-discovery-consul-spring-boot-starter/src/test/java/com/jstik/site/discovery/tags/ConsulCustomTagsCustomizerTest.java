package com.jstik.site.discovery.tags;


import com.jstik.site.discovery.ConsulCustomTagAutoConfigurationApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ConfigFileApplicationContextInitializer.class,ConsulCustomTagAutoConfigurationApp.class})
@TestPropertySource("classpath:stub-on.properties")
public class ConsulCustomTagsCustomizerTest {

    @Inject
    ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers;

    @Test
    public void customize() throws Exception {
        List<ConsulRegistrationCustomizer> registration = registrationCustomizers.getIfAvailable();
    }

}