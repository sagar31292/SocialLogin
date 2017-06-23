package com.sagar.sociallogin;

/**
 * Created by sm185435 on 6/14/2017.
 */

public class Products {

    private String name;
    private String price;
    private String discount;



    public Products(String name, String price, String discount) {
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount() {

        return discount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {

        return name;
    }

    public String getPrice() {
        return price;
    }
}
