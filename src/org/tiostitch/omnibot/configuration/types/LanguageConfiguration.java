package org.tiostitch.omnibot.configuration.types;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public final class LanguageConfiguration {

    //Universal Settings
    @SerializedName("UNIVERSAL_COIN_NAME")
    private String UNIVERSAL_COIN_NAME;

    //Profile Command
    @SerializedName("PROFILE_PERSONAL_CREATING")
    private String PROFILE_PERSONAL_CREATING;

    @SerializedName("PROFILE_PERSONAL_CREATED")
    private String PROFILE_PERSONAL_CREATED;

    @SerializedName("PROFILE_PERSONAL_DATA_RECEIVE_INVENTORY")
    private String PROFILE_PERSONAL_DATA_RECEIVE_INVENTORY;

    @SerializedName("PROFILE_PERSONAL_DATA_PER_ITEM")
    private String PROFILE_PERSONAL_DATA_PER_ITEM;

    @SerializedName("PROFILE_PERSONAL_DATA_RECEIVE")
    private String PROFILE_PERSONAL_DATA_RECEIVE;

    //ProfileView Command

    @SerializedName("PROFILE_VIEW_EMPTY_USER")
    private String PROFILE_VIEW_EMPTY_USER;

    @SerializedName("PROFILE_VIEW_NOT_FOUND")
    private String PROFILE_VIEW_NOT_FOUND;

    @SerializedName("PROFILE_VIEW_DATA_RECEIVE")
    private String PROFILE_VIEW_DATA_RECEIVE;

    @SerializedName("PROFILE_VIEW_DATA_RECEIVE_INVENTORY")
    private String PROFILE_VIEW_DATA_RECEIVE_INVENTORY;

    @SerializedName("PROFILE_VIEW_DATA_PER_ITEM")
    private String PROFILE_VIEW_DATA_PER_ITEM;

    //Wiki Command

    @SerializedName("WIKI_DATA_NOT_FOUND")
    private String WIKI_DATA_NOT_FOUND;

    @SerializedName("WIKI_DATA_RECEIVE")
    private String WIKI_DATA_RECEIVE;

}
