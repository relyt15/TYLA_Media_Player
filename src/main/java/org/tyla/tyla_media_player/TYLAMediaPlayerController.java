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

public class TYLAMediaPlayerController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private Button btnStart, btnStop, btnReset,btnScare, btnIncrementForward, btnIncrementBackwards;

    @FXML
    private Slider scrlMediaPlayerSlider;

    @FXML
    private TextField txtIncrementValue;

    /*private File file;
    private Media media;
    private MediaPlayer mediaPlayer;*/

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private Slider progressSlider;
    private boolean sliding = false;
    private String incrementValue;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
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

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(imageView));
        mediaPlayer.media().prepare("tcp.mp4");


    }

    public void btnStartOnClick(){
        mediaPlayer.controls().play();
    }
    public void btnStopOnClick(){
        mediaPlayer.controls().stop();
    }
    public void btnResetOnClick(){
        mediaPlayer.controls().setTime(0);
        mediaPlayer.controls().nextFrame();
    }

    public void btnIncrementForwardOnClick(){
        // Current playback position in milliseconds
        long currentTime = mediaPlayer.status().time();

        // Total duration in milliseconds
        long totalDuration = mediaPlayer.media().info().duration();

        if((currentTime + Integer.parseInt(incrementValue)) >= totalDuration){
            mediaPlayer.controls().setTime(totalDuration);
        }
        else {
            mediaPlayer.controls().setTime(currentTime + Integer.parseInt(incrementValue));
        }
    }

    public void btnIncrementBackwardOnClick(){
        // Current playback position in milliseconds
        long currentTime = mediaPlayer.status().time();

        // Total duration in milliseconds
        long totalDuration = mediaPlayer.media().info().duration();

        if((currentTime - Integer.parseInt(incrementValue)) <= 0){
            mediaPlayer.controls().setTime(0);
        }
        else {
            mediaPlayer.controls().setTime(currentTime - Integer.parseInt(incrementValue));
        }

    }

    public void btnScareOnClick() {

    }

    public void txtIncrementValueOnAction(){
         incrementValue = txtIncrementValue.getText().trim();

        if (incrementValue.isEmpty()) {
            txtIncrementValue.setText(""); // clear if empty
            return;
        }

        int number = Integer.parseInt(incrementValue);

        // Natural number check: must be >= 1
        if (number < 1) {
            txtIncrementValue.setText("1"); // snap up to minimum
            return;
        }

        // Number is valid — keep it displayed and use it
        txtIncrementValue.setText(String.valueOf(number));

    }

    public void mediaViewOnError(){

    }
}
