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
    public void futureIteratingTest() throws Exception {
        // given
        val iteratingFuture = Futures.iterating(this::incrementingFuture, 5);

        // when
        val start = Instant.now();
        val count = iteratingFuture.apply(0).get();
        val end = Instant.now();

        val durationMillis = end.toEpochMilli() - start.toEpochMilli();

        // then
        assertThat(count).isEqualTo(5);
        assertThat(durationMillis).isCloseTo(500, Offset.offset(100L));
    }

    private CompletableFuture<Integer> incrementingFuture(int count) {
        Threading.sleep(100);
        return CompletableFuture.completedFuture(count + 1);
    }

    @Test
    public void futureSequenceTest() throws Exception {
        // given
        val originalSequence = List.of(1, 2, 3, 4, 5);
        val resultSequence = new ArrayList<>(5);

        // when
        val start = Instant.now();
        Futures.sequence(originalSequence, i -> capturingFuture(i, resultSequence::add)).get();
        val end = Instant.now();

        val durationMillis = end.toEpochMilli() - start.toEpochMilli();

        // then
        assertThat(resultSequence).containsExactlyElementsOf(originalSequence);
        assertThat(durationMillis).isCloseTo(500, Offset.offset(100L));
    }

    private CompletableFuture<Void> capturingFuture(int element, Consumer<Integer> captureAction) {
        Threading.sleep(100);
        captureAction.accept(element);
        return CompletableFuture.completedFuture(null);
    }
}
