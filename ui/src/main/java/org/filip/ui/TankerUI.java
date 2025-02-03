package org.filip.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.filip.ui.RMI.Tanker;
import org.filip.ui.Tests.IOffice;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class TankerUI extends Application
{
    private Tanker tanker;
    private IOffice office;
    private ListView<String> orderListView;
    private int tankerId;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tanker");

        orderListView = new ListView<>();
        VBox layout = new VBox(10);
        layout.getChildren().add(orderListView);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(this::initializeTanker).start();
    }

    private void initializeTanker() {
        try {
            Registry registry = LocateRegistry.getRegistry(2137);
            office = (IOffice) registry.lookup("Office");

            int maxCapacity = new Random().nextInt(300, 1000);
            tanker = new Tanker(maxCapacity);

            String tankerName = "Tanker-" + System.currentTimeMillis() % 1000;

            tanker.registerAtOffice(office, tankerName);
            tankerId = tanker.getId();

            office.setReadyToServe(tankerId);

            // Tanker is ready, but Office assigns jobs
            updateOrderList("Ready to Serve");

            startOrderProcessing();

        } catch (Exception e) {
            updateOrderList("Init Error: " + e.getMessage());
        }
    }

    private void startOrderProcessing() {
        new Thread(() ->
        {
            while (true)
            {
                try
                {
                    System.out.println("Tanker: Looking for orders");

                    Thread.sleep(5000);

                } catch (Exception e) {
                    updateOrderList("Processing Error: " + e.getMessage());
                }
            }
        }).start();
    }

    private void updateOrderList(String message) {
        Platform.runLater(() -> {
            orderListView.getItems().add(0, message);
            if (orderListView.getItems().size() > 10) {
                orderListView.getItems().remove(10);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
