package MiniMazeGame;

/**
 * Wyliczenie typów pól w labiryncie
 * Określa rodzaj komórki na planszy labiryntu
 *
 */
public enum AreaType {

    /** Ściana*/
    wall,
    /** Krawędź(przy ścianach można przechodzić)*/
    border,
    /** Start*/
    start,
    /** Meta*/
    end,
    /** Puste pole*/
    empty;
}
