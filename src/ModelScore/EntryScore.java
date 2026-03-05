package ModelScore;


/**
 * Klasa reprezentująca pojedynczny wpis w tabeli wyników
 * Przechowuje imię gracza, wynik oraz poziom trudności
 */
public class EntryScore {


    /** Imię gracza*/
    private String playerName;

    /** Wynik gracza*/
    private int score;

    /** Poziom trudności 1-łatwy, 2-średni, 3-trudny*/
    private int difficulty;


    /**
     * Konstruktor tworzy nowy wpis wyniku.
     *
     * @param playerName imię gracza
     * @param score wynik (punkty lub czas)
     * @param difficulty poziom trudności (1-3)
     */
    public EntryScore(String playerName, int score, int difficulty){
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
    }

    /**
     * @return imię gracza
     */
    public String getPlayerName(){
        return playerName;
    }

    /**
     * @return wynik gracza
     */
    public int getScore() {
        return score;
    }

    /**
     * @return poziom trudności (1-3)
     */
    public int getDifficulty(){
        return difficulty;
    }
}
