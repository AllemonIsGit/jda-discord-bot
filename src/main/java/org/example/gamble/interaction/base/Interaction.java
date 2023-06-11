package org.example.gamble.interaction.base;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class Interaction<T, R> {

    public abstract CompletableFuture<R> apply(T input);

    public final R applyAndWait(T input) {
        try {
            return apply(input).get();
        } catch (ExecutionException e) {
            throw new InteractionException(e.getCause());
        } catch (InterruptedException e) {
            throw new InteractionException(e);
        }
    }
}
