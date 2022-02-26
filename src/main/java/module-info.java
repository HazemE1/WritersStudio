module com.team34 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;

    opens com.team34 to javafx.fxml;
    exports com.team34;
}