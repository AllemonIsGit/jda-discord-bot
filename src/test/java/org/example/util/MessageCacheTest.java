package org.example.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MessageCacheTest {

    @Test
    void messageShouldExist() {
        //given
        MessageCache messageCache = MessageCache.getInstance();
        messageCache.clearCache();

        SimpleMessage message = new SimpleMessage("1", "1");

        //when
        messageCache.add(message);

        //then
        Assertions.assertTrue(messageCache.existsByMessageId(message.getMessageId()));
    }

    @Test
    void messageShouldNotExist() {
        //given
        MessageCache messageCache = MessageCache.getInstance();
        messageCache.clearCache();

        //when
        //then
        Assertions.assertFalse(messageCache.existsByMessageId("1"));
    }

    @Test
    void allShouldExist() {
        //given
        MessageCache messageCache = MessageCache.getInstance();
        messageCache.clearCache();

        List<SimpleMessage> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SimpleMessage message = new SimpleMessage(Integer.toString(i), Integer.toString(i));
            list.add(message);
        }

        //when
        list.forEach(messageCache::add);
        //then
        list.forEach((e) -> Assertions.assertTrue(messageCache.existsByMessageId(e.getMessageId())));
    }

    @Test
    void firstShouldNotExist() {
        MessageCache messageCache = MessageCache.getInstance();
        messageCache.clearCache();

        List<SimpleMessage> list = new ArrayList<>();
        for (int i = 0; i <= messageCache.getCacheLimit() + 1; i++) {
            SimpleMessage message = new SimpleMessage(Integer.toString(i), Integer.toString(i));
            list.add(message);
        }

        //when
        list.forEach(messageCache::add);
        //then
        Assertions.assertFalse(messageCache.existsByMessageId(list.get(0).getMessageId()));
    }

}