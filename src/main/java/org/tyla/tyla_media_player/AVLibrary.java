package org.tyla.tyla_media_player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AVLibrary {
    private ArrayList<File> dirList;
    private HashMap<String, MetaPull> audioMap; // stores audio files with their respective metadata
    private HashMap<String, MetaPull> videoMap; // stores video files with their respective metadata
    private int dirTotal; // used to track number of directories in the list
    private int audioTotal; // used to track number of audio files in the hashmap
    private MetaPull = metaMap; // used to hold a hashmap<string, object> that holds metadata for media items
    private int videoTotal; // used to track number of videos in the hashmap
    private String[] audioFileExtensions = {".mp3", ".wav", ".flac", ".ogg", ".opus", ".aac", ".aiff",
            ".pcm", ".wma", ".m4a",".alac", ".ape", ".au"};
    private String[] videoFileExtensions = {".mp4", ".mov", ".mkv"};

    public AVLibrary(){
        dirList = new ArrayList<>();
        audioMap = new HashMap<String, MetaPull>();
        videoMap = new HashMap<String, MetaPull>();
        dirTotal = 0;
        audioTotal = 0;
        videoTotal = 0;
        metaMap = new MetaPull();

    } // end of constructor

    public int getAudioTotal() {
        return audioTotal;
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
     * then adds all music files within to a hashmap of audio filess with their metadata as a value
     *
     */
    public void addDir(File newDir){
        if(newDir.exists() && newDir.isDirectory()){
            for(File f : dirList){
                if(!newDir.getPath().equals(f.getPath())){ //2026-02-19 TC: need to verify .equals method verifies that the file paths are identical
                    dirList.add(newDir);
                    dirTotal++;
                    addAudio(newDir);
                    addVideos(newDir);
                }
            }
            if(dirTotal == 0){
                dirList.add(newDir);
                dirTotal++;
                addAudio(newDir);
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
            removeVideo(f.getPath(), videoMap.get(f.getPath()));
            removeAudio(f.getPath(), audioMap.get(f.getPath()));
        }
        if(dirList.contains(directory)) {
            dirList.remove(directory);
            dirTotal--;
        }
    }

    /**
     * @precondition: none
     * @postcondition: Clears all directories from the directory list and all their media files from their respective audioMap or videoMap
     */
    public void clearDirList(){
        for(int i = 0; i < dirTotal; i++){
            removeDir(dirList.get(i));
        }
    }

    /**
     * @precondition: dirList must not be empty
     * @postcondition: scans all audio and video files and places them in their respective audioMap or videoMap
     */
    public void rescanDirs(){
        for(File f : dirList){
            if(f.exists() && f.isDirectory()){
                addAudio(f);
                addVideos(f);
            }
        }
    }

    /**
     * @precondition: none
     * @postcondition: removes audio file from audioMap
     * @param audioFile
     */
    public void removeAudio(String audioFile, HashMap<String,Object> hash){
        if(audioMap.remove(audioFile, hash)) {
            audioTotal--;
        }
    }

    /**
     * @precondition: none
     * @postcondition: removes video from videoMap
     * @param video
     */
    public void removeVideo(String video, HashMap<String,Object> hash){
        if(videoMap.remove(video, hash)) {
            videoTotal--;
        }
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
     * @postcondition: adds all audio files within a directory to audioMap
     */
    private void addAudio(File directory){
        //HashMap<String,Object> metaData = new HashMap<String,Object>();
        for(File f : directory.listFiles()){    //2026-02-19 TC: need to check how to handle if the directory.listfiles() is null
            if (f.isDirectory() && f.exists()){
                addAudio(f);
            }
            for (String fileExtension : audioFileExtensions) {
                if (f.getName().endsWith(fileExtension)) {
                    metaMap.MetadataPull(f.getPath()); // update with whatever luc names the class and methods
                    audioMap.put(f.getPath(), metaMap);
                    audioTotal++;
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
        //HashMap<String,Object> metaData = new HashMap<String,Object>();
        for(File f : directory.listFiles()){    //2026-02-19 TC: need to check how to handle if the directory.listfiles() is null
            if (f.isDirectory() && f.exists()){
                addVideos(f);
            }
            for (String fileExtension : videoFileExtensions) {
                if (f.getName().endsWith(fileExtension)) {
                    metaMap.MetadataPull(f.getPath()); // update with whatever luc names the class and methods
                    videoMap.put(f.getPath(), metaMap);
                    videoTotal++;
                    break;
                }
            }
        }
    }

    public String getAudioPath(){
        String audioFiles = "";
        for (String f : audioMap.keySet()){
            audioFiles += f;
        }
        return audioFiles;
    }

    public String getVideoPath(){
        String videos = "";
        for(String f : videoMap.keySet()){
            videos += f;
        }
        return videos;
    }

    public HashMap<String,HashMap<String,Object>> getAudioMap() {
        return audioMap;
    }

    public HashMap<String, HashMap<String,Object>> getVideoMap(){
        return videoMap;
    }




} // end of Music Library Class





/*
musicLibrary needs to have a list of directories being used X
should be able to add directories to this list at any point X
needs to have list of audio files
when a directory is added, search through all files/directories within the chosen directory and
    add all music files to audio files list
needs to be able to filter out any files that are not audio files
needs to be able to return a string of all directories in the directory list
needs to be able to return a string of all file extensions in the audio files list
need to be able to extend length of array holding audio files/directories

needs to be able to retrieve metadata from any audio file and output that data as a string
needs to be able to edit metadata on the file

if reading a png (or other image file) in the directory, link the png with the album art showing on the itunes style UI page
    how do we link the png with the correct album?
    need to create a list of image file extensions
    then, with the itunes style layout, we will call upon this list of file extensions



 */