package com.jstik.site.discovery.tags;


import com.ecwid.consul.v1.agent.model.NewService;
import com.jstik.site.discovery.ConsulCustomTagAutoConfigurationApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ConfigFileApplicationContextInitializer.class,ConsulCustomTagAutoConfigurationApp.class})
@TestPropertySource("classpath:stub-on.properties")
public class ConsulCustomTagsCustomizerTest {

    @Inject
    private ObjectProvider<ConsulCustomTagsCustomizer> registrationCustomizers;



    @Test
    public void customize() throws Exception {
        ConsulCustomTagsCustomizer registration = registrationCustomizers.getIfAvailable();
        Assert.assertNotNull(registration);

        ConsulDiscoveryProperties emptyProperties = new ConsulDiscoveryProperties(new InetUtils(new InetUtilsProperties()));
        ConsulRegistration consulRegistration = new ConsulRegistration(new NewService(), emptyProperties);
        registration.customize(consulRegistration);

        Assert.assertThat(consulRegistration.getMetadata(), is(expectedTags()));
    }

    private Map<String, String> expectedTags(){
        Map<String, String> result = new HashMap<>();
        result.put("custom.tag1", "testTag1");
        result.put("custom.tag2", "testTag2");
        return  result;
    }

}