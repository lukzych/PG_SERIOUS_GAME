package Main;

import javax.swing.*;

/**
 * Klasa główna aplikacji - punkt wejścia programu
 * Tworzy okno gry i uruchamia
 *
 */
public class Main {

    /**
     * Metoda main - rozpoczyna działanie programu.
     *
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setTitle("Serious Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}