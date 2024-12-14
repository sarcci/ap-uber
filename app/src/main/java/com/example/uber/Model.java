package com.example.uber;

public class Model {
    String name;
    String vehicle;
    Float rating;
    int price;
    int driver;

    public Model(Float rating, String name, String vehicle, int price, int driver) {
        this.rating = rating;
        this.name = name;
        this.vehicle = vehicle;
        this.price=price;
        this.driver = driver;

    }

    public String getName() {
        return name;
    }

    public String getVehicle() {
        return vehicle;
    }

    public Float getRating() {
        return rating;
    }
    public int getPrice() {
        return price;
    }

    public int getDriver() {return driver;}

}
