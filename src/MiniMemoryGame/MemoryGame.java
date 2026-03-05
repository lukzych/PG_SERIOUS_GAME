package MiniMemoryGame;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Gra Memory - zapamiętywanie kolejności kolorów.
 * Gracz musi odtworzyć sekwencję kolorów w poprawnej kolejności.
 * Gra składa się z 5 rund, trudność (liczba kolorów) zależy od wybranego poziomu.
 *
 */
public class MemoryGame extends JDialog {

    /** Czy gracz może klikać przyciski (true po pokazaniu sekwencji) */
    boolean canPlay = false;

    /** Postęp gracza w bieżącej rundzie (ile kolorów już kliknął poprawnie) */
    int playerProgress = 0;

    /** Numer bieżącej rundy */
    int currentRound = 1;

    /** Całkowita liczba rund w grze */
    int totalRounds = 5;

    /** Liczba poprawnie ukończonych rund */
    int score = 0;

    /** Timer do animacji pokazywania sekwencji kolorów */
    Timer timer;

    /** Panel główny zawierający wszystkie elementy interfejsu */
    JPanel gamePanel;

    /** Panel górny z licznikami i przyciskiem powrotu */
    JPanel topPanel;

    /** Panel centralny z głównym przyciskiem pokazującym kolory */
    JPanel centerPanel;

    /** Panel dolny z przyciskami odpowiedzi */
    JPanel bottomPanel;

    /** Label wyświetlający aktualny wynik (np. "Wynik: 3/5") */
    JLabel scoreLabel;

    /** Label wyświetlający numer rundy (np. "Runda: 2/5") */
    JLabel roundLabel;

    /** Label z instrukcją dla gracza */
    JLabel instructionLabel;

    /** Przycisk powrotu do menu */
    JButton back;

    /** Główny przycisk w centrum pokazujący sekwencję kolorów */
    JButton mainColorButton;

    /** Tablica 6 przycisków odpowiedzi (widocznych tyle ile sequenceLength) */
    JButton[] answerButtons = new JButton[6];

    /** Długość sekwencji w grze (4-łatwy, 5-średni, 6-trudny) - stała przez całą grę */
    int sequenceLength;

    /** Poziom trudności (1-łatwy, 2-średni, 3-trudny) */
    int difficulty;

    /** Sekwencja kolorów do zapamiętania w bieżącej rundzie */
    List<Color> colorList;

    /**
     * Konstruktor inicjalizuje grę Memory
     * Ustawia długość sekwencji w zależności od trudności (stała przez wszystkie rundy)
     * Tworzy interfejs i rozpoczyna pierwszą rundę
     *
     * @param parent okno nadrzędne (JFrame)
     * @param difficulty poziom trudności
     */
    public MemoryGame(JFrame parent, int difficulty) {
        super(parent, "Memory - Gra", true);

        this.difficulty = difficulty;


        if (difficulty == 1) {
            sequenceLength = 4;
        } else if (difficulty == 2) {
            sequenceLength = 5;
        } else {
            sequenceLength = 6;
        }

        initializeUI();
        startNewRound();

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Rozpoczyna nową rundę gry
     * Generuje nową sekwencję kolorów, resetuje progress i uruchamia animację
     */
    private void startNewRound() {
        playerProgress = 0;
        canPlay = false;

        colorList = generateSequence(sequenceLength);


        updateLabels();

        getColorButtons();

        resetButtonBorders();

        start();
    }

    /**
     * Aktualizuje labele z numerem rundy i wynikiem
     */
    private void updateLabels() {
        roundLabel.setText("Runda: " + currentRound + "/" + totalRounds);
        scoreLabel.setText("Wynik: " + score + "/" + totalRounds);
    }

    /**
     * Resetuje ramki wszystkich przycisków do stanu początkowego (szare)
     * Odblokowuje wszystkie przyciski
     */
    private void resetButtonBorders() {
        for (int i = 0; i < sequenceLength; i++) {
            answerButtons[i].setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
            answerButtons[i].setEnabled(true);
        }
    }

    /**
     * Generuje losową sekwencję kolorów o podanej długości
     * Wybiera kolory z 8 dostępnych kolorów i miesza je losowo
     *
     * @param sequenceLength liczba kolorów w sekwencji
     * @return lista kolorów do zapamiętania
     */
    public List<Color> generateSequence(int sequenceLength) {
        List<Color> list = new ArrayList<>();
        list.add(Color.BLUE);
        list.add(Color.BLACK);
        list.add(Color.GREEN);
        list.add(Color.GRAY);
        list.add(Color.ORANGE);
        list.add(Color.RED);
        list.add(Color.PINK);
        list.add(Color.YELLOW);

        // losowe pomieszanie
        Collections.shuffle(list);

        // Zostawia tylko potrzebna liczbe kolorow
        if (sequenceLength < list.size()) {
            list.subList(sequenceLength, list.size()).clear();
        }

        return list;
    }

    /**
     * Uruchamia animację pokazującą sekwencję kolorów
     * Kolory pojawiają się na głównym przycisku co 1 sekundę
     * Po zakończeniu animacji odblokowuje przyciski odpowiedzi dla gracza
     */
    public void start() {
        canPlay = false;

        // Blokuje przyciski w trakcje animacji
        for (int i = 0; i < sequenceLength; i++) {
            answerButtons[i].setEnabled(false);
        }

        int delay = 1000;  // 1 sekunda
        int[] index = {0};

        timer = new Timer(delay, e -> {
            if (index[0] < colorList.size()) {
                // Pokaż kolejny kolor
                mainColorButton.setBackground(colorList.get(index[0]));
                index[0]++;
            } else {
                // Koniec sekwencji
                ((Timer) e.getSource()).stop();
                mainColorButton.setBackground(Color.WHITE);

                canPlay = true;  //mozna zgadywac sekwencje

                for (int i = 0; i < sequenceLength; i++) {
                    answerButtons[i].setEnabled(true);
                }
            }
        });

        timer.start();
    }

    /**
     * Sprawdza poprawność klikniętego koloru
     * Jeśli kolor jest poprawny (kolejny w sekwencji), zwiększa progress i zaznacza przycisk na zielono
     * Jeśli sekwencja ukończona poprawnie, po 0.8s przechodzi do kolejnej rundy i dodaje punkt
     * Jeśli kolor niepoprawny, zaznacza na czerwono i po 1s przechodzi do kolejnej rundy bez punktu
     *
     * @param buttonIndex indeks klikniętego przycisku (0-5)
     */
    public void checkAnswer(int buttonIndex) {
        if (!canPlay) {
            return;  // Zignorowanie kliknięcia podczas animacji
        }

        Color pressedButton = answerButtons[buttonIndex].getBackground();
        Color rightColor = colorList.get(playerProgress);

        if (pressedButton.equals(rightColor)) {

            answerButtons[buttonIndex].setEnabled(false);
            answerButtons[buttonIndex].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));

            playerProgress++;


            scoreLabel.setText("Progress: " + playerProgress + "/" + sequenceLength);


            if (playerProgress == colorList.size()) {
                score++;
                canPlay = false;


                Timer successTimer = new Timer(800, evt -> {
                    nextRound();
                });
                successTimer.setRepeats(false);
                successTimer.start();
            }
        } else {
            // ZŁY KOLOR
            canPlay = false;
            answerButtons[buttonIndex].setBorder(BorderFactory.createLineBorder(Color.RED, 3));

            // Po 1s przejdź do kolejnej rundy (bez punktu)
            Timer errorTimer = new Timer(1000, evt -> {
                nextRound();
            });
            errorTimer.setRepeats(false);
            errorTimer.start();
        }
    }

    /**
     * Przechodzi do kolejnej rundy lub kończy grę
     * Jeśli to była ostatnia (5.) runda, wyświetla końcowy wynik
     * W przeciwnym razie rozpoczyna nową rundę
     */
    private void nextRound() {
        currentRound++;

        if (currentRound <= totalRounds) {

            startNewRound();
        } else {

            showFinalScore();
        }
    }

    /**
     * Wyświetla końcowy wynik gry
     * Pokazuje liczbę poprawnie ukończonych rund oraz procent sukcesu
     * Tytuł okna zależy od osiągniętego wyniku
     */
    private void showFinalScore() {
        double percentage = (score * 100.0) / totalRounds;

        String message = String.format(
                "Koniec gry!\n\nUkończone rundy: %d/%d\nProcent: %.1f%%",
                score, totalRounds, percentage
        );

        String title;
        if (percentage > 0) {
            title = "Koniec! Brawo";

        } else {
            title = "Spróbuj ponownie! ";
        }

        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }

    /**
     * Inicjalizuje wszystkie komponenty interfejsu użytkownika
     * Tworzy panele: górny (liczniki i przycisk powrotu), centralny (główny przycisk), dolny (przyciski odpowiedzi)
     * Ustawia fonty, kolory i layout wszystkich elementów
     */
    public void initializeUI() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(230, 230, 230));


        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(230, 230, 230));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));


        JPanel labelsPanel = new JPanel(new BorderLayout());
        labelsPanel.setBackground(new Color(230, 230, 230));

        roundLabel = new JLabel("Runda: 1/" + totalRounds);
        roundLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        scoreLabel = new JLabel("Wynik: 0/" + totalRounds);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        labelsPanel.add(roundLabel, BorderLayout.WEST);
        labelsPanel.add(scoreLabel, BorderLayout.EAST);


        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        leftPanel.setOpaque(false);

        back = new JButton("← Powrót");
        back.setOpaque(true);
        back.setBorderPainted(false);
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setBackground(new Color(200, 200, 200));
        back.setForeground(Color.BLACK);
        back.setPreferredSize(new Dimension(140, 50));
        back.setFocusable(false);
        back.addActionListener(e -> dispose());

        leftPanel.add(back);

        topPanel.add(labelsPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(leftPanel);


        mainColorButton = new JButton("");
        mainColorButton.setFont(new Font("Arial", Font.BOLD, 32));
        mainColorButton.setFocusable(false);
        mainColorButton.setPreferredSize(new Dimension(200, 200));
        mainColorButton.setMaximumSize(new Dimension(200, 200));
        mainColorButton.setOpaque(true);
        mainColorButton.setBorderPainted(false);
        mainColorButton.setBackground(Color.WHITE);
        mainColorButton.setForeground(Color.BLACK);
        mainColorButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        mainColorButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(230, 230, 230));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        instructionLabel = new JLabel("Zapamiętaj kolejność kolorów");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(mainColorButton);
        centerPanel.add(Box.createVerticalGlue());


        bottomPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        bottomPanel.setBackground(new Color(230, 230, 230));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 40, 100));


        for (int i = 0; i < 6; i++) {
            answerButtons[i] = new JButton("");
            answerButtons[i].setFont(new Font("Arial", Font.BOLD, 32));
            answerButtons[i].setFocusable(false);
            answerButtons[i].setPreferredSize(new Dimension(120, 80));

            answerButtons[i].setOpaque(true);
            answerButtons[i].setBorderPainted(false);
            answerButtons[i].setBackground(Color.WHITE);
            answerButtons[i].setForeground(Color.BLACK);
            answerButtons[i].setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

            final int buttonIndex = i;
            answerButtons[i].addActionListener(e -> checkAnswer(buttonIndex));

            bottomPanel.add(answerButtons[i]);
        }

        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);

        add(gamePanel);
    }

    /**
     * Ustawia kolory na przyciskach odpowiedzi
     * Kolory są w losowej kolejności (innej niż sekwencja do zapamiętania)
     * Pokazuje tylko tyle przycisków ile wynosi sequenceLength, resztę ukrywa
     */
    public void getColorButtons() {

        List<Color> shuffleList = new ArrayList<>(colorList);
        Collections.shuffle(shuffleList);


        for (int i = 0; i < sequenceLength; i++) {
            answerButtons[i].setBackground(shuffleList.get(i));
            answerButtons[i].setVisible(true);
        }


        for (int i = sequenceLength; i < 6; i++) {
            answerButtons[i].setVisible(false);
        }
    }
}