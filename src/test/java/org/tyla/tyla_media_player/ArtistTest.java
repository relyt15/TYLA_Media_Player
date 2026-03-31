package org.tyla.tyla_media_player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArtistTest {

    private File file;
    private AVLibrary2 lib;
    private Song song;
    private Artist art;
    private ArrayList<Song> list;

    @BeforeEach
    void setUp() throws InterruptedException {

        lib = new AVLibrary2();
        file = new File("C:\\Users\\karlt\\OneDrive\\Documents\\TestDirSchool");
        lib.addDir(file);
        song = lib.getSong("Konflict");
        art = new Artist("Test Artist");
        list = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {

        lib = null;
        file = null;
        song = null;
        art = null;
        list = null;
    }

    @Test
    void createAlbum() {

        assertEquals(0, art.getAlbumsSize());
        art.createAlbum("test ablum", song);
        assertEquals(1, art.getAlbumsSize());
    }

    @Test
    void containsAlbum() {

        assertFalse(art.containsAlbum("test ablum"));
        art.createAlbum("test ablum", song);
        assertTrue(art.containsAlbum("test ablum"));
    }

    @Test
    void addSongToAlbum() {

        art.createAlbum("test ablum", song);
        assertTrue(art.getAlbum("test ablum").songs.contains(song));
    }
}