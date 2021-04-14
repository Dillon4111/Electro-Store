package com.example.electrostore.classes;

import com.example.electrostore.patterns.Strategy;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class Product implements Serializable {

    private String id;
    private String name;
    private String category;
    private String description;
    private String manufacturer;
    private double price, overallRating;
    private int totalRatings, stockLevel;
    private List<String> images;

    public Product() {
    }

    public Product(String name, String category, String description,
                   String manufacturer, double price, int stockLevel, List<String> images) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.images = images;
        this.overallRating = 0;
        this.totalRatings = 0;
        this.stockLevel = stockLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
