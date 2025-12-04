package com.example.esmanager;

import com.example.esmanager.DatabaseStuff.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Text title = new Text();

    @Override
    public void start( Stage stage) throws IOException {
        Database db = Database.getInstance();

        title.setText("Login");

        BorderPane root = new BorderPane();
        HBox topPane = new HBox();
        Scene scene = new Scene(root);
        Login loginScreen = new Login();
        loginScreen.setOnLoginSuccess(() ->{
            Homepage homepage = new Homepage(root,loginScreen);
            title.setText("Home");
            root.setCenter(homepage);
        });


        stage.setTitle("ESManager");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        topPane.setId("title-bar");

        stage.setMaximized(true);
        stage.show();


        SplashLogo splashScreen = new SplashLogo(root,topPane,loginScreen, "logo.png",300);
        splashScreen.play();


        MenuBar menu = new MenuBar();
        //topPane.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE,null,null)));
        //topPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        //title.translateYProperty().set(50);
        title.setFont(Font.font("Roboto", FontWeight.BOLD, FontPosture.REGULAR, 60));



    }

    public static void main(String[] args) {
        launch();
    }
}