package org.example.gamble;

import lombok.val;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.gamble.interaction.base.Interaction;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

public class GambleListenerTest {

    private static final String GAMBLE_START_MESSAGE = "!gamble start";

    @Test
    @SuppressWarnings("unchecked")
    public void shouldExecuteActionInteractionWhenChannelIsNotBusy() {
        // given
        val channelLockedInteraction = (Interaction<MessageReceivedEvent, ?>) mock(Interaction.class);
        val actionInteraction = (Interaction<MessageReceivedEvent, ?>) mock(Interaction.class);
        when(actionInteraction.apply(any(MessageReceivedEvent.class))).thenReturn(CompletableFuture.completedFuture(null));

        val gambleListener = new GambleListener(channelLockedInteraction, actionInteraction);

        val gambleStartMessage = mock(Message.class, RETURNS_DEEP_STUBS);
        when(gambleStartMessage.getContentRaw()).thenReturn(GAMBLE_START_MESSAGE);
        when(gambleStartMessage.getChannel().getId()).thenReturn("ID");

        val gambleMessageEvent = new MessageReceivedEvent(mock(JDA.class), 0, gambleStartMessage);

        // when
        gambleListener.onMessageReceived(gambleMessageEvent);

        // then
        verify(actionInteraction).apply(gambleMessageEvent);
        verifyNoInteractions(channelLockedInteraction);
    }
}
