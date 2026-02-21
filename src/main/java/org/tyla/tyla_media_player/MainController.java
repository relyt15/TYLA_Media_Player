package org.tyla.tyla_media_player;

import javafx.animation.KeyValue;
import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController {

    @FXML
    private VBox sidebar;

    @FXML
    private Button menuButton;

    @FXML
    public void initialize() {
        sidebar.setPrefWidth(100);
        sidebar.setMinWidth(100);
        sidebar.setMaxWidth(100);
    }

    private boolean isOpen = true;

    @FXML
    private void toggleMenu() {

        double targetWidth = isOpen ? 0 : 100;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(sidebar.prefWidthProperty(), targetWidth),
                        new KeyValue(sidebar.minWidthProperty(), targetWidth),
                        new KeyValue(sidebar.maxWidthProperty(), targetWidth)
                )
        );

        timeline.play();
        isOpen = !isOpen;

        menuButton.setTextFill(isOpen
                ? javafx.scene.paint.Color.web("FFFFFF")
                : javafx.scene.paint.Color.web("424549"));
    }
}