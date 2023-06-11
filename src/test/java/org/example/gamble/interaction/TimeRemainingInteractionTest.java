package org.example.gamble.interaction;

import lombok.val;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.assertj.core.data.Offset;
import org.example.gamble.interaction.base.TimeRemainingInteraction;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TimeRemainingInteractionTest {

    private static final List<Duration> DURATIONS = List.of(
            Duration.ofSeconds(2),
            Duration.ofSeconds(1),
            Duration.ofMillis(500));

    private static final long TOTAL_DURATION_MILLIS = 3500L;

    @Test
    public void shouldCompleteAfterWaitingForSpecifiedPeriods() {
        // given
        val channel = mock(TextChannel.class, RETURNS_DEEP_STUBS);
        when(channel.sendMessage(any(String.class)).submit()).thenReturn(CompletableFuture.completedFuture(null));

        val timeRemainingInteraction = new TimeRemainingInteraction(DURATIONS);

        // when
        val start = Instant.now();
        timeRemainingInteraction.applyAndWait(channel);
        val end = Instant.now();

        // then
        assertThat(Duration.between(start, end).toMillis())
                .isCloseTo(TOTAL_DURATION_MILLIS, Offset.offset(200L));
    }

    @Test
    public void shouldSendTimeRemainingNotifications() {
        // given
        val channel = mock(TextChannel.class, RETURNS_DEEP_STUBS);
        when(channel.sendMessage(any(String.class)).submit()).thenReturn(CompletableFuture.completedFuture(null));

        val timeRemainingInteraction = new TimeRemainingInteraction(DURATIONS);

        // when
        timeRemainingInteraction.applyAndWait(channel);

        // then
        val messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(channel, times(4)).sendMessage(messageCaptor.capture());

        val messages = messageCaptor.getAllValues();
        assertThat(messages.get(1)).contains("Seconds remaining: 3");
        assertThat(messages.get(2)).contains("Seconds remaining: 1");
        assertThat(messages.get(3)).contains("Seconds remaining: 0");
    }
}
