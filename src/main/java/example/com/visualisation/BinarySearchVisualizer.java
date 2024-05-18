package example.com.visualisation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BinarySearchVisualizer extends Application {

    private TextField arraySizeInput;
    private TextField searchValueInput;
    private TextField delayInput;
    private Label statusLabel;
    private HBox arrayDisplay;
    private Timeline timeline;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Binary Search Visualizer");

        // UI elements
        arraySizeInput = new TextField("10");
        searchValueInput = new TextField();
        delayInput = new TextField("500");
        Button startButton = new Button("Start Search");
        statusLabel = new Label("Enter array size, search value and delay.");
        arrayDisplay = new HBox();
        arrayDisplay.setSpacing(5);

        // Layout
        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(
                new Label("Array Size:"), arraySizeInput,
                new Label("Search Value:"), searchValueInput,
                new Label("Delay (ms):"), delayInput,
                startButton,
                statusLabel,
                arrayDisplay
        );

        // Event Handling
        startButton.setOnAction(event -> startBinarySearch());

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    private void startBinarySearch() {
        try {
            int size = Integer.parseInt(arraySizeInput.getText());
            int searchValue = Integer.parseInt(searchValueInput.getText());
            int delay = Integer.parseInt(delayInput.getText());

            // Generate a sorted array
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = i + 1;
            }

            // Display the array
            arrayDisplay.getChildren().clear();
            for (int num : array) {
                Label label = new Label(String.valueOf(num));
                label.setStyle("-fx-border-color: black; -fx-padding: 5px;");
                arrayDisplay.getChildren().add(label);
            }

            // Start visualization
            binarySearchWithVisualization(array, searchValue, delay);

        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter valid numbers.");
        }
    }

    private void binarySearchWithVisualization(int[] array, int key, int delay) {
        final int[] low = {0};
        final int[] high = {array.length - 1};
        final int[] iterCount = {0};
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delay), event -> {
            int mid = (low[0] + high[0]) / 2;
            iterCount[0]++;
            statusLabel.setText("Iteration count: " + iterCount[0] + ". Range: " + low[0] + " - " + high[0] + ". Mid: " + mid);
            if (low[0] <= high[0]) {
                highlightArrayPositions(low[0], mid, high[0]);
                if (array[mid] < key) {
                    low[0] = mid + 1;
                } else if (array[mid] > key) {
                    high[0] = mid - 1;
                } else {
                    statusLabel.setText("Value found at index: " + mid + " in " + iterCount[0] + " iterations");
                    timeline.stop();
                }
            } else {
                statusLabel.setText("Value not found in " + iterCount[0] + " iterations");
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void highlightArrayPositions(int low, int mid, int high) {
        for (int i = 0; i < arrayDisplay.getChildren().size(); i++) {
            Label label = (Label) arrayDisplay.getChildren().get(i);
            if (i == mid) {
                label.setStyle("-fx-border-color: red; -fx-padding: 5px;");
            } else if (i >= low && i <= high) {
                label.setStyle("-fx-border-color: yellow; -fx-padding: 5px;");
            } else {
                label.setStyle("-fx-border-color: black; -fx-padding: 5px;");
            }
        }
    }
}
