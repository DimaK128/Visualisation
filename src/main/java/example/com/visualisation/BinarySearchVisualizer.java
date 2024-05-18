import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Random;

public class BinarySearchVisualizer extends Application {

    private int[] array;
    private Label statusLabel;
    private TextField delayInput;
    private VBox arrayDisplay;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Binary Search Visualizer");

        TextField arraySizeInput = new TextField();
        arraySizeInput.setPromptText("Enter array size");

        Button generateButton = new Button("Generate Array");
        generateButton.setOnAction(e -> generateArray(Integer.parseInt(arraySizeInput.getText())));

        TextField searchValueInput = new TextField();
        searchValueInput.setPromptText("Enter value to search");

        delayInput = new TextField();
        delayInput.setPromptText("Enter delay in ms");

        Button searchButton = new Button("Start Search");
        searchButton.setOnAction(e -> binarySearchWithVisualization(array, Integer.parseInt(searchValueInput.getText())));

        statusLabel = new Label("Status: Ready");

        arrayDisplay = new VBox();

        VBox layout = new VBox(10, arraySizeInput, generateButton, searchValueInput, delayInput, searchButton, statusLabel, arrayDisplay);
        Scene scene = new Scene(layout, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateArray(int size) {
        array = new Random().ints(size, 0, 100).sorted().toArray();
        updateArrayDisplay();
    }

    private void updateArrayDisplay() {
        arrayDisplay.getChildren().clear();
        for (int num : array) {
            Label label = new Label(String.valueOf(num));
            arrayDisplay.getChildren().add(label);
        }
    }

    private void binarySearchWithVisualization(int[] array, int key) {
        final int[] low = {0};
        final int[] high = {array.length - 1};
        final int[] step = {0};
        final int[] iterCount = {0};

        Timeline timeline = new Timeline();
        int duration = Integer.parseInt(delayInput.getText());
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), event -> {
            int mid = (low[0] + high[0]) / 2;
            iterCount[0]++;
            statusLabel.setText("Iteration count: " + iterCount[0] + ". Range: " + low[0] + " - " + high[0] + ". Mid: " + mid);

            highlightArrayPositions(low[0], mid, high[0]);

            if (low[0] <= high[0]) {
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
                label.setStyle("-fx-background-color: yellow");
            } else if (i >= low && i <= high) {
                label.setStyle("-fx-background-color: lightgray");
            } else {
                label.setStyle("");
            }
        }
    }
}
