package org.tyla.tyla_media_player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TYLAMediaPlayerController implements Initializable {
    @FXML
    private MediaView mediaView;
    @FXML
    private Button btnStart, btnStop, btnReset,btnScare;

    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        file = new File("kirby.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    public void btnStartOnClick(){
        mediaPlayer.play();
    }
    public void btnStopOnClick(){
        mediaPlayer.pause();
    }
    public void btnResetOnClick(){
        mediaPlayer.seek(Duration.seconds(0));
    }
    public void btnScareOnClick() {

    }
    public void mediaViewOnError(){

    }
}
