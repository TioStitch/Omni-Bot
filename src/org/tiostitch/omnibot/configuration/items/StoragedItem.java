package org.tiostitch.omnibot.configuration.items;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class StoragedItem {

    @SerializedName("item_info")
    private Item item_info;

    @SerializedName("amount")
    private int amount;

}
