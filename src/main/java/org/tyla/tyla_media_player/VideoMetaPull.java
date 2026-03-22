package org.tyla.tyla_media_player;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

import java.util.HashMap;

public class VideoMetaPull {

    private static MediaPlayerFactory factory;

    static {
        new NativeDiscovery().discover();
        factory = new MediaPlayerFactory();
    }

    // Prevent instantiation
    private VideoMetaPull() {}

    /**
     * Pulls metadata from a video file.
     *
     * @param filePath absolute path to the video file
     * @return HashMap with keys: "title", "director", "duration_ms", "duration_formatted"
     */
    public static HashMap<String, String> getMetadata(String filePath) {
        HashMap<String, String> metadata = new HashMap<>();
        Media media = null;

        try {
            media = factory.media().newMedia(filePath);
            media.parsing().parse();

            String title    = media.meta().get(Meta.TITLE);
            String director = media.meta().get(Meta.DIRECTOR);
            long durationMs = media.info().duration();

            metadata.put("title",              title    != null ? title    : "Unknown");
            metadata.put("director",           director != null ? director : "Unknown");
            metadata.put("duration_ms",        String.valueOf(durationMs));
            metadata.put("duration_formatted", formatDuration(durationMs));

        } catch (Exception e) {
            System.err.println("Failed to pull metadata from: " + filePath);
            e.printStackTrace();

            metadata.put("title",              "Unknown");
            metadata.put("director",           "Unknown");
            metadata.put("duration_ms",        "0");
            metadata.put("duration_formatted", "00:00:00");

        } finally {
            if (media != null) {
                media.release();
            }
        }

        return metadata;
    }

    /**
     * Converts milliseconds to HH:MM:SS format.
     */
    private static String formatDuration(long durationMs) {
        if (durationMs <= 0) return "00:00:00";

        long totalSeconds = durationMs / 1000;
        long hours        = totalSeconds / 3600;
        long minutes      = (totalSeconds % 3600) / 60;
        long seconds      = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Release the factory — call this once when shutting down your app.
     */
    public static void release() {
        if (factory != null) {
            factory.release();
            factory = null;
        }
    }
}