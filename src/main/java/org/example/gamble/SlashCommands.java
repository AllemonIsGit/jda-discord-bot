package org.example.gamble;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SlashCommands {

    public static final String INIT_GAMBLE_COMMAND_NAME = "gamble";

    public static final SlashCommandData INIT_GAMBLE = Commands.slash(INIT_GAMBLE_COMMAND_NAME, "Start new gamble");

    public static final String PLACE_BET_COMMAND_NAME = "bet";

    public static final SlashCommandData PLACE_BET = Commands.slash(PLACE_BET_COMMAND_NAME, "Place a bet")
            .addOption(OptionType.INTEGER, "amount", "Amount of points to bet", true);
}
