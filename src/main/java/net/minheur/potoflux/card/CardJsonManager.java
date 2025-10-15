package net.minheur.potoflux.card;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CardJsonManager {
    public static CardList fromJson(JsonObject json, boolean randomized) {
        if (json == null) return null;
        String name = json.get("name").toString();
        if (name == null) return null;
        JsonArray cardsJsonArray = json.getAsJsonArray("cards");
        List<Card> cardsArray = new ArrayList<>();

        for (int i = 0; i < cardsJsonArray.size(); i++) {
            JsonObject j = cardsJsonArray.get(i).getAsJsonObject();

            Card c = new Card();

            if (randomized) {
                if (Math.random() < 0.5) {
                    c.main = j.get("main").getAsString();
                    c.secondary = j.get("secondary").getAsString();
                } else {
                    c.secondary = j.get("main").getAsString();
                    c.main = j.get("secondary").getAsString();
                }
            } else {
                c.main = j.get("main").getAsString();
                c.secondary = j.get("secondary").getAsString();
            }

            if (c.main == null || c.secondary == null) return null;

            cardsArray.add(c);
        }

        CardList list = new CardList();

        list.name = name;
        list.cards = cardsArray;

        return list;
    }
}
