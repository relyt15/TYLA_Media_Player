module org.tyla.tyla_media_player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires jaudiotagger;
    requires uk.co.caprica.vlcj;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires uk.co.caprica.vlcj.javafx;

    opens org.tyla.tyla_media_player to javafx.fxml;
    exports org.tyla.tyla_media_player;
}