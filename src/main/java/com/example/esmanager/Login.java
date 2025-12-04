package com.example.esmanager;

import com.example.esmanager.UserStuff.CurrentUser;
import com.example.esmanager.UserStuff.User;
import com.example.esmanager.UserStuff.UserTable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Login extends BorderPane {
    private VBox loginPage;
    private VBox regPage;

    private TextField loginUsername;
    private PasswordField loginPassword;
    private Runnable onLoginSuccess;

    private TextField registerUsername;
    private PasswordField registerPassword;
    private TextField registerEmail;

    public Login() {
        addLoginPage();
        addRegPage();

        setCenter(loginPage);
        setTop(addToggleBar());

        loginPage.getStyleClass().add("login-page");
        regPage.getStyleClass().add("register-page");

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
        toggleBar.getStyleClass().add("toggle-bar");

        return toggleBar;
    }

    private void addLoginPage() {
        Label title = new Label("Login");

        loginUsername = new TextField();
        loginUsername.setMaxWidth(200);
        loginUsername.setPromptText("Enter Username");

        loginPassword = new PasswordField();
        loginPassword.setMaxWidth(200);
        loginPassword.setPromptText("Enter Password");

        Button submit = new Button("Login");
        submit.setPrefWidth(150);

        submit.setOnAction(e->{
            userLogin();
        });

        loginPage = new VBox(title, loginUsername, loginPassword, submit);
        loginPage.setAlignment(Pos.TOP_CENTER);
    }

    private void addRegPage(){
        Label title = new Label("Register");

        registerEmail = new TextField();
        registerEmail.setMaxWidth(200);
        registerEmail.setPromptText("Enter Email");

        registerUsername = new TextField();
        registerUsername.setMaxWidth(200);
        registerUsername.setPromptText("Enter Username");

        registerPassword = new PasswordField();
        registerPassword.setMaxWidth(200);
        registerPassword.setPromptText("Enter Password");

        Button submit = new Button("Create Account");
        submit.setPrefWidth(150);


        //Submit the users info here and run a check seeing if the user already exists or not
        submit.setOnAction(e-> userRegister());

        regPage = new VBox(title,registerEmail,registerUsername,registerPassword,submit);
        regPage.setAlignment(Pos.CENTER);
    }

    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback;
    }

    public void userLogin(){
        String username = loginUsername.getText().trim();
        String pass = loginPassword.getText();

        UserTable userTable = UserTable.getInstance();
        User loggedInUser = userTable.getUser(username,pass);

        if(loggedInUser != null) {
            CurrentUser.login(loggedInUser.getId());
            if(onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            // Show something something username or password is incorrect here
            Alert invalidUser = new Alert(Alert.AlertType.INFORMATION);
            invalidUser.setTitle("Invalid User");
            invalidUser.setHeaderText("Invalid User");
            invalidUser.showAndWait();

        }
    }

    private void userRegister(){
        String username = registerUsername.getText().trim();
        String pass = registerPassword.getText().trim();
        String email = registerEmail.getText().trim();

        if(username.isEmpty() || pass.isEmpty()){
            Alert invalidUser = new Alert(Alert.AlertType.INFORMATION);
            invalidUser.setTitle("Invalid User");
            invalidUser.showAndWait();
            return;
        }

        UserTable userTable = UserTable.getInstance();
        boolean created = userTable.createUser(username, pass, email);
        if(!created){
            Alert alreadyExists = new Alert(Alert.AlertType.INFORMATION);
            alreadyExists.setTitle("User already exists");
            alreadyExists.setHeaderText("User already exists");
            alreadyExists.showAndWait();
        }else{
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText("Success");
            success.showAndWait();

        }
    }

    public void clearLogin(){
        loginPassword.clear();
    }

}
