package org.example.gamble.utils;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

public class FuturesTest {

    @Test
    public void futureIteratingTest() throws Exception {
        // given
        val iteratingFuture = Futures.iterating(this::incrementingFuture, 5);

        // when
        val count = iteratingFuture.apply(0).get();

        // then
        assertThat(count).isEqualTo(5);
    }

    private CompletableFuture<Integer> incrementingFuture(int count) {
        Threading.sleep(100);
        return CompletableFuture.completedFuture(count + 1);
    }
}
