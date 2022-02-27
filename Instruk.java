package sample;
//INSTRUKCJA OBSLUGI

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.util.Duration;

/**
 * Klasa odpowiedzialna za wyswietlanie instrukcji gry
 */
public class Instruk extends SubScene {
    private boolean IS_PANEL_HIDDEN;
    private Opis_Gry opis_gry;

    /**
     * Subscena do wyswietlania intrukcji gry na szarym kwadracie
     */
    public Instruk() {
        super(new AnchorPane(), 500.0D, 500.0D);
        this.prefWidth(500.0D);
        this.prefHeight(500.0D);
        this.opis_gry = new Opis_Gry("Witamy w grze Catch health. Gra polega na odpowiednim poruszaniu się bohaterem w celu zdobycia jak najwiekszej ilosc owocow i uniknięcia zderzenia z fast foodami. Sterowanie bohaterem odbywa się używajac lewego i prawego klawisza strzałek. Powodzenia! ");
        this.opis_gry.setLayoutX(0.0D);
        this.opis_gry.setLayoutY(-80.0D);
        Image ikonasceny = new Image("instrukcja2.png");
        BackgroundImage subscena = new BackgroundImage(ikonasceny, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, (BackgroundSize)null);
        AnchorPane anchorPane = (AnchorPane)this.getRoot();
        anchorPane.setBackground(new Background(new BackgroundImage[]{subscena}));
        anchorPane.getChildren().add(this.opis_gry);
        this.setLayoutX(1280.0D);
        this.setLayoutY(200.0D);
        this.IS_PANEL_HIDDEN = true;
    }

    /**
     * Ruszanie Subscena wyswietlanie i chowanie
     */
    public void movesubscene() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3D)); //szybkosc animacji
        transition.setNode(this);
        if (this.IS_PANEL_HIDDEN) {
            transition.setToX(-676.0D);
            this.IS_PANEL_HIDDEN = false;
        } else {
            transition.setToX(0.0D);
            this.IS_PANEL_HIDDEN = true;
        }

        transition.play();
    }
}
