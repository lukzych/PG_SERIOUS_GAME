package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Obsługuje zdarzenia klawiatury
 * Śledzi stan klawiszy WASD (ruch) i E (interakcja)
 */
public class KeyHandler implements KeyListener {

    /** Czy wciśnięty klawisz W (góra) */
    public boolean upPressed;

    /** Czy wciśnięty klawisz S (Dół) */
    public boolean downPressed;

    /** Czy wciśnięty klawisz A (lewo) */
    public boolean leftPressed;

    /** Czy wciśnięty klawisz D (prawo) */
    public boolean rightPressed;

    /** Czy wciśnięty klawisz E (interakcja) */
    public boolean ePressed;

    /**
     * Wywoływane gdy klawisz jest wpisywany
     * Nieużywane
     * @param e zdarzenie klawiatury
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Wywoływane gdy klawisz jest wciśnięty
     * Ustawia odpowiednią zmienną na true
     * @param e zdarzenie klawiatury
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch(code) {
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_E:
                ePressed = true;
                break;
        }
    }

    /**
     * Wywoływane gdy klawisz jest zwolniony
     * Ustawia odpowiednią zmienną na false
     * @param e zdarzenie klawiatury
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch(code) {
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_E:
                ePressed = false;
                break;
        }
    }
}