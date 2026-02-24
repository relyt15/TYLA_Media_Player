package org.tyla.tyla_media_player;

import java.io.File;


public class AVLibraryDemo {
    public static void main(String[] args) throws InterruptedException {
        AVLibrary lib1 = new AVLibrary();
        File file = new File("C:\\Users\\Tyler\\Documents\\School Stuff\\TYLA_MP Test Dir");

        lib1.addDir(file);
        System.out.println("current song list: " + lib1.getAudioPath());
        System.out.println("current video list: " + lib1.getVideoPath());
        System.out.println("Current dirList: " + lib1.getDirList());
        System.out.println("File SongName: " + lib1.getAudioMap().get("C:\\Users\\Tyler\\Documents\\School Stuff\\TYLA_MP Test Dir\\Clapwverb.mp3").get("title"));


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

    }
}
