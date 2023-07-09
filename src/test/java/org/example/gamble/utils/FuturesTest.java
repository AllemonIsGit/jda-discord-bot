package org.example.gamble.utils;

import lombok.val;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class FuturesTest {

    @Test
    public void futureWaitingMillisIsAsyncTest() {
        // when
        val start = Instant.now();
        Futures.waitMillis(1000);
        val end = Instant.now();

        val durationMillis = end.toEpochMilli() - start.toEpochMilli();

        // then
        assertThat(durationMillis).isCloseTo(0, Offset.offset(10L));
    }

    @Test
    public void futureIteratingTest() {
        // given
        val iteratingFutureFunction = Futures.iterating(this::incrementingFuture, 5);

        // when
        val start = Instant.now();
        val runningFuture = iteratingFutureFunction.apply(0);
        val count = Futures.get(runningFuture);
        val end = Instant.now();

        val durationMillis = end.toEpochMilli() - start.toEpochMilli();

        // then
        assertThat(count).isEqualTo(5);
        assertThat(durationMillis).isCloseTo(500, Offset.offset(100L));
    }

    private CompletableFuture<Integer> incrementingFuture(int count) {
        return Futures.waitMillis(100)
                .thenApply($ -> count + 1);
    }

    @Test
    public void futureSequenceTest() {
        // given
        val originalSequence = List.of(1, 2, 3, 4, 5);
        val resultSequence = new ArrayList<>(5);

        // when
        val start = Instant.now();
        val runningFuture = Futures.sequence(originalSequence, i -> capturingFuture(i, resultSequence::add));
        Futures.get(runningFuture);
        val end = Instant.now();

        val durationMillis = end.toEpochMilli() - start.toEpochMilli();

        // then
        assertThat(resultSequence).containsExactlyElementsOf(originalSequence);
        assertThat(durationMillis).isCloseTo(500, Offset.offset(100L));
    }

    private CompletableFuture<Void> capturingFuture(int element, Consumer<Integer> captureAction) {
        return Futures.waitMillis(100)
                .thenAccept($ -> captureAction.accept(element));
    }
}
