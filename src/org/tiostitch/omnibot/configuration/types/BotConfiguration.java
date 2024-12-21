package org.tiostitch.omnibot.configuration.types;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.OnlineStatus;

@Getter @Setter
@NoArgsConstructor
public final class BotConfiguration {

    public enum Activity {
        PLAYING,
        LISTENING,
        STREAMING,
        WATCHING,
        COMPETING;
    }

    @SerializedName("ACTIVITY")
    private Activity ACTIVITY;

    @SerializedName("ACTIVITY_MESSAGE")
    private String ACTIVITY_MESSAGE;

    @SerializedName("ONLINE_STATUS")
    private OnlineStatus ONLINE_STATUS;

    @SerializedName("STREAMING_URL")
    private String STREAMING_URL;

}
