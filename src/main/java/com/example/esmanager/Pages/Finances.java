package com.example.esmanager.Pages;

import com.example.esmanager.DatabaseStuff.Database;
import com.example.esmanager.Pages.DataStuff.Finance;
import com.example.esmanager.UserStuff.CurrentUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.esmanager.DatabaseStuff.DBConst.*;

public class Finances extends VBox {
    private TableView<Finance> table;
    private ObservableList<Finance> data;


    public Finances() {
        this.getStyleClass().add("page");

        Button create = new Button("Add");
        Button delete = new Button("Delete");
        Button update = new Button("Edit");
        Button refresh = new Button("Refresh Table");

        HBox crud =  new HBox(10, create, delete, update, refresh);

        this.table = new TableView<>();

        TableColumn<Finance, String> cat = new TableColumn<>("Category");
        TableColumn<Finance, Double> amount =  new TableColumn<>("Amount");
        TableColumn<Finance, String> date =  new TableColumn<>("Date");


        cat.setMinWidth(200);
        amount.setMinWidth(200);
        date.setMinWidth(200);

        cat.setCellValueFactory(c -> c.getValue().catProperty());
        amount.setCellValueFactory(c -> c.getValue().amountProperty().asObject());
        date.setCellValueFactory(c -> c.getValue().dateProperty());

        this.table.getColumns().addAll(cat, amount, date);

        this.getChildren().addAll(crud, table);

        this.setSpacing(10);
        this.setPadding(new Insets(10));

        refresh.setOnAction(e -> {loadData();});
        create.setOnAction(e -> {handleCreate();});
        delete.setOnAction(e -> {handleDelete();});
        update.setOnAction(e -> {handleUpdate();});

        loadData();




    }

    private void handleCreate() {
        TextInputDialog categoryDialog = new TextInputDialog();
        categoryDialog.setTitle("Add New Finance Record");
        categoryDialog.setHeaderText("Step 1 of 2: Enter Record Category");
        categoryDialog.setContentText("Category:");


        categoryDialog.showAndWait().ifPresent(category -> {
            String finalCategory = category.trim();


            if (finalCategory.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Category cannot be empty. Creation cancelled.").showAndWait();
                return;
            }


            TextInputDialog amountDialog = new TextInputDialog();
            amountDialog.setTitle("Add New Finance Record");
            amountDialog.setHeaderText("Step 2 of 2: Enter Financial Amount");
            amountDialog.setContentText("Amount:");

            amountDialog.showAndWait().ifPresent(amountStr -> {
                String finalAmountStr = amountStr.trim();


                if (finalAmountStr.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Amount cannot be empty. Creation cancelled.").showAndWait();
                    return;
                }

                try {
                    double amount = Double.parseDouble(finalAmountStr);
                    String date = java.time.LocalDate.now().toString(); // Use current date


                    Finance newFinance = new Finance(finalCategory, amount, date);


                    if (addFinance(newFinance)) {
                        loadData();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to save record to the database.").showAndWait();
                    }

                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Input: Please enter a valid number for the amount.").showAndWait();
                }
            });
        });

    }

    private void handleDelete() {
        Finance selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a finance record to delete.").showAndWait();
            return;
        }

        if (deleteFinance(selected.getId())) {
            loadData();
        }
    }

    private void handleUpdate() {
        Finance selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a finance record to edit.").showAndWait();
            return;
        }

        TextInputDialog categoryDialog = new TextInputDialog(selected.catProperty().get());
        categoryDialog.setTitle("Edit Finance Record");
        categoryDialog.setHeaderText("Step 1 of 2: Edit Record Category");
        categoryDialog.setContentText("Category:");

        categoryDialog.showAndWait().ifPresent(category -> {
            String finalCategory = category.trim();

            if (finalCategory.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Category cannot be empty. Edit cancelled.").showAndWait();
                return;
            }

            TextInputDialog amountDialog = new TextInputDialog(String.valueOf(selected.amountProperty().get()));
            amountDialog.setTitle("Edit Finance Record");
            amountDialog.setHeaderText("Step 2 of 2: Edit Financial Amount");
            amountDialog.setContentText("Amount:");

            amountDialog.showAndWait().ifPresent(amountStr -> {
                String finalAmountStr = amountStr.trim();

                if (finalAmountStr.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Amount cannot be empty. Edit cancelled.").showAndWait();
                    return;
                }


                try {
                    double newAmount = Double.parseDouble(finalAmountStr);


                    selected.setCat(finalCategory);

                    selected.amountProperty().set(newAmount);


                    if (updateFinance(selected)) {
                        loadData();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to update record in the database.").showAndWait();
                    }

                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Input: Please enter a valid number for the amount.").showAndWait();
                }
            });
        });
    }

    private void loadData() {
        data = FXCollections.observableArrayList();
        String userId = CurrentUser.getUserId();

        if (userId == null) {
            System.err.println("Error: No user currently logged in.");
            table.setItems(data);
            return;
        }

        String sql = "SELECT * FROM " + TABLE_FINANCE + " WHERE " + COLUMN_ID + " = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, userId);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                data.add(new Finance(
                        res.getInt(FINANCE_COLUMN_ID),
                        res.getString(FINANCE_COLUMN_CATEGORY),
                        res.getDouble(FINANCE_COLUMN_AMOUNT),
                        res.getString(FINANCE_COLUMN_DATE)
                ));
            }
            table.setItems(data);
        } catch (SQLException e) {
            System.err.println("Database error loading finances: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean addFinance(Finance finance) {
        String userId = CurrentUser.getUserId();
        if (userId == null) {
            new Alert(Alert.AlertType.ERROR, "Login required to add finance records.").showAndWait();
            return false;
        }

        String sql = "INSERT INTO " + TABLE_FINANCE + " (" +
                FINANCE_COLUMN_CATEGORY + ", " +
                FINANCE_COLUMN_AMOUNT + ", " +
                FINANCE_COLUMN_DATE + ", " +
                COLUMN_ID + ") VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, finance.getCat());

            statement.setDouble(2, finance.amountProperty().get());
            statement.setString(3, finance.dateProperty().get());
            statement.setString(4, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error adding finance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateFinance(Finance finance) {
        String sql = "UPDATE " + TABLE_FINANCE + " SET " +
                FINANCE_COLUMN_CATEGORY + " = ?, " +
                FINANCE_COLUMN_AMOUNT + " = ?, " +
                FINANCE_COLUMN_DATE + " = ? WHERE " +
                FINANCE_COLUMN_ID + " = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, finance.catProperty().get());
            statement.setDouble(2, finance.amountProperty().get());
            statement.setString(3, finance.dateProperty().get());
            statement.setInt(4, finance.idProperty().get());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error updating finance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteFinance(int financeId) {
        String sql = "DELETE FROM " + TABLE_FINANCE + " WHERE " + FINANCE_COLUMN_ID + " = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, financeId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error deleting finance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
