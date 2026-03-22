package org.tyla.tyla_media_player;

import java.util.HashMap;

public class Artist {
    public String name;
    public HashMap<String, Album> albums = new HashMap<>();

    public Artist(String name) {
        this.name = name;
    }
}