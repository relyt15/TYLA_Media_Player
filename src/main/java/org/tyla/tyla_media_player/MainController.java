package org.tyla.tyla_media_player;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;

public class MainController {
    //sidebar and options //
    @FXML private VBox sidebar;
    @FXML private VBox homeView;
    @FXML private StackPane mediaPane;
    @FXML private Button menuButton;

    // player bar //
    @FXML private HBox playerBar;
    @FXML private Button btnPlayPause;
    @FXML private Label lblNowPlaying;
    @FXML private Slider audioSeekBar;
    @FXML private Label lblAudioTime;

    //calls controllers //
    private TYLAMediaPlayerController tylaMediaPlayerController;
    private LibraryController libraryController;
    private SettingsController settingsController;
    private AVLibrary2 library = new AVLibrary2();

    private MediaPlayer mediaPlayer;
    private boolean seeking = false;
    private boolean isOpen = true;

    @FXML
    // set sidebar measurements //
    public void initialize() {
        sidebar.setPrefWidth(100);
        sidebar.setMinWidth(100);
        sidebar.setMaxWidth(100);

        // seek bar drag handling //
        audioSeekBar.setOnMousePressed(e -> seeking = true);
        audioSeekBar.setOnMouseReleased(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(audioSeekBar.getValue()));
            }
            seeking = false;
        });

        Platform.runLater(() -> {
            Scene scene = mediaPane.getScene();
            if (scene != null) applyLightMode(scene);
        });
    }

    // audio playback //

    //stops previous media //
    public void playSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    // create new media player ///

        Media media = new Media(new File(song.getPath()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

    // update ui and start playing ///
        mediaPlayer.setOnReady(() -> {
            audioSeekBar.setMax(mediaPlayer.getTotalDuration().toSeconds());
            lblNowPlaying.setText(song.getArtist() + " — " + song.getTitle());
            btnPlayPause.setText("⏸");
            mediaPlayer.play();
            showPlayerBar(true);
        });
    // update seek bar //

        mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            if (!seeking) {
                audioSeekBar.setValue(newVal.toSeconds());
                lblAudioTime.setText(formatTime(newVal) + " / " +
                        formatTime(mediaPlayer.getTotalDuration()));
            }
        });
    // pause when song is done //

        mediaPlayer.setOnEndOfMedia(() -> {
            btnPlayPause.setText("▶");
        });
    }

    @FXML
    private void togglePlayPause() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            btnPlayPause.setText("▶");
        } else {
            mediaPlayer.play();
            btnPlayPause.setText("⏸");
        }
    }

    @FXML
    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            btnPlayPause.setText("▶");
        }
    }
// manage play bar //
    private void showPlayerBar(boolean show) {
        playerBar.setVisible(show);
        playerBar.setManaged(show);
    }
// converts duration into proper format //
    private String formatTime(Duration d) {
        int s = (int) d.toSeconds();
        return String.format("%d:%02d", s / 60, s % 60);
    }

    // navigation //
    // loads and shows different views //

    @FXML
    private void showHome() {
        shutdownSubControllers();
        mediaPane.getChildren().setAll(homeView);
    }

    @FXML
    private void showLibrary() throws IOException {
        shutdownSubControllers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/org/tyla/tyla_media_player/tylaLibraryFXML.fxml"));
        Parent root = loader.load();
        libraryController = loader.getController();
        libraryController.setLibrary(library, this);
        mediaPane.getChildren().setAll(root);
    }

    @FXML
    private void showVideoLibrary() throws IOException {
        shutdownSubControllers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/org/tyla/tyla_media_player/tylaVideoLibraryFXML.fxml"));
        Parent root = loader.load();
        VideoLibraryController ctrl = loader.getController();
        ctrl.setLibrary(library, this);
        mediaPane.getChildren().setAll(root);
    }

    // opens video player w selected video //

    public void showVideoPlayer(String videoPath) throws IOException {
        shutdownSubControllers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/org/tyla/tyla_media_player/tylaMediaViewerFXML.fxml"));
        Parent root = loader.load();
        tylaMediaPlayerController = loader.getController();
        tylaMediaPlayerController.loadVideo(videoPath);
        mediaPane.getChildren().setAll(root);
    }

    @FXML
    private void toggleSettings() throws IOException {
        shutdownSubControllers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/org/tyla/tyla_media_player/tylaSettingsFXML.fxml"));
        Parent root = loader.load();
        settingsController = loader.getController();
        settingsController.setScene(mediaPane.getScene());
        mediaPane.getChildren().setAll(root);
    }

    // sidebar tools //

    @FXML
    // sidebar animation //
    private void toggleMenu() {
        double targetWidth = isOpen ? 0 : 100;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(sidebar.prefWidthProperty(), targetWidth),
                        new KeyValue(sidebar.minWidthProperty(), targetWidth),
                        new KeyValue(sidebar.maxWidthProperty(), targetWidth)));
        timeline.play();
        isOpen = !isOpen;
        menuButton.setTextFill(isOpen
                ? javafx.scene.paint.Color.web("FFFFFF")
                : javafx.scene.paint.Color.web("424549"));
    }

    // theme //
    // handles light and dark stylesheets //

    public static void applyLightMode(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(MainController.class.getResource(
                "/org/tyla/tyla_media_player/tylaMediaPlayerController.css").toExternalForm());
        scene.getStylesheets().add(MainController.class.getResource(
                "/org/tyla/tyla_media_player/tylaLight.css").toExternalForm());
    }

    public static void applyDarkMode(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(MainController.class.getResource(
                "/org/tyla/tyla_media_player/tylaMediaPlayerController.css").toExternalForm());
    }

    // cleanup //

    private void shutdownSubControllers() {
        if (tylaMediaPlayerController != null) {
            tylaMediaPlayerController.shutdown();
            tylaMediaPlayerController = null;
        }
        mediaPane.getChildren().clear();
        showPlayerBar(false);
    }

    public void shutdown() {
        shutdownSubControllers();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}