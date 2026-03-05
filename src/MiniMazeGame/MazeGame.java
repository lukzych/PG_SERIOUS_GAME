package MiniMazeGame;

import ModelScore.EntryScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Okno głównej gry Labirynt.
 * Gracz rysuje ścieżkę myszką od zielonego pola startowego do czerwonej mety,
 * unikając kolizji ze ścianami. Gra mierzy czas i zapisuje wyniki.
 * Mechanika:
 * - Kliknij i przytrzymaj mysz na zielonym polu (start)
 * - Przeciągnij mysz do czerwonego pola (meta) nie dotykając ścian
 * - Jeśli dotkniesz ściany, ścieżka resetuje się
 * - Po dotarciu do mety możesz zapisać wynik (imię + czas)
 *
 */

public class MazeGame extends JDialog {

    /** Panel na którym rysowany jest labirynt i ścieżka gracza */
    private JPanel gamePanel;

    /** Dwuwymiarowa tablica reprezentująca strukturę labiryntu */
    private AreaType[][] maze;

    /** Rozmiar pojedynczej komórki labiryntu w pikselach */
    private int cellSize;

    /** Timer odliczający czas gry (aktualizacja co sekundę) */
    private Timer timer;

    /** Liczba sekund od rozpoczęcia gry */
    private int seconds = 0;

    /** Szerokość okna gry w pikselach */
    private final int WINDOW_WIDTH = 800;

    /** Wysokość okna gry w pikselach */
    private final int WINDOW_HEIGHT = 600;

    /** Zbiór komórek przez które przeszła ścieżka gracza */
    private Set<Point> playerPath;

    /** Aktualna komórka na której znajduje się mysz gracza */
    private Point currentCell;

    /** Czy gracz aktualnie rysuje ścieżkę (przytrzymana mysz) */
    private boolean isDrawing;

    /** Pozycja pola startowego (zielone) */
    private Point startCell;

    /** Pozycja pola końcowego - mety (czerwone) */
    private Point endCell;

    /** Czy gra została wygrana (osiągnięto metę) */
    private boolean gameWon;

    /** Offset X do centrowania labiryntu w oknie (margines lewy)
     * Gra o większym poziomie trudności ma labirynt większych rozmiarów
     * */
    private int offsetX;

    /** Offset Y do centrowania labiryntu w oknie (margines górny)
     * Gra o większym poziomie trudności ma labirynt większych rozmiarów
     * */
    private int offsetY;

    /** Poziom trudności do zapisania w wyniku (1-łatwy, 2-średni, 3-trudny) */
    private int difficultyToSave;

    /**
     * Konstruktor inicjalizuje grę Labirynt.
     * Tworzy labirynt o rozmiarze zależnym od poziomu trudności,
     * ustawia obsługę myszy i uruchamia timer.
     *
     * @param parent okno nadrzędne (JFrame)
     * @param difficulty poziom trudności (1-łatwy: 10x10, 2-średni: 15x15, 3-trudny: 20x20)
     */
    public MazeGame(JFrame parent, int difficulty) {
        super(parent, "Labirynt - Gra", true);

        difficultyToSave = difficulty;

        int mazeWidth, mazeHeight;
        switch (difficulty) {
            case 1:
                mazeWidth = 10;
                mazeHeight = 10;
                break;
            case 2:
                mazeWidth = 15;
                mazeHeight = 15;
                break;
            case 3:
                mazeWidth = 20;
                mazeHeight = 20;
                break;
            default:
                mazeWidth = 35;
                mazeHeight = 25;
        }


        MazeGenerator generator = new MazeGenerator(mazeWidth, mazeHeight);
        maze = generator.generate();

        //Rozmiar komórki aby zmieścił się w oknie
        cellSize = calculateCellSize(mazeWidth, mazeHeight);


        playerPath = new HashSet<>();
        isDrawing = false;
        gameWon = false;


        findStartAndEnd();


        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };

        /**
         * Obsługuje wciśnięcie przycisku myszy.
         * Rozpoczyna rysowanie ścieżki jeśli kliknięto na pole startowe.
         */
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point cell = getCellFromPixel(e.getX(), e.getY());
                if (cell != null && cell.equals(startCell)) {
                    isDrawing = true;
                    playerPath.clear();
                    playerPath.add(cell);
                    currentCell = cell;
                    gamePanel.repaint();
                }
            }

            /**
             * Obsługuje zwolnienie przycisku myszy.
             * Kończy rysowanie i resetuje ścieżkę.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawing = false;
                playerPath.clear();
            }

            /**
             * Obsługuje przeciąganie myszy.
             * Rysuje ścieżkę gracza, sprawdza kolizje ze ścianami
             * i wykrywa osiągnięcie mety.
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing && !gameWon) {
                    Point cell = getCellFromPixel(e.getX(), e.getY());

                    if (cell != null && !cell.equals(currentCell)) {

                        if (canMoveTo(cell)) {
                            playerPath.add(cell);
                            currentCell = cell;


                            if (cell.equals(endCell)) {
                                gameWon = true;
                                timer.stop();
                                showVictory();
                            }

                            gamePanel.repaint();
                        } else {

                            isDrawing = false;
                            playerPath.clear();
                            gamePanel.repaint();
                        }
                    }
                }
            }
        };

        gamePanel.addMouseListener(mouseAdapter);
        gamePanel.addMouseMotionListener(mouseAdapter);

        timer = new Timer(1000, e -> {
            seconds++;
            gamePanel.repaint();
        });
        timer.start();

        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        gamePanel.setBackground(Color.WHITE);

        add(gamePanel);
        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }


    /**
     * Znajduje pozycje pola startowego i końcowego w labiryncie.
     * Przeszukuje całą tablicę maze szukając komórek typu start i end.
     * Wyniki zapisywane są w zmiennych startCell i endCell.
     */
    private void findStartAndEnd() {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == AreaType.start) {
                    startCell = new Point(x, y);
                }
                if (maze[y][x] == AreaType.end) {
                    endCell = new Point(x, y);
                }
            }
        }
    }


    /**
     * Konwertuje współrzędne pikseli na współrzędne komórki labiryntu.
     * Uwzględnia offset (centrowanie labiryntu) i rozmiar komórki.
     *
     * @param px współrzędna X w pikselach (pozycja myszy)
     * @param py współrzędna Y w pikselach (pozycja myszy)
     * @return Point reprezentujący komórkę labiryntu, lub null jeśli poza labiryntem
     */
    private Point getCellFromPixel(int px, int py) {
        int mazePixelWidth = maze[0].length * cellSize;
        int mazePixelHeight = maze.length * cellSize;

        offsetX = (WINDOW_WIDTH - mazePixelWidth) / 2;
        offsetY = (WINDOW_HEIGHT - mazePixelHeight) / 2;

        int x = (px - offsetX) / cellSize;
        int y = (py - offsetY) / cellSize;

        if (x >= 0 && x < maze[0].length && y >= 0 && y < maze.length) {
            return new Point(x, y);
        }
        return null;
    }


    /**
     * Sprawdza czy gracz może przejść do danej komórki.
     * Dozwolone typy pól: start, end, empty, border.
     * Niedozwolone: wall (ściany).
     *
     * @param cell komórka do sprawdzenia
     * @return true jeśli można przejść, false jeśli jest ściana lub poza granicami
     */
    private boolean canMoveTo(Point cell) {
        if (cell.x < 0 || cell.x >= maze[0].length ||
                cell.y < 0 || cell.y >= maze.length) {
            return false;
        }

        AreaType type = maze[cell.y][cell.x];


        return type == AreaType.start ||
                type == AreaType.end ||
                type == AreaType.empty ||
                type == AreaType.border;
    }

    /**
     * Oblicza optymalny rozmiar komórki aby cały labirynt zmieścił się w oknie.
     * Uwzględnia marginesy (80px) i wybiera mniejszą wartość z width/height
     * aby zachować kwadratowe komórki.
     *
     * @param mazeWidth szerokość labiryntu w komórkach
     * @param mazeHeight wysokość labiryntu w komórkach
     * @return rozmiar komórki w pikselach
     */
    private int calculateCellSize(int mazeWidth, int mazeHeight) {
        int availableWidth = WINDOW_WIDTH - 80;
        int availableHeight = WINDOW_HEIGHT - 80;

        int cellSizeByWidth = availableWidth / mazeWidth;
        int cellSizeByHeight = availableHeight / mazeHeight;

        return Math.min(cellSizeByWidth, cellSizeByHeight);
    }

    /**
     * Rysuje cały labirynt i ścieżkę gracza.
     *  Komórki labiryntu (ściany, start, meta, puste pola)
     *  Siatka (cienkie linie między komórkami)
     *  Ścieżka gracza (niebieskie wypełnienie)
     *  Timer (czas w prawym górnym rogu)
     *
     * @param g obiekt Graphics do rysowania
     */
    private void drawMaze(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int mazePixelWidth = maze[0].length * cellSize;
        int mazePixelHeight = maze.length * cellSize;

        offsetX = (WINDOW_WIDTH - mazePixelWidth) / 2;
        offsetY = (WINDOW_HEIGHT - mazePixelHeight) / 2;

        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                int px = offsetX + (x * cellSize);
                int py = offsetY + (y * cellSize);

                switch (maze[y][x]) {
                    case wall:
                        g2.setColor(Color.BLACK);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                    case start:
                        g2.setColor(Color.GREEN);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                    case end:
                        g2.setColor(Color.RED);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                    default:
                        g2.setColor(Color.WHITE);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                }


                g2.setColor(new Color(200, 200, 200));
                g2.drawRect(px, py, cellSize, cellSize);
            }
        }


        //ścieżka ka bo niebieska
        g2.setColor(new Color(50, 150, 255, 180));
        for (Point p : playerPath) {
            int px = offsetX + (p.x * cellSize);
            int py = offsetY + (p.y * cellSize);
            g2.fillRect(px, py, cellSize, cellSize);
        }


        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Czas: " + seconds + "s", WINDOW_WIDTH - 120, 30);


        if (gameWon) {
            showVictory();
        }
    }


    /**
     * Wyświetla okno dialogowe po wygraniu gry.
     * Pyta gracza o imię i zapisuje wynik (imię + czas + poziom trudności)
     * do listy wyników w MazeGameHome.scores.
     * punkty czyli czas zostaja zapisane do scores
     * Po zapisie (lub odmowie) zamyka okno gry.
     */
    private void showVictory() {
        if (gameWon) {

            JPanel panel = new JPanel(new BorderLayout(5,5));
            JLabel label = new JLabel("Podaj swoje imię:");
            JTextField nameField = new JTextField(15);
            panel.add(label, BorderLayout.NORTH);
            panel.add(nameField, BorderLayout.CENTER);


            int choice = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Czy chcesz zapisać wynik?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                String playerName = nameField.getText().trim();

                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Imię nie może być puste!",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }


                MazeGameHome.scores.add(new EntryScore(playerName, seconds, difficultyToSave));
                JOptionPane.showMessageDialog(this, "Wynik zapisany!");
            }


            dispose();
        }
    }
}