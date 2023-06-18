package org.example.gamble.utils;

import io.vavr.collection.Stream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class Futures {

    public static <T> T complete(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<Void> waitMillis(long millis) {
        Threading.sleep(millis);
        return CompletableFuture.completedFuture(null);
    }

    public static Function<Object, CompletableFuture<Void>> discardResult() {
        return $ -> CompletableFuture.completedFuture(null);
    }

    public static <T> Function<T, CompletableFuture<T>> peeking(Function<T, CompletableFuture<?>> peek) {
        return t -> peek.apply(t).thenApply($ -> t);
    }

    public static <T> Function<T, CompletableFuture<T>> iterating(Function<T, CompletableFuture<T>> iteration, int times) {
        return seed -> Stream.iterate(CompletableFuture.completedFuture(seed), f -> f.thenCompose(iteration))
                .take(times + 1)
                .last();
    }

    public static <T, R> CompletableFuture<R> sequence(Iterable<T> ts, Function<T, CompletableFuture<R>> futureFunction) {
        return Stream.ofAll(ts)
                .foldLeft(
                        CompletableFuture.completedFuture(null),
                        (acc, next) -> acc.thenCompose($ -> futureFunction.apply(next)));
    }
}
