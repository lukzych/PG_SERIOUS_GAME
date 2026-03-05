package MiniPatternGame;

import java.util.Random;


/**
 * Klasa reprezentująca pojedyncze pytanie w grze Wzorce.
 * Zawiera sekwencję liczb z ukrytym elementem oraz opcje odpowiedzi.
 */

public class Pattern {

    Random random = new Random();

    /** Pełna sekwencja liczb (wraz z ukrytym elementem) */
    private int[] fullSequence;

    /** Indeks ukrytego elementu w sekwencji */
    private int hiddenIndex;

    /** Poprawna odpowiedź (wartość ukrytego elementu) */
    private int correctAnswer;

    /** Tablica z 4 opcjami odpowiedzi */
    private int[] answerOptions;

    /** Indeks poprawnej odpowiedzi w tablicy answerOptions */
    private int correctAnswerIndex;

    /** Poziom trudności pytania (1-łatwy, 2-średni, 3-trudny) */
    private int difficulty;

    /**
     * Konstruktor tworzy nowe pytanie z daną sekwencją.
     *
     * @param fullSequence pełna sekwencja liczb
     * @param hiddenIndex indeks elementu do ukrycia
     * @param difficulty poziom trudności (1-3)
     */
    public Pattern(int[] fullSequence, int hiddenIndex, int difficulty) {
        this.fullSequence = fullSequence;
        this.hiddenIndex = hiddenIndex;
        this.correctAnswer = fullSequence[hiddenIndex];
        this.difficulty = difficulty;

        generateAnswerOptions();
    }


    /**
     * Generuje 4 opcje odpowiedzi, w tym jedną poprawną.
     * Niepoprawne odpowiedzi są bliskie wartości poprawnej odpowiedzi.
     * Opcje są następnie losowo mieszane (shuffle).
     */
    private void generateAnswerOptions() {
        answerOptions = new int[4];

        answerOptions[0] = correctAnswer;
        answerOptions[1] = correctAnswer + random.nextInt(3) + 1;
        answerOptions[2] = correctAnswer - random.nextInt(3) - 1;
        answerOptions[3] = correctAnswer + random.nextInt(5) + 4;


        // Pomieszaj
        shuffle(answerOptions);

        // Znajdź poprawną
        for (int i = 0; i < 4; i++) {
            if (answerOptions[i] == correctAnswer) {
                correctAnswerIndex = i;
                break;
            }
        }
    }

    /**
     * Miesza elementy tablicy.
     *
     * @param array tablica do pomieszania
     */
    private void shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    /**
     * @return pełna sekwencja liczb
     */
    public int[] getFullSequence() {
        return fullSequence;
    }

    /**
     * @return indeks ukrytego elementu
     */
    public int getHiddenIndex() {
        return hiddenIndex;
    }

    /**
     * @return tablica z 4 opcjami odpowiedzi
     */
    public int[] getAnswerOptions() {
        return answerOptions;
    }

    /**
     * @return indeks poprawnej odpowiedzi w tablicy opcji
     */
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    /**
     * @return poprawna odpowiedź (wartość ukrytego elementu)
     */
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sprawdza czy wybrana odpowiedź jest poprawna.
     *
     * @param selectedIndex indeks wybranej opcji
     * @return true jeśli odpowiedź poprawna, false jeśli nie
     */
    public boolean checkAnswer(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }

    /**
     * @return poziom trudności pytania (1-3)
     */
    public int getDifficulty(){
        return difficulty;
    }
}