module com.example.carwash {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.carwash to javafx.fxml;
    exports com.example.carwash;
    exports com.example.carwash.controller;
    opens com.example.carwash.controller to javafx.fxml;
}