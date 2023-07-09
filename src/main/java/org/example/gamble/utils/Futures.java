package org.example.gamble.utils;

import io.vavr.collection.Stream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class Futures {

    public static <T> T get(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new FutureEvaluationException(e.getCause());
        } catch (InterruptedException e) {
            throw new FutureEvaluationException("Future was interrupted");
        }
    }

    public static CompletableFuture<Void> waitMillis(long millis) {
        return CompletableFuture.supplyAsync(() -> {
            Threading.sleep(millis);
            return null;
        });
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
