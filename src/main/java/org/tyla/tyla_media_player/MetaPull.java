package org.tyla.tyla_media_player;

import java.io.File;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MetaPull {

    //internet told me that making a mao would be cleaner for the project.
    public static HashMap<String, Object> MetadataPull(String path) throws InterruptedException {

        //We need this because from what I've read, the filePath needs to be a URI to be fully read.
        File filePath = new File(path);
        String URI = filePath.toURI().toString();

        Media baldhead = new Media(URI);
        MediaPlayer necessary = new MediaPlayer(baldhead);
        //as far as I can tell, I need a media player as well to trigger the loading of the metadata.

        HashMap<String, Object> results = new HashMap<>();

        results.putAll(baldhead.getMetadata());

        necessary.dispose();

        return results;
    }
}
