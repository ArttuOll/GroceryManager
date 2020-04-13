package com.bsuuv.grocerymanager;

import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class FineliSearch {

    List<FoodItem> search(String query) throws IOException, JSONException {
        String urlString = "https://fineli.fi/fineli/api/v1/foods?q=" + query;
        URL url = new URL(urlString);

        try (InputStream stream = url.openStream()) {
            JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            List<FoodItem> results = new ArrayList<>();

            //Alkaa lukemaan taulukon arvoja.
            reader.beginArray();
            while (reader.hasNext()) {
                results.add(readFoodItem(reader));
            }
            reader.endArray();

            return results;
        }
    }

    private FoodItem readFoodItem(JsonReader reader) throws IOException {
        FoodItem foodItem = new FoodItem();

        //Lukee taulukon arvona olevan yksittäisen objektin.
        reader.beginObject();
        while (reader.hasNext()) {
            //Name tarkoittaa Json-olion kentän nimeä.
            String name = reader.nextName();

            switch (name) {
                case "id":
                    foodItem.setId(reader.nextInt());
                case "name":
                    foodItem.setName(readName(reader));
                case "salt":
                    foodItem.setSalt(reader.nextDouble());
                case "energyKcal":
                    foodItem.setEnergy(reader.nextDouble());
                case "fat":
                    foodItem.setFat(reader.nextDouble());
                case "protein":
                    foodItem.setProtein(reader.nextDouble());
                case "carbohydrate":
                    foodItem.setCarbs(reader.nextDouble());
                case "alcohol":
                    foodItem.setAlcohol(reader.nextDouble());
                case "fiber":
                    foodItem.setFiber(reader.nextDouble());
                case "sugar":
                    foodItem.setSugar(reader.nextDouble());
                case "organicAcids":
                    foodItem.setOrgAcids(reader.nextDouble());
                case "sugarAlcohol":
                    foodItem.setSugarAlcohol(reader.nextDouble());
                case "saturatedFat":
                    foodItem.setSatFat(reader.nextDouble());
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return foodItem;
    }

    private String readName(JsonReader reader) throws IOException {
        String currentLocale = Locale.getDefault().toString();
        String foodItemName = "";

        //Lukee taulukossa olevan yksittäisen objektin nimi-objektin.
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("fi") && currentLocale.contains("fi")) {
                foodItemName = reader.nextString();
            } else if (name.equals("en") && currentLocale.contains("en")) {
                foodItemName = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return foodItemName;
    }
}
