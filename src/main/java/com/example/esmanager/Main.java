package com.example.esmanager;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        BorderPane root = new BorderPane();
        BorderPane topPane = new BorderPane();
        Scene scene = new Scene(root);
        Login loginScreen = new Login();


        stage.setTitle("ESManager");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();


        SplashLogo splashScreen = new SplashLogo(root,topPane,loginScreen, "placeholder.png",300);
        splashScreen.play();


        MenuBar menu = new MenuBar();
        topPane.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE,null,null)));
        topPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));

        Text title = new Text();
        title.setText("Login");

        topPane.getChildren().addAll(title);
        title.setLayoutX(root.getWidth()/6);
        title.setLayoutY(root.getHeight()/8.5);
        title.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 60));


    }

    public static void main(String[] args) {
        launch();
    }
}