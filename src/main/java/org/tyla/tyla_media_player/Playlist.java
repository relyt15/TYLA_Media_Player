package org.tyla.tyla_media_player;

import java.util.ArrayList;
import java.util.HashMap;

public class Playlist {
    //private HashMap<String, HashMap<String, String>> songMap;
    private ArrayList<Song> songList;
    private int songTotal;
    private String name;

    public Playlist(String name){
        songTotal = 0;
        songList = new ArrayList<>();
        this.name = name;
    }

    /**
     * @precondition: input must be from AVLibrary object
     * @postcondition: adds an audio file from the AVLibrary object, into a new collection called a Playlist.
     * @param song
     * @return
     */
    public void addSong(Song song){
        if (!songList.contains(song)){
            songList.add(song);
            songTotal++;
        }
    }

    public void removeSong(Song song){
        if(songList.contains(song)){
            songList.remove(song);
            songTotal--;
        }
    }

    public void clearPlaylist(){
        while(songTotal > 0){
            removeSong(songList.get(songTotal-1));
        }
    }

    public void changeName(String newName){
        this.name = newName;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Song> getSongList(){
        return songList;
    }
    public int getSongTotal(){
        return songTotal;
    }

    public String getSongNames(){
        String songNames = "";
        for (Song f : songList){
            songNames += f.getTitle() + "\n";
        }
        return songNames;
    }

    public void changeSongOrder(Song song, int num){
        int prevIndex = -1;
        Song songBuffer;

        if(songList.contains(song) && songList.size() <= num){
            prevIndex = songList.indexOf(song); //saves current index of input song
            songBuffer = songList.get(num); //saves current song in index num
            //songList.add(num, song); //places input song into index num
            if(num > songList.indexOf(song)){
                for(int i = songList.indexOf(song) + 1; i < num; i++){
                    songList.add(songList.indexOf(songList.get(i-1)), songList.get(i));
                }
                songList.add(num, songBuffer);
            }
            if (num < songList.indexOf(song)){
                for(int i = songList.indexOf(song) - 1; i > num; i--){
                    songList.add(songList.indexOf(songList.get(i+1)), songList.get(i));
                }
                songList.add(num, songBuffer);
            }
        }
    }

    /**
     * @precondition:
     * @postcondition: returns -1 if song is not in the playlist, else returns index of the song
     * @param song
     * @return
     */
    public int getSongOrder(Song song){
        int index = -1;
        if (songList.contains(song)) {
            index = songList.indexOf(song);
        }
        return index;
    }




}
