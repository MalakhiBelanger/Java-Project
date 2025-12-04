module com.example.esmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires javafx.base;


    opens com.example.esmanager to javafx.fxml;
    exports com.example.esmanager;
    exports com.example.esmanager.Pages;
    opens com.example.esmanager.Pages to javafx.fxml;
    exports com.example.esmanager.UserStuff;
    opens com.example.esmanager.UserStuff to javafx.fxml;
    exports com.example.esmanager.DatabaseStuff;
    opens com.example.esmanager.DatabaseStuff to javafx.fxml;
}