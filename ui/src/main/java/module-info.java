module org.filip.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;

    opens org.filip.ui to javafx.fxml;
    exports org.filip.ui;
}