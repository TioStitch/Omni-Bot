package org.tiostitch.omnibot.database;

import net.dv8tion.jda.api.entities.User;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.abstraction.DataRequestImpl;
import org.tiostitch.omnibot.abstraction.UserExtradata;
import org.tiostitch.omnibot.abstraction.Userdata;

public class DataApi {

    public static double getUniversalCoin(String username) {
        return OmniCore.instance.getDatabase().getRequest().getUserdata(username).getExtradata().getCoins();
    }

    public static String getCoinName() {
        return OmniCore.instance.getConfig().getLanguageConfiguration().getUNIVERSAL_COIN_NAME();
    }

    public static double withdrawCoins(String username, double withdraw) {
        final DataRequestImpl request = OmniCore.instance.getDatabase().getRequest();
        final Userdata userdata = request.getUserdata(username);
        final UserExtradata extradata = userdata.getExtradata();

        extradata.setCoins(Math.max(0, extradata.getCoins() - withdraw));
        userdata.setExtradata(extradata);

        request.updateUserdata(username, userdata);
        return extradata.getCoins();
    }

    public static double depositCoins(String username, double deposit) {
        final DataRequestImpl request = OmniCore.instance.getDatabase().getRequest();
        final Userdata userdata = request.getUserdata(username);
        final UserExtradata extradata = userdata.getExtradata();

        extradata.setCoins(extradata.getCoins() + deposit);
        userdata.setExtradata(extradata);

        request.updateUserdata(username, userdata);
        return extradata.getCoins();
    }
}
