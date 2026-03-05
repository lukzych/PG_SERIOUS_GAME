package PlayerInfo;

import java.awt.image.BufferedImage;

/**
 * Klasa bazowa przechowująca podstawowe informacje o graczu
 * Zawiera pozycje, prędkość, kierunek oraz obrazy
 */
public class PlayerInfo {

    /** Pozycja X i Y  gracza na ekranie (px)*/
    public int x, y;

    /** Prędkość poruszania się czyli ile px będziemy dodawać odejmować z naciśnięciem klawisza*/
    public int speed;

    /** Obraz gracza patrząceg w lewo*/
    public BufferedImage  left;

    /** Obraz gracza patrząceg w prawo*/
    public BufferedImage  right;

    /** Aktualny kierunek albo lewo albo prawo*/
    public String direction;
}
