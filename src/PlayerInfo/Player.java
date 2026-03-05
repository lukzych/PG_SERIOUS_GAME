package PlayerInfo;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Klasa reprezentująca gracza w grze
 * Zarządza pozycją, ruchem i interakcją z mini-grami
 */
public class Player extends PlayerInfo {

    GamePanel gamePanel;
    KeyHandler keyHandler;

    /** Flaga czy gracz jest przy wejściu do gry 1 (0 lub 1) */
    int enterMiniGame1 = 0;

    /** Flaga czy gracz jest przy wejściu do gry 2 (0 lub 1) */
    int enterMiniGame2 = 0;

    /** Flaga czy gracz jest przy wejściu do gry 3 (0 lub 1) */
    int enterMiniGame3 = 0;

    /**
     * Konstruktor gracza
     *
     * @param gamePanel panel gry
     * @param keyHandler obsługa klawiatury
     */
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        playerImage();
        setValues();
    }

    /**
     * Ustawia początkową pozycję i prędkość gracza
     */
    public void setValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "left";
    }

    public void playerImage(){

        try{
            left = ImageIO.read(getClass().getResourceAsStream("/Images/boy_stand_left.png"));
            right = ImageIO.read(getClass().getResourceAsStream("/Images/boy_stand_right.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje pozycję gracza na podstawie wciśniętych klawiszy
     * Sprawdza kolizje z granicami ekranu i wejściami do mini-gier
     */
    public void update() {
        // Ruch gracza
        if (keyHandler.upPressed) {
            y -= speed;
            direction = "left";
        }
        if (keyHandler.downPressed) {
            y += speed;
            direction = "right";
        }
        if (keyHandler.leftPressed) {
            x -= speed;
            direction = "left";
        }
        if (keyHandler.rightPressed) {
            x += speed;
            direction = "right";
        }

        // Ograniczenie do granic ekranu
        x = Math.max(0, Math.min(x, 720));
        y = Math.max(0, Math.min(y, 526));

        // Sprawdź wejście do GRY 1 (lewa krawędź)
        if (x == 0 && y > 133 && y < 155) {
            enterMiniGame1 = 1;
            if (keyHandler.ePressed) {
                gamePanel.startMiniGame1();
                keyHandler.ePressed = false;
            }
        } else {
            enterMiniGame1 = 0;
        }

        // Sprawdź wejście do GRY 2 (dolna krawędź)
        if (y == 526 && x > 474 && x < 490) {
            enterMiniGame2 = 1;
            if (keyHandler.ePressed) {
                gamePanel.startMiniGame2();
                keyHandler.ePressed = false;
            }
        } else {
            enterMiniGame2 = 0;
        }

        // Sprawdź wejście do GRY 3 (prawa krawędź)
        if (x == 720 && y > 230 && y < 252) {
            enterMiniGame3 = 1;
            if (keyHandler.ePressed) {
                gamePanel.startMiniGame3();
                keyHandler.ePressed = false;
            }
        } else {
            enterMiniGame3 = 0;
        }
    }

    /**
     * Rysuje gracza na ekranie
     *
     * @param g2 obiekt Graphics2D do rysowania
     */
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch(direction){

            case "left":
                image = left;
                break;
            case "right":
                image = right;
                break;
        }

        g2.drawImage(image, x , y , gamePanel.tileSize, gamePanel.tileSize,null);
    }

    /**
     * @return 1 jeśli gracz jest przy wejściu do gry 1, 0 jeśli go nie ma
     */
    public int getEnterMiniGame1() {
        return enterMiniGame1;
    }

    /**
     * @return 1 jeśli gracz jest przy wejściu do gry 2, 0 jeśli go nie ma
     */
    public int getEnterMiniGame2() {
        return enterMiniGame2;
    }

    /**
     * @return 1 jeśli gracz jest przy wejściu do gry 3, 0 jeśli go nie ma
     */
    public int getEnterMiniGame3() {
        return enterMiniGame3;
    }
}