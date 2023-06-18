package org.example.gamble.interaction.base;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Interaction<T, R> extends Function<T, CompletableFuture<R>> {
}
