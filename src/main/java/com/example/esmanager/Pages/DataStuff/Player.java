package com.example.esmanager.Pages.DataStuff;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty number = new SimpleIntegerProperty();
    private final StringProperty position = new SimpleStringProperty();


    public Player(int id, String name, int number, String position) {
        this.id.set(id);
        this.name.set(name);
        this.number.set(number);
        this.position.set(position);
    }


    public Player(String name, int number, String position) {
        this.name.set(name);
        this.number.set(number);
        this.position.set(position);
    }


    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty numberProperty() { return number; }
    public StringProperty positionProperty() { return position; }


    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public int getNumber() { return number.get(); }
    public String getPosition() { return position.get(); }

    public void setName(String name) { this.name.set(name); }
    public void setNumber(int number) { this.number.set(number); }
    public void setPosition(String position) { this.position.set(position); }
}
