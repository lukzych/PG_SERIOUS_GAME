package Main;

import Block.BlockHandler;
import MiniMazeGame.MazeGameHome;
import MiniMemoryGame.MemoryGameHome;
import MiniPatternGame.PatternGameHome;
import PlayerInfo.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Panel główny gry zawierający pętlę gry i renderowanie.
 * Zarządza logiką gry, rysowaniem ekranu oraz uruchamianiem mini-gier.
 * Implementuje Runnable dla wielowątkowości (game loop w osobnym wątku).
 */
public class GamePanel extends JPanel implements Runnable {

    /** Oryginalny rozmiar kafelka w pikselach (przed skalowaniem) */
    final int originalTileSize = 16;

    /** Skala  do powiększenia*/
    final int scale = 3;

    /** Rzeczywisty rozmiar kafelka po skalowaniu (16 * 3 = 48px) */
    public int tileSize = originalTileSize * scale;

    /** Maksymalna liczba kolumn na ekranie */
    public final int maxCol = 16;

    /** Maksymalna liczba wierszy na ekranie */
    public final int maxRow = 12;

    /** Szerokość ekranu w pikselach (48 * 16 = 768px) */
    public final int screenWidth = tileSize * maxCol;

    /** Wysokość ekranu w pikselach (48 * 12 = 576px) */
    public final int screenHeight = tileSize * maxRow;

    /** Liczba klatek na sekunde*/
    int FPS = 60;

    /** Handler zarządzający blokami mapy i wejściami do mini-gier */
    BlockHandler blockHandler = new BlockHandler(this);

    /** Handler obsługujący zdarzenia klawiatury */
    KeyHandler keyH = new KeyHandler();

    /** Wątek wykonujący główną pętlę gry */
    Thread gameThread;

    /** Obiekt gracza */
    Player player = new Player(this, keyH);

    /**
     * Konstruktor inicjalizuje panel gry
     * Ustawia rozmiar okna, kolor tła,
     * ustawia focus i dodaje obsługę klawiatury
     * Wczytuje obrazy bloków mapy
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyH);

        blockHandler.blockImage();
    }

    /**
     * Uruchamia wątek gry.
     * Tworzy nowy wątek i rozpoczyna wykonywanie metody run()
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Główna pętla gry
     * Wykonuje się w osobnym wątku i zapewnia 60 FPS.
     * W każdej iteracji aktualizuje stan gry i odświeża ekran
     * Używa algorytmu delta time dla płynności animacji
     */
    @Override
    public void run() {

        //obliczanie czasu na jedna klatkę
        double drawInterval = 1000000000 / FPS;

        // mozna zaplanować kiedy powinna pojawić się kolejna klatka
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                //Ile czasu do następnej klatki ?
                double remainingTime = nextDrawTime - System.nanoTime();

                remainingTime = remainingTime / 1000000; //na ms

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                //Usypianie wątku do czasu następnej klatki
                Thread.sleep((long) remainingTime);

                //Kolejna klatka za kolekne drawinterval
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Aktualizuje stan gry.
     * Wywołuje metodę update() gracza, która obsługuje:
     * - ruch na podstawie wciśniętych klawiszy
     * - wejście na granice ekranu
     * - sprawdzanie wejść do mini-gier
     */
    public void update() {
        player.update();
    }

    /**
     * Renderuje wszystkie elementy gry na ekranie,
     * mapa oraz gracza
     * @param g obiekt Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        blockHandler.draw(g2);
        player.draw(g2);

        g2.dispose();
    }

    /**
            * Uruchamia mini-grę Labirynt
            * Znajduje okno nadrzędne (JFrame) i otwiera dialog z grą
            * Gra jest modalna - blokuje główne okno do zamknięcia
     */
    public void startMiniGame1() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new MazeGameHome(parentFrame);
    }

    /**
     * Uruchamia mini-grę Wzorce.
     * Znajduje okno nadrzędne (JFrame) i otwiera dialog z grą.
     * Gra jest modalna - blokuje główne okno do zamknięcia.
     */
    public void startMiniGame2() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new PatternGameHome(parentFrame);
    }

    /**
            * Uruchamia mini-grę Memory (Kolory).
            * Znajduje okno nadrzędne (JFrame) i otwiera dialog z grą.
            * Gra jest modalna - blokuje główne okno do zamknięcia.
     */
    public void startMiniGame3() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new MemoryGameHome(parentFrame);
    }

    /**
     * Sprawdza czy gracz znajduje się przy wejściu do mini-gry 1
     * @return 1 jeśli gracz jest przy wejściu (czerwona ramka), 0 w przeciwnym razie
     */
    public int getEnterMiniGame1() {
        return player.getEnterMiniGame1();
    }

    /**
     * Sprawdza czy gracz znajduje się przy wejściu do mini-gry 2
     *
     * @return 1 jeśli gracz jest przy wejściu (czerwona ramka), 0 w przeciwnym razie
     */
    public int getEnterMiniGame2() {
        return player.getEnterMiniGame2();
    }

    /**
     * Sprawdza czy gracz znajduje się przy wejściu do mini-gry 3
     *
     * @return 1 jeśli gracz jest przy wejściu (czerwona ramka), 0 w przeciwnym razie
     */
    public int getEnterMiniGame3() {
        return player.getEnterMiniGame3();
    }
}