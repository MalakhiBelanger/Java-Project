package com.example.esmanager;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SplashLogo {
    private final ImageView logo;
    private final BorderPane root;
    private BorderPane topPane;

    public SplashLogo(BorderPane root,BorderPane topPane, String imgPath, double width) {
        this.root = root;
        this.topPane = topPane;

        logo = new ImageView(new Image(imgPath));
        logo.setFitWidth(width);
        logo.setPreserveRatio(true);
    }

    public void play(){
        StackPane splashPane = new StackPane(logo);
        splashPane.setAlignment(Pos.CENTER);

        root.setCenter(splashPane);


        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), logo);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);


        TranslateTransition move = new TranslateTransition(Duration.seconds(0.5), logo);
        root.applyCss();
        root.layout();

        Bounds logoX = logo.localToScene(logo.getBoundsInLocal());
        double xcoord = 0 - logoX.getMinX();

        Bounds logoY = logo.localToScene(logo.getBoundsInLocal());
        double ycoord = 0 - logoY.getMinY();



        move.setByX(xcoord);
        move.setByY(ycoord);

        ScaleTransition logoShrink = new ScaleTransition(Duration.seconds(0.5), logo);
        logoShrink.setToX(0.75);
        logoShrink.setToY(0.75);

        ParallelTransition moveShrink = new ParallelTransition(move, logoShrink);

        SequentialTransition sequence = new SequentialTransition(fadeIn, moveShrink);

        sequence.setOnFinished(e -> {

            root.setCenter(null);
            logo.setTranslateX(0);
            logo.setTranslateY(0);

            topPane.setLeft(logo);

            root.setTop(topPane);

        });

        sequence.play();
    }

    public BorderPane getTopPane(){
        return topPane;
    }
}
