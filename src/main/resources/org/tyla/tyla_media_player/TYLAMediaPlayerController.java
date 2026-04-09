package org.tyla.tyla_media_player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import javafx.application.Platform;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TYLAMediaPlayerController implements Initializable {

    @FXML private ImageView imageView;
    @FXML private Button btnStart, btnStop, btnReset, btnIncrementForward, btnIncrementBackwards;
    @FXML private Slider scrlMediaPlayerSlider;
    @FXML private TextField txtIncrementValue;
    @FXML private Label lblMediaTime;

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private boolean sliding = false;
    private ScheduledExecutorService scheduler;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getControlNewText().matches("\\d*")) return change;
            return null;
        };
        txtIncrementValue.setTextFormatter(new TextFormatter<>(filter));

        txtIncrementValue.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                String text = txtIncrementValue.getText().trim();
                if (text.isEmpty() || Integer.parseInt(text) < 1) {
                    txtIncrementValue.setText("1");
                }
            }
        });

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(imageView));

        scrlMediaPlayerSlider.setMin(0);
        scrlMediaPlayerSlider.setMax(100);

        scrlMediaPlayerSlider.setOnMousePressed(e -> sliding = true);
        scrlMediaPlayerSlider.setOnMouseReleased(e -> {
            sliding = false;
            double percent = scrlMediaPlayerSlider.getValue() / 100.0;
            long duration = mediaPlayer.media().info().duration();
            mediaPlayer.controls().setTime((long)(percent * duration));
        });

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (!sliding) {
                long current = mediaPlayer.status().time();
                long duration = mediaPlayer.media().info().duration();
                if (duration > 0) {
                    double progress = (double) current / duration * 100.0;
                    Platform.runLater(() -> {
                        scrlMediaPlayerSlider.setValue(progress);
                        lblMediaTime.setText(formatTime(current) + " / " + formatTime(duration));
                    });
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        scrlMediaPlayerSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double percent = (newVal.doubleValue() / scrlMediaPlayerSlider.getMax()) * 100;
            scrlMediaPlayerSlider.lookup(".track").setStyle(
                    String.format("-fx-background-color: linear-gradient(to right, #00aaff %.1f%%, #3a3a3a %.1f%%);",
                            percent, percent)
            );
        });

        lblMediaTime.setText("00:00 / 00:00");
    }

    // called by MainController when a video is selected from the video library
    public void loadVideo(String path) {
        mediaPlayer.media().prepare(path);
        mediaPlayer.controls().play();
    }

    public void shutdown() {
        if (scheduler != null) scheduler.shutdown();
        if (mediaPlayer != null) mediaPlayer.release();
        if (factory != null) factory.release();
    }

    @FXML
    public void btnStartOnClick() { mediaPlayer.controls().play(); }

    @FXML
    public void btnStopOnClick() { mediaPlayer.controls().pause(); }

    @FXML
    public void btnResetOnClick() {
        mediaPlayer.controls().setTime(0);
        mediaPlayer.controls().nextFrame();
    }

    @FXML
    public void btnIncrementForwardOnClick() {
        String text = txtIncrementValue.getText().trim();
        if (text.isEmpty()) return;
        long increment = Long.parseLong(text) * 1000;
        long currentTime = mediaPlayer.status().time();
        long totalDuration = mediaPlayer.media().info().duration();
        long newTime = currentTime + increment;
        mediaPlayer.controls().setTime(Math.min(newTime, totalDuration));
    }

    @FXML
    public void btnIncrementBackwardOnClick() {
        String text = txtIncrementValue.getText().trim();
        if (text.isEmpty()) return;
        long increment = Long.parseLong(text) * 1000;
        long currentTime = mediaPlayer.status().time();
        long newTime = currentTime - increment;
        mediaPlayer.controls().setTime(Math.max(newTime, 0));
    }

    private String formatTime(long millis) {
        long totalSeconds = millis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        if (hours > 0) return String.format("%d:%02d:%02d", hours, minutes, seconds);
        return String.format("%02d:%02d", minutes, seconds);
    }
}