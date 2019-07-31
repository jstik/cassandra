package com.jstik.fancy.account.api;

import com.jstik.fancy.account.TestApp;
import com.jstik.fancy.account.model.authority.AccessLevel;
import com.jstik.fancy.account.model.authority.AuthorityDTO;
import com.jstik.fancy.account.search.dao.repository.elastic.ElasticConfig;
import com.jstik.fancy.account.search.service.ElasticServiceConfig;
import com.jstik.fancy.account.security.UserServiceSecurityConfig;
import com.jstik.fancy.account.storage.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.service.IAuthorityService;
import com.jstik.fancy.account.storage.service.IUserService;
import com.jstik.fancy.account.storage.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {
        TestApp.class,
        UserServiceSecurityConfig.class,
        ElasticConfig.class,
        ElasticServiceConfig.class
})
@WebMvcTest({UserEndpoint.class})
public class UserEndpointTest extends AbstractControllerTest {


    @MockBean
    protected IUserService userService;

    @MockBean
    protected IAuthorityService authorityService;

    protected final String LOGIN = "uLogin";
    protected final String[] CLIENTS = {"client","anotherClient"};
    protected final String[] TAGS = {"tag1", "tag2"};
    protected final String[] GROUPS = {"group1", "group2"};


    protected User user;


    @Before
    public void doBefore() {
        user = new User("uLogin", "uFirstName", "uLastName", "u@Email.org");
        user.addGroup(GROUPS[0]);
        user.addClients(Arrays.asList(CLIENTS));

        Mockito.doReturn(Mono.just(user))
                .when(userService)
                .findUserOrThrow(ArgumentMatchers.eq(LOGIN));
    }

    @Test
    public void getUserDetailsShouldReturnOkData() throws Exception {
        AuthorityDTO USER_AUTHORITY_DTO = new AuthorityDTO(CLIENTS[0], "aWrite", AccessLevel.WRITE);
        AuthorityDTO GROUP_AUTHORITY_DTO = new AuthorityDTO(CLIENTS[0], "aRead", AccessLevel.READ);
        List<Authority> USER_AUTHORITIES = Arrays.asList(
                new Authority(USER_AUTHORITY_DTO, user));
        List<Authority> GROUP_AUTHORITIES = Arrays.asList(
                new Authority(GROUP_AUTHORITY_DTO, GROUPS[0]));

        Mockito.doReturn(Mono.just(USER_AUTHORITIES))
                .when(authorityService)
                .findUserAuthorities(ArgumentMatchers.eq(CLIENTS[0]), ArgumentMatchers.eq(user));

        Mockito.doReturn(Mono.just(GROUP_AUTHORITIES))
                .when(authorityService)
                .findGroupsAuthorities(ArgumentMatchers.eq(CLIENTS[0]), ArgumentMatchers.eq(user));


        Object result = mockMvc.perform(MockMvcRequestBuilders
                .get("/user/{login}/{client}", LOGIN, CLIENTS[0])
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getAsyncResult();

        assertTrue(result instanceof User);
        User resUser = (User) result;
        assertEquals(resUser, user);
        assertNotNull(resUser.getAuthorities());
        assertEquals(resUser.getAuthorities().size(), 2);

        assertTrue(resUser.getAuthorities().stream()
                .filter(authority -> AccessLevel.WRITE.equals(authority.getAccessLevel()))
                .count() == 1L);
        assertTrue(resUser.getAuthorities().stream()
                .filter(authority -> AccessLevel.READ.equals(authority.getAccessLevel()))
                .count() == 1L);
    }

    @Test
    public void addUserTagsShouldReturnOkData() throws Exception {

        Mockito.doAnswer(invocation -> {
            user.addTags(invocation.getArgument(0));
            return Mono.just(user);
        })
                .when(userService)
                .addUserTags(ArgumentMatchers.anyCollection(), ArgumentMatchers.eq(user));

        Object result = mockMvc.perform(MockMvcRequestBuilders
                .put("/user/{login}/tags", LOGIN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("tags", TAGS)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getAsyncResult();

        assertTrue(result instanceof User);
        User resUser = (User) result;
        assertEquals(resUser.getTags(), new HashSet<>(Arrays.asList(TAGS)));
    }

    @Test
    public void deleteUserTagsShouldReturnOkData() throws Exception{
        user.addTags(Arrays.asList(TAGS));
        assertEquals(user.getTags(), new HashSet<>(Arrays.asList(TAGS)));

        Mockito.doAnswer(invocation -> {
            user.deleteTags(invocation.getArgument(0));
            return Mono.just(user);
        })
                .when(userService)
                .deleteUserTags(ArgumentMatchers.anyCollection(), ArgumentMatchers.eq(user));

        Object result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/user/{login}/tags", LOGIN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("tags", TAGS)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getAsyncResult();

        assertTrue(result instanceof User);
        User resUser = (User) result;
        assertEquals(resUser.getTags().size(),0);

    }





    @Test
    public void addUserClientsShouldReturnOkData() throws Exception{
        Mockito.doAnswer(invocation -> {
            user.addClients(invocation.getArgument(0));
            return Mono.just(user);
        })
                .when(userService)
                .addUserClients(ArgumentMatchers.anyCollection(), ArgumentMatchers.eq(user));


        Object result = mockMvc.perform(MockMvcRequestBuilders
                .put("/user/{login}/clients", LOGIN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("clients", CLIENTS)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getAsyncResult();

        assertTrue(result instanceof User);
        User resUser = (User) result;
        assertEquals(resUser.getClients(), new HashSet<>(Arrays.asList(CLIENTS)));

    }


    @Test
    public void deleteUserClients() throws Exception {

        user.addClients(Arrays.asList(CLIENTS));
        assertEquals(user.getClients(), new HashSet<>(Arrays.asList(CLIENTS)));

        Mockito.doAnswer(invocation -> {
            user.deleteClients(invocation.getArgument(0));
            return Mono.just(user);
        })
                .when(userService)
                .deleteUserClients(ArgumentMatchers.anyCollection(), ArgumentMatchers.eq(user));

        Object result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/user/{login}/clients", LOGIN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("clients", CLIENTS)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getAsyncResult();

        assertTrue(result instanceof User);
        User resUser = (User) result;
        assertEquals(resUser.getClients().size(),0);
    }
    /*
    @Test
    public void updateUserInfo() throws Exception{
    }
*/


    @Test
    public void addUserGroups() throws Exception{
        Mockito.doAnswer(invocation -> {
            user.addGroups(invocation.getArgument(0));
            return Mono.just(user);
        })
                .when(userService)
                .addUserGroups(ArgumentMatchers.anyCollection(), ArgumentMatchers.eq(user));
        mockUserServiceAddCollection(strings -> userService.addUserGroups(strings, user), user);

        Object result = mockMvc.perform(MockMvcRequestBuilders
                .put("/user/{login}/groups", LOGIN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("groups", GROUPS)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getAsyncResult();

        assertTrue(result instanceof User);
        User resUser = (User) result;
        assertEquals(resUser.getClients(), new HashSet<>(Arrays.asList(CLIENTS)));

    }

    @Test
    public void deleteUserGroups()throws Exception {
    }


    private void mockUserServiceAddCollection(Consumer<Collection<String>> userServiceMethod, User user){
        Mockito.doAnswer(invocation -> {
          //  userServiceMethod.accept(invocation.getArgument(0));
            return Mono.just(user);
        })
                .when(userService)
                .addUserGroups(ArgumentMatchers.anyCollection(), ArgumentMatchers.eq(user));
    }

/*
    private void checkAddCollection(Consumer<Collection<String>> userServiceMethod){




        Object result = mockMvc.perform(MockMvcRequestBuilders
                .put("/user/{login}/groups", LOGIN)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("groups", GROUPS)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getAsyncResult();

        assertTrue(result instanceof User);
        User resUser = (User) result;
        assertEquals(resUser.getClients(), new HashSet<>(Arrays.asList(CLIENTS)));
    }*/


}