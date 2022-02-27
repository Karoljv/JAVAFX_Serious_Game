package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Klasa menu
 */
public class Menu {


    private final double SZEROKOSC_GRY = 1280;
    private final double WYSOKOSC_GRY = 1024;

    private AnchorPane mainpane; //layout
    private Scene mainScene;
    private Stage mainStage; // boss

    private Instruk Inctruction; //obiekt klasy Instruk
    private Game game; // obiekt klasy Game
    private Muzyka muzyka; // obiekt klasy Muzyka

    /**
     * Konstruktor, tworzenie menu
     */
    public Menu()
    {

        muzyka = new Muzyka();
        mainpane = new AnchorPane();
        mainScene = new Scene(mainpane,SZEROKOSC_GRY,WYSOKOSC_GRY);// dodawanie layoutu i wymiarow
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        Subscena();
        CreateButton();
        Menubackground();
        Logo();



    }

    /**
     *
     * @return getter
     */
    public Stage getMainStage()
    {
        return mainStage;
    }

    /**
     * Wywolanie subsceny
     */
    private void Subscena()
    {
        Inctruction = new Instruk();
        mainpane.getChildren().add(Inctruction);
    }

    /**
     * Tworzenie buttownow do menu
     */
    private void CreateButton() // dodanie przyvisku
    {
        Przyciski start = new Przyciski("Graj"); // przechodzenie do glownej gry
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game = new Game();
                game.Start_game(mainStage);


            }
        });



        Przyciski exit = new Przyciski("Exit"); // wyjscie z gry
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.close();
            }
        });


        Przyciski obsluga_gry = new Przyciski("Instrukcja"); //instrukcja obslugi
        obsluga_gry.setOnAction(new EventHandler<ActionEvent>() { // ruszanie instrukcja obslugi(subscena)
            @Override
            public void handle(ActionEvent actionEvent) {
                Inctruction.movesubscene();
            }
        });


        mainpane.getChildren().add(start);
        mainpane.getChildren().add(obsluga_gry);
        mainpane.getChildren().add(exit);

        start.setLayoutY(200);
        start.setLayoutX(200); // pozycja startowa przycisku

        obsluga_gry.setLayoutY(400);
        obsluga_gry.setLayoutX(200); // pozycja startowa przycisku

        exit.setLayoutY(600);
        exit.setLayoutX(200); // pozycja startowa przycisku

    }

    /**
     * Logo gry Catch Health
     */
    private void Logo()
    {
        ImageView imageView = new ImageView("cooltext.png");
        imageView.setLayoutX(500);
        imageView.setLayoutY(100);

        mainpane.getChildren().add(imageView);
    }

    /**
     * Tlo do menu glownego
     */
    private void Menubackground()// menu glowne tlo
    {
        Image menuimage = new Image("menu.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(menuimage, BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,null);
        mainpane.setBackground(new Background(backgroundImage));
    }

}
