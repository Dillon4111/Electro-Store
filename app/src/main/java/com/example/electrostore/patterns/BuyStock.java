package com.example.electrostore.patterns;

import com.example.electrostore.classes.Product;

public class BuyStock implements Command {
    private Product product;
    private int count;

    public BuyStock(Product product, int count){
        this.product = product;
        this.count = count;
    }

    public void execute() {
        product.buy(count);
    }
}
