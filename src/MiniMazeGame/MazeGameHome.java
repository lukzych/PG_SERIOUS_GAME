package MiniMazeGame;

import MiniPatternGame.PatternGame;
import ModelScore.EntryScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Okno menu głównego gry Labirynt.
 * Pozwala wybrać poziom trudności,
 * uruchomić grę oraz wyświetlić listę wyników.
 *
 */
public class MazeGameHome extends JDialog {

    /** Panel główny zawierający wszystkie elementy interfejsu */
    JPanel gamePanel;

    /** Panel górny z przyciskami nawigacji i tytułem */
    JPanel topPanel;

    /** Panel centralny z opcjami gry */
    JPanel centerPanel;

    /** Label wyświetlający tytuł gry "LABIRYNT" */
    JLabel titleLabel;

    /** Label z instrukcjami dla gracza */
    JLabel instructionLabel;

    /** Przycisk powrotu do menu głównego */
    JButton back;

    /** Przycisk uruchamiający grę */
    JButton start;

    /** Przycisk wyświetlający listę wyników */
    JButton score;

    /** Radio button dla poziomu łatwego (10x10) */
    JRadioButton easyRadio;

    /** Radio button dla poziomu średniego (15x15) */
    JRadioButton mediumRadio;

    /** Radio button dla poziomu trudnego (20x20) */
    JRadioButton hardRadio;

    /** Grupa radio buttonów zapewniająca wybór tylko jednej opcji */
    ButtonGroup difficultyGroup;

    /** Statyczna lista przechowująca wszystkie wyniki graczy */
    public static List<EntryScore> scores = new ArrayList<>();

    /**
     * Konstruktor tworzy okno menu gry Labirynt.
     * Inicjalizuje interfejs użytkownika z trzema poziomami trudności,
     * przyciskiem START, przyciskiem wyników oraz przyciskiem powrotu.
     * Domyślnie wybrany jest poziom średni (15x15).
     *
     * @param parent okno nadrzędne (JFrame)
     */
    public MazeGameHome(JFrame parent){
        super(parent, "Labirynt", true);


        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(230, 230, 230));


        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(230, 230, 230));
        topPanel.setPreferredSize(new Dimension(800, 100));


        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        leftPanel.setOpaque(false);

        back = new JButton("Powrót");
        back.setOpaque(true);
        back.setBorderPainted(false);
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setBackground(new Color(200,200,200));
        back.setForeground(Color.BLACK);
        back.setPreferredSize(new Dimension(140, 55));
        back.setFocusable(false);
        back.addActionListener(e -> dispose());

        leftPanel.add(back);


        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 25));

        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(180, 100));

        score = new JButton("Scores");
        score.setOpaque(true);
        score.setBorderPainted(false);
        score.setFont(new Font("Segoe UI", Font.BOLD, 16));
        score.setBackground(new Color(200,200,200));
        score.setForeground(Color.BLACK);
        score.setPreferredSize(new Dimension(140, 55));
        score.setFocusable(false);
        score.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ModelScore.ScoreView(parent, MazeGameHome.scores);
            }
        });

        rightPanel.add(score);

        titleLabel = new JLabel("LABIRYNT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Color.BLACK);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);


        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(230, 230, 230));


        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Instrukcja
        instructionLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "Wybierz poziom trudności<br>" +
                        "Przeprowadź myszkę od zielonego do czerwonego" +
                        "</div></html>"
        );
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.setOpaque(false);
        radioPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));


        easyRadio = new JRadioButton("Łatwy (10x10)");
        easyRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        easyRadio.setOpaque(false);
        easyRadio.setFocusable(false);
        easyRadio.setAlignmentX(Component.CENTER_ALIGNMENT);

        mediumRadio = new JRadioButton("Średni (15x15)");
        mediumRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mediumRadio.setOpaque(false);
        mediumRadio.setFocusable(false);
        mediumRadio.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumRadio.setSelected(true);

        hardRadio = new JRadioButton("Trudny (20x20)");
        hardRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        hardRadio.setOpaque(false);
        hardRadio.setFocusable(false);
        hardRadio.setAlignmentX(Component.CENTER_ALIGNMENT);


        difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyRadio);
        difficultyGroup.add(mediumRadio);
        difficultyGroup.add(hardRadio);


        radioPanel.add(easyRadio);
        radioPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        radioPanel.add(mediumRadio);
        radioPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        radioPanel.add(hardRadio);


        start = new JButton("START");
        start.setFont(new Font("Segoe UI", Font.BOLD, 18));
        start.setPreferredSize(new Dimension(160, 60));
        start.setMaximumSize(new Dimension(160, 60));

        // macOS ignoruje setBackground() dla przycisków, żeby zachować natywny wygląd.
        //mac ma look and feel a więc chcemy xd
        //Domyślnie (opaque = false), setBorderPainted (true) brzydki windows
        start.setOpaque(true);
        start.setBorderPainted(false);
        start.setBackground(new Color(40, 217, 115));
        start.setForeground(Color.WHITE);
        start.setFocusable(false);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);


        start.addActionListener(e -> {

            int difficulty;
            if (easyRadio.isSelected()) {
                difficulty = 1;
            } else if (mediumRadio.isSelected()) {
                difficulty = 2;
            } else if (hardRadio.isSelected()) {
                difficulty = 3;
            } else {
                difficulty = 2;
            }

            dispose();
            new MazeGame(parent, difficulty); //Przekaż poziom trudności
        });


        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(radioPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(start);

        centerPanel.add(contentPanel);


        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        add(gamePanel);

        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
}