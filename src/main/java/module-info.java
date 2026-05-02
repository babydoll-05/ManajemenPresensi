module com.rplbo.app.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rplbo.app.demo to javafx.fxml;
    exports com.rplbo.app.demo;
}