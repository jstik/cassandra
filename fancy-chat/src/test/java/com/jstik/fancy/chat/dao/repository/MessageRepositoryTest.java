package com.jstik.fancy.chat.dao.repository;

import com.datastax.driver.core.utils.UUIDs;
import com.jstik.fancy.chat.dao.config.CassandraConfig;
import com.jstik.fancy.chat.model.Message;
import com.jstik.fancy.util.EmbeddedCassandraConfig;
import com.jstik.fancy.util.EmbeddedCassandraEnvironment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, CassandraConfig.class})
@TestPropertySource("classpath:test.properties")
public class MessageRepositoryTest extends EmbeddedCassandraEnvironment {

    @Inject
    private MessageRepository messageRepository;

    @Test
    public void createMessageTest(){
        Message.MessagePK pk = new Message.MessagePK("group1", UUIDs.timeBased());
        Message message = new Message(pk);
        message.setContent("Hello");
        messageRepository.save(message).block();
        Message messageFromDb = messageRepository.findByMessagePKAddress("group1").blockFirst();
        Assert.assertNotNull(messageFromDb);
    }

}