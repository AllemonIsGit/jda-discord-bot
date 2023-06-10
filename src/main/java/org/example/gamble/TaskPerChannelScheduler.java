package org.example.gamble;

import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class TaskPerChannelScheduler {

    private final Map<String, CompletableFuture<?>> runningTasks = new HashMap<>();


    public synchronized boolean isBusy(Channel channel) {
        return getActiveTaskForChannel(channel).isPresent();
    }

    public synchronized <R> CompletableFuture<R> executeWhenAvailable(Channel channel, CompletableFuture<R> nextTask) {
        return getActiveTaskForChannel(channel)
                .map(activeTask -> activeTask.thenCompose($ -> nextTask))
                .orElseGet(() -> scheduleNewForChannel(channel, nextTask));
    }

    private Optional<CompletableFuture<?>> getActiveTaskForChannel(Channel channel) {
        runningTasks.values().removeIf(Future::isDone);
        return Optional.ofNullable(runningTasks.get(channel.getId()));
    }

    private <R> CompletableFuture<R> scheduleNewForChannel(Channel channel, CompletableFuture<R> nextTask) {
        final CompletableFuture<R> safeFuture = nextTask.exceptionally($ -> null);
        runningTasks.put(channel.getId(), safeFuture);
        return safeFuture;
    }
}
