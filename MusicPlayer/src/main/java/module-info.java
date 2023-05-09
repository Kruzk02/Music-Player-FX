module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires javafx.media;

    opens com.example.musicplayer to javafx.fxml;
    exports com.example.musicplayer;
}