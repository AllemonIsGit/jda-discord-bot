package org.example.gamble.interaction.base;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class Interaction<T, R> {

    public abstract CompletableFuture<R> apply(T input);

    public final R applyAndWait(T input) {
        return executeAndWait(apply(input));
    }

    public static <V> V executeAndWait(CompletableFuture<V> future) {
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new InteractionException(e.getCause());
        } catch (InterruptedException e) {
            throw new InteractionException(e);
        }
    }
}
