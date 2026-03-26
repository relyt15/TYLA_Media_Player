package org.tyla.tyla_media_player;

import java.util.ArrayList;

public class Album {
    public String name;
    public ArrayList<Song> songs = new ArrayList<>();

    public Album(String name, Song song) {
        this.name = name;
        addSong(song);
    }

    public void addSong(Song song){
        songs.add(song);
    }

    public String getAlbumName(){
        return this.name;
    }


}