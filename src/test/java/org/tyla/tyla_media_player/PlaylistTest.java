package org.tyla.tyla_media_player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    private File file;
    private AVLibrary2 lib;
    private Song song;
    private Song song2;
    private Playlist play;
    private ArrayList<Song> list;

    @BeforeEach
    void setUp() throws InterruptedException {
        lib = new AVLibrary2();
        file = new File("C:\\Users\\Tyler\\Documents\\School Stuff\\TYLA_MP Test Dir");
        lib.addDir(file);
        song = lib.getSong("Clap w verb");
        song2 = lib.getSong("Oof");
        play = new Playlist("Test Playlist");
        list = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        lib = null;
        file = null;
        song = null;
        play = null;
        list = null;
    }

    @Test
    void addSong() {
        play.addSong(song);
        assertFalse(play.getSongList().isEmpty());
        assertTrue(play.getSongList().contains(song));
        assertNotEquals(0, play.getSongTotal());
    }

    @Test
    void removeSong() {
        play.addSong(song);
        play.removeSong(song);
        assertTrue(play.getSongList().isEmpty());
        assertEquals(0, play.getSongTotal());
    }

    @Test
    void clearPlaylist() {
        play.addSong(song);
        play.clearPlaylist();
        assertTrue(play.getSongList().isEmpty());
        assertEquals(0, play.getSongTotal());
    }

    @Test
    void changeName() {
        play.changeName("NEWTESTNAME");
        assertEquals("NEWTESTNAME", play.getName());
        assertNotEquals("Test Playlist", play.getName());
    }

    @Test
    void getName() {
        assertFalse(play.getName().isEmpty());
        assertEquals("Test Playlist", play.getName());
    }

    @Test
    void getSongList() {
        play.addSong(song);
        list.add(song);
        assertFalse(play.getSongList().isEmpty());
        assertEquals(list, play.getSongList());
    }

    @Test
    void getSongTotal(){
        assertEquals(0, play.getSongTotal());
        play.addSong(song);
        assertEquals(1, play.getSongTotal());
    }

    @Test
    void getSongNames() {
        play.addSong(song);
        assertEquals(song.getTitle() + "\n", play.getSongNames()); // only setup for a single song currently.
    }

    @Test
    void changeSongOrder() {
        play.addSong(song);
        play.addSong(song2);
        play.changeSongOrder(song, 1);
        assertEquals(0, play.getSongOrder(song));
    }

    @Test
    void getSongOrder() {
        play.addSong(song);
        play.addSong(song2);
        assertEquals(0, play.getSongOrder(song));
        assertEquals(1, play.getSongOrder(song2));
    }
}