package org.example.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MessageCacheTest {
    private MessageCache messageCache;

    @BeforeEach
    void beforeEach() {
        this.messageCache = MessageCache.getInstance();
        messageCache.clearCache();
    }

    @Test
    void messageShouldExist() {
        //given
        SimpleMessage message = new SimpleMessage("1", "1");

        //when
        messageCache.add(message);

        //then
        assertThat(messageCache.existsByMessageId(message.getMessageId())).isTrue();
    }

    @Test
    void messageShouldNotExist() {
        assertThat(messageCache.existsByMessageId("1")).isFalse();
    }

    @Test
    void allShouldExist() {
        //given

        List<SimpleMessage> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SimpleMessage message = new SimpleMessage(Integer.toString(i), Integer.toString(i));
            list.add(message);
        }

        //when
        list.forEach(messageCache::add);

        //then
        list.forEach((e) -> assertThat(messageCache.existsByMessageId(e.getMessageId())).isTrue());
    }

    @Test
    void firstShouldNotExist() {
        //given

        List<SimpleMessage> list = new ArrayList<>();
        for (int i = 0; i <= messageCache.getCacheLimit() + 1; i++) {
            SimpleMessage message = new SimpleMessage(Integer.toString(i), Integer.toString(i));
            list.add(message);
        }

        //when
        list.forEach(messageCache::add);

        //then
        assertThat(messageCache.existsByMessageId(list.get(0).getMessageId())).isFalse();
    }

}