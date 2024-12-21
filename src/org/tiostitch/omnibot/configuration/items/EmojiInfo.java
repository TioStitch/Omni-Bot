package org.tiostitch.omnibot.configuration.items;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public final class EmojiInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("animated")
    private boolean animated;

}
