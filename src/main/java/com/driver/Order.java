package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        String[] c = deliveryTime.split(":");
        int h = Integer.parseInt(c[0])*60;
        int m = Integer.parseInt(c[1]);
        this.deliveryTime = h+m;
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}
