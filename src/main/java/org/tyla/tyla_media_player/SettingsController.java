package org.tyla.tyla_media_player;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private ToggleButton btnDark;
    @FXML private ToggleButton btnLight;

    private Scene scene;

    // maincontroller calls this after loading so it has access to the scene
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // light and dark mode //
        btnDark.setSelected(true);
        btnDark.setStyle("-fx-opacity: 1.0;");
        btnLight.setStyle("-fx-opacity: 0.5;");
    }

    @FXML
// sets dark / light mode //


    private void setDarkMode() {
        // checks scene exists //
        if (scene == null) return;

        //apply visual styling //

        MainController.applyDarkMode(scene);

        btnDark.setSelected(true);
        btnLight.setSelected(false);
        btnDark.setStyle("-fx-opacity: 1.0;");
        btnLight.setStyle("-fx-opacity: 0.5;");
    }

    @FXML
    private void setLightMode() {
        if (scene == null) return;

        MainController.applyLightMode(scene);

        btnDark.setSelected(false);
        btnLight.setSelected(true);
        btnDark.setStyle("-fx-opacity: 0.5;");
        btnLight.setStyle("-fx-opacity: 1.0;");
    }
}