package sample;

//LABEL DO PUNKTOW

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

/**
 * Tworzenie Labela do punktow w grze
 */
public class Punkty extends Label {

    /**
     * Zolty Label do punktow
     * @param text punkty gracza jako String
     */
    public Punkty(String text)
    {
        setPrefHeight(58);
        setPrefWidth(200);
        BackgroundImage backgroundImage = new BackgroundImage(new Image("button_yellow.png",200,53,false,true),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        setBackground(new Background(backgroundImage));
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10,10,10,10));
        Setfont();
        setText(text);
    }

    /**
     * Ustawienie czcionki
     */
    private void Setfont()
    {
        setFont(Font.font("Comic Sans MS",20));
    }
}
