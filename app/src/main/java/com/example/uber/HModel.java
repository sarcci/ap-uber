package com.example.uber;

import java.util.Date;

public class HModel {
    String name;
    String datum;
    int ID;


    public HModel(String name, String datum, int ID) {
        this.name = name;
        this.datum = datum;
        this.ID = ID;
    }
    public String getName() {
        return name;
    }

    public String getDatum() {
        return datum;
    }

    public int getID() {return ID;}
}
