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
import org.tiostitch.omnibot.abstraction.DataRequestImpl;
import org.tiostitch.omnibot.abstraction.Userdata;
import org.tiostitch.omnibot.configuration.items.Item;
import org.tiostitch.omnibot.configuration.items.EmojiInfo;
import org.tiostitch.omnibot.configuration.ItemManager;
import org.tiostitch.omnibot.configuration.items.StoragedItem;
import org.tiostitch.omnibot.configuration.types.LanguageConfiguration;

import java.util.ArrayList;

@RequiredArgsConstructor
public class GiveItemCommand
implements CommandImpl {

    private final OmniCore omniCore;
    private String[] available_items = null;

    @Override
    public String getName() {
        return "giveitem";
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
        OptionData optionData = new OptionData(OptionType.STRING, "item", "Digite o nome do item que deseja receber", true);
        OptionData amountData = new OptionData(OptionType.INTEGER, "quantidade", "Digite a quantidade que deseja receber", true);

        for (String opcao : available_items) {
            optionData.addChoice(opcao, opcao);
        }

        return Commands.slash(getName(), getDescription())
                .addOptions(optionData, amountData);
    }

    @Override
    public void onSlashCommandAction(@NotNull SlashCommandInteractionEvent event) {

        final LanguageConfiguration langCfg = omniCore.getConfig().getLanguageConfiguration();
        final DataRequestImpl request = omniCore.getDatabase().getRequest();

        final String username = event.getUser().getName();
        final long discord_id = event.getUser().getIdLong();

        final String notFound = langCfg.getWIKI_DATA_NOT_FOUND();
        final Item item = omniCore.getItem().getItemByName(event.getOptions().get(0).getAsString());
        if (item == null) {
            event.reply(notFound).queue();
            return;
        }

        final EmojiInfo itemEmoji = item.getEmojiInfo();
        final Emoji customEmoji = Emoji.fromCustom(itemEmoji.getName(), Long.parseLong(itemEmoji.getId()), itemEmoji.isAnimated());

        final int amount = event.getOptions().get(1).getAsInt();

        final Userdata userdata = request.loadUserdata(username, discord_id);
        final ArrayList<StoragedItem> bag = userdata.getBag();

        if (userdata.containsInBag(item)) {

            final StoragedItem storagedItem = userdata.getStorageItemInBag(item);
            if (storagedItem == null) return;

            storagedItem.setAmount(storagedItem.getAmount() + amount);

            bag.set(userdata.getItemIndexInBag(item), storagedItem);
        } else {

            final StoragedItem storagedItem = new StoragedItem();
            storagedItem.setItem_info(item);
            storagedItem.setAmount(amount);

            bag.add(storagedItem);
        }

        userdata.setBag(bag);
        request.updateUserdata(username, userdata);

        event.reply("**[CENTRAL DE DADOS]** Enviando para seu inventário: {item_emoji} - {item_amount}x **{item_name}**"
                        .replace("{item_emoji}", customEmoji.getFormatted())
                        .replace("{item_amount}", String.valueOf(amount))
                        .replace("{item_name}", item.getName()))
                .queue();
    }
}
