package com.jstik.fancy.account.api;

import com.jstik.fancy.account.TestApp;
import com.jstik.fancy.account.search.dao.repository.elastic.ElasticConfig;
import com.jstik.fancy.account.search.service.ElasticServiceConfig;
import com.jstik.fancy.account.search.service.IUserTypeService;
import com.jstik.fancy.account.security.UserServiceSecurityConfig;
import com.jstik.site.fancy.elastic.test.EmbeddedElasticConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;


@RunWith(SpringRunner.class)
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;


}
