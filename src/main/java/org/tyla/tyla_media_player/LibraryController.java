package org.tyla.tyla_media_player;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {
    //playlist names, songs, and counts
    @FXML private ListView<String> playlistListView;
    @FXML private ListView<String> playlistSongView;
    @FXML private Label lblPlaylistSongCount;

    // control buttons //
    @FXML private Button btnNewPlaylist;
    @FXML private Button btnDeletePlaylist;
    @FXML private Button btnAddToPlaylist;
    @FXML private Button btnRemoveFromPlaylist;

    // libraries and directories //
    @FXML private ListView<Song> songListView;
    @FXML private ListView<String> dirListView;
    @FXML private TextField txtSearch;
    @FXML private Label lblLibrarySongCount;
    @FXML private Button btnAddDir;

    // library manager //
    @FXML private Button btnRescan;
    @FXML private Button btnClearLib;
    @FXML private Button btnRemoveDir;

    // sets library/controller //
    private AVLibrary2 library;
    private MainController mainController;
    public void setLibrary(AVLibrary2 library, MainController mainController) {
        this.library = library;
        this.mainController = mainController;
        refreshSongList();
        refreshDirList();
    }

    // compatibility
    public void setLibrary(AVLibrary2 library) {
        setLibrary(library, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //load songs when playlist selected//
        playlistListView.getSelectionModel().selectedItemProperty().addListener((obs, old, name) -> {
            if (name != null) {
                Playlist p = library.getPlaylist(name);
                if (p != null) refreshPlaylistSongs(p);
            }
        });
        songListView.setCellFactory(lv -> new ListCell<Song>() {
            @Override
            protected void updateItem(Song song, boolean empty) {
                // listen for playlist selection changes and refresh the song view when a different playlist is clicked
                super.updateItem(song, empty);
                if (empty || song == null) { setText(null); setGraphic(null); return; }

                int dur = song.duration;
                String time = String.format("%d:%02d", dur / 60, dur % 60);

                javafx.scene.layout.GridPane row = new javafx.scene.layout.GridPane();
                row.setHgap(5);

                javafx.scene.layout.ColumnConstraints c0 = new javafx.scene.layout.ColumnConstraints(28);
                javafx.scene.layout.ColumnConstraints c1 = new javafx.scene.layout.ColumnConstraints();
                c1.setHgrow(javafx.scene.layout.Priority.ALWAYS);
                c1.setFillWidth(true);
                c1.setMinWidth(60); // don't let it collapse to nothing
                javafx.scene.layout.ColumnConstraints c2 = new javafx.scene.layout.ColumnConstraints(140);
                c2.setMinWidth(140); c2.setMaxWidth(140);
                javafx.scene.layout.ColumnConstraints c3 = new javafx.scene.layout.ColumnConstraints(130);
                c3.setMinWidth(130); c3.setMaxWidth(130);
                javafx.scene.layout.ColumnConstraints c4 = new javafx.scene.layout.ColumnConstraints(50);
                c4.setMinWidth(50); c4.setMaxWidth(50);

                row.getColumnConstraints().addAll(c0, c1, c2, c3, c4);

                // Make the row fill the full cell width
                row.prefWidthProperty().bind(lv.widthProperty().subtract(18));

                Label num    = styledLabel(String.valueOf(getIndex() + 1), "#444444");
                Label title  = styledLabel(song.getTitle(), "#cccccc");
                Label artist = styledLabel(song.getArtist(), "#777777");
                Label album  = styledLabel(song.getAlbum(), "#555555");
                Label time_  = styledLabel(time, "#555555");

                // Title fills its column naturally via GridPane constraint
                title.setMaxWidth(Double.MAX_VALUE);

                row.add(num, 0, 0);
                row.add(title, 1, 0);
                row.add(artist, 2, 0);
                row.add(album, 3, 0);
                row.add(time_, 4, 0);
                setGraphic(row);
                setText(null);
            }
        });

        // doubleclick to play
        songListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && mainController != null) {
                Song selected = songListView.getSelectionModel().getSelectedItem();
                if (selected != null) mainController.playSong(selected);
            }
        });

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filterSongs(newVal));
    }
    // search filtering
    private Label styledLabel(String text, String color) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-family: 'Courier New'; " +
                "-fx-font-size: 10px; -fx-padding: 0 4 0 0;");
        lbl.setEllipsisString("…");
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }
    //creates new playlist
    @FXML
    private void btnNewPlaylistOnClick() {
        TextInputDialog dialog = new TextInputDialog("New Playlist");
        dialog.setTitle("Create Playlist");
        dialog.setHeaderText(null);
        dialog.setContentText("Playlist name:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                library.createPlaylist(name.trim());
                refreshPlaylistList();
            }
        });
    }
//deletes playlists
    @FXML
    private void btnDeletePlaylistOnClick() {
        String selected = playlistListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Playlist p = library.getPlaylist(selected);
            if (p != null) {
                library.removePlaylist(p);
                refreshPlaylistList();
                playlistSongView.getItems().clear();
                lblPlaylistSongCount.setText("0 SONGS");
            }
        }
    }
    // adds the song currently selected in the main song list to the selected playlist
    @FXML
    private void btnAddToPlaylistOnClick() {
        String selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
        Song selectedSong = songListView.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null && selectedSong != null) {
            Playlist p = library.getPlaylist(selectedPlaylist);
            if (p != null) {
                p.addSong(selectedSong);
                refreshPlaylistSongs(p);
            }
        }
    }
    // repopulates the playlist list view with all current playlist names
    @FXML
    private void btnRemoveFromPlaylistOnClick() {
        String selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
        String selectedTitle = playlistSongView.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null && selectedTitle != null) {
            Playlist p = library.getPlaylist(selectedPlaylist);
            if (p != null) {
                p.getSongList().stream()
                        .filter(s -> s.getTitle().equals(selectedTitle))
                        .findFirst()
                        .ifPresent(s -> { p.removeSong(s); refreshPlaylistSongs(p); });
            }
        }
    }
    // refresh managers
    private void refreshPlaylistList() {
        playlistListView.getItems().clear();
        for (Playlist p : library.getPlaylists())
            playlistListView.getItems().add(p.getName());
    }

    private void refreshPlaylistSongs(Playlist p) {
        playlistSongView.getItems().clear();
        for (Song s : p.getSongList())
            playlistSongView.getItems().add(s.getTitle());
        lblPlaylistSongCount.setText(p.getSongTotal() + " SONGS");
    }

    @FXML
    private void btnAddDirOnClick() throws InterruptedException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Add Music Folder");
        File dir = chooser.showDialog(btnAddDir.getScene().getWindow());
        if (dir != null) {
            library.addDir(dir);
            refreshSongList();
            refreshDirList();
        }
    }

    @FXML
    private void btnRescanOnClick() throws InterruptedException {
        library.rescanDirs();
        refreshSongList();
    }

    @FXML
    private void btnClearLibOnClick() {
        library.clearDirList();
        refreshSongList();
        refreshDirList();
    }

    @FXML
    private void btnRemoveDirOnClick() {
        String selected = dirListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            library.removeDir(new File(selected));
            refreshSongList();
            refreshDirList();
        }
    }

    private void filterSongs(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            songListView.getItems().setAll(library.getAudioList());
        } else {
            songListView.getItems().setAll(
                    librarySearch.searchLibrary(library.getAudioList(), keyword));
        }
        updateCount();
    }

    private void refreshSongList() {
        songListView.getItems().setAll(library.getAudioList());
        updateCount();
    }

    private void refreshDirList() {
        dirListView.getItems().clear();
        String dirs = library.getDirList();
        if (!dirs.isBlank()) {
            for (String d : dirs.split("\n")) {
                dirListView.getItems().add(d.trim());
            }
        }
    }

    private void updateCount() {
        lblLibrarySongCount.setText(songListView.getItems().size() + " SONGS");
    }
}