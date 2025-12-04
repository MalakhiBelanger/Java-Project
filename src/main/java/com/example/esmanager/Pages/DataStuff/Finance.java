package com.example.esmanager.Pages.DataStuff;

import javafx.beans.property.*;

public class Finance {
    private final IntegerProperty id =  new SimpleIntegerProperty();
    private final StringProperty cat = new SimpleStringProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final StringProperty date = new SimpleStringProperty();


    //This one is for adding new finances since ID's are automated
    public Finance(String cat, double amount, String date) {
        this.cat.set(cat);
        this.amount.set(amount);
        this.date.set(date);
    }

    //This one is for loading from the database
    public Finance(int id, String cat, double amount, String date) {
        this.id.set(id);
        this.cat.set(cat);
        this.amount.set(amount);
        this.date.set(date);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty catProperty() {
        return cat;
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setCat(String cat) {
        this.cat.set(cat);
    }

    public String getCat() {
        return cat.get();
    }

    public int getId() {
        return id.get();
    }

    public double getAmount() {
        return amount.get();
    }

    public String getDate() {
        return date.get();
    }
}
