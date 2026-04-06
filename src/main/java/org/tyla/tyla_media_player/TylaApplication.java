package org.tyla.tyla_media_player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//we need this file
public class TylaApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/tyla/tyla_media_player/tylaUI.fxml")
        );
        Scene scene = new Scene(loader.load());

        //this will load the css related to my tylaMediaPlayerController
        //i assume you need to do this per every place you want a css
        MainController controller = loader.getController();
        scene.getStylesheets().add(getClass().getResource("/org/tyla/tyla_media_player/tylaMediaPlayerController.css").toExternalForm());
        stage.setOnCloseRequest(e -> controller.shutdown());

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }
}