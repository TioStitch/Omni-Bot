package org.tiostitch.omnibot.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.OmniLogger;
import org.tiostitch.omnibot.abstraction.CommandImpl;
import org.tiostitch.omnibot.commands.types.*;

import java.util.ArrayList;

@RequiredArgsConstructor
public class CommandManager
extends ListenerAdapter {

    private final OmniCore omniCore;

    private final ArrayList<CommandImpl> registered_commands = new ArrayList<>();

    public void init() {

        registered_commands.add(new WikiCommand(omniCore));
        registered_commands.add(new GiveItemCommand(omniCore));
        registered_commands.add(new ProfileViewCommand(omniCore));
        registered_commands.add(new ProfileCommand(omniCore));
        registered_commands.add(new ShopCommand(omniCore));

        final CommandData[] loaded_commands = new CommandData[registered_commands.size()];

        for (int command_index = 0; command_index < registered_commands.size(); command_index++) {

            final CommandImpl commandImpl = registered_commands.get(command_index);

            commandImpl.init();

            loaded_commands[command_index] = commandImpl.getCommand();
        }

        omniCore.getBOT().updateCommands().addCommands(loaded_commands).queue();

        omniCore.getBOT().addEventListener(this);

        OmniLogger.success(registered_commands.size() + " Comandos carregados com sucesso!");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (CommandImpl command : registered_commands) {
            if (!event.getName().equalsIgnoreCase(command.getName())) continue;
            command.onSlashCommandAction(event);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        for (CommandImpl command : registered_commands) {
            if (!event.getName().equalsIgnoreCase(command.getName())) continue;
            command.onCommandAutoCompleteAction(event);
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (CommandImpl command : registered_commands) {
            command.onButtonInteraction(event);
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        for (CommandImpl command : registered_commands) {
            if (event.getModalId().contains(command.getName())) {
                command.onModalInteraction(event);
            }
        }
    }
}