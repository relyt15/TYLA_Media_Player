package org.tyla.tyla_media_player;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class AVLibraryDemo extends Application {
    public void start(Stage stage) throws Exception {
        AVLibrary lib1 = new AVLibrary();
        File file = new File("C:/Users/karlt/OneDrive/Documents/TestDirSchool");

        lib1.addDir(file);
        System.out.println("current song list: " + lib1.getAudioPath());
        System.out.println("current video list: " + lib1.getVideoPath());
        System.out.println("Current dirList: " + lib1.getDirList());

        lib1.clearDirList();
        System.out.println("cleared dirList" + lib1.getAudioPath());

        lib1.removeDir(file);
        System.out.println("removed directory" + lib1.getAudioPath());

        System.out.println("audioTotal: " + lib1.getAudioTotal());
        System.out.println("videoTotal: " + lib1.getVideoTotal());
        System.out.println("current audio files: " + lib1.getAudioPath());

        lib1.addDir(file);

        System.out.println("audioTotal: " + lib1.getAudioTotal());
        System.out.println("videoTotal: " + lib1.getVideoTotal());
        System.out.println("current audio files: " + lib1.getAudioPath());

        String keyword = "piano";
        List<String> results = librarySearch.searchLibrary(lib1, keyword);

        for(String res : results){

            System.out.println(res);
        }

        Platform.exit();
    }

    public static void main(String[] args){

        launch(args);
    }
}
