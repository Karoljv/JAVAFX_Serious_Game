package sample;

//MUZYKA

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Objects;

/**
 * Granie muzyki
 */
public class Muzyka {

    private MediaPlayer mediaPlayer;
    private Media sound;

    /**
     * Granie muyzki
     */
    public Muzyka()
    {

        String musicFile = "gra.mp3";

        sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.013); //ustawienie glosnosci
        mediaPlayer.setAutoPlay(true);
    }
}
