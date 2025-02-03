package org.filip.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.filip.ui.RMI.House;

import java.rmi.RemoteException;

import static javafx.application.Application.launch;

public class HouseUI extends Application
{
    private House house;
    private Label volumeLabel;
    private ProgressBar volumeProgressBar;
    private Button pauseResumeButton;
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create House with 100 capacity and ID 1
            house = new House(100, 1);

            // Setup UI
            VBox root = new VBox(10);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);

            // House ID Label
            Label houseIdLabel = new Label("House #" + house.getId());
            houseIdLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Volume Progress Bar
            volumeProgressBar = new ProgressBar(0);
            volumeProgressBar.setPrefWidth(300);

            // Volume Label
            volumeLabel = new Label("Current Volume: 0 / 100");

            // Status Label
            statusLabel = new Label("Status: Running");

            // Pause/Resume Button
            pauseResumeButton = new Button("Pause");
            pauseResumeButton.setOnAction(e -> togglePause());

            root.getChildren().addAll(
                    houseIdLabel,
                    volumeProgressBar,
                    volumeLabel,
                    statusLabel,
                    pauseResumeButton
            );

            Scene scene = new Scene(root, 400, 300);
            primaryStage.setTitle("Sewage Tank Simulation");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Start simulation and UI updates
            house.startSimulation();
            startUIUpdates();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void startUIUpdates() {
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Update every second

                    Platform.runLater(() -> {
                        int currentVolume = house.getCurrentVolume().get();
                        int capacity = house.getCapacity();

                        // Update volume label and progress bar
                        volumeLabel.setText(String.format("Current Volume: %d / %d",
                                currentVolume, capacity));
                        volumeProgressBar.setProgress((double) currentVolume / capacity);

                        // Update status based on pause state
                        statusLabel.setText(house.getIsPaused() ?
                                "Status: Paused (Waiting for Emptying)" :
                                "Status: Running");

                        // Update pause/resume button text
                        pauseResumeButton.setText(house.getIsPaused() ? "Resume" : "Pause");
                    });
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    private void togglePause() {
        if (house.getIsPaused())
        {
            house.resume();
        }
        else
        {
            house.pause();
        }
    }
}
