package org.filip.ui;

import interfaces.ITanker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.filip.ui.RMI.Tailor;
import org.filip.ui.Tests.IOffice;
import org.filip.ui.helper.TankerDetails;
import org.filip.ui.viewModel.HouseOrderViewModel;
import org.filip.ui.viewModel.TankerViewModel;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OfficeUI extends Application
{
    private IOffice office;
    private ObservableList<TankerViewModel> tankerData;
    private ObservableList<HouseOrderViewModel> houseOrderData;
    private TableView<TankerViewModel> tankerTableView;
    private TableView<HouseOrderViewModel> houseOrderTableView;

    // Track house orders separately
    private ConcurrentHashMap<String, LocalDateTime> pendingHouseOrders = new ConcurrentHashMap<>();

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try {
            // Locate the Office through RMI (using Tailor);
            office = (IOffice) Tailor.lookup("localhost", "Office");

            // Setup UI
            VBox root = new VBox(10);
            root.setPadding(new Insets(20));

            // Title
            Label titleLabel = new Label("Sewage Management Office");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // Tankers Section
            Label tankerLabel = new Label("Registered Tankers");
            tankerLabel.setStyle("-fx-font-weight: bold;");
            tankerData = FXCollections.observableArrayList();
            tankerTableView = createTankerTableView();

            // House Orders Section
            Label orderLabel = new Label("House Orders");
            orderLabel.setStyle("-fx-font-weight: bold;");
            houseOrderData = FXCollections.observableArrayList();
            houseOrderTableView = createHouseOrderTableView();

            root.getChildren().addAll(
                    titleLabel,
                    tankerLabel,
                    tankerTableView,
                    orderLabel,
                    houseOrderTableView
            );

            Scene scene = new Scene(root, 700, 600);
            primaryStage.setTitle("Sewage Tanker Management System");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Start periodic UI updates
            startUIUpdates();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error initializing Office UI: " + e.getMessage());
        }
    }

    private TableView<TankerViewModel> createTankerTableView() {
        TableView<TankerViewModel> tableView = new TableView<>();

        TableColumn<TankerViewModel, Integer> idColumn = new TableColumn<>("Tanker ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("tankerId"));

        TableColumn<TankerViewModel, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TankerViewModel, Boolean> readyColumn = new TableColumn<>("Ready to Serve");
        readyColumn.setCellValueFactory(new PropertyValueFactory<>("readyToServe"));

        tableView.getColumns().addAll(idColumn, nameColumn, readyColumn);
        tableView.setItems(tankerData);

        return tableView;
    }

    private TableView<HouseOrderViewModel> createHouseOrderTableView() {
        TableView<HouseOrderViewModel> tableView = new TableView<>();

        TableColumn<HouseOrderViewModel, String> houseColumn = new TableColumn<>("House");
        houseColumn.setCellValueFactory(new PropertyValueFactory<>("houseName"));

        TableColumn<HouseOrderViewModel, String> timeColumn = new TableColumn<>("Order Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));

        TableColumn<HouseOrderViewModel, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(houseColumn, timeColumn, statusColumn);
        tableView.setItems(houseOrderData);

        return tableView;
    }

    private void startUIUpdates() {
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000); // Update every 2 seconds

                    Platform.runLater(() -> {
                        refreshTankerTable();
                        refreshHouseOrderTable();
                    });

                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    private void refreshTankerTable() {
        try {
            tankerData.clear();
            for (Map.Entry<TankerDetails, ITanker> entry : office.getTankers().entrySet()) {
                TankerDetails details = entry.getKey();
                tankerData.add(new TankerViewModel(
                        details.getTankerNumber(),
                        "Tanker " + details.getTankerNumber(),
                        details.isReadyToServe()
                ));
            }
        } catch (Exception e) {
            showAlert("Error refreshing tanker table: " + e.getMessage());
        }
    }

    private void refreshHouseOrderTable() {
        try {
            houseOrderData.clear();

            for (Map.Entry<String, LocalDateTime> entry : office.getHouseOrders().entrySet()) {
                String houseName = entry.getKey();
                LocalDateTime orderTime = entry.getValue();
                pendingHouseOrders.put(houseName, orderTime);
            }

            // Convert tracked house orders to view models
            pendingHouseOrders.forEach((houseName, orderTime) -> {
                houseOrderData.add(new HouseOrderViewModel(
                        houseName,
                        orderTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        "Pending"
                ));
            });
        } catch (Exception e) {
            showAlert("Error refreshing house order table: " + e.getMessage());
        }
    }

    private void markTankerReady(String tankerIdStr)
    {
        try
        {
            if (tankerIdStr.trim().isEmpty())
            {
                showAlert("Please enter a tanker ID");
                return;
            }

            int tankerId = Integer.parseInt(tankerIdStr);
            office.setReadyToServe(tankerId);
            showAlert("Tanker " + tankerId + " marked as ready");
            refreshTankerTable();
        } catch (NumberFormatException e) {
            showAlert("Invalid tanker ID");
        } catch (RemoteException e) {
            showAlert("Error marking tanker ready: " + e.getMessage());
        }
    }

    private void showAlert(String message)
    {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Office Management");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}