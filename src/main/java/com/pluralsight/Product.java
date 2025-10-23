package com.pluralsight;

public class Product {
    private String sku;
    private String productName;
    private double price;


    public Product(String sku, String productName, double price) {
        this.sku = sku;
        this.productName = productName;
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {

        return String.format("%-25s|%-40s| $%.2f", sku, productName, price);
    }
}
