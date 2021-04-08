package com.example.electrostore.classes;

import java.util.ArrayList;

public class Order {

    private String id, userID;
    private ArrayList<Product> products = new ArrayList<>();

    public Order() {
    }

    public Order(String userID, ArrayList<Product> products) {
        this.userID = userID;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void addToOrder(Product p) {
        products.add(p);
    }

    public void removeProduct(Product p) {
        products.remove(p);
    }
}
