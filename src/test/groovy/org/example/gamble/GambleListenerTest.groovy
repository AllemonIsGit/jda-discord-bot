package org.example.gamble

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.example.gamble.interaction.base.Interaction
import org.example.gamble.utils.Futures
import org.example.gamble.utils.Threading
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class GambleListenerTest extends Specification {

    SlashCommandInteractionEvent channel0Gamble = Stub(SlashCommandInteractionEvent) {
        getFullCommandName() >> SlashCommands.INIT_GAMBLE_COMMAND_NAME
        getChannel() >> "text channel union stub"(0)
    }

    SlashCommandInteractionEvent channel1Gamble = Stub(SlashCommandInteractionEvent) {
        getFullCommandName() >> SlashCommands.INIT_GAMBLE_COMMAND_NAME
        getChannel() >> "text channel union stub"(1)
    }

    def "should invoke init gamble interaction given slash command event"() {
        given:
        def errorInteraction = Mock(Interaction)
        def actionNotAllowedInteraction = Mock(Interaction)
        def performGambleInteraction = Mock(Interaction)

        def gambleListener = new GambleListener(errorInteraction, actionNotAllowedInteraction, performGambleInteraction)

        when:
        gambleListener.onSlashCommandInteraction(channel0Gamble)

        then:
        1 * performGambleInteraction.apply(channel0Gamble) >> CompletableFuture.completedFuture(null)
        0 * errorInteraction.apply(_) >> CompletableFuture.completedFuture(null)
        0 * actionNotAllowedInteraction.apply(_) >> CompletableFuture.completedFuture(null)
    }

    def "should invoke action not allowed interaction when channel busy"() {
        given:
        def errorInteraction = Mock(Interaction)
        def actionNotAllowedInteraction = Mock(Interaction)
        def performGambleInteraction = Mock(Interaction)

        def gambleListener = new GambleListener(errorInteraction, actionNotAllowedInteraction, performGambleInteraction)

        when:
        gambleListener.onSlashCommandInteraction(channel0Gamble)
        gambleListener.onSlashCommandInteraction(channel0Gamble)
        gambleListener.onSlashCommandInteraction(channel1Gamble)

        then:
        1 * performGambleInteraction.apply(channel0Gamble) >> { Futures.waitMillis(300) }
        1 * actionNotAllowedInteraction.apply(channel0Gamble) >> CompletableFuture.completedFuture(null)

        then:
        1 * performGambleInteraction.apply(channel1Gamble) >> CompletableFuture.completedFuture(null)
        0 * errorInteraction.apply(_) >> CompletableFuture.completedFuture(null)
    }

    def "should invoke error interaction and unlock channel when performing gamble throws"() {
        given:
        def errorInteraction = Mock(Interaction)
        def actionNotAllowedInteraction = Mock(Interaction)
        def performGambleInteraction = Mock(Interaction)

        def gambleListener = new GambleListener(errorInteraction, actionNotAllowedInteraction, performGambleInteraction)

        when:
        gambleListener.onSlashCommandInteraction(channel0Gamble)
        Threading.sleep(50)
        gambleListener.onSlashCommandInteraction(channel0Gamble)

        then:
        1 * performGambleInteraction.apply(channel0Gamble) >> CompletableFuture.failedFuture("test failure"())

        then:
        1 * errorInteraction.apply(channel0Gamble) >> CompletableFuture.completedFuture(null)
        1 * performGambleInteraction.apply(channel0Gamble) >> CompletableFuture.completedFuture(null)
        0 * actionNotAllowedInteraction.apply(channel0Gamble) >> CompletableFuture.completedFuture(null)
    }

    def "text channel union stub"(id) {
        Stub(MessageChannelUnion) {
            asTextChannel() >> Stub(TextChannel) {
                getId() >> id
            }
        }
    }

    def "test failure"() {
        return new IllegalArgumentException("TEST")
    }
}
