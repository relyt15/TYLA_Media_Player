package org.tyla.tyla_media_player;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.util.HashMap;

public class MetaPull2 {

    /**
     * @param filePath it the filepath of the media file
     * @return a HashMap containing metadata fields as keys and their values as Objects
     * @precondition: filePath must point to a valid, readable media file
     * @postcondition: returns a populated metadata map, or an empty map if the file could not be read
     */
    public static HashMap<String, String> metadataPull(String filePath) {
        HashMap<String, String> metadata = new HashMap<>();

        try {
            File file = new File(filePath);
            AudioFile audioFile = AudioFileIO.read(file); // AudioFileIO is the read/write arm of jAudioTagger
            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            if (tag != null) {
                metadata.put("title", getFieldSafe(tag, FieldKey.TITLE));
                metadata.put("artist", getFieldSafe(tag, FieldKey.ARTIST));
                metadata.put("album", getFieldSafe(tag, FieldKey.ALBUM));
                metadata.put("year", getFieldSafe(tag, FieldKey.YEAR));
                metadata.put("genre", getFieldSafe(tag, FieldKey.GENRE));
                metadata.put("trackNumber", getFieldSafe(tag, FieldKey.TRACK));
                metadata.put("comment", getFieldSafe(tag, FieldKey.COMMENT));
            }

            if (header != null) {
                metadata.put("duration", String.valueOf(header.getTrackLength()));
                metadata.put("bitRate", header.getBitRate());
                metadata.put("sampleRate", header.getSampleRate());
                metadata.put("format", header.getFormat());
            }

        } catch (Exception e) {
            System.err.println("MetaPull: Error reading metadata for: " + filePath);
            e.printStackTrace();
        }

        return metadata;
    }

    private static String getFieldSafe(Tag tag, FieldKey key) {
        try {
            String value = tag.getFirst(key);
            return (value != null && !value.isEmpty()) ? value : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
}