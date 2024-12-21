package org.tiostitch.omnibot.abstraction;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

public interface CommandImpl {

    String getName();

    String getDescription();

    SlashCommandData getCommand();

    default void init() {
        return;
    }

    default void onSlashCommandAction(@NotNull SlashCommandInteractionEvent event) {
        return;
    }

    default void onCommandAutoCompleteAction(CommandAutoCompleteInteractionEvent event) {
        return;
    }

    default void onButtonInteraction(ButtonInteractionEvent event) {
        return;
    }

    default void onModalInteraction(ModalInteractionEvent event) {
        return;
    }

}
