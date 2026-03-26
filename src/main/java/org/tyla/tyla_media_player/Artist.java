package org.tyla.tyla_media_player;

import java.util.ArrayList;
import java.util.HashMap;

public class Artist {
    public String name;
    public ArrayList<Album> albums = new ArrayList<>(); // This should be an arraylist like Albums class TC 20260325

    public Artist(String name) {
        this.name = name;
    }

    public void createAlbum(String title, Song song){
        Album newAlbum = new Album(title, song);
    }

    public boolean containsAlbum(String album){
        boolean bool = false;
        for(Album al : albums){
            if(al.getAlbumName().equals(album)){
                bool = true;
            }
        }
        return bool;
    }

    public void addSongToAlbum(String albumName, Song song){
        for(Album al : albums){
            if(al.getAlbumName().equals(albumName)){
                al.addSong(song);
            }
        }
    }

}