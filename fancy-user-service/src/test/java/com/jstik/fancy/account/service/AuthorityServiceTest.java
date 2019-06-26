package com.jstik.fancy.account.service;

import com.google.common.collect.Sets;
import com.jstik.fancy.account.dao.repository.cassandra.UserServiceCassandraConfig;
import com.jstik.fancy.account.dao.repository.cassandra.authority.AuthorityRepository;
import com.jstik.fancy.account.model.authority.AccessLevel;
import com.jstik.fancy.account.model.authority.AuthorityDTO;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static com.jstik.fancy.account.entity.cassandra.authority.AuthorityType.USER;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static reactor.test.StepVerifier.create;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                LinkedServicesConfig.class,
        }
)
@TestPropertySource({"classpath:embedded-test.properties"})
public class AuthorityServiceTest {


    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    private AuthorityService authorityService;

    @Inject
    private AuthorityRepository authorityRepository;

    @Test
    public void addAuthorities() throws Exception {
        HashSet<AuthorityDTO> authorities = Sets.newHashSet(
                prepareAuthority("client", "books"),
                prepareAuthority("client", "shelf"));
        create(authorityService.addAuthorities("user1", authorities, USER))
                .assertNext(Assert::assertTrue).verifyComplete();
        create(authorityRepository.findAll()).assertNext(authority -> {
            assertThat(authority.getAuthority(), anyOf(is("books"), is("shelf")));
        }).assertNext(authority -> {
            assertThat(authority.getAuthority(), anyOf(is("books"), is("shelf")));
        }).verifyComplete();
    }

    @Test
    public void deleteAuthorities() throws Exception {
        authorityService.addAuthorities("user1", prepareAuthorities(), USER).block();
        HashSet<String> authorities = Sets.newHashSet("books");
        create(authorityService.deleteAuthorities("client", "user1", authorities, USER))
                .verifyComplete();
        create(authorityRepository.findAll()).assertNext(authority -> {
            assertThat(authority.getAuthority(), is("shelf"));
        }).verifyComplete();
    }

    @Test
    public void findAuthorities() throws Exception {
        authorityService.addAuthorities("user1", prepareAuthorities(), USER).block();
        create(authorityService.findAuthorities("client", "user1", USER)).assertNext(authority -> {
            assertThat(authority.getAuthority(), anyOf(is("books"), is("shelf")));
        }).assertNext(authority -> {
            assertThat(authority.getAuthority(), anyOf(is("books"), is("shelf")));
        }).verifyComplete();
    }

    @Test
    public void findAuthorities1() throws Exception {
        AuthorityDTO dto = prepareAuthority("client", "books");
        authorityService.addAuthorities("user1", Collections.singleton(dto), USER).block();
        authorityService.addAuthorities("user2", Collections.singleton(dto), USER).block();
        create(authorityService.findAuthorities("client", Sets.newHashSet("user1", "user2"), USER))
                .assertNext(authority -> assertThat(authority.getId(), anyOf(is("user1"), is("user2"))))
                .assertNext(authority -> assertThat(authority.getId(), anyOf(is("user1"), is("user2"))))
                .verifyComplete();
    }

    @Test
    public void insertAuthority() throws Exception {
    }

    private Collection<AuthorityDTO> prepareAuthorities() {
        return Sets.newHashSet(
                prepareAuthority("client", "books"),
                prepareAuthority("client", "shelf"));
    }

    private static AuthorityDTO prepareAuthority(String client, String authority) {
        return new AuthorityDTO(client, authority, AccessLevel.WRITE);
    }

}