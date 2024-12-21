package org.tiostitch.omnibot.utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.tiostitch.omnibot.abstraction.UserExtradata;
import org.tiostitch.omnibot.configuration.items.Item;
import org.tiostitch.omnibot.configuration.items.StoragedItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

public class Serializer {

    public static String bagToJson(ArrayList<StoragedItem> items) {
        final Gson gson = new Gson();
        return gson.toJson(items);
    }

    public static String extraDataToJson(UserExtradata extradata) {
        final Gson gson = new Gson();
        return gson.toJson(extradata);
    }

    public static ArrayList<StoragedItem> bagFromJson(String json) {

        final Gson gson = new Gson();
        if (json.isEmpty() || json.equals("{}")) {
            return new ArrayList<>();
        }

        final Type type = new TypeToken<ArrayList<StoragedItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static UserExtradata extraDataFromJson(String json) {

        final Gson gson = new Gson();
        if (json.isEmpty() || json.equals("{}")) {
            return new UserExtradata();
        }

        return gson.fromJson(json, UserExtradata.class);
    }
}
