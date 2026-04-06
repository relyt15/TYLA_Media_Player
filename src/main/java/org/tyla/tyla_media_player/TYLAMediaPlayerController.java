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
    @FXML
    private ImageView imageView;
    @FXML
    private Button btnStart, btnStop, btnReset,btnScare, btnIncrementForward, btnIncrementBackwards;

    @FXML
    private Slider scrlMediaPlayerSlider;

    @FXML
    private TextField txtIncrementValue;

    @FXML
    private Label lblMediaTime;

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private boolean sliding = false;
    private ScheduledExecutorService scheduler;

    @Override

    //this will call when you play any sort of media that you can get a path from the library
    public void initialize(URL arg0, ResourceBundle arg1){

        //for the textbox that picks only integers for the incrementation
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        };
        txtIncrementValue.setTextFormatter(new TextFormatter<>(filter));

        // Snap to 1 if user types 0 or clears the field and moves away
        txtIncrementValue.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // user just left the field
                String text = txtIncrementValue.getText().trim();
                if (text.isEmpty() || Integer.parseInt(text) < 1) {
                    txtIncrementValue.setText("1");
                }
            }
        });

        // when you call a file path, change the media().prepare(x.mp4) to call any file and put it into the media player
        //This also assumes that the video or song is already loaded onto the library so just grab the path
        // from how the library loads it into the program
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(imageView));
        mediaPlayer.media().prepare("tcp.mp4");

        //everything below is the slider setup including css
        scrlMediaPlayerSlider.setMin(0);
        scrlMediaPlayerSlider.setMax(100);

        // Scrubbing: when user clicks/drags the slider, seek to that position
        scrlMediaPlayerSlider.setOnMousePressed(e -> sliding = true);

        scrlMediaPlayerSlider.setOnMouseReleased(e -> {
            sliding = false;
            double percent = scrlMediaPlayerSlider.getValue() / 100.0;
            long duration = mediaPlayer.media().info().duration();
            mediaPlayer.controls().setTime((long)(percent * duration));
        });

        // Poll the media player every 500ms and update the slider and label
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (!sliding) {
                long current = mediaPlayer.status().time();
                long duration = mediaPlayer.media().info().duration();
                if (duration > 0) {
                    double progress = (double) current / duration * 100.0;
                    Platform.runLater(() -> {
                        scrlMediaPlayerSlider.setValue(progress);

                        String currentTimeStr = formatTime(current);
                        String totalTimeStr = formatTime(duration);

                        lblMediaTime.setText(currentTimeStr + " / " + totalTimeStr);
                    });
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        //Css for the slider
        scrlMediaPlayerSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double percent = (newVal.doubleValue() / scrlMediaPlayerSlider.getMax()) * 100;
            scrlMediaPlayerSlider.lookup(".track").setStyle(
                    String.format("-fx-background-color: linear-gradient(to right, #00aaff %.1f%%, #3a3a3a %.1f%%);",
                            percent, percent)
            );
        });

        lblMediaTime.setText("00:00 / 00:00");
    }

    //claude told me to put this here, apparently it doesnt do good things without it
    public void shutdown() {
        if (scheduler != null) scheduler.shutdown();
        if (mediaPlayer != null) mediaPlayer.release();
        if (factory != null) factory.release();
    }

    public void btnStartOnClick(){
        mediaPlayer.controls().play();
    }
    public void btnStopOnClick(){
        mediaPlayer.controls().pause();
    }
    public void btnResetOnClick(){
        mediaPlayer.controls().setTime(0);
        mediaPlayer.controls().nextFrame();
    }

    public void btnIncrementForwardOnClick(){
        String text = txtIncrementValue.getText().trim();
        if (text.isEmpty()) return;

        long increment = Long.parseLong(text) * 1000;
        long currentTime = mediaPlayer.status().time();
        long totalDuration = mediaPlayer.media().info().duration();

        long newTime = currentTime + increment;

        if (newTime >= totalDuration) {
            mediaPlayer.controls().setTime(totalDuration);
        } else {
            mediaPlayer.controls().setTime(newTime);
        }
    }

    public void btnIncrementBackwardOnClick(){
        String text = txtIncrementValue.getText().trim();
        if (text.isEmpty()) return;

        long increment = Long.parseLong(text) * 1000;
        long currentTime = mediaPlayer.status().time();

        long newTime = currentTime - increment;

        if (newTime <= 0) {
            mediaPlayer.controls().setTime(0);
        } else {
            mediaPlayer.controls().setTime(newTime);
        }

    }


    //we will remove this eventually
    public void btnScareOnClick() {
        //this is how u load songs per button
        mediaPlayer.media().prepare("scary.mp4");
        mediaPlayer.controls().play();
    }

    public void txtIncrementValueOnAction(){

    }

    public void mediaViewOnError(){

    }

    //for the label
    private String formatTime(long millis) {
        long totalSeconds = millis / 1000;

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
