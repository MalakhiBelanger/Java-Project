module com.example.esmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.esmanager to javafx.fxml;
    exports com.example.esmanager;
}