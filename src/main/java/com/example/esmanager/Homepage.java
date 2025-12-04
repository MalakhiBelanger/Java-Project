package com.example.esmanager;

import com.example.esmanager.Pages.Finances;
import com.example.esmanager.Pages.Games;
import com.example.esmanager.Pages.Players;
import com.example.esmanager.Pages.Stats;
import com.example.esmanager.UserStuff.CurrentUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Homepage extends BorderPane {
    private BorderPane root;
    private Login loginScreen;
    public Homepage(BorderPane root, Login loginScreen) {
        this.root = root;
        this.loginScreen = loginScreen;


        Finances financePage = new Finances();
        Games gamesPage = new Games();
        Players playersPage = new Players();
        Stats statsPage = new Stats();

        VBox overview = new VBox(20);

        VBox sidebar = new VBox();
        sidebar.setSpacing(10);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(20));

        Insets sidebarMargins = new Insets(0,0,40,0);


        //These buttons can be used to switch the
        Button viewHome = new Button("Home");
        Button viewPlayers = new Button("Players");
        Button viewGames = new Button("Games");
        Button viewStats = new Button("Team Stats");
        Button viewFinances = new Button("Financial");

        Button logoutButton = new Button("Logout");

        VBox.setMargin(viewHome,sidebarMargins);
        VBox.setMargin(viewPlayers,sidebarMargins);
        VBox.setMargin(viewGames,sidebarMargins);
        VBox.setMargin(viewStats,sidebarMargins);
        VBox.setMargin(viewFinances,sidebarMargins);

        logoutButton.setAlignment(Pos.BOTTOM_CENTER);

        viewHome.setMaxWidth(Double.MAX_VALUE);
        viewPlayers.setMaxWidth(Double.MAX_VALUE);
        viewGames.setMaxWidth(Double.MAX_VALUE);
        viewStats.setMaxWidth(Double.MAX_VALUE);
        viewFinances.setMaxWidth(Double.MAX_VALUE);

        Region thevoid = new Region();
        VBox.setVgrow(thevoid,Priority.ALWAYS);

        sidebar.getChildren().addAll(viewHome,viewPlayers,viewGames,viewStats,viewFinances,thevoid,logoutButton);

        sidebar.prefWidthProperty().bind(this.widthProperty().multiply(0.15));
        sidebar.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY, new BorderWidths(1))));


        Label welcomeLabel = new Label("Welcome " + CurrentUser.getUserId());
        welcomeLabel.setFont(Font.font("Roboto"));
        welcomeLabel.setTextFill(Color.web("#ec625c"));
        welcomeLabel.getStyleClass().add("homepage-welcome");

        Label instructionLabel = new Label("Use the sidebar to manage your team's players, games, matches, and finances");
        instructionLabel.setFont(Font.font("Roboto"));
        instructionLabel.setWrapText(true);
        instructionLabel.getStyleClass().add("homepage-instruction");

        instructionLabel.setPadding(new Insets(20));
        welcomeLabel.setPadding(new Insets(30));

        overview.getChildren().addAll(welcomeLabel, instructionLabel);



        viewHome.setOnAction(e->{
            if(this.getCenter()!=overview){
                this.setCenter(overview);
                Main.title.setText("Home");
            }
        });

        viewFinances.setOnAction(e->{
            if(this.getCenter()!=financePage){
                this.setCenter(financePage);
                Main.title.setText("Finances");
            }
        });

        viewGames.setOnAction(e->{
            if(this.getCenter()!=gamesPage){
                this.setCenter(gamesPage);
                Main.title.setText("Games");
            }
        });

        viewPlayers.setOnAction(e->{
            if(this.getCenter()!=playersPage){
                this.setCenter(playersPage);
                Main.title.setText("Players");
            }
        });

        viewStats.setOnAction(e->{
            if(this.getCenter()!=statsPage){
                this.setCenter(statsPage);
                statsPage.loadStatsData();
                Main.title.setText("Stats");
            }
        });

        logoutButton.setOnAction(e->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure?");
            alert.setContentText("Do you want to logout?");


            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");

            alert.getButtonTypes().setAll(yes,no);

            alert.showAndWait().ifPresent(r->{
                if(r == yes){
                    Main.title.setText("Login");
                    root.setCenter(loginScreen);
                    loginScreen.clearLogin();

                    /*Please reset the user to null here when doing database stuff
                 =
                 =
                 =
                 =
                 =*/
                    CurrentUser.logout();
                    System.out.println(CurrentUser.getUserId());

                } else {

                }
            });
        });

        this.setLeft(sidebar);
        this.setCenter(overview);
    }

}
