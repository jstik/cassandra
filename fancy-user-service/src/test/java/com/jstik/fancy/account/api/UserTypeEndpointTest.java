package com.jstik.fancy.account.api;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.jstik.fancy.account.AccountServiceApp;
import com.jstik.fancy.account.TestApp;
import com.jstik.fancy.account.search.dao.repository.elastic.ElasticConfig;
import com.jstik.fancy.account.search.entity.elastic.UserType;
import com.jstik.fancy.account.search.service.ElasticServiceConfig;
import com.jstik.fancy.account.search.service.IUserTypeService;
import com.jstik.fancy.account.security.UserServiceSecurityConfig;
import com.jstik.fancy.account.storage.service.LinkedServicesConfig;
import com.jstik.site.fancy.elastic.test.EmbeddedElasticConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ContextConfiguration(classes= {
        TestApp.class,
        UserServiceSecurityConfig.class,
        ElasticConfig.class,
        ElasticServiceConfig.class
})
@WebMvcTest({UserTypeEndpoint.class})
public class UserTypeEndpointTest extends AbstractControllerTest {

    private final String PATH_USERS = "/users";

    @MockBean
    protected IUserTypeService userTypeService;

    @Test
    public void shouldReturnValidData() throws Exception {
        int page = 2;
        Page<UserType> response = new PageImpl<>(Arrays.asList(
                new UserType("login0", "firstname0", "lastname0", "email0"),
                new UserType("login1", "firstname1", "lastname1", "email1")
        ));
        int size = response.getNumberOfElements();
        when(userTypeService.getAllUsers(page, size)).thenReturn(response);

        this.mockMvc.perform(get(PATH_USERS)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(size)))
                .andExpect(jsonPath("$.content[0].login", is("login0")))
                .andExpect(jsonPath("$.content[0].firstName", is("firstname0")))
                .andExpect(jsonPath("$.content[0].lastName", is("lastname0")))
                .andExpect(jsonPath("$.content[0].email", is("email0")))
                .andExpect(jsonPath("$.content[1].login", is("login1")))
                .andExpect(jsonPath("$.content[1].firstName", is("firstname1")))
                .andExpect(jsonPath("$.content[1].lastName", is("lastname1")))
                .andExpect(jsonPath("$.content[1].email", is("email1")));
    }

    @Test
    public void shouldReturnOkWithoutSize() throws Exception {
        int page = 2;
        Page<UserType> response = new PageImpl<>(Collections.emptyList());
        when(userTypeService.getAllUsers(ArgumentMatchers.eq(page), ArgumentMatchers.anyInt())).thenReturn(response);
        this.mockMvc.perform(get(PATH_USERS)
                .param("page", String.valueOf(page))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestWithoutPage() throws Exception {
        this.mockMvc.perform(get(PATH_USERS)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWithWrongParameterType() throws Exception {
        this.mockMvc.perform(get(PATH_USERS)
                .param("page","notInt")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}