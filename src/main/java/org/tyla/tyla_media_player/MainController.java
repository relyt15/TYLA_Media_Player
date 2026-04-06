package org.tyla.tyla_media_player;

import javafx.animation.KeyValue;
import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private VBox sidebar;

    @FXML
    private StackPane mediaPane;

    @FXML
    private Button menuButton, homeButton, settingButton, libraryButton;

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

    @FXML
    private void toggleSettings() throws IOException {

        //to check if it exist
        URL url = getClass().getResource("/org/tyla/tyla_media_player/tylaMediaViewerFXML.fxml");
        System.out.println(url);

        //this should work, i got an error where the fxml file was looking for a method with javafx25 and we have javafx 21 on this. in this case onDragDropped is a part of 25 and not a part of 21
        //so i removed the onDragDropped action and it work.
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/tyla/tyla_media_player/tylaMediaViewerFXML.fxml"));
            mediaPane.getChildren().setAll(root); // replaces everything inside
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}