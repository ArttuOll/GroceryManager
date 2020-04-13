package com.bsuuv.grocerymanager;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class LogicTests {

    @Test
    public void parseJson() {
        FineliSearch fineliSearch = new FineliSearch();

        try {
            List<FoodItem> foodItems = fineliSearch.search("ruisleipä");
            foodItems.forEach(System.out::println);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
