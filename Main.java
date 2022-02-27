package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;

/**
 * Klasa Main
 */
public class Main extends Application {
    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Menu menu = new Menu();
        primaryStage = menu.getMainStage();
        primaryStage.show();
        primaryStage.setTitle("Catch health");
        primaryStage.getIcons().add(new Image("file:apple.png"));


    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}



    /*Brazilian Street Fight by Punch Deck | https://soundcloud.com/punch-deck
        Music promoted by https://www.chosic.com/free-music/all/
        Creative Commons Attribution 3.0 Unported License
        https://creativecommons.org/licenses/by/3.0/deed.en_US

     */