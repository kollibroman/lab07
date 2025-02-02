module org.filip.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires logic;
    requires sewagelib;

    opens org.filip.ui to javafx.fxml;
    exports org.filip.ui;
}