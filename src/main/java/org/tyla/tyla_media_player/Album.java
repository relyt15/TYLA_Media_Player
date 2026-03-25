package org.tyla.tyla_media_player;

import java.util.ArrayList;

public class Album {
    public String name;
    public ArrayList<Song> songs = new ArrayList<>();

    public Album(String name) {
        this.name = name;
    }
}