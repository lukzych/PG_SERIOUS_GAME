package MiniMazeGame;

import java.awt.Point;
import java.util.*;

/**
 * Klasa reprezentująca element (kształt) do wstawiania w labiryncie
 * Losowość labiryntu będzie "udawana" czyli
 * Element składa się z punktów (ścian) i otaczających je punktów brzegowych
 * Używana przez MazeGenerator do budowania struktury labiryntu poprzez
 * doklejanie różnych kształtów (krzyży, prostokątów, linii) do istniejących ramek
 * Algorytm:
 * 1. Generuj różne kształty (krzyże, prostokąty)
 * 2. Znajdź najlepsze miejsce na planszy (najwięcej wspólnych punktów z border)
 * 3. Wstawia kształt na planszę jako ściany (wall)
 */
public class Element {

    /** Zbiór punktów tworzących główny kształt elementu (będą ścianami) */
    private Set<Point> points;

    /** Zbiór punktów otaczających kształt (ramka wokół points) */
    private Set<Point> borderPoints;

    /** Referencja do planszy labiryntu */
    private AreaType[][] border;

    /** Szerokość planszy */
    private int boardWidth;

    /** Wysokość planszy */
    private int boardHeight;


    /**
     * Konstruktor tworzy pusty element.
     * @param border referencja do planszy labiryntu
     */
    public Element(AreaType[][] border) {
        this.border = border;
        points = new HashSet<>();
        borderPoints = new HashSet<>();
        this.boardWidth = border[0].length;
        this.boardHeight = border.length;
    }

    /**
     * Próbuje dodać element do planszy w najlepszym możliwym miejscu.
     * Przeszukuje całą planszę w poszukiwaniu miejsca gdzie kształt się zmieści
     * Dla każdego możliwego miejsca liczy ile punktów borderPoints pokrywa się
     *    z istniejącymi AreaType.border na planszy
     * Wybiera miejsce z największą liczbą wspólnych punktów (najlepsze dopasowanie)
     * Da nam to najlepsze odwzorowanie wąskich ścierzek labiryntu
     * Wstawia kształt: points jako ściany (wall), borderPoints jako ramki (border)
     *
     * @return true jeśli udało się dodać element, false jeśli nie znaleziono miejsca
     */
    public boolean tryAddElementToBorder() {
        int bestX = -1;
        int bestY = -1;
        int bestCount = 0;

        //PRzeszukuje cała plansze
        for (int y = 2; y < boardHeight - 2; y++) {
            for (int x = 2; x < boardWidth - 2; x++) {
                if (checkPosition(x, y)) {
                    int count = calculateCommonPoints(x, y);
                    if (bestCount < count) {
                        bestCount = count;
                        bestX = x; //Znaleziono lepsze miejsce zapisz pozyscje
                        bestY = y;
                    }
                }
            }
        }


        if (bestX == -1 && bestY == -1) {
            return false;
        }

        //nasze besty musza != -1 wiec wstawia sie element w najlepszej pozycji
        for (Point p : points) {
            border[p.y + bestY][p.x + bestX] = AreaType.wall;
        }
        for (Point p : borderPoints) {
            border[p.y + bestY][p.x + bestX] = AreaType.border;
        }

        return true;
    }


    /**
     * Dodaje punkt do zbioru głównych punktów kształtu.
     *
     * @param x współrzędna X punktu
     * @param y współrzędna Y punktu
     */
    private void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    /**
     * Oblicza i dodaje punkty brzegowe (border) wokół głównych punktów kształtu.
     * Dla każdego punktu w points dodaje 8 sąsiadujących punktów do borderPoints
     * (górny, dolny, lewy, prawy oraz 4 narożniki).
     * Punkty brzegowe nie mogą pokrywać się z głównymi punktami.
     */
    private void calculateBorder() {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            addBorderPoint(x - 1, y - 1);
            addBorderPoint(x, y - 1);
            addBorderPoint(x + 1, y - 1);
            addBorderPoint(x - 1, y);
            addBorderPoint(x + 1, y);
            addBorderPoint(x - 1, y + 1);
            addBorderPoint(x, y + 1);
            addBorderPoint(x + 1, y + 1);
        }
    }

    /**
     * Dodaje punkt brzegowy do zbioru borderPoints.
     * Punkt nie zostanie dodany jeśli już istnieje w points lub borderPoints.
     *
     * @param x współrzędna X punktu brzegowego
     * @param y współrzędna Y punktu brzegowego
     */
    private void addBorderPoint(int x, int y) {
        Point border = new Point(x, y);
        if (!points.contains(border) && !borderPoints.contains(border)) {
            borderPoints.add(border);
        }
    }

    /**
     * Sprawdza czy element może zostać umieszczony w danej pozycji.
     * - wszystkie punkty elementu muszą być w granicach planszy (z marginesem 2)
     * - wszystkie punkty muszą być na pustych polach (AreaType.empty)
     *
     * @param x pozycja X do sprawdzenia
     * @param y pozycja Y do sprawdzenia
     * @return true jeśli element się zmieści, false w przeciwnym razie
     */
    public boolean checkPosition(int x, int y) {
        for (Point p : points) {
            int px = p.x + x;
            int py = p.y + y;
            if (px <= 1 || boardWidth - 2 <= px || py <= 1 || boardHeight - 2 <= py) {
                return false;
            }
            if (border[py][px] != AreaType.empty) {
                return false;
            }
        }
        return true;
    }

    /**
     * Oblicza ile punktów brzegowych elementu pokrywa się z istniejącymi
     * punktami AreaType.border na planszy w danej pozycji.
     * Im więcej wspólnych punktów, tym lepsze dopasowanie elementu.
     *
     * @param x pozycja X do sprawdzenia
     * @param y pozycja Y do sprawdzenia
     * @return liczba wspólnych punktów brzegowych
     */
    public int calculateCommonPoints(int x, int y) {
        int count = 0;
        for (Point p : borderPoints) {
            int px = p.x + x;
            int py = p.y + y;
            if (border[py][px] == AreaType.border) {
                count++;
            }
        }
        return count;
    }

    /**
     * Wykonuje przesunięcie wszystkich punktów o wektor [-xmin, -ymin],
     * gdzie xmin i ymin to minimalne współrzędne w zbiorze punktów.
     * Ułatwia nam to sprawdzanie pozycji na planszy, ponieważ
     * niektóre punkty w kształcie mogłyby mieć współrzędne ujemne
     * Punkt o minimalnych współrzędnych staje się punktem (0,0),
     * zachowując kształt i rozmiar figury.
     */
    private void moveFigureToBegin() {
        int ymin = Integer.MAX_VALUE;
        int xmin = Integer.MAX_VALUE;

        for (Point p : points) {
            if (ymin > p.y) ymin = p.y;
            if (xmin > p.x) xmin = p.x;
        }

        Set<Point> newSet = new HashSet<>();
        for (Point p : points) {
            newSet.add(new Point(p.x - xmin, p.y - ymin));
        }
        points = newSet;
    }

    /**
     * Generuje listę wszystkich dostępnych kształtów elementów.
     * Tworzy kształty:
     * - Krzyże pełne i niepełne (różnych rozmiarów)
     * - Prostokąty (różnych wymiarów)
     *
     * @param border referencja do planszy labiryntu
     * @return lista wygenerowanych elementów gotowych do wstawienia
     */
    public static List<Element> generate(AreaType[][] border) {
        List<Element> list = new ArrayList<>();

        // Krzyże różnych rozmiarów
        list.add(generateCross(border, 3, 3, 3, 3));
        generateCrossWithoutOnePart(list, border, 3);
        list.add(generateCross(border, 2, 2, 2, 2));
        generateCrossWithoutOnePart(list, border, 2);

        // Prostokąty
        list.add(generateArea(border, 1, 1));
        list.add(generateArea(border, 2, 1));
        list.add(generateArea(border, 1, 2));
        list.add(generateArea(border, 1, 3));
        list.add(generateArea(border, 3, 1));
        list.add(generateArea(border, 2, 2));
        list.add(generateArea(border, 2, 3));
        list.add(generateArea(border, 3, 2));

        return list;
    }

    /**
     * Generuje 4 krzyże z brakującym jednym ramieniem (kształty T, L).
     * Dodaje do listy krzyże bez: górnego, prawego, dolnego i lewego ramienia.
     *
     * @param list lista do której dodawane są elementy
     * @param border referencja do planszy
     * @param x długość ramienia krzyża
     */
    private static void generateCrossWithoutOnePart(List<Element> list, AreaType[][] border, int x) {
        list.add(generateCross(border, 0, x, x, x));
        list.add(generateCross(border, x, 0, x, x));
        list.add(generateCross(border, x, x, 0, x));
        list.add(generateCross(border, x, x, x, 0));
    }

    /**
     * Generuje element w kształcie krzyża
     * Krzyż składa się z 4 ramion wychodzących ze środka w kierunkach:
     * góra, prawo, dół, lewo
     *
     * @param border referencja do planszy
     * @param up długość ramienia w górę
     * @param right długość ramienia w prawo
     * @param down długość ramienia w dół
     * @param left długość ramienia w lewo
     * @return wygenerowany element krzyża
     */
    private static Element generateCross(AreaType[][] border, int up, int right, int down, int left) {
        Element element = new Element(border);
        generateLine(element, 0, 0, Move.up, up);
        generateLine(element, 0, 0, Move.right, right);
        generateLine(element, 0, 0, Move.down, down);
        generateLine(element, 0, 0, Move.left, left);
        element.moveFigureToBegin();
        element.calculateBorder();
        return element;
    }

    /**
     * Generuje element w kształcie prostokąta (wypełnionego obszaru).
     *
     * @param border referencja do planszy
     * @param width szerokość prostokąta
     * @param height wysokość prostokąta
     * @return wygenerowany element prostokąta
     */
    private static Element generateArea(AreaType[][] border, int width, int height) {
        Element element = new Element(border);

        //Wypełnia prostokąt punktami
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                element.addPoint(x, y);
            }
        }
        //Oblicz punkty brzegowe
        element.calculateBorder();
        return element;
    }

    /**
     * Generuje linię punktów w określonym kierunku.
     * Dodaje punkty do elementu zaczynając od (x,y) i idąc w kierunku move
     * przez len punktów.
     *
     * @param element element do którego dodawane są punkty
     * @param x współrzędna X początkowa
     * @param y współrzędna Y początkowa
     * @param move kierunek linii (up, down, left, right)
     * @param len długość linii (liczba punktów)
     */
    private static void generateLine(Element element, int x, int y, Move move, int len) {
        for (int i = 0; i < len; i++) {
            element.addPoint(x, y);
            switch (move) {
                case up -> y--;
                case down -> y++;
                case left -> x--;
                case right -> x++;
            }
        }
    }

    /**
     * Wyliczenie kierunków ruchu dla generowania linii.
     */
    enum Move {
        up, down, left, right
    }
}