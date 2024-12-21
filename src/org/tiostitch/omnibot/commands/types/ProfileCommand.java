package org.tiostitch.omnibot.commands.types;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.abstraction.CommandImpl;
import org.tiostitch.omnibot.abstraction.DataRequestImpl;
import org.tiostitch.omnibot.abstraction.UserExtradata;
import org.tiostitch.omnibot.abstraction.Userdata;
import org.tiostitch.omnibot.configuration.items.Item;
import org.tiostitch.omnibot.configuration.items.EmojiInfo;
import org.tiostitch.omnibot.configuration.items.StoragedItem;
import org.tiostitch.omnibot.configuration.types.LanguageConfiguration;
import org.tiostitch.omnibot.utilities.TextUtils;

@RequiredArgsConstructor
public class ProfileCommand
implements CommandImpl {

    private final OmniCore omniCore;

    @Override
    public String getName() {
        return "perfil";
    }

    @Override
    public String getDescription() {
        return "Veja suas informações.";
    }

    @Override
    public SlashCommandData getCommand() {
        return Commands.slash(getName(), getDescription())
                .addOption(OptionType.BOOLEAN, "inventário", "Caso ativo, mostra seu inventário", true);
    }

    @Override
    public void onSlashCommandAction(@NotNull SlashCommandInteractionEvent event) {

        final LanguageConfiguration langCfg = omniCore.getConfig().getLanguageConfiguration();
        final DataRequestImpl request = omniCore.getDatabase().getRequest();

        final String username = event.getUser().getName();
        final long discord_id = event.getUser().getIdLong();

        final String CREATE_USER = langCfg.getPROFILE_PERSONAL_CREATING();
        if (!request.existUserById(discord_id)) {

            event.reply(CREATE_USER).queue();

            if (request.loadUserdata(username, discord_id) != null) {
                final String CREATED_USER = langCfg.getPROFILE_PERSONAL_CREATED();
                event.reply(CREATED_USER).queue();
            }

            return;
        }

        boolean isInventoryView = event.getOptions().get(0).getAsBoolean();
        final Userdata userdata = request.loadUserdata(username, discord_id);

        if (isInventoryView) {
            event.reply(getInventoryView(userdata, langCfg)).queue();
            return;
        }

        final String DATA_RECEIVE = getUserInfo(userdata, langCfg);
        event.reply(DATA_RECEIVE).queue();
    }

    @NotNull
    private static String getInventoryView(Userdata userdata, LanguageConfiguration langCfg) {
        final StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append(langCfg.getPROFILE_PERSONAL_DATA_RECEIVE_INVENTORY());

        if (!userdata.getBag().isEmpty()) {
            for (StoragedItem storagedItem : userdata.getBag()) {

                final Item item = storagedItem.getItem_info();

                final EmojiInfo itemEmoji = item.getEmojiInfo();
                final Emoji emoji = Emoji.fromCustom(itemEmoji.getName(), Long.parseLong(itemEmoji.getId()), itemEmoji.isAnimated());

                String item_string = langCfg.getPROFILE_PERSONAL_DATA_PER_ITEM()
                        .replace("{item_name}", item.getName())
                        .replace("{item_amount}", String.valueOf(storagedItem.getAmount()))
                        .replace("{item_description}", item.getDescription())
                        .replace("{item_isCraftable}", item.isCraftable() ? "Sim" : "Não")
                        .replace("{item_emoji}", emoji.getFormatted());

                messageBuilder.append(item_string);
            }
        } else {
            messageBuilder.append("Não há nada em seu inventário!");
        }

        return messageBuilder.toString();
    }

    @NotNull
    private static String getUserInfo(Userdata userdata, LanguageConfiguration langCfg) {
        final UserExtradata extraData = userdata.getExtradata();

        return langCfg.getPROFILE_PERSONAL_DATA_RECEIVE()
                .replace("{UNIVERSAL_COINS_NAME}", langCfg.getUNIVERSAL_COIN_NAME())
                .replace("{user_name}", userdata.getName())
                .replace("{extra_coins}", String.valueOf(extraData.getCoins()))
                .replace("{extra_last_loot_box}", TextUtils.formatTimeLeft(extraData.getLastLootBox()))
                .replace("{extra_last_daily_box}",  TextUtils.formatTimeLeft(extraData.getLastDailyBox()));
    }
}
