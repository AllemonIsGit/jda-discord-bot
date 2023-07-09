package org.example.gamble.embed;

import net.dv8tion.jda.api.entities.User;
import org.example.gamble.Gamble;

import java.util.concurrent.CompletableFuture;

public interface GambleResultEmbed {

    Gamble getGamble();

    CompletableFuture<Void> prepare();

    CompletableFuture<Void> announceWinner(User winner);
}
