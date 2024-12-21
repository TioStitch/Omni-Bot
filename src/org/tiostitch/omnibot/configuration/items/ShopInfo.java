package org.tiostitch.omnibot.configuration.items;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ShopInfo {

    @SerializedName("buy_enabled")
    private boolean buy_enabled;

    @SerializedName("buy_price")
    private double buy_price;

    @SerializedName("sell_enabled")
    private boolean sell_enabled;

    @SerializedName("sell_price")
    private double sell_price;

}
