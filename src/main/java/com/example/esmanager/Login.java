package com.example.esmanager;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Login extends BorderPane {
    private VBox loginPage;
    private VBox regPage;

    private TextField loginUsername;
    private PasswordField loginPassword;
    private Runnable onLoginSuccess;

    private TextField registerUsername;
    private PasswordField registerPassword;
    private Runnable onRegisterSuccess;

    public Login() {
        this.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        addLoginPage();
        addRegPage();

        setCenter(loginPage);
        setTop(addToggleBar());

    }

    private HBox addToggleBar() {
        Button loginButton = new Button("Login");
        Button regButton = new Button("Register");

        loginButton.setPrefWidth(150);
        regButton.setPrefWidth(150);

        loginButton.setOnAction(e->{
            setCenter(loginPage);
        });
        regButton.setOnAction(e->{
            setCenter(regPage);
        });

        HBox toggleBar = new HBox(loginButton, regButton);
        toggleBar.setAlignment(Pos.CENTER);

        return toggleBar;
    }

    private void addLoginPage() {
        Label title = new Label("Login");

        loginUsername = new TextField();
        loginUsername.setPromptText("Enter Username");

        loginPassword = new PasswordField();
        loginPassword.setPromptText("Enter Password");

        Button submit = new Button("Login");
        submit.setPrefWidth(150);

        submit.setOnAction(e->{
            userLogin();
        });

        loginPage = new VBox(title, loginUsername, loginPassword, submit);
        loginPage.setAlignment(Pos.CENTER);
    }

    private void addRegPage(){
        Label title = new Label("Register");

        TextField email = new TextField();
        email.setPromptText("Enter Email");

        TextField username = new TextField();
        username.setPromptText("Enter Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Enter Password");

        Button submit = new Button("Create Account");
        submit.setPrefWidth(150);


        //Submit the users info here and run a check seeing if the user already exists or not
        submit.setOnAction(e->{
        });

        regPage = new VBox(title,email,username,password,submit);
        regPage.setAlignment(Pos.CENTER);
    }

    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback;
    }

    public void userLogin(){
        String username = loginUsername.getText().trim();
        String pass = loginPassword.getText();

        if(username.equals("admin") && pass.equals("1234")){

            if(onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        }
    }

    public void clearLogin(){
        loginPassword.clear();
    }

}
