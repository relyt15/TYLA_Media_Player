package org.tyla.tyla_media_player;

import java.util.HashMap;

public class Song {
    public String path;
    public String title;
    public String artist;
    public String album;
    public int trackNumber;

    public Song(String path, HashMap<String, String> meta) {
        this.path = path;
        this.title = meta.getOrDefault("title", "Unknown Title");
        this.artist = meta.getOrDefault("artist", "Unknown Artist");
        this.album = meta.getOrDefault("album", "Unknown Album");

        try {
            this.trackNumber = Integer.parseInt(meta.getOrDefault("track", "0"));
        } catch (Exception e) {
            this.trackNumber = 0;
        }
    }
}