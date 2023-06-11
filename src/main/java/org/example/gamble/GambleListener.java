package org.example.gamble;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.gamble.interaction.base.DeleteMessageInteraction;
import org.example.gamble.interaction.base.ErrorInteraction;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.interaction.gamble.PerformGambleInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class GambleListener extends ListenerAdapter {

    private static final Predicate<String> GAMBLE_INIT_MESSAGE_PREDICATE = "!gamble"::equals;

    public GambleListener() {
        this(new DeleteMessageInteraction(), new PerformGambleInteraction());
    }

    private final TaskPerChannelScheduler taskPerChannelScheduler = new TaskPerChannelScheduler();

    private final Interaction<TextChannel, Void> errorInteraction = new ErrorInteraction();

    private final Interaction<MessageReceivedEvent, Void> channelLockedInteraction;

    private final Interaction<MessageReceivedEvent, Void> actionInteraction;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (GAMBLE_INIT_MESSAGE_PREDICATE.test(event.getMessage().getContentRaw())) {
            onGambleInitRequest(event);
        }
    }

    private void onGambleInitRequest(MessageReceivedEvent event) {
        val targetChannel = event.getChannel().asTextChannel();
        if (taskPerChannelScheduler.isBusy(targetChannel)) {
            channelLockedInteraction.applyAndWait(event);
            return;
        }

        val gambleTask = actionInteraction.apply(event)
                .exceptionallyCompose(error -> reportException(error, targetChannel));
        taskPerChannelScheduler.executeWhenAvailable(targetChannel, gambleTask);
    }

    private CompletableFuture<Void> reportException(Throwable throwable, TextChannel targetChannel) {
        System.err.println(getClass().getSimpleName() + ": " + throwable.getMessage());
        throwable.printStackTrace();
        return errorInteraction.apply(targetChannel);
    }
}
