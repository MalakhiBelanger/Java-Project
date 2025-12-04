package com.example.esmanager.Pages;

import com.example.esmanager.DatabaseStuff.Database;
import com.example.esmanager.UserStuff.CurrentUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import static com.example.esmanager.DatabaseStuff.DBConst.*;

public class Stats extends VBox {

    private final TilePane statWidgets;
    private final Label pageTitle;

    public Stats() {
        this.getStyleClass().add("page");
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setAlignment(Pos.TOP_CENTER);

        pageTitle = new Label("Team Performance Overview");
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        pageTitle.getStyleClass().add("page-title");

        statWidgets = new TilePane();
        statWidgets.setHgap(20);
        statWidgets.setVgap(20);
        statWidgets.setPrefColumns(3);
        statWidgets.setAlignment(Pos.CENTER);

        this.getChildren().addAll(pageTitle, statWidgets);

        loadStatsData();
    }


    private VBox createStatWidget(String title, String value, String unit, String styleClass) {
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        valueLabel.getStyleClass().add("stat-value");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        titleLabel.getStyleClass().add("stat-title");

        Label unitLabel = new Label(unit);
        unitLabel.setFont(Font.font("Arial", FontWeight.LIGHT, 12));
        unitLabel.getStyleClass().add("stat-unit");

        VBox contentBox = new VBox(5, valueLabel, titleLabel, unitLabel);
        contentBox.setAlignment(Pos.CENTER);

        VBox widget = new VBox(contentBox);
        widget.setAlignment(Pos.CENTER);
        widget.setPrefSize(200, 150);
        widget.setPadding(new Insets(15));

        widget.setStyle("-fx-background-color: #f7f7f7; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        widget.getStyleClass().add("stat-widget");
        widget.getStyleClass().add(styleClass);

        return widget;
    }

    public void loadStatsData() {
        statWidgets.getChildren().clear();
        String userId = CurrentUser.getUserId();
        if (userId == null) {
            System.err.println("Error: No user currently logged in. Cannot load stats.");
            return;
        }


        String matchSql =
                "SELECT COUNT(" + MATCH_COLUMN_ID + ") AS total_matches, " +
                        "SUM(CASE WHEN " + MATCH_COLUMN_WON + " = TRUE THEN 1 ELSE 0 END) AS total_wins, " +
                        "AVG(" + MATCH_COLUMN_SCORE_A + ") AS avg_scoreA " +
                        "FROM " + TABLE_MATCH + " WHERE " + COLUMN_ID + " = ?";

        int totalMatches = 0;
        int totalWins = 0;
        double avgScore = 0.0;

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(matchSql)) {

            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                totalMatches = rs.getInt("total_matches");
                totalWins = rs.getInt("total_wins");
                avgScore = rs.getDouble("avg_scoreA");
            }

        } catch (SQLException e) {
            System.err.println("Database error loading match stats: " + e.getMessage());
            e.printStackTrace();
        }



        int totalPlayers = getCountFromTable(TABLE_PLAYER, userId);
        int totalGames = getCountFromTable(TABLE_GAME, userId);



        double winRate = (totalMatches > 0) ? ((double) totalWins / totalMatches) * 100 : 0.0;
        DecimalFormat df = new DecimalFormat("0.0");




        statWidgets.getChildren().add(createStatWidget(
                "Matches", String.valueOf(totalMatches), "Played", "stat-total-matches"));


        statWidgets.getChildren().add(createStatWidget(
                "Win Rate", df.format(winRate), "%", "stat-win-rate"));


        statWidgets.getChildren().add(createStatWidget(
                "Avg Score", df.format(avgScore), "Per Match", "stat-avg-score"));


        statWidgets.getChildren().add(createStatWidget(
                "Players", String.valueOf(totalPlayers), "Rostered", "stat-total-players"));


        statWidgets.getChildren().add(createStatWidget(
                "Games", String.valueOf(totalGames), "in Library", "stat-total-games"));
    }


    private int getCountFromTable(String tableName, String userId) {
        String countSql = "SELECT COUNT(*) AS count FROM " + tableName + " WHERE " + COLUMN_ID + " = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(countSql)) {

            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Database error loading count for table " + tableName + ": " + e.getMessage());
        }
        return 0;
    }
}
