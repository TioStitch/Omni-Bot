package org.tiostitch.omnibot.commands.types;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.abstraction.CommandImpl;
import org.tiostitch.omnibot.abstraction.DataRequestImpl;
import org.tiostitch.omnibot.abstraction.UserExtradata;
import org.tiostitch.omnibot.abstraction.Userdata;
import org.tiostitch.omnibot.configuration.items.Item;
import org.tiostitch.omnibot.configuration.items.EmojiInfo;
import org.tiostitch.omnibot.configuration.items.ShopInfo;
import org.tiostitch.omnibot.configuration.items.StoragedItem;
import org.tiostitch.omnibot.database.DataApi;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShopCommand
        implements CommandImpl {

    private final OmniCore omniCore;

    @Override
    public String getName() {
        return "loja";
    }

    @Override
    public String getDescription() {
        return "Comando para ver a loja.";
    }

    @Override
    public SlashCommandData getCommand() {

        final OptionData shopOptions = new OptionData(OptionType.STRING, "ver", "Digite o nome do que deseja buscar.", true);

        shopOptions.addChoice("Comprar", "Compre itens disponíveis na loja.");
        shopOptions.addChoice("Vender", "Venda itens na loja.");

        final OptionData pageOption = new OptionData(OptionType.INTEGER, "página", "Digite o número da página de busca.", true);

        return Commands.slash(getName(), getDescription())
                .addOptions(shopOptions, pageOption);
    }

    @Override
    public void onSlashCommandAction(@NotNull SlashCommandInteractionEvent event) {

        final String shopOption = event.getOptions().get(0).getAsString();
        final int shopPage = event.getOptions().get(1).getAsInt();

        if (shopOption.equalsIgnoreCase("Compre itens disponíveis na loja.")) {

            event.replyEmbeds(getShopBuyEmbed(shopPage))
                    .addActionRow(
                            Button.success("shopBuyItem", Emoji.fromFormatted("<:Icon_Buy:1297266015050141736>")))
                    .queue();

        } else if (shopOption.equalsIgnoreCase("Venda itens na loja.")) {
            event.replyEmbeds(getShopSellEmbed(shopPage))
                    .addActionRow(
                            Button.success("shopSellItem", Emoji.fromFormatted("<:Icon_Sell:1297266306088960171>")))
                    .queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        final String component = event.getComponentId();

        if (component.equalsIgnoreCase("shopBuyItem")) {

            TextInput amount = TextInput.create("amount", "Digite a quantidade do item:", TextInputStyle.SHORT)
                    .setPlaceholder("Digite a quantidade")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build();
            TextInput subject = TextInput.create("subject", "Digite o nome do item:", TextInputStyle.SHORT)
                    .setPlaceholder("Digite a quantidade")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build();

            Modal modal = Modal.create("loja_shopBuy", "Compra de Item")
                    .addActionRow(subject)
                    .addActionRow(amount)
                    .build();

            event.replyModal(modal).queue();
            return;
        }

        if (component.equalsIgnoreCase("shopSellItem")) {

            TextInput amount = TextInput.create("amount", "Digite a quantidade do item:", TextInputStyle.SHORT)
                    .setPlaceholder("Digite a quantidade")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build();

            TextInput subject = TextInput.create("subject", "Digite o nome do item:", TextInputStyle.SHORT)
                    .setPlaceholder("Digite a quantidade")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build();

            Modal modal = Modal.create("loja_shopSell", "Compra de Item")
                    .addActionRow(subject)
                    .addActionRow(amount)
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        final String modalId = event.getModalId();

        final String result = event.getValue("subject").getAsString();
        final int amount = Integer.parseInt(event.getValue("amount").getAsString());

        if (modalId.equalsIgnoreCase("loja_shopBuy")) {

            final Item item = omniCore.getItem().getItemByName(result);
            if (item == null || !item.getShopInfo().isBuy_enabled()) {
                event.reply("**[Central de Dados]** Não encontramos este item a venda em nossa loja!").queue();
                return;
            }

            final double price = (amount * item.getShopInfo().getBuy_price());
            if (price > DataApi.getUniversalCoin(event.getUser().getName())) {
                event.reply("**[CENTRAL DE DADOS]** Você não tem " + DataApi.getCoinName() + " o suficiente para comprar isto!").queue();
                return;
            }

            buyHandle(event.getUser(), item, amount, price);

            event.reply("**[CENTRAL DE DADOS]** Você comprou {amount}x {item_name} por {price} {universal_coins}"
                            .replace("{amount}", String.valueOf(amount))
                            .replace("{item_name}", item.getName())
                            .replace("{price}", String.valueOf(price))
                            .replace("{universal_coins}", DataApi.getCoinName()))
                    .queue();
            return;
        }

        if (modalId.equalsIgnoreCase("loja_shopSell")) {

            final Item item = omniCore.getItem().getItemByName(result);
            if (item == null || !item.getShopInfo().isSell_enabled()) {
                event.reply("**[Central de Dados]** Não compramos este item em nossa loja!").queue();
                return;
            }

            final double price = (amount * item.getShopInfo().getSell_price());
            if (!sellHandle(event.getUser(), item, amount, price)) {
                event.reply("**[Central de Dados]** Você não tem itens deste tipo suficientes para vender!").queue();
                return;
            }

            event.reply("**[CENTRAL DE DADOS]** Você vendeu {amount}x {item_name} por {price} {universal_coins}"
                            .replace("{amount}", String.valueOf(amount))
                            .replace("{item_name}", item.getName())
                            .replace("{price}", String.valueOf(price))
                            .replace("{universal_coins}", DataApi.getCoinName()))
                    .queue();
            return;
        }
    }

    public void buyHandle(User user, Item item, int amount, double price) {
        final DataRequestImpl request = omniCore.getDatabase().getRequest();

        final Userdata userdata = request.loadUserdata(user.getName(), user.getIdLong());
        final UserExtradata extradata = userdata.getExtradata();

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

        extradata.setCoins(Math.max(0, extradata.getCoins() - price));

        userdata.setExtradata(extradata);
        userdata.setBag(bag);

        request.updateUserdata(user.getName(), userdata);
    }

    public boolean sellHandle(User user, Item item, int amount, double receiveValue) {
        final DataRequestImpl request = omniCore.getDatabase().getRequest();

        final Userdata userdata = request.loadUserdata(user.getName(), user.getIdLong());
        final UserExtradata extradata = userdata.getExtradata();

        final ArrayList<StoragedItem> bag = userdata.getBag();

        if (userdata.containsInBag(item)) {
            final StoragedItem storagedItem = userdata.getStorageItemInBag(item);
            if (storagedItem == null) return false;

            storagedItem.setAmount(storagedItem.getAmount() - amount);
            bag.set(userdata.getItemIndexInBag(item), storagedItem);

            if (storagedItem.getAmount() <= 0) {
                bag.remove(storagedItem);
            }

            extradata.setCoins(extradata.getCoins() + receiveValue);

            userdata.setExtradata(extradata);
            userdata.setBag(bag);

            request.updateUserdata(user.getName(), userdata);
            return true;
        }

        return false;
    }

    public MessageEmbed getShopBuyEmbed(int page) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String TITLE = "Loja do OmniBot - Compra";
        final StringBuilder DESCRIPTION = new StringBuilder("Compre itens na loja usando suas moedas.\nOs itens comprados vão diretamente para o\nseu inventário.\n");

        // Obtendo a lista de itens que podem ser comprados
        List<Item> buyableItems = omniCore.getItem().getRegistered_items().values().stream()
                .filter(item -> item.getShopInfo().isBuy_enabled())
                .collect(Collectors.toList());

        // Calculando o início e o fim dos itens para a página atual
        final int itemsPerPage = 4;
        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, buyableItems.size());

        // Se não houver itens para a página, retorne uma mensagem indicando isso
        if (start >= buyableItems.size()) {
            DESCRIPTION.append("\nNenhum item disponível nesta página.");
        } else {
            List<Item> itemsToDisplay = buyableItems.subList(start, end);
            for (Item item : itemsToDisplay) {
                ShopInfo shopInfo = item.getShopInfo();
                EmojiInfo emojiInfo = item.getEmojiInfo();
                Emoji emoji = Emoji.fromCustom(emojiInfo.getName(), Long.parseLong(emojiInfo.getId()), emojiInfo.isAnimated());

                DESCRIPTION.append("\n(")
                        .append(emoji.getFormatted())
                        .append(") - **")
                        .append(item.getName())
                        .append("** (Preço: ")
                        .append(shopInfo.getBuy_price())
                        .append(")");
            }
        }

        embedBuilder.setTitle(TITLE)
                .setDescription(DESCRIPTION)
                .setColor(Color.CYAN)
                .setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();
    }

    public MessageEmbed getShopSellEmbed(int page) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String TITLE = "Loja do OmniBot - Venda";
        final StringBuilder DESCRIPTION = new StringBuilder("Venda itens na loja e receba moedas.\nOs itens vendidos serão consumidos\ndo seu inventário.\n");

        // Obtendo a lista de itens que podem ser vendidos
        List<Item> sellableItems = omniCore.getItem().getRegistered_items().values().stream()
                .filter(item -> item.getShopInfo().isSell_enabled())
                .collect(Collectors.toList());

        // Calculando o início e o fim dos itens para a página atual
        final int itemsPerPage = 4;
        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, sellableItems.size());

        // Se não houver itens para a página, retorne uma mensagem indicando isso
        if (start >= sellableItems.size()) {
            DESCRIPTION.append("\nNenhum item disponível nesta página.");
        } else {
            List<Item> itemsToDisplay = sellableItems.subList(start, end);
            for (Item item : itemsToDisplay) {
                ShopInfo shopInfo = item.getShopInfo();
                EmojiInfo emojiInfo = item.getEmojiInfo();
                Emoji emoji = Emoji.fromCustom(emojiInfo.getName(), Long.parseLong(emojiInfo.getId()), emojiInfo.isAnimated());

                DESCRIPTION.append("\n(")
                        .append(emoji.getFormatted())
                        .append(") - **")
                        .append(item.getName())
                        .append("** (Preço: ")
                        .append(shopInfo.getSell_price())
                        .append(")");
            }
        }

        embedBuilder.setTitle(TITLE)
                .setDescription(DESCRIPTION)
                .setColor(Color.CYAN)
                .setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();
    }
}
