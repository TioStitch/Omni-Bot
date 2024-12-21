package org.tiostitch.omnibot.abstraction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tiostitch.omnibot.configuration.items.Item;
import org.tiostitch.omnibot.configuration.items.StoragedItem;

import java.util.ArrayList;

@Getter @Setter
@NoArgsConstructor
public final class Userdata {

    private String name;
    private long discId;
    private UserExtradata extradata;
    private ArrayList<StoragedItem> bag;

    public Userdata getEmptyUserdata(String key) {
        final Userdata userdata = new Userdata();

        userdata.setName(key);
        userdata.setBag(new ArrayList<>());

        userdata.setExtradata(new UserExtradata());

        return userdata;
    }

    public boolean containsInBag(Item item) {
        if (bag.isEmpty()) return false;
        return bag.stream().anyMatch(storagedItem -> storagedItem.getItem_info().getName().equalsIgnoreCase(item.getName()));
    }

    public int getItemIndexInBag(Item item) {
        if (bag.isEmpty()) return -1;

        int itemIndex = 0;
        for (StoragedItem storagedItem : bag) {
            if (storagedItem.getItem_info().getName().equalsIgnoreCase(item.getName())) {
                break;
            }

            itemIndex++;
        }

        return itemIndex;
    }

    public StoragedItem getStorageItemInBag(Item item) {
        if (bag.isEmpty()) return null;

        for (StoragedItem storagedItem : bag) {
            if (storagedItem.getItem_info().getName().equalsIgnoreCase(item.getName())) {
                return storagedItem;
            }
        }

        return null;
    }
}
