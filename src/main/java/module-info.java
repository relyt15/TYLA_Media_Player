module org.tyla.tyla_media_player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens org.tyla.tyla_media_player to javafx.fxml;
    exports org.tyla.tyla_media_player;
}