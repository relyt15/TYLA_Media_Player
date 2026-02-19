package org.tyla.tyla_media_player;

import java.io.File;


public class AVLibraryDemo {
    public static void main(String[] args) {
        AVLibrary lib1 = new AVLibrary();
        File file = new File("C:/Users/Tyler/Documents/School Stuff/TYLA_MP Test Dir");

        lib1.addDir(file);
        System.out.println(lib1.getAudioPath());



    }
}
