package org.tyla.tyla_media_player;

import java.util.HashMap;

public class Playlist {
    private HashMap<String, HashMap<String, String>> songMap;
    private int songTotal;
    private String name;

    public Playlist(String name){
        songTotal = 0;
        this.name = name;
    }

    /**
     * @precondition: input must be from AVLibrary object
     * @postcondition: adds an audio file from the AVLibrary object, into a new collection called a Playlist.
     * @param pathname
     * @param infoHash
     * @return
     */
    public void addSong(String pathname, HashMap<String, String> infoHash){
        if (!songMap.containsKey(pathname)){
            songMap.put(pathname, infoHash);
            songTotal++;
        }
    }

    public void removeSong(String pathname, HashMap<String, String> infoHash){
        if(songMap.containsKey(pathname)){
            songMap.remove(pathname, infoHash);
            songTotal--;
        }
    }

    public void changeName(String newName){
        this.name = newName;
    }

    public String getName(){
        return this.name;
    }

    public HashMap<String, HashMap<String, String>> getSongMap(){
        return songMap;
    }

    public String getSongNames(){
        String songNames = "";
        for (String f : songMap.keySet()){
            songNames += songMap.get(f).get("title") + "\n";
        }
        return songNames;
    }

    public String getSongArtists(){
        String artists = "";
        for (String f : songMap.keySet()){
            artists += songMap.get(f).get("artist") + "\n";
        }
        return artists;
    }

    public String getSongGenres(){
        String genres = "";
        for (String f : songMap.keySet()){
            genres += songMap.get(f).get("genre") + "\n";
        }
        return genres;
    }

    public String getSongAlbums(){
        String albums = "";
        for (String f : songMap.keySet()){
            albums += songMap.get(f).get("album") + "\n";
        }
        return albums;
    }




}
