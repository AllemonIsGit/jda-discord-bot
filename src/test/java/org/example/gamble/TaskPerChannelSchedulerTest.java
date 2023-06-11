package org.example.gamble;

import lombok.val;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.assertj.core.data.Offset;
import org.example.gamble.utils.Threading;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskPerChannelSchedulerTest {

    @Test
    public void shouldScheduleNextTaskForChannelWhenItsBusy() throws Exception {
        // given
        val channel = mock(Channel.class);
        when(channel.getId()).thenReturn("ID");

        val scheduler = new TaskPerChannelScheduler();

        // when
        assertThat(scheduler.isBusy(channel)).isFalse();

        val firstTask = scheduler.executeWhenAvailable(channel, completionTimeRecordingFuture());
        assertThat(scheduler.isBusy(channel)).isTrue();
        Thread.sleep(100);

        val secondTask = scheduler.executeWhenAvailable(channel, completionTimeRecordingFuture());
        assertThat(scheduler.isBusy(channel)).isTrue();
        Thread.sleep(300);

        val thirdTask = scheduler.executeWhenAvailable(channel, completionTimeRecordingFuture());
        assertThat(scheduler.isBusy(channel)).isTrue();

        thirdTask.get();

        // then
        assertThat(scheduler.isBusy(channel)).isFalse();

        val allTasks = List.of(firstTask, secondTask, thirdTask);

        assertThat(allTasks).extracting(Future::isDone)
                .allMatch(Boolean.TRUE::equals);

        assertThat(allTasks).extracting(Future::get)
                .isSorted();
    }

    @Test
    public void shouldExecuteTasksInParallelForSeparateChannels() throws Exception {
        // given
        val channelA = mock(Channel.class);
        when(channelA.getId()).thenReturn("ID-A");

        val channelB = mock(Channel.class);
        when(channelB.getId()).thenReturn("ID-B");

        val scheduler = new TaskPerChannelScheduler();

        // when
        assertThat(scheduler.isBusy(channelA)).isFalse();
        assertThat(scheduler.isBusy(channelB)).isFalse();

        val firstTask = scheduler.executeWhenAvailable(channelA, completionTimeRecordingFuture());
        val secondTask = scheduler.executeWhenAvailable(channelB, completionTimeRecordingFuture());

        assertThat(scheduler.isBusy(channelA)).isTrue();
        assertThat(scheduler.isBusy(channelB)).isTrue();

        firstTask.get();

        // then
        assertThat(scheduler.isBusy(channelA)).isFalse();
        assertThat(scheduler.isBusy(channelB)).isFalse();

        val allTasks = List.of(firstTask, secondTask);

        assertThat(allTasks).extracting(Future::isDone)
                .allMatch(Boolean.TRUE::equals);

        assertThat(firstTask.get().toEpochMilli())
                .isCloseTo(secondTask.get().toEpochMilli(), Offset.offset(10L));
    }

    private CompletableFuture<Instant> completionTimeRecordingFuture() {
        return CompletableFuture.supplyAsync(() -> {
            Threading.sleep(1000);
            return Instant.now();
        });
    }
}
