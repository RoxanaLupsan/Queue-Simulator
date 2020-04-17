package ro.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ro.control.Simulation;

import java.util.Observable;
import java.util.Observer;


public class GUI extends Application implements Observer {

    private TextArea logOfEventsTextArea;
    private TextField maxArrivingTimeInput;
    private TextField minArrivingTimeInput;
    private TextField minServiceTimeInput;
    private TextField maxServiceTimeInput;
    private TextField nrQueuesInput;
    private TextField simulationIntervalInput;
    private Button startSimulationButton;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Queues Simulation");
        Stage window1 = primaryStage;

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        //Data Inputs
        Label introLabel = new Label("Data Inputs");
        introLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 14));
        GridPane.setConstraints(introLabel, 0, 0);
        gridPane.getChildren().add(introLabel);

        Label minArrivingTimeLabel = new Label("Min. Arriving Time:");
        minArrivingTimeLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 12));
        GridPane.setConstraints(minArrivingTimeLabel, 0, 1);
        gridPane.getChildren().add(minArrivingTimeLabel);

        minArrivingTimeInput = new TextField();
        GridPane.setConstraints(minArrivingTimeInput, 1, 1);
        gridPane.getChildren().add(minArrivingTimeInput);
        minArrivingTimeInput.setEditable(true);

        Label maxArrivingTimeLabel = new Label("Max. Arriving Time:");
        maxArrivingTimeLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 12));
        GridPane.setConstraints(maxArrivingTimeLabel, 0, 2);
        gridPane.getChildren().add(maxArrivingTimeLabel);

        maxArrivingTimeInput = new TextField();
        GridPane.setConstraints(maxArrivingTimeInput, 1, 2);
        gridPane.getChildren().add(maxArrivingTimeInput);
        maxArrivingTimeInput.setEditable(true);

        Label minServiceTimeLabel = new Label("Min. Service Time:");
        minServiceTimeLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 12));
        GridPane.setConstraints(minServiceTimeLabel, 0, 3);
        gridPane.getChildren().add(minServiceTimeLabel);

        minServiceTimeInput = new TextField();
        GridPane.setConstraints(minServiceTimeInput, 1, 3);
        gridPane.getChildren().add(minServiceTimeInput);
        minServiceTimeInput.setEditable(true);

        Label maxServiceTimeLabel = new Label("Max. Service Time:");
        maxServiceTimeLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 12));
        GridPane.setConstraints(maxServiceTimeLabel, 0, 4);
        gridPane.getChildren().add(maxServiceTimeLabel);

        maxServiceTimeInput = new TextField();
        GridPane.setConstraints(maxServiceTimeInput, 1, 4);
        gridPane.getChildren().add(maxServiceTimeInput);
        maxServiceTimeInput.setEditable(true);

        Label nrQueuesLabel = new Label("Number of queues:");
        nrQueuesLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 12));
        GridPane.setConstraints(nrQueuesLabel, 0, 5);
        gridPane.getChildren().add(nrQueuesLabel);

        nrQueuesInput = new TextField();
        GridPane.setConstraints(nrQueuesInput, 1, 5);
        gridPane.getChildren().add(nrQueuesInput);
        nrQueuesInput.setEditable(true);

        Label simulationIntervalLabel = new Label("Simulation interval:");
        simulationIntervalLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 12));
        GridPane.setConstraints(simulationIntervalLabel, 0, 6);
        gridPane.getChildren().add(simulationIntervalLabel);

        simulationIntervalInput = new TextField();
        GridPane.setConstraints(simulationIntervalInput, 1, 6);
        gridPane.getChildren().add(simulationIntervalInput);
        simulationIntervalInput.setEditable(true);

        //Buttons
        Button clearButton = new Button("Clear All Fields");
        GridPane.setConstraints(clearButton, 1, 8);
        clearButton.setOnAction(e -> {
            minArrivingTimeInput.clear();
            maxArrivingTimeInput.clear();
            minServiceTimeInput.clear();
            maxServiceTimeInput.clear();
            nrQueuesInput.clear();
            simulationIntervalInput.clear();
        });
        clearButton.setMaxWidth(Double.MAX_VALUE);
        gridPane.getChildren().add(clearButton);

        startSimulationButton = new Button("Start Simulation");
        startSimulationButton.setMaxWidth(Double.MAX_VALUE);
        GridPane.setConstraints(startSimulationButton, 1, 9);
        gridPane.getChildren().add(startSimulationButton);
        startSimulationButton.setOnAction(event -> {
            logOfEventsTextArea.clear();
            new Simulation(this).start();
            startSimulationButton.setDisable(true);
        });

        //Log of Events
        Label logLabel = new Label("Log of Events");
        logLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 14));
        GridPane.setConstraints(logLabel, 8, 10);
        gridPane.getChildren().add(logLabel);

        logOfEventsTextArea = new TextArea();
        logOfEventsTextArea.setPrefWidth(300);
        logOfEventsTextArea.setPrefHeight(400);
        logOfEventsTextArea.setEditable(false);
        GridPane.setConstraints(logOfEventsTextArea, 8, 11);
        gridPane.getChildren().add(logOfEventsTextArea);

        Scene scene = new Scene(gridPane);
        window1.setScene(scene);
        window1.setResizable(true);
        window1.setWidth(800);
        window1.setHeight(720);
        window1.show();
    }

    /**
     * @param o Observable, message Object
     *          It syncronized the log of events text area, in order to get in real time all the updates from the services.
     *          It does represent a real time simulation.
     *          Inside, it gets the String text from the text area and it adds the new message from the observable.
     *          The new text is set in the text area.
     */
    @Override
    public void update(Observable o, Object message) {
        synchronized (logOfEventsTextArea) {
            String text = logOfEventsTextArea.getText();
            text += message.toString() + "\n";
            logOfEventsTextArea.setText(text);
        }
    }

    public TextField getMaxArrivingTimeInput() {
        return maxArrivingTimeInput;
    }

    public TextField getMinArrivingTimeInput() {
        return minArrivingTimeInput;
    }

    public TextField getMinServiceTimeInput() {
        return minServiceTimeInput;
    }

    public TextField getMaxServiceTimeInput() {
        return maxServiceTimeInput;
    }

    public TextField getNrQueuesInput() {
        return nrQueuesInput;
    }

    public TextField getSimulationIntervalInput() {
        return simulationIntervalInput;
    }

    public Button getStartSimulationButton() {
        return startSimulationButton;
    }

}
