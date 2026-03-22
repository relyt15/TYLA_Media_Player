package org.tyla.tyla_media_player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class TYLAMediaPlayerController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private Button btnStart, btnStop, btnReset,btnScare;

    /*private File file;
    private Media media;
    private MediaPlayer mediaPlayer;*/

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private Slider progressSlider;
    private boolean sliding = false;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        /*file = new File("kirby.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);*/
        factory     = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(imageView));
        mediaPlayer.media().prepare("kirby.mp4");
    }

    public void btnStartOnClick(){
        mediaPlayer.controls().play();
    }
    public void btnStopOnClick(){
        mediaPlayer.controls().stop();
    }
    public void btnResetOnClick(){
        mediaPlayer.controls().setTime(0);
    }
    public void btnScareOnClick() {

    }
    public void mediaViewOnError(){

    }
}
