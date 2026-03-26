package org.tyla.tyla_media_player;

import java.io.File;


public class AVLibraryDemo {
    public static void main(String[] args) throws InterruptedException {
        AVLibrary2 lib1 = new AVLibrary2();
        File file = new File("C:\\Users\\Tyler\\Documents\\School Stuff\\TYLA_MP Test Dir");
        Playlist plst = new Playlist("Fav Tunes");

        lib1.addDir(file);
        System.out.println("current song list: " + lib1.getAudioPaths());
        System.out.println("current song list: " + lib1.getAudioTitles());
        System.out.println("current video list: " + lib1.getVideoPath());
        System.out.println("Current dirList: " + lib1.getDirList());

        plst.addSong(lib1.getSong("Clap w verb"));
        plst.addSong(lib1.getSong("Unknown Title"));
        System.out.println(plst.getSongList());
        plst.removeSong(lib1.getSong("Clap w verb"));
        System.out.println(plst.getSongList());
        plst.addSong(lib1.getSong("Clap w verb"));
        System.out.println(plst.getSongList());
        plst.clearPlaylist();
        System.out.println(plst.getSongList());

//        System.out.println("File SongName: " + lib1.getAudioList());
//
//        System.out.println(lib1.getDirTotal());
//        lib1.clearDirList();
//        System.out.println("cleared dirList" + lib1.getAudioPaths());
//        System.out.println(lib1.getDirTotal());
//
//        lib1.removeDir(file);
//        System.out.println("removed directory" + lib1.getAudioPaths());
//
//        System.out.println("audioTotal: " + lib1.getAudioTotal());
//        System.out.println("videoTotal: " + lib1.getVideoTotal());
//        System.out.println("current audio files: " + lib1.getAudioPaths());
//
//        lib1.addDir(file);
//
//        System.out.println("audioTotal: " + lib1.getAudioTotal());
//        System.out.println("videoTotal: " + lib1.getVideoTotal());
//        System.out.println("current audio files: " + lib1.getAudioPaths());
//        System.out.println("current video files: " + lib1.getVideoPath());


    }
}
