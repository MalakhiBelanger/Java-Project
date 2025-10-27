module com.example.esportsmanageproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.esportsmanageproject to javafx.fxml;
    exports com.example.esportsmanageproject;
}