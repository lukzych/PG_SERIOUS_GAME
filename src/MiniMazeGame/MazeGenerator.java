package MiniMazeGame;

import java.util.List;
import java.util.Random;

/**
 * Generator losowych labiryntów.
 * Tworzy planszę labiryntu z wykorzystaniem algorytmu
 * dodawania elementów do ramek (borders).
 */

public class MazeGenerator {

    /** Dwuwymiarowa tablica reprezentująca planszę */
    private AreaType[][] board;

    /** Generator liczb losowych */
    private Random random;

    /** Szerokość labiryntu */
    private int width;

    /** Wysokość labiryntu */
    private int height;

    /**
     * Konstruktor inicjalizuje generator labiryntu.
     *
     * @param width szerokość labiryntu
     * @param height wysokość labiryntu
     */
    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        board = new AreaType[height][width];
        random = new Random();
    }


    /**
     * Generuje labirynt.
     * Tworzy bazowe ramki, dodaje losowe elementy,
     * a następnie umieszcza punkt startowy i metę.
     * @return dwuwymiarowa tablica z wygenerowanym labiryntem
     */
    public AreaType[][] generate() {
        addBaseBorders();

        List<Element> list = Element.generate(board);

        //Iteracje to zabezpieczenie dodawanie tego samego elemetu nieskończona petla
        int iterations = 0;
        while (!list.isEmpty() && iterations < 1000) {
            Element element = list.get(random.nextInt(list.size()));
            if (!element.tryAddElementToBorder()) {
                list.remove(element);
            }
            iterations++;
        }

        addStartAndEnd();

        return board;
    }

    /**
     * Dodaje bazowe ramki (ściany zewnętrzne i wewnętrzne).
     * Zewnętrzne krawędzie to ściany (wall),
     * wewnętrzne ramki to border, reszta to puste pola (empty).
     */
    private void addBaseBorders() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (x == 0 || y == 0 || x == board[y].length - 1 || y == board.length - 1) {
                    board[y][x] = AreaType.wall;
                } else if (x == 1 || y == 1 || x == board[y].length - 2 || y == board.length - 2) {
                    board[y][x] = AreaType.border;
                } else {
                    board[y][x] = AreaType.empty;
                }
            }
        }
    }

    /**
     * Umieszcza punkt startowy (start) i metę (end) na planszy.
     * Start jest z lewej strony, meta z prawej.
     */
    private void addStartAndEnd() {
        //start
        for (int y = 2; y < height - 2; y++) {
            if (board[y][2] == AreaType.border || board[y][2] == AreaType.empty) {
                board[y][2] = AreaType.start;
                break;
            }
        }

        //meta
        for (int y = height - 3; y >= 2; y--) {
            if (board[y][width - 3] == AreaType.border || board[y][width - 3] == AreaType.empty) {
                board[y][width - 3] = AreaType.end;
                break;
            }
        }
    }

    /**
     * @return szerokość labiryntu
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return wysokość labiryntu
     */
    public int getHeight() {
        return height;
    }
}