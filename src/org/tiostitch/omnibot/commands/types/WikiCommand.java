package org.tiostitch.omnibot.commands.types;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.abstraction.CommandImpl;
import org.tiostitch.omnibot.configuration.items.Item;
import org.tiostitch.omnibot.configuration.items.EmojiInfo;
import org.tiostitch.omnibot.configuration.ItemManager;

@RequiredArgsConstructor
public class WikiCommand
implements CommandImpl {

    private final OmniCore omniCore;
    private String[] available_items = null;

    @Override
    public String getName() {
        return "wiki";
    }

    @Override
    public String getDescription() {
        return "Comando de busca de informações dos itens.";
    }

    @Override
    public void init() {

        final ItemManager itemManager = omniCore.getItem();
        final int items_size = itemManager.getRegistered_items().size();

        available_items = new String[items_size];

        for (int item_index = 1; item_index <= items_size; item_index++) {
            final Item item = itemManager.getRegistered_items().get(item_index);
            available_items[item_index - 1] = item.getName();
        }
    }

    @Override
    public SlashCommandData getCommand() {
        OptionData optionData = new OptionData(OptionType.STRING, "item", "Digite o nome do item que deseja buscar", true);

        for (String opcao : available_items) {
            optionData.addChoice(opcao, opcao);
        }

        return Commands.slash(getName(), getDescription())
                .addOptions(optionData);
    }

    @Override
    public void onSlashCommandAction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("wiki")) {

            final String notFound = omniCore.getConfig().getLanguageConfiguration().getWIKI_DATA_NOT_FOUND();

            final Item item = omniCore.getItem().getItemByName(event.getOptions().get(0).getAsString());
            if (item == null) {
                event.reply(notFound).queue();
                return;
            }

            final EmojiInfo itemEmoji = item.getEmojiInfo();

            final Emoji customEmoji = Emoji.fromCustom(itemEmoji.getName(), Long.parseLong(itemEmoji.getId()), itemEmoji.isAnimated());

            final String dataReceive = omniCore.getConfig().getLanguageConfiguration().getWIKI_DATA_RECEIVE()
                    .replace("{item_name}", item.getName())
                    .replace("{item_description}", item.getDescription())
                    .replace("{item_isCraftable}", item.isCraftable() ? "Sim" : "Não")
                    .replace("{item_emoji}", customEmoji.getFormatted());

            event.reply(dataReceive).queue();
        }
    }
}
