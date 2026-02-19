package org.tyla.tyla_media_player;

import java.io.File;
import java.util.ArrayList;
//import javafx.scene.media.Media;
import java.util.HashMap;
import java.util.Set;

public class AVLibrary {
    private ArrayList<File> dirList;
    private HashMap<String, String[]> songMap; // stores audio files with their respective metadata
    private HashMap<String, String[]> videoMap; // stores video files with their respective metadata
    //private Media media;
    private int dirTotal; // used to track number of directories in the list
    private int songTotal; // used to track number of songs in the hashmap
    private int videoTotal; // used to track number of videos in the hashmap
    private String[] audioFileExtensions = {".mp3", ".wav", ".flac", ".ogg", ".opus", ".aac", ".aiff",
            ".pcm", ".wma", ".m4a",".alac", ".ape", ".au"};
    private String[] videoFileExtensions = {".mp4", ".mov", ".mkv"};
    public AVLibrary(){
        dirList = new ArrayList<>();
        songMap = new HashMap<String, String[]>();
        videoMap = new HashMap<String, String[]>();
        dirTotal = 0;
        songTotal = 0;
        videoTotal = 0;

    } // end of constructor

    public int getSongTotal() {
        return songTotal;
    }

    public int getVideoTotal() {
        return videoTotal;
    }

    public int getDirTotal(){
        return dirTotal;
    }

    /**
     * @param newDir
     * @precondition: newDir must be a directory
     * @postcondition: takes a new directory input, adds it to a hashmap of directories,
     * then adds all music files within to a hashmap of songs with their metadata as a value
     *
     */
    public void addDir(File newDir){
        if(newDir.exists() && newDir.isDirectory()){
            for(File f : dirList){
                if(!newDir.getPath().equals(f.getPath())){ //2026-02-19 TC: need to verify .equals method verifies that the file paths are identical
                    dirList.add(newDir);
                    dirTotal++;
                    addSongs(newDir);
                    addVideos(newDir);
                }
            }
            if(dirTotal == 0){
                dirList.add(newDir);
                dirTotal++;
                addSongs(newDir);
                addVideos(newDir);
            }
        }
    }

    /**
     * @precondition: requires that the directory input is not null
     * @postcondition: removes all the files in the directory from the audio and video hashMaps and then removes the directory from the directory list
     * @param directory
     */
    public void removeDir(File directory){
        for(File f : directory.listFiles()){    //2026-02-19 TC: need to check how to handle if the directory.listfiles() is null
            if(f.isDirectory() && f.exists()){
                removeDir(f);
            }
            removeVideo(f.getPath());
            removeSong(f.getPath());
        }
        dirList.remove(directory);
        dirTotal--;
    }

    /**
     * @precondition: none
     * @postcondition: Clears all directories from the directory list and all their media files from their respective songMap or videoMap
     */
    public void clearDirList(){
        for(File f : dirList){
            removeDir(f);
        }
    }

    /**
     * @precondition: dirList must not be empty
     * @postcondition: scans all audio and video files and places them in their respective songMap or videoMap
     */
    public void rescanDirs(){
        for(File f : dirList){
            if(f.exists() && f.isDirectory()){
                addSongs(f);
                addVideos(f);
            }
        }
    }

    /**
     * @precondition: none
     * @postcondition: removes song from songMap
     * @param song
     */
    public void removeSong(String song){
        songMap.remove(song);
        songTotal--;
    }

    /**
     * @precondition: none
     * @postcondition: removes video from videoMap
     * @param video
     */
    public void removeVideo(String video){
        videoMap.remove(video);
        videoTotal--;
    }



    /**
     * @return a string listing all directories in dirList.
     * @precondition: dirList should not be empty
     * @postcondition:
     */
    public String getDirList() {
        String directories = "";
        for (File f : dirList){
            if(directories.length() > 1){
                directories += ", ";
            }
            directories += f;
        }
        return directories;
    }



    /**
     * @precondition: directory should not be empty
     * @postcondition: adds all audio files within a directory to songMap
     */
    private void addSongs(File directory){
        String[] metaData = new String[5];
        for(File f : directory.listFiles()){    //2026-02-19 TC: need to check how to handle if the directory.listfiles() is null
            if (f.isDirectory() && f.exists()){
                addSongs(f);
            }
            for (String fileExtension : audioFileExtensions) {
                if (f.getName().endsWith(fileExtension)) {
                    //metaData = media.getArray(); // update with whatever luc names the class and methods
                    songMap.put(f.getPath(), metaData);
                    songTotal++;
                    break;
                }
            }
        }
    }

    /**
     * @precondition: directory should not be empty
     * @postcondition: adds all video files within a directory to videoMap
     */
    private void addVideos(File directory){
        String[] metaData = new String [5];
        for(File f : directory.listFiles()){    //2026-02-19 TC: need to check how to handle if the directory.listfiles() is null
            if (f.isDirectory() && f.exists()){
                addVideos(f);
            }
            for (String fileExtension : videoFileExtensions) {
                if (f.getName().endsWith(fileExtension)) {
                    //metaData = media.getArray(); // update with whatever luc names the class and methods
                    videoMap.put(f.getPath(), metaData);
                    videoTotal++;
                    break;
                }
            }
        }
    }

    public String getAudioPath(){
        String songs = "";
        for (String f : songMap.keySet()){
            songs += f;
        }
        return songs;
    }

    public String getVideoPath(){
        String videos = "";
        for(String f : videoMap.keySet()){
            videos += f;
        }
        return videos;
    }

    public HashMap<String, String[]> getSongMap() {
        return songMap;
    }

    public HashMap<String, String[]> getVideoMap(){
        return videoMap;
    }




} // end of Music Library Class





/*
musicLibrary needs to have a list of directories being used X
should be able to add directories to this list at any point X
needs to have list of songs
when a directory is added, search through all files/directories within the chosen directory and
    add all music files to song list
needs to be able to filter out any files that are not audio files
needs to be able to return a string of all directories in the directory list
needs to be able to return a string of all file extensions in the song list
need to be able to extend length of array holding songs/directories

needs to be able to retrieve metadata from any audio file and output that data as a string
needs to be able to edit metadata on the file

if reading a png (or other image file) in the directory, link the png with the album art showing on the itunes style UI page
    how do we link the png with the correct album?
    need to create a list of image file extensions
    then, with the itunes style layout, we will call upon this list of file extensions



 */