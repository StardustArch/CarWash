module com.example.carwash {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.carwash to javafx.fxml;
    exports com.example.carwash;

    // This line allows javafx.fxml to access your controller classes
    opens com.example.carwash.controller to javafx.fxml;
    exports com.example.carwash.controller;
}
