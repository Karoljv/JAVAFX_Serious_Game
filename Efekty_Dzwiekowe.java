package sample;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Objects;

/**
 * Efekty dzwiekowe dla przy kontakcie z owocami i fast foodami
 */

public class Efekty_Dzwiekowe {


    private String musicFile;
    private Media sound;
    private MediaPlayer mediaPlayer;

    public Efekty_Dzwiekowe(){}

    /**
     * Efekt dzwiekowy dla zdobycia punktow
     */
    public void punkty_up()
    {
        musicFile = "Dzwiek_zebrania_owocow.wav";     // For example

        sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.7);
        mediaPlayer.setAutoPlay(true);
    }

    /**
     * Efekt dzwiekowy dla stracenia punktow
     */
    public void health_down()
    {
        musicFile = "fast_food_cut.mp3";
        sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.7);
        mediaPlayer.setAutoPlay(true);
    }

}
