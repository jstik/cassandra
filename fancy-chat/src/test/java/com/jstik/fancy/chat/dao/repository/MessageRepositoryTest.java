package com.jstik.fancy.chat.dao.repository;


import com.jstik.fancy.chat.dao.config.CassandraConfig;
import com.jstik.fancy.chat.model.entity.Message;
import com.jstik.fancy.chat.model.entity.UserBriefInfo;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, CassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class MessageRepositoryTest extends EmbeddedCassandraEnvironment {

    @Inject
    private MessageRepository messageRepository;

    @Test
    public void createMessageTest(){
        Message.MessagePrimaryKey pk = new Message.MessagePrimaryKey("group1");
        Message message = new Message(pk, "Hello", new UserBriefInfo("user@login", "userName"));
        messageRepository.save(message).block();
        Message block = messageRepository.findByMessageKeyAddressAndMessageKeyDayCreated("group1", LocalDate.now()).blockFirst();
        Assert.assertNotNull(block);
    }

    @Test
    public void findByMessagesByAddressAndDayCreatedBetweenTest(){
        String address = "group";
        createMessage(prepareMessage(prepareUserInfo("login"), address));
        Flux<Message> byMessagesByAddressAndDayCreatedBetween = messageRepository.findByMessagesByAddressAndDayCreatedBetween(address, LocalDate.now(), LocalDate.now());

    }

    private UserBriefInfo prepareUserInfo(String login){
       return new UserBriefInfo(login, login);
    }

    private Message prepareMessage(UserBriefInfo user, String address){
        Message.MessagePrimaryKey pk = new Message.MessagePrimaryKey(address);
        return new Message(pk, "Hello",user);
    }

    private Message createMessage(Message message){
       return messageRepository.save(message).block();
    }

}