package org.tiostitch.omnibot.configuration.items;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public final class Item {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("isCraftable")
    private boolean isCraftable;

    @SerializedName("craft")
    private List<String> craft;

    @SerializedName("emoji_info")
    private EmojiInfo emojiInfo;

    @SerializedName("shop_info")
    private ShopInfo shopInfo;

}
