package com.example.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    @FXML
    Button playButton,pauseButton,nextButton,prevButton,shuffleButton,repeatButton,volumeButton,muteButton;
    @FXML
    Slider volumeSlider;
    @FXML
    ProgressBar progressbar;
    @FXML
    Label songLabel;

    Media media;
    MediaPlayer mediaPlayer;

    File directory;
    File[] file;

    ArrayList<File> songs;

    Timer timer;
    TimerTask timerTask;

    int songNumber;
    boolean isPlaying;
    boolean isShuffle;
    boolean isRepeat;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        songs = new ArrayList<>();

        directory = new File("music");
        file = directory.listFiles();

        if(file != null){
            for(File file : file){
                songs.add(file);
            }
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
            }
        });
        progressbar.setStyle("-fx-accent:#4E38A9;");
    }
    public void Play() {
        beginTime();
        mediaPlayer.play();

        mediaPlayer.setVolume(volumeSlider.getValue()*0.01);

        playButton.setVisible(false);
        pauseButton.setVisible(true);
    }

    public void Pause() {
        pauseTimer();
        mediaPlayer.pause();
        playButton.setVisible(true);
        pauseButton.setVisible(false);
    }

    public void Next() {
        if(songNumber <songs.size()-1){
            songNumber++;
            mediaPlayer.stop();
            if(isPlaying){
                pauseTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            Play();
        }else{
            songNumber = 0;
            mediaPlayer.stop();

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            Play();
        }
    }

    public void Prev(ActionEvent actionEvent) {
        if(songNumber >0){
            songNumber--;
            mediaPlayer.stop();
            if(isPlaying){
                pauseTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            Play();
        }else{
            songNumber = songs.size()-1;
            mediaPlayer.stop();

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            Play();
        }
    }

    public void Shuffle(ActionEvent actionEvent) {
        isShuffle = !isShuffle;
        if(isShuffle){
            int n = songs.size();
            for (int i = n - 1; i > 0; i--) {
                int j = (int) (Math.random() * (i + 1));
                Collections.swap(songs, i, j);
            }
            shuffleButton.setStyle("-fx-background-color:#212120");
        }else {
            shuffleButton.setStyle("-fx-background-color:#403f3e");
        }
    }

    public void Repeat(ActionEvent actionEvent) {
        isRepeat = !isRepeat;
        if (isRepeat){
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                mediaPlayer.seek(Duration.ZERO);
                }
            });
            Play();
            repeatButton.setStyle("-fx-background-color:#212120");
        }else{
            repeatButton.setStyle("-fx-background-color:#403f3e");
        }
    }

    public void Volume(ActionEvent actionEvent) {
        mediaPlayer.setMute(true);
        volumeButton.setVisible(false);
        muteButton.setVisible(true);
    }

    public void Mute(ActionEvent actionEvent) {
        mediaPlayer.setMute(false);
        volumeButton.setVisible(true);
        muteButton.setVisible(false);
    }
    public void beginTime(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                isPlaying = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();

                progressbar.setProgress(current/end);

                if(current/end ==1){
                    pauseTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }
    public void pauseTimer() {
        isPlaying = false;
        timer.cancel();
    }
}