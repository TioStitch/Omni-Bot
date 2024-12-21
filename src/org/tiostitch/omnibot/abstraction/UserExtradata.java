package org.tiostitch.omnibot.abstraction;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public final class UserExtradata {

    @SerializedName("coins")
    private double coins = 0.0;

    @SerializedName("last_loot_box")
    private long lastLootBox = 0L;

    @SerializedName("last_daily_box")
    private long lastDailyBox = 0L;

}
