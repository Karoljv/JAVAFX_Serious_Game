package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Klasa odpowiedzialna za calosc gry
 */
public class Game {

    private final double SZEROKOSC_GRY = 1280;
    private final double WYSOKOSC_GRY = 1024;


    private AnchorPane Gamepane; //layout
    private Scene GameScene;
    private Stage GameStage;

    private Stage menustage;
    private ImageView bohater;
    private AnimationTimer animationTimer;

    private ImageView[] fruits; //tablica obrazow owocow
    private ImageView[] fast_foods; // tablica obrazow fast_food
    private Random randomowe_spadanie; // klasa random


    private Punkty punkty; // klasa punkty
    private Efekty_Dzwiekowe efekty_dzwiekowe; // efekty dzwiekiowe


    private Instant start; // liczenie czasu
    private Instant stop; // liczenie czasu
    private long timeElapsed; // roznica czasu

    private long start_time; // liczenie czasu dla rozrywki(zwiekszanie poziomu trudnosci)

    private ImageView[] zycia_gracza; // tablica obrazow zycia gracza
    private int aktualne_zycie_gracza; // akutalne zycie gracza
    private int akutalne_punkty; // aktualne punkty


    private final static int Bohater_promien = 70; // traktowanie bohatera jako okrag o porminiu 70
    private final static int Owoce_i_fast_promien = 50; // traktowanie obiektow spadajacych jako okrag o promieniu 50

    private Wyniki wyniki; // wyniki do pliku txt
    private Alert alert; // wyskakujace okienko jesli sie przegralo, pytajace czy chce sie grac dalej

    /**
     * Konstruktor klasy Game
     */
    public Game()
    {
        start = Instant.now(); // rozpoczacie pomiaru czasu(dla pliku txt i wyswietlania komunikatu o wyniku)
        start_time = System.nanoTime();

        Gamepane = new AnchorPane();
        GameScene = new Scene(Gamepane,SZEROKOSC_GRY,WYSOKOSC_GRY);
        GameStage = new Stage();
        GameStage.setScene(GameScene);
        GameStage.getIcons().add(new Image("file:apple.png")); // mini ikona gry(jablko)
        Gamebackground(); // rysowanie tla
        randomowe_spadanie = new Random(); // wywolanie klasy random
        efekty_dzwiekowe = new Efekty_Dzwiekowe(); // wywolanie efektow dzwiekowych dla obiektow spadajacych
        PRZYCISKI_MENU();//wstawienie buttonow do gry

    }

    /**
     * Tworzenie przyciskow w grze
     */
    public void PRZYCISKI_MENU()
    {

        Przyciski przycisk_resetu = new Przyciski("PAUZA"); // Pauza ANIMACJI
        przycisk_resetu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                animationTimer.stop();
            }
        });
        Przyciski wznow = new Przyciski("WZNOW"); // WZNOWIENIE ANIMACJI
        wznow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                animationTimer.start();
            }
        });

        Przyciski wroc_do_menu = new Przyciski("MENU");
        wroc_do_menu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameStage.close(); // zamykanie ekranu gry
                animationTimer.stop(); // zatrzymanie animacji
                menustage.show();
                stop = Instant.now(); // zatrzymanie czasu klasy Instant
                timeElapsed = java.time.Duration.between(start,stop).toSeconds(); // uplyniety czas
                wyniki = new Wyniki(akutalne_punkty,timeElapsed);//dodawanie wynikow gracza po wybraniu menu
            }
        });



        Gamepane.getChildren().add(przycisk_resetu);
        przycisk_resetu.setLayoutX(1100);
        przycisk_resetu.setLayoutY(950);

        Gamepane.getChildren().add(wznow);
        wznow.setLayoutX(1100);
        wznow.setLayoutY(850);

        Gamepane.getChildren().add(wroc_do_menu);
        wroc_do_menu.setLayoutY(750);
        wroc_do_menu.setLayoutX(1100);


    }


    /**
     *
     * @param menustage menustage
     */
    public void Start_game(Stage menustage)
    {
        this.menustage = menustage;
        this.menustage.hide();
        Bohater();
        Owoce();
        Points();
        Fast_foody();
        Gameloop();
        Kolizja();
        GameStage.show();

    }

    /**
     * Wstawienie panelu z punktami w lewym gornym oraz paska zycia(serc) w prawym gornym rogu
     */
    private void Points()
    {
        punkty = new Punkty("Punkty 00");
        punkty.setLayoutX(10);
        punkty.setLayoutY(10);
        Gamepane.getChildren().add(punkty);


        aktualne_zycie_gracza = 2;

        zycia_gracza = new ImageView[3];
        for (int i = 0; i < zycia_gracza.length; i++) { //petla ustawienia 3 serc w prawym gornym
            zycia_gracza[i] = new ImageView("serce.png"); //zapelnienie tablicy obrazami serca
            zycia_gracza[i].setFitHeight(60); // powiekszenie obrazu serca
            zycia_gracza[i].setFitWidth(60); // powiekszenie obrazu serca
            zycia_gracza[i].setLayoutX(1100 + (i*50));
            zycia_gracza[i].setLayoutY(10);
            Gamepane.getChildren().add(zycia_gracza[i]);
        }


    }


    /**
     * Kolizja bohatera z obiektami
     */
    private void Kolizja()
    {

        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fruits[0].getLayoutX()+200,bohater.getLayoutY()+258,fruits[0].getLayoutY()+200))
        {
            Pozycja_spadania(fruits[0]); //resetowanie spadania po natrafieniu na obiekt
            Dodaj_punkty(); //dodawanie punktow
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fruits[1].getLayoutX()+200,bohater.getLayoutY()+258,fruits[1].getLayoutY()+200))
        {
            Pozycja_spadania(fruits[1]);
            Dodaj_punkty();
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fruits[2].getLayoutX()+200,bohater.getLayoutY()+258,fruits[2].getLayoutY()+200))
        {
            Pozycja_spadania(fruits[2]);
            Dodaj_punkty();
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fruits[3].getLayoutX()+200,bohater.getLayoutY()+258,fruits[3].getLayoutY()+200))
        {
            Pozycja_spadania(fruits[3]);
            Dodaj_punkty();
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fruits[4].getLayoutX()+200,bohater.getLayoutY()+258,fruits[4].getLayoutY()+200))
        {
            Pozycja_spadania(fruits[4]);
            Dodaj_punkty();
        }

        /////////////////////////////////////////////////////////////////////
        // fast_foody
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fast_foods[0].getLayoutX()+200,bohater.getLayoutY()+258,fast_foods[0].getLayoutY()+200))
        {
            Pozycja_spadania(fast_foods[0]); //resetowanie pozycji spadania danego fast_foodu
            Zabierz_zycie(); // zabieranie zycia
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fast_foods[1].getLayoutX()+200,bohater.getLayoutY()+258,fast_foods[1].getLayoutY()+200))
        {
            Pozycja_spadania(fast_foods[1]);
            Zabierz_zycie();
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fast_foods[2].getLayoutX()+200,bohater.getLayoutY()+258,fast_foods[2].getLayoutY()+200))
        {
            Pozycja_spadania(fast_foods[2]);
            Zabierz_zycie();
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fast_foods[3].getLayoutX()+200,bohater.getLayoutY()+258,fast_foods[3].getLayoutY()+200))
        {
            Pozycja_spadania(fast_foods[3]);
            Zabierz_zycie();
        }
        if(Bohater_promien + Owoce_i_fast_promien > ODLEGOSC_BOHATERA_I_OBIKETOW(bohater.getLayoutX()+366,fast_foods[4].getLayoutX()+200,bohater.getLayoutY()+258,fast_foods[4].getLayoutY()+200))
        {
            Pozycja_spadania(fast_foods[4]);
            Zabierz_zycie();
        }

    }

    /**
     * zabieranie zycia bohaterowi
     */
    private void Zabierz_zycie()
    {
        //akutalne zycie gracza jako 2 w tabilcy imageview[3]
            Gamepane.getChildren().remove(zycia_gracza[aktualne_zycie_gracza]);
            aktualne_zycie_gracza--;
            if (aktualne_zycie_gracza == -1) { // -1 bo dla dwoch serc by sie crashowalo

                animationTimer.stop(); // zatrzymanie animacji spadania

                stop = Instant.now(); // zatrzymanie czasu klasy Instant
                timeElapsed = java.time.Duration.between(start,stop).toSeconds(); // uplyniety czas
                wyniki = new Wyniki(akutalne_punkty,timeElapsed);//dodawanie wynikow gracza po wybraniu menu

                Platform.runLater(() -> { // Platform.runLater aby dzialalo ShowandWait
                    alert = new Alert(Alert.AlertType.CONFIRMATION); // wyskakiwanie okienka z pytaniem o ponowna gre
                    alert.setTitle("PRZEGRALES!");
                    alert.setHeaderText("Twoj wynik to " + akutalne_punkty + " punktow" + " w czasie " + timeElapsed + " sekund" );
                    alert.setContentText("Czy chcesz zagrac ponownie? Wybierz OK aby zaczac od nowa lub Cancel przy przejsc do menu");
                    alert.showAndWait(); //nie dziala bez runLater()
                    if(alert.getResult() == ButtonType.OK) { // wychodzenie do menu lub ponowna gra
                        GameStage.close();
                        Gamepane.getChildren().clear();// czysci animacje z poprzedniej gry
                        akutalne_punkty = 0; // zerowanie punktow do nastepnej gry
                        start = Instant.now(); // zerowanie czasu z poprzedniej animacjii
                        //tworzenie nowej gry(to samo co start_game)
                        PRZYCISKI_MENU();
                        Bohater();
                        Owoce();
                        Points();
                        Fast_foody();
                        Gameloop();
                        Kolizja();
                        GameStage.show();
                    }
                    else //przechodzenie do menu w wypadku nie wybrania OK
                    {
                        GameStage.close();
                        menustage.show();
                    }

                        }
                );
            }

            efekty_dzwiekowe.health_down(); // wywolanie efektu dzwiekowego dla fast_foodow

    }

    /**
     * metoda liczy odlegosc bohatera i obiektow
     * @param x1 wspolrzedne x bohatera
     * @param x2 wspolrzedne x obiektu
     * @param y1 wspolrzedne y bohatera
     * @param y2 wspolrzedne y obiektu
     * @return modul odleglosci
     */
    private double ODLEGOSC_BOHATERA_I_OBIKETOW(double x1, double x2, double y1, double y2) // obliczanie odległość bohatera i innych obiektów
    {
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));

    }

    /**
     * Dodawanie punktow za zebranie owocow i warzyw
     */
    private void Dodaj_punkty() // dodawanie punktów za zebranie owoców
    {

        akutalne_punkty++;
        String textToSet = "POINTS : ";
        if (akutalne_punkty < 10) {
            textToSet = textToSet + "0";
        }
        punkty.setText(textToSet + akutalne_punkty);//ustawianie punktow
        efekty_dzwiekowe.punkty_up(); // wywolanie efektu dzwiekowego dla owocow i warzyw

    }

    /**
     * Wywolanie spadania oraz Kolizji
     * Rozpoczecie Timera
     */
    private void Gameloop() // KOLIZJA I SPADANIE
    {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                Spadanie();
                Kolizja();

            }
        };

        animationTimer.start();
    }

    /**
     * zapelnienie tablicy owocow
     */
    private void Owoce()
    {
        fruits = new ImageView[5];
        fruits[0] = new ImageView("apple.png");
        fruits[1] = new ImageView("bananek.png");
        fruits[2] = new ImageView("lemon.png");
        fruits[3] = new ImageView("gruszka.png");
        fruits[4] = new ImageView("Orange.png");

        for (ImageView fruit : fruits) {
            Pozycja_spadania(fruit);
            Gamepane.getChildren().add(fruit);
        }

    }

    /**
     * zapelnienie tablicy fast_foodow
     */
    private void Fast_foody()
    {
        fast_foods = new ImageView[5];
        fast_foods[0] = new ImageView("cola.png");
        fast_foods[1] = new ImageView("burger.png");
        fast_foods[2] = new ImageView("frytki.png");
        fast_foods[3] = new ImageView("pizza.png");
        fast_foods[4] = new ImageView("chipsy.png");

        for (ImageView fast_food:fast_foods) {
            Pozycja_spadania(fast_food);
            Gamepane.getChildren().add(fast_food);
        }

    }

    /**
     * ustawienie losowych pozycji spadania
     * @param losowo parametr przekazany z metod Fast_foody() oraz Owoce()
     */
    private void Pozycja_spadania(ImageView losowo)
    {
        losowo.setLayoutX(randomowe_spadanie.nextInt(900));
        losowo.setLayoutY(-(randomowe_spadanie.nextInt(3200)+400));

    }


    /**
     * spadanie obiektow i resetowanie ich po znajdowaniu sie poza ekranem
     */
    private void Spadanie()
    {
        long elapsedtime = System.nanoTime() - start_time; // uplyniety czas
        long seconds = TimeUnit.SECONDS.convert(elapsedtime,TimeUnit.NANOSECONDS); // zamiana na sekundy
        for (ImageView fruit : fruits) {
            if(fruit.getLayoutY()>1024) // jesli wyjdzie poza ekran - resetuj spadanie
            {
                Pozycja_spadania(fruit);
            }
            fruit.setLayoutY(fruit.getLayoutY()+3); // szybkosc spadania(stala)
        }
        for (ImageView fast_food:fast_foods) {
            if(fast_food.getLayoutY()>1024)
            {
                Pozycja_spadania(fast_food);
            }//zwiekszanie szybkosci spadania fastfoodow wraz z uplywem czasu(stopniowanie poziomu trudnosci
            if(seconds < 10)
            fast_food.setLayoutY(fast_food.getLayoutY()+0.7);
            if(seconds < 20 && seconds >= 10)
                fast_food.setLayoutY(fast_food.getLayoutY()+1.4);
            if(seconds < 30 && seconds >=20)
                fast_food.setLayoutY(fast_food.getLayoutY()+2.1);
            if(seconds <40 && seconds >=30)
                fast_food.setLayoutY(fast_food.getLayoutY()+3);
            if(seconds < 50 && seconds >=40)
                fast_food.setLayoutY(fast_food.getLayoutY()+4);
            if(seconds >=50)
                fast_food.setLayoutY(fast_food.getLayoutY()+4.5);

        }



    }

    /**
     * Inicjacja bohatera
     * Dodanie efektu poslizgu
     */
    private void Bohater()
    {
        bohater = new ImageView("hero.png");
        bohater.setLayoutX(SZEROKOSC_GRY/2); // wysrodkowanie
        bohater.setLayoutY(WYSOKOSC_GRY - 258); //258 wysokosc bohatera
        Gamepane.getChildren().add(bohater);

        // poslizg

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(bohater);

        short velocity = 20; // poczatkowa predkosc

        GameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.A)
                {
                    if(aktualne_zycie_gracza == 2) { // predkosc bohatera w zaleznosci od zycia
                        bohater.setLayoutX(bohater.getLayoutX() - velocity);//zmiana pozycji
                    }
                    if(aktualne_zycie_gracza == 1)
                    {
                        bohater.setLayoutX(bohater.getLayoutX() - (velocity-5)); // spowolnienie gracza za stracenie życia
                    }
                    else
                    {
                        bohater.setLayoutX(bohater.getLayoutX() - (velocity-10)); // spowolnienie gracza za stracenie życia
                    }
                    transition.setToX(-20);
                    transition.play();

                }
                if(keyEvent.getCode() == KeyCode.D)
                {
                    if(aktualne_zycie_gracza == 2) {
                        bohater.setLayoutX(bohater.getLayoutX() + velocity);//zmiana pozycji
                    }
                    if(aktualne_zycie_gracza == 1)
                    {
                        bohater.setLayoutX(bohater.getLayoutX() + (velocity-5)); //// spowolnienie gracza za stracenie życia
                    }
                    else
                    {
                        bohater.setLayoutX(bohater.getLayoutX() + (velocity-10)); // spowolnienie gracza za stracenie życia
                    }
                    transition.setToX(20);
                    transition.play();
                }
            }
        });

    }

    /**
     * Glowne tlo gry
     */
    private void Gamebackground()
    {
        Image gameimage = new Image("fajne_tlo.png");
        BackgroundImage backgroundImage = new BackgroundImage(gameimage, BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,null);
        Gamepane.setBackground(new Background(backgroundImage));
    }

}
