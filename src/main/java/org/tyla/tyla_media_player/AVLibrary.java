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
    private String[] fileExtensions = {".mp3", ".wav", ".flac", ".ogg", ".opus", ".aac", ".aiff",
            ".pcm", ".wma", ".m4a",".alac", ".ape", ".au"};

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
                if(!newDir.getPath().equals(f.getPath())){
                    dirList.add(newDir);
                    dirTotal++;
                    addSongs(newDir);
                    addVideos(newDir);
                }
            }

        }
    }

    public void removeDir(File directory){
        dirList.remove(directory);
    }

    public void clearDirList(){
        dirList.clear();
    }

    public void rescanDirs(){
        for(File f : dirList){
            if(f.exists() && f.isDirectory()){
                addSongs(f);
                addVideos(f);
            }
        }
    }




    /**
     * @return a string listing all directories in dirList.
     * @precondition:
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
     * @precondition:
     * @postcondition: adds all music file extensions within a directory to String array songList
     */
    private void addSongs(File directory){
        String[] metaData = new String[5];
        for(File f : directory.listFiles()){
            if (f.isDirectory() && f.exists()){
                addSongs(f);
            }
            for (String fileExtension : fileExtensions) {
                if (f.getName().endsWith(fileExtension)) {
                    //metaData = media.getArray(); // update with whatever luc names the class and methods
                    songMap.put(f.getPath(), metaData);
                    songTotal++;
                    break;
                }
            }
        }
    }

    private void addVideos(File directory){
        String[] metaData = new String [5];
        for(File f : directory.listFiles()){
            if (f.isDirectory() && f.exists()){
                addVideos(f);
            }
            for (String fileExtension : fileExtensions) {
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

    public HashMap<String, String[]> getSongMap() {
        return songMap;
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