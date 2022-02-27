package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * Label do opisu gry
 */
public class Opis_Gry extends Label {


    /**
     * Label do opisu gry
     * @param text instrukcja obslugi gry
     */

    public Opis_Gry(String text)
    {

        setPrefHeight(500);
        setPrefWidth(500);
        setPadding(new Insets(80,40,40,40));
        setText(text);
        setWrapText(true);
        setFont(Font.font("Verdana",25));
    }



}
