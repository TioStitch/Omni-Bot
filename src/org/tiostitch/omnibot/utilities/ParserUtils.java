package org.tiostitch.omnibot.utilities;

import org.tiostitch.omnibot.abstraction.Userdata;
import org.tiostitch.omnibot.database.DataApi;

public class ParserUtils {

    public static boolean parseCode(String code) {

        final String[] parsed_code = code.split(" ");

        switch (parsed_code[0]) {
            case "unlockDNA":
                unlockDNA(parsed_code[1], parsed_code[2]);
                return true;
            case "getCoins":
                DataApi.depositCoins(parsed_code[1], Double.parseDouble(parsed_code[2]));
                return true;
        }

        return false;
    }

    public static void unlockDNA(String username, String DNA) {

    }
}
