package org.tyla.tyla_media_player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AVLibrary2 {
    private ArrayList<File> dirList;
    private ArrayList<Song> audioList; // stores video files with their respective metadata
    private HashMap<String, HashMap<String, String>> videoMap;  // stores video files with their respective metadata
    private int dirTotal; // used to track number of directories in the list
    private int audioTotal; // used to track number of audio files in the hashmap
    private int videoTotal; // used to track number of videos in the hashmap
    private ArrayList<Artist> artistList;
    private String[] audioFileExtensions = {".mp3", ".wav", ".flac", ".ogg", ".opus", ".wma", ".dsf"};
    private String[] videoFileExtensions = {".mp4", ".mov", ".mkv"};
    private ArrayList<Playlist> playlists = new ArrayList<>();

    public AVLibrary2() {
        artistList = new ArrayList<>();
        dirList = new ArrayList<>();
        audioList = new ArrayList<>();
        videoMap = new HashMap<>();
        dirTotal = 0;
        audioTotal = 0;
        videoTotal = 0;
    } // used to track number of videos in the hashmap

    // creates and deletes playlist container
    public void createPlaylist(String name) {
        playlists.add(new Playlist(name));
    }

    public void removePlaylist(Playlist p) {
        playlists.remove(p);
    }

    //returns list of playlists
    public ArrayList<Playlist> getPlaylists() { return playlists; }
    // searches playlists/ignores cases
    public Playlist getPlaylist(String name) {
        for (Playlist p : playlists)
            if (p.getName().equalsIgnoreCase(name)) return p;
        return null;
    }
    public int getAudioTotal() { return audioTotal; }
    public int getVideoTotal() { return videoTotal; }
    public int getDirTotal()   { return dirTotal; }

    /**
     * @param newDir
     * @precondition: newDir must be a directory
     * @postcondition: takes a new directory input, adds it to a hashmap of directories,
     * then adds all music files within to a hashmap of audio files with their metadata as a value
     *
     */

    public void addDir(File newDir) throws InterruptedException {
        if (!newDir.exists() || !newDir.isDirectory()) return;

        // check for duplicate before adding
        for (File f : dirList) {
            if (newDir.getPath().equals(f.getPath())) return;
        }

        dirList.add(newDir);
        dirTotal++;
        addAudio(newDir);
        addVideos(newDir);
        artistCreator();
    }
    /**
     * @precondition: requires that the directory input is not null
     * @postcondition: removes all the files in the directory from the audio and video hashMaps and then removes the directory from the directory list
     * @param directory
     */

    public void removeDir(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && f.exists()) {
                    removeDir(f);
                } else {
                    removeVideo(f.getPath(), videoMap.get(f.getPath()));
                    for (Song s : new ArrayList<>(audioList)) {
                        if (s.getPath().equals(f.getPath())) {
                            removeAudio(s);
                            break;
                        }
                    }
                }
            }
        }
        if (dirList.contains(directory)) {
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
        for (File f : dirList) {
            if (!directories.isEmpty()) directories += "\n";
            directories += f;
        }
        return directories;
    }
    /**
     * @precondition: none
     * @postcondition: Clears all directories from the directory list and all their media files from their respective audioMap or videoMap
     */

    public void clearDirList() {
        while (!dirList.isEmpty()) {
            removeDir(dirList.get(0));
        }
    }
    /**
     * @precondition: dirList must not be empty
     * @postcondition: scans all audio and video files and places them in their respective audioMap or videoMap
     */

    public void rescanDirs() throws InterruptedException {
        for (File f : dirList) {
            if (f.exists() && f.isDirectory()) {
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
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory() && f.exists()) {
                addAudio(f);
                continue;
            }
            for (String ext : audioFileExtensions) {
                if (f.getName().endsWith(ext)) {
                    Song newSong = new Song(f.getPath(), MetaPull2.metadataPull(f.getPath()));
                    audioList.add(newSong);
                    audioTotal++;

                    boolean artistFound = false;
                    for (Artist a : artistList) {
                        if (newSong.getArtist().equals(a.name)) {
                            artistFound = true;
                            if (!a.containsAlbum(newSong.getAlbum())) {
                                a.createAlbum(newSong.getAlbum(), newSong);
                            } else {
                                a.addSongToAlbum(newSong.getAlbum(), newSong);
                            }
                            break;
                        }
                    }
                    if (!artistFound) {
                        Artist newArtist = new Artist(newSong.getArtist());
                        artistList.add(newArtist);
                        newArtist.createAlbum(newSong.getAlbum(), newSong);
                    }
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
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory() && f.exists()) {
                addVideos(f);
                continue;
            }
            for (String ext : videoFileExtensions) {
                if (f.getName().endsWith(ext)) {
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
     * @param song
     */

    private void removeAudio(Song song) {
        if (audioList.remove(song)) audioTotal--;
    }

    /**
     * @precondition: none
     * @postcondition: removes video from videoMap
     * @param video
     */

    private void removeVideo(String video, HashMap<String, String> hash) {
        if (hash != null && videoMap.remove(video, hash)) videoTotal--;
    }

    public String getAudioPaths() {
        String audioFiles = "";
        for (Song s : audioList) {
            if (!audioFiles.isEmpty()) audioFiles += "\n";
            audioFiles += s.getPath();
        }
        return audioFiles;
    }

    public Song getSong(String songTitle) {
        for (Song s : audioList) {
            if (s.getTitle().equalsIgnoreCase(songTitle)) return s;
        }
        return null;
    }

    public String getAudioTitles() {
        String audioTitles = "";
        for (Song s : audioList) {
            if (!audioTitles.isEmpty()) audioTitles += "\n";
            audioTitles += s.getTitle();
        }
        return audioTitles;
    }

    public String getVideoPath() {
        String videos = "";
        for (String f : videoMap.keySet()) {
            if (!videos.isEmpty()) videos += "\n";
            videos += f;
        }
        return videos;
    }

    public ArrayList<Song> getAudioList() { return audioList; }
    public HashMap<String, HashMap<String, String>> getVideoMap() { return videoMap; }

    private void artistCreator() {
        // end of Music Library Class
    }
}