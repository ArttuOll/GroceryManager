package com.bsuuv.grocerymanager;

/**
 * A class representing a food-item in a meal.
 * There is currently a total of 57 nutrients in a food-item and they are grouped into maps to keep them organized.
 */
public class FoodItem {

    private int id;
    private String name;
    private double salt;
    private double energy;
    private double fat;
    private double protein;
    private double carbs;
    private double alcohol;
    private double fiber;
    private double sugar;
    private double orgAcids;
    private double sugarAlcohol;
    private double satFat;

    FoodItem() {
    }

    FoodItem(int id, double salt, int energy, double fat, double protein, double carbs,
             double alcohol, double fiber, double sugar, double orgAcids,
             double sugarAlcohol, double satFat) {
        this.id = id;
        this.salt = salt;
        this.energy = energy;
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
        this.alcohol = alcohol;
        this.fiber = fiber;
        this.sugar = sugar;
        this.orgAcids = orgAcids;
        this.sugarAlcohol = sugarAlcohol;
        this.satFat = satFat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSalt() {
        return salt;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(double alcohol) {
        this.alcohol = alcohol;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getOrgAcids() {
        return orgAcids;
    }

    public void setOrgAcids(double orgAcids) {
        this.orgAcids = orgAcids;
    }

    public double getSugarAlcohol() {
        return sugarAlcohol;
    }

    public void setSugarAlcohol(double sugarAlcohol) {
        this.sugarAlcohol = sugarAlcohol;
    }

    public double getSatFat() {
        return satFat;
    }

    public void setSatFat(double satFat) {
        this.satFat = satFat;
    }
}