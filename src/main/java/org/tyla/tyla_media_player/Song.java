package org.tyla.tyla_media_player;

import java.util.HashMap;

public class Song {

    public final String path;

    public final String title;
    public final String artist;
    public final String album;
    public final String genre;
    public final String year;

    public final int trackNumber;
    public final int duration; // seconds

    public final String bitRate;
    public final String sampleRate;
    public final String format;

    public Song(String path, HashMap<String, String> meta) {
        this.path = path;

        this.title = normalize(meta.get("title"), "Unknown Title");
        this.artist = normalize(meta.get("artist"), "Unknown Artist");
        this.album = normalize(meta.get("album"), "Unknown Album");
        this.genre = normalize(meta.get("genre"), "Unknown Genre");
        this.year = normalize(meta.get("year"), "Unknown Year");

        this.trackNumber = parseIntSafe(meta.get("trackNumber"));
        this.duration = parseIntSafe(meta.get("duration"));

        this.bitRate = normalize(meta.get("bitRate"), "Unknown");
        this.sampleRate = normalize(meta.get("sampleRate"), "Unknown");
        this.format = normalize(meta.get("format"), "Unknown");
    }

    private String normalize(String value, String fallback) {
        if (value == null) return fallback;

        value = value.trim();

        if (value.isEmpty() || value.equalsIgnoreCase("Unknown")) {
            return fallback;
        }

        return value;
    }

    private int parseIntSafe(String value) {
        try {
            if (value == null) return 0;

            if (value.contains("/")) {
                value = value.split("/")[0];
            }

            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return artist + " - " + title + " (" + album + ")";
    }
}