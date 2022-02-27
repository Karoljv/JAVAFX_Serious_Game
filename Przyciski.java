package sample;
//BUTTONY W GRZE


import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

/**
 * Klasa odpowiedzialna za tworzenie przyciskow w grze
 */
public class Przyciski extends Button {

    /**
     *
     * @param text tekst na buttonie
     */
    public Przyciski(String text)
    {
        setText(text);
        setButtonFont();
        setPrefHeight(49); //wymiary button
        setPrefWidth(190); // wymiary button
        Buttonlistener();
    }

    /**
     * Ustawienie czcionki na button
     */
    private void setButtonFont()
    {
        setFont(Font.font("Comic Sans MS",25));
    }

    /**
     * Zmniejsze buttonu po nacisnieciu
     */
    private void setButtonPressed()
    {
        setPrefHeight(45); // zmniejszanie rozmiaru buttonu
        setLayoutY(getLayoutY()+4);
    }

    /**
     * Zwiekszenie buttonu gdy nie ma na nim myszki
     */
    private void setButtonReleased()
    {
        setPrefHeight(49);
        setLayoutY(getLayoutY()-4);
    }

    /**
     *Button Listenery
     */
    private void Buttonlistener(){


        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    setButtonPressed();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    setButtonReleased();
                }
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                    setEffect(new DropShadow());
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    setEffect(null);
                }
            }
        });
    }


}
