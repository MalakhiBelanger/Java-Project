package com.example.esmanager.Pages.DataStuff;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Game {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty imagePath = new SimpleStringProperty();

    private final IntegerProperty matchesPlayed = new SimpleIntegerProperty(0);

    public Game(int id, String title, String imagePath) {
        this.id.set(id);
        this.title.set(title);
        this.imagePath.set(imagePath);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public IntegerProperty matchesPlayedProperty() { return matchesPlayed; }
    public StringProperty imagePathProperty() { return imagePath; }

    /*geter :P*/
    public int getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getImagePath() { return imagePath.get(); }

    /*seter :D*/
    public void setMatchesPlayed(int count) { this.matchesPlayed.set(count); }
    public void setTitle(String title) { this.title.set(title); }
    public void setImagePath(String imagePath) { this.imagePath.set(imagePath); }
}
