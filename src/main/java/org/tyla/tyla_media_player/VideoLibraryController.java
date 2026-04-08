package org.tyla.tyla_media_player;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class VideoLibraryController implements Initializable {

    // stores file paths //
    @FXML private ListView<String> videoListView;
    @FXML private ListView<String> dirListView;
    @FXML private Label lblVideoCount;

    // adding and removing directories //

    @FXML private Button btnAddDir;
    @FXML private Button btnRemoveDir;

    private AVLibrary2 library;
    private MainController mainController;

    // injects library and main controller //
    public void setLibrary(AVLibrary2 library, MainController mainController) {
        this.library = library;
        this.mainController = mainController;
        refreshVideoList();
        refreshDirList();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // creates cells for formatting
        videoListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null) { setText(null); setGraphic(null); return; }

                HashMap<String, HashMap<String, String>> map = library != null
                        ? library.getVideoMap() : new HashMap<>();
                HashMap<String, String> meta = map.getOrDefault(path, new HashMap<>());

                String title    = meta.getOrDefault("title", "Unknown");
                String duration = meta.getOrDefault("duration_formatted", "00:00:00");

                // handle unknown name
                if (title.equals("Unknown")) {
                    title = new File(path).getName();
                }
                // gridpane to line up metadata with header //
                GridPane row = new GridPane();
                row.setHgap(5);

                ColumnConstraints cNum  = new ColumnConstraints(28);
                ColumnConstraints cTitle = new ColumnConstraints();
                cTitle.setHgrow(Priority.ALWAYS);
                cTitle.setFillWidth(true);
                cTitle.setMinWidth(60);
                ColumnConstraints cDur  = new ColumnConstraints(80);
                cDur.setMinWidth(80); cDur.setMaxWidth(80);

                row.getColumnConstraints().addAll(cNum, cTitle, cDur);
                row.prefWidthProperty().bind(lv.widthProperty().subtract(20));

                Label num   = cell(String.valueOf(getIndex() + 1), "#444444");
                Label name  = cell(title,    "#cccccc");
                Label dur   = cell(duration, "#555555");

                name.setMaxWidth(Double.MAX_VALUE);

                row.add(num,  0, 0);
                row.add(name, 1, 0);
                row.add(dur,  2, 0);
                setGraphic(row);
                setText(null);
            }
        });

        // double click to play //
        videoListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && mainController != null) {
                String selected = videoListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try { mainController.showVideoPlayer(selected); }
                    catch (Exception ex) { ex.printStackTrace(); }
                }
            }
        });
    }
// method for cell labels //
    private Label cell(String text, String color) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-family: 'Courier New'; " +
                "-fx-font-size: 10px; -fx-padding: 0 4 0 0;");
        lbl.setEllipsisString("…");
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

    @FXML
    // opens folder picker/adds directory //
    private void btnAddDirOnClick() throws InterruptedException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Add Video Folder");
        File dir = chooser.showDialog(btnAddDir.getScene().getWindow());
        if (dir != null) {
            library.addDir(dir);
            refreshVideoList();
            refreshDirList();
        }
    }

    @FXML
    // removes directory //

    private void btnRemoveDirOnClick() {
        String selected = dirListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            library.removeDir(new File(selected));
            refreshVideoList();
            refreshDirList();
        }
    }

    private void refreshVideoList() {
        // refreshes video list currently kinda broken //
        videoListView.getItems().clear();
        if (library == null) return;
        HashMap<String, HashMap<String, String>> map = library.getVideoMap();
        videoListView.getItems().addAll(map.keySet());
        lblVideoCount.setText(videoListView.getItems().size() + " VIDEOS");
    }

    private void refreshDirList() {
        dirListView.getItems().clear();
        if (library == null) return;
        String dirs = library.getDirList();
        if (!dirs.isBlank()) {
            for (String d : dirs.split("\n")) {
                dirListView.getItems().add(d.trim());
            }
        }
    }
}