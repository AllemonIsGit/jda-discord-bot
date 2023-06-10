package org.example.gamble;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.gamble.interaction.base.DeleteMessageInteraction;
import org.example.gamble.interaction.gamble.GambleInteraction;
import org.example.gamble.interaction.base.Interaction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class GambleListener extends ListenerAdapter {

    private static final Predicate<String> GAMBLE_INIT_MESSAGE_PREDICATE = "!gamble start"::equals;

    public GambleListener() {
        this(new DeleteMessageInteraction(), new GambleInteraction());
    }

    private final TaskPerChannelScheduler taskPerChannelScheduler = new TaskPerChannelScheduler();

    private final Interaction<MessageReceivedEvent, ?> channelLockedInteraction;

    private final Interaction<MessageReceivedEvent, ?> actionInteraction;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (GAMBLE_INIT_MESSAGE_PREDICATE.test(event.getMessage().getContentRaw())) {
            onGambleInitRequest(event);
        }
    }

    private void onGambleInitRequest(MessageReceivedEvent event) {
        val targetChannel = event.getChannel();
        if (taskPerChannelScheduler.isBusy(targetChannel)) {
            channelLockedInteraction.applyAndWait(event);
            return;
        }

        val gambleFuture = taskPerChannelScheduler.executeWhenAvailable(targetChannel, actionInteraction.apply(event));
        Interaction.executeAndWait(gambleFuture);
    }
}
