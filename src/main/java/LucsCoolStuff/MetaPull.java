package LucsCoolStuff;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MetaPull {

    //internet told me that making a mao would be cleaner for the project.
    public static Map<String, Object> MetadataPull(String path) throws InterruptedException {

        //We need this because from what I've read, the filePath needs to be a URI to be fully read.
        File filePath = new File(path);
        String URI = filePath.toURI().toString();

        Media baldhead = new Media(URI);
        MediaPlayer necessary = new MediaPlayer(baldhead);
        //as far as I can tell, I need a media player as well to trigger the loading of the metadata.

        Map<String, Object> results = new HashMap<>();

        CountDownLatch parry = new CountDownLatch(1);
        //Apparently the metadata loads too slow, so we need to stall it until it's ready.
        //It needs to be '1' because we are waiting for the metadata loading event to finish.

        necessary.setOnReady(() -> {

           results.putAll(baldhead.getMetadata());

           parry.countDown();
        });

        parry.await();
        necessary.dispose();

        return results;
    }
}
