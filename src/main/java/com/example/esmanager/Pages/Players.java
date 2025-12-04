package com.example.esmanager.Pages;

import com.example.esmanager.DatabaseStuff.Database;
import com.example.esmanager.Pages.DataStuff.Player;
import com.example.esmanager.UserStuff.CurrentUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import static com.example.esmanager.DatabaseStuff.DBConst.*;

public class Players extends VBox {
    private TableView<Player> table;
    private ObservableList<Player> data;

    public Players() {
        this.getStyleClass().add("page");
        this.setPadding(new Insets(10));
        this.setSpacing(10);

        Button create = new Button("Add Player");
        Button delete = new Button("Delete Player");
        Button refresh = new Button("Refresh Table");

        HBox crud = new HBox(10, create, delete, refresh);

        this.table = new TableView<>();

        TableColumn<Player, String> nameCol = new TableColumn<>("Name");
        TableColumn<Player, Integer> numberCol = new TableColumn<>("#");
        TableColumn<Player, String> positionCol = new TableColumn<>("Position");

        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        numberCol.setCellValueFactory(c -> c.getValue().numberProperty().asObject());
        positionCol.setCellValueFactory(c -> c.getValue().positionProperty());

        nameCol.setPrefWidth(250);
        numberCol.setPrefWidth(100);
        positionCol.setPrefWidth(200);

        this.table.getColumns().addAll(nameCol, numberCol, positionCol);

        this.getChildren().addAll(crud, table);

        refresh.setOnAction(e -> loadData());
        create.setOnAction(e -> handleCreate());
        delete.setOnAction(e -> handleDelete());

        loadData();
    }

    /*Handler methods (literally are only here to make things look nice)*/

    private void handleCreate() {
        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle("Add New Player");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField numberField = new TextField();
        TextField positionField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Number:"), 0, 1);
        grid.add(numberField, 1, 1);
        grid.add(new Label("Position/Main Character:"), 0, 2);
        grid.add(positionField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        javafx.application.Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText().trim();
                String numberStr = numberField.getText().trim();
                String position = positionField.getText().trim();

                if (name.isEmpty() || numberStr.isEmpty() || position.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "All fields must be inputted").showAndWait();
                    return null;
                }
                try {
                    int number = Integer.parseInt(numberStr);
                    return new Player(name, number, position);
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Input: Please enter a valid number for the number field.").showAndWait();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newPlayer -> {
            if (addPlayer(newPlayer)) {
                loadData();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save player record").showAndWait();
            }
        });
    }

    private void handleDelete() {
        Player selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a player to delete").showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete player '" + selected.getName() + "'?",
                ButtonType.YES, ButtonType.NO);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (deletePlayer(selected.getId())) {
                    loadData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete player.").showAndWait();
                }
            }
        });
    }

    /*methods that do sql stuff again*/


    private void loadData() {
        data = FXCollections.observableArrayList();
        String userId = CurrentUser.getUserId();

        if (userId == null) {
            table.setItems(data);
            return;
        }

        String sql = "SELECT * FROM " + TABLE_PLAYER + " WHERE " + COLUMN_ID + " = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                data.add(new Player(
                        rs.getInt(PLAYER_COLUMN_ID),
                        rs.getString(PLAYER_COLUMN_NAME),
                        rs.getInt(PLAYER_COLUMN_NUMBER),
                        rs.getString(PLAYER_COLUMN_POSITION)
                ));
            }
            table.setItems(data);
        } catch (SQLException e) {
            System.err.println("Database error loading players: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private boolean addPlayer(Player player) {
        String userId = CurrentUser.getUserId();
        if (userId == null) return false;

        String sql = "INSERT INTO " + TABLE_PLAYER + " (" +
                PLAYER_COLUMN_NAME + ", " +
                PLAYER_COLUMN_NUMBER + ", " +
                PLAYER_COLUMN_POSITION + ", " +
                COLUMN_ID + ") VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, player.getName());
            statement.setInt(2, player.getNumber());
            statement.setString(3, player.getPosition());
            statement.setString(4, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error adding player: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private boolean deletePlayer(int playerId) {
        String sql = "DELETE FROM " + TABLE_PLAYER + " WHERE " + PLAYER_COLUMN_ID + " = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, playerId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error deleting player: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
