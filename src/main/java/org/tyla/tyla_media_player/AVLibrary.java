package org.tyla.tyla_media_player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AVLibrary {
    private ArrayList<File> dirList;
    private HashMap<String, HashMap<String, String>> audioMap; // stores video files with their respective metadata
    private HashMap<String, HashMap<String, String>> videoMap; // stores video files with their respective metadata
    private int dirTotal; // used to track number of directories in the list
    private int audioTotal; // used to track number of audio files in the hashmap
    private int videoTotal; // used to track number of videos in the hashmap
    private String[] audioFileExtensions = {".mp3", ".wav", ".flac", ".ogg", ".opus", ".wma", ".dsf"};
    private String[] videoFileExtensions = {".mp4", ".mov", ".mkv"};

    public AVLibrary(){
        dirList = new ArrayList<>();
        audioMap = new HashMap<String, HashMap<String, String>>();
        videoMap = new HashMap<String, HashMap<String, String>>();
        dirTotal = 0;
        audioTotal = 0;
        videoTotal = 0;
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
     * then adds all music files within to a hashmap of audio files with their metadata as a value
     *
     */
    public void addDir(File newDir) throws InterruptedException {
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
     * @return a string listing all directories in dirList.
     * @precondition: dirList should not be empty
     * @postcondition:
     */
    public String getDirList() {
        String directories = "";
        for (File f : dirList){
            if(!directories.isEmpty()){
                directories += "\n";
            }
            directories += f;
        }
        return directories;
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
    public void rescanDirs() throws InterruptedException {
        for(File f : dirList){
            if(f.exists() && f.isDirectory()){
                addAudio(f);
                addVideos(f);
            }
        }
    }

    /**
     * @precondition: directory should not be empty
     * @postcondition: adds all audio files within a directory to audioMap
     */
    private void addAudio(File directory) throws InterruptedException {
        for(File f : directory.listFiles()){    //2026-02-19 TC: need to check how to handle if the directory.listfiles() is null
            if (f.isDirectory() && f.exists()){
                addAudio(f);
            }
            for (String fileExtension : audioFileExtensions) {
                if (f.getName().endsWith(fileExtension)) {
                    audioMap.put(f.getPath(), MetaPull2.metadataPull(f.getPath()));
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
    private void addVideos(File directory) throws InterruptedException {
        for(File f : directory.listFiles()){    //2026-02-19 TC: need to check how to handle if the directory.listfiles() is null
            if (f.isDirectory() && f.exists()){
                addVideos(f);
            }
            for (String fileExtension : videoFileExtensions) {
                if (f.getName().endsWith(fileExtension)) {
                    videoMap.put(f.getPath(), VideoMetaPull.getMetadata(f.getPath()));
                    videoTotal++;
                    break;
                }
            }
        }
    }


    /**
     * @precondition: none
     * @postcondition: removes audio file from audioMap
     * @param audioFile
     */
    public void removeAudio(String audioFile, HashMap<String,String> hash){
        if(audioMap.remove(audioFile, hash)) {
            audioTotal--;
        }
    }

    /**
     * @precondition: none
     * @postcondition: removes video from videoMap
     * @param video
     */
    public void removeVideo(String video, HashMap<String,String> hash){
        if(videoMap.remove(video, hash)) {
            videoTotal--;
        }
    }

    public String getAudioPath(){
        String audioFiles = "";
        for (String f : audioMap.keySet()){
            if(!audioFiles.isEmpty()){
                audioFiles += "\n";
            }
            audioFiles += (f);
        }
        return audioFiles;
    }

    public String getVideoPath(){
        String videos = "";
        for (String f : videoMap.keySet()){
            if(!videos.isEmpty()){
                videos += "\n";
            }
            videos += (f);
        }
        return videos;
    }

    public HashMap<String,HashMap<String,String>> getAudioMap() {
        return audioMap;
    }
    public HashMap<String, HashMap<String,String>> getVideoMap(){
        return videoMap;
    }

} // end of Music Library Class
