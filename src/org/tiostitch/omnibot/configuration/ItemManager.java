package org.tiostitch.omnibot.configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.OmniLogger;
import org.tiostitch.omnibot.configuration.items.Item;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter @Setter
@RequiredArgsConstructor
public class ItemManager {

    private final OmniCore omniCore;

    private final Map<Integer, Item> registered_items = new LinkedHashMap<>();

    public void init() {

        final Configuration configuration = omniCore.getConfig();
        final Gson gson = new Gson();

        final Type type = new TypeToken<Map<Integer, Item>>(){}.getType();

        Map<Integer, Item> itemsMap = gson.fromJson(configuration.getJsonByFile("itemConfiguration.json"), type);

        registered_items.putAll(itemsMap);

        OmniLogger.success(registered_items.size() + " Itens carregados com sucesso!");
    }

    public Item getItemById(int id) {
        return registered_items.get(id);
    }

    public Item getItemByName(String name) {

        Item result_item = null;

        for (Item item : registered_items.values()) {
            if (!item.getName().equalsIgnoreCase(name)) continue;
            result_item = item;
            break;
        }

        return result_item;
    }

}
