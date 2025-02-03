module org.filip.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires sewagelib;
    requires java.management;
    requires static lombok;
    requires java.rmi;

    opens org.filip.ui to javafx.fxml;
    opens org.filip.ui.viewModel to javafx.base;
    exports org.filip.ui;
    exports org.filip.ui.Tests to java.rmi;
}