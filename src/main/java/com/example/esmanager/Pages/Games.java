package com.example.esmanager.Pages;


import com.example.esmanager.DatabaseStuff.Database;
import com.example.esmanager.Main;
import com.example.esmanager.Pages.DataStuff.Game;
import com.example.esmanager.Pages.DataStuff.Match;
import com.example.esmanager.UserStuff.CurrentUser;
import com.example.esmanager.UserStuff.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.esmanager.DatabaseStuff.DBConst.*;

public class Games extends VBox {
    private TableView<Game> table;
    private ObservableList<Game> data;
    private TilePane gameCovers;

    public Games(){
        this.getStyleClass().add("page");

        this.setPadding(new Insets(10));
        this.setSpacing(10);

        Button create = new Button("Add Game");
        Button delete = new Button("Delete Game");
        Button reload = new Button("Reload Table");
        Button recordMatch =  new Button("Record Match");

        HBox crud = new HBox(10,create,delete,reload, recordMatch);


        this.table = new TableView<>();

        TableColumn<Game, String> titleCol = new TableColumn<>("Game Title");
        TableColumn<Game, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Game, Integer> matchesCol = new TableColumn<>("Matches Played");
        TableColumn<Game, Integer> gamesWon  = new TableColumn<>("Games Won");
        TableColumn<Game, Integer> gamesLost = new TableColumn<>("Games Lost");

        titleCol.setCellValueFactory(c -> c.getValue().titleProperty());
        idCol.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        matchesCol.setCellValueFactory(c -> c.getValue().matchesPlayedProperty().asObject());
        gamesWon.setCellValueFactory(c -> c.getValue().matchesWonProperty().asObject());
        gamesLost.setCellValueFactory(c -> c.getValue().matchesLostProperty().asObject());


        titleCol.setPrefWidth(300);
        idCol.setPrefWidth(300);
        matchesCol.setPrefWidth(300);
        gamesWon.setPrefWidth(300);
        gamesLost.setPrefWidth(300);

        this.table.getColumns().addAll(titleCol, matchesCol, gamesWon, gamesLost);

        this.gameCovers = new TilePane();
        this.gameCovers.setPadding(new Insets(10));
        this.gameCovers.setHgap(10);
        this.gameCovers.setVgap(10);


        this.getChildren().addAll(crud,table,gameCovers);

        reload.setOnAction(e -> loadData());
        create.setOnAction(e -> handleCreate());
        delete.setOnAction(e -> handleDelete());
        recordMatch.setOnAction(e-> handleRecord());

        loadData();
    }


    private void updateGameCover(){
        this.gameCovers.getChildren().clear();

        for (Game game : data){
            this.gameCovers.getChildren().add(createGameCover(game));
        }
    }

    private VBox createGameCover(Game game){
        final int coverSize = 300;

        ImageView imageView =  new ImageView();
        imageView.setFitHeight(coverSize);
        imageView.setFitWidth(coverSize);

        if(game.getImagePath() != null && !game.getImagePath().isEmpty()){
            try{
                Image image = new Image(game.getImagePath(), (double) coverSize /2,coverSize, true, true);
                if(image.isError()){
                    System.out.println("Image dont work");
                    imageView.setImage(new Image("placeholder.png"));
                }else{
                    imageView.setImage(image);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            imageView.setImage(new Image("placeholder.png"));
        }

        VBox gameCover = new VBox(imageView);
        gameCover.setAlignment(Pos.TOP_CENTER);
        gameCover.setPadding(new Insets(10));

        gameCover.setOnMouseClicked(e->{
            table.getSelectionModel().select(game);
            table.scrollTo(game);
        });

        return gameCover;
    }

    private void handleRecord() {
        Game selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a game to record a match").showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Match Record");
        dialog.setHeaderText("Enter your teams score");

        dialog.showAndWait().ifPresent( scoreA -> {
            int score = Integer.parseInt(scoreA);


            TextInputDialog dialog2 = new TextInputDialog();
            dialog2.setTitle("Match Record");
            dialog2.setHeaderText("Enter opposing teams score");

            dialog2.showAndWait().ifPresent( scoreB -> {
                int score2 =  Integer.parseInt(scoreB);
                boolean matchWon = false;
                if(score>score2){
                    matchWon = true;
                }

                recordMatch(matchWon, score, score2, selected.getId());
                loadData();
            });
        });
    }



    private void handleCreate() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add New Game");
        titleDialog.setHeaderText("Enter game title");
        titleDialog.setContentText("Game Title:");

        titleDialog.showAndWait().ifPresent(title -> {
            String finalTitle = title.trim();
            if (finalTitle.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Game title cannot be empty").showAndWait();
                return;
            }


            TextInputDialog imageDialog = new TextInputDialog();
            imageDialog.setTitle("Add New Game");
            imageDialog.setHeaderText("Enter game cover url");
            imageDialog.setContentText("Image URL:");

            imageDialog.showAndWait().ifPresent(imagePath -> {
                String finalImagePath = imagePath.trim();


                if (addGame(finalTitle, finalImagePath)) {
                    loadData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save game to the database.").showAndWait();
                }
            });

        });
    }

    private void handleDelete() {
        Game selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a game to delete").showAndWait();
            return;
        }


        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the game '" + selected.getTitle() + "'? This is permanent.",
                ButtonType.YES, ButtonType.NO);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (deleteGame(selected.getId())) {
                    loadData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete game. Check if matches are still linked to it.").showAndWait();
                }
            }
        });
    }


    private boolean recordMatch(boolean won, int scoreA, int scoreB, int gameId) {
        String userId = CurrentUser.getUserId();
        if (userId == null) {
            new Alert(Alert.AlertType.ERROR, "Login required to add match").showAndWait();
            return false;
        }

        String sql = "INSERT INTO " + TABLE_MATCH + "(" +MATCH_COLUMN_ID + "," + MATCH_COLUMN_WON + "," + MATCH_COLUMN_SCORE_A + "," + MATCH_COLUMN_SCORE_B + "," + COLUMN_ID + "," + GAME_COLUMN_ID +
                ") VALUES (0,?,?,?,?,?);";
                /*"0," +
                " TRUE or FALSE," +
                " int," +
                " int," +
                " currentUser.id," +
                " selectedGame.id);";*/

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setBoolean(1, won);
            statement.setInt(2, scoreA);
            statement.setInt(3, scoreB);
            statement.setString(4, userId);
            statement.setInt(5, gameId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error adding game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    /*Methods that use sql queries*/
    private void loadData() {
        data = FXCollections.observableArrayList();
        String userId = CurrentUser.getUserId();

        if (userId == null) {
            System.err.println("Error: No user currently logged in.");
            table.setItems(data);
            updateGameCover();
            return;
        }

        String sql =
                "SELECT g." + GAME_COLUMN_ID + ", g." + GAME_COLUMN_TITLE + ", g." + GAME_COLUMN_IMAGE_PATH + ", COUNT(m." + MATCH_COLUMN_ID + ") AS matches_count " +
                        ", COUNT(CASE WHEN m." + MATCH_COLUMN_WON + " THEN 1 END) AS matches_won " +
                        "FROM " + TABLE_GAME + " g " +
                        "LEFT JOIN " + TABLE_MATCH + " m ON g." + GAME_COLUMN_ID + " = m." + GAME_COLUMN_ID + " " +
                        "WHERE g." + COLUMN_ID + " = ? " +
                        "GROUP BY g." + GAME_COLUMN_ID + ", g." + GAME_COLUMN_TITLE + ", g." + GAME_COLUMN_IMAGE_PATH;

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ArrayList<Match> matches = new ArrayList<>();
                int played = rs.getInt("matches_count");
                int won = rs.getInt("matches_won");
                int loss = played - won;
                Game game = new Game(
                        rs.getInt(GAME_COLUMN_ID),
                        rs.getString(GAME_COLUMN_TITLE),
                        rs.getString(GAME_COLUMN_IMAGE_PATH),
                        played,
                        won,
                        loss
                );

                // game.setMatchesPlayed(countMatches(game.getId()));
                data.add(game);
            }
            table.setItems(data);
            updateGameCover();
        } catch (SQLException e) {
            System.err.println("Database error loading games: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean addGame(String title, String imagePath) {
        String userId = CurrentUser.getUserId();
        if (userId == null) {
            new Alert(Alert.AlertType.ERROR, "Login required to add games.").showAndWait();
            return false;
        }

        String sql = "INSERT INTO " + TABLE_GAME + " (" +
                GAME_COLUMN_TITLE + ", " +
                GAME_COLUMN_IMAGE_PATH + ", " +
                COLUMN_ID + ") VALUES (?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, imagePath.isEmpty() ? null : imagePath); // Store null if empty
            statement.setString(3, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error adding game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteGame(int gameId) {
        String sql = "DELETE FROM " + TABLE_GAME + " WHERE " + GAME_COLUMN_ID + " = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, gameId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error deleting game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

