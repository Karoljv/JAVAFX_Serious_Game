package sample;


//SKLADOWANIE WYNIKOW NA KONIEC GRY


import java.io.FileWriter;
import java.io.IOException;

/**
 * Dodawanie wynikow do pliku txt
 */
public class Wyniki {

    private String filename;
    private FileWriter fileWriter;

    /**
     * Zapisywanie wynikow do pliku .txt
     * @param points liczba zdobytych punktow
     * @param game_time czas gry
     */
    public Wyniki(int points, long game_time)
    {
        try {
            filename = "Wyniki.txt";
            fileWriter = new FileWriter(filename, true);
            fileWriter.write("punkty: " + points + " czas gry: " + game_time + " sekund" + "\n");
            fileWriter.close();
        }
        catch(IOException ioException)
        {

            System.err.println("IOExecption " + ioException.getMessage());

        }

    }


}
