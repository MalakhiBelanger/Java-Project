module com.example.esmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.example.esmanager to javafx.fxml;
    exports com.example.esmanager;
}