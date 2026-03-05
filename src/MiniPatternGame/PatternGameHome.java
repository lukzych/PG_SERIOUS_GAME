package MiniPatternGame;

import ModelScore.EntryScore;
import ModelScore.ScoreView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * Okno menu głównego gry Wzorce.
 * Pozwala graczowi wybrać poziom trudności (łatwy/średni/trudny),
 * liczbę pytań (5-30) oraz wyświetlić listę wyników.
 *
 */
public class PatternGameHome extends JDialog {

    /** Panel główny zawierający wszystkie elementy */
    JPanel gamePanel;

    /** Panel górny z przyciskami nawigacji i tytułem */
    JPanel topPanel;

    /** Panel centralny z opcjami gry */
    JPanel centerPanel;

    /** Label z tytułem gry */
    JLabel titleLabel;

    /** Label z instrukcją dla gracza */
    JLabel instructionLabel;

    /** Przycisk powrotu do menu głównego */
    JButton back;

    /** Przycisk uruchamiający grę */
    JButton start;

    /** Przycisk wyświetlający listę wyników */
    JButton score;

    /** Radio button dla poziomu łatwego */
    JRadioButton easyRadio;

    /** Radio button dla poziomu średniego */
    JRadioButton mediumRadio;

    /** Radio button dla poziomu trudnego */
    JRadioButton hardRadio;

    /** Grupa radio buttonów zapewniająca wybór tylko jednej opcji */
    ButtonGroup difficultyGroup;

    /** Spinner do wyboru liczby pytań (5-30) */
    JSpinner questionsSpinner;


    /**
     * Konstruktor tworzy menu główne gry.
     * Inicjalizuje wszystkie komponenty interfejsu: przyciski nawigacji,
     * radio buttony wyboru poziomu, spinner liczby pytań i przycisk START.
     *
     * @param parent okno nadrzędne (JFrame)
     * */
    public PatternGameHome(JFrame parent){
        super(parent, "Wzorce", true);


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
                new ModelScore.ScoreView(parent, PatternGame.scores);
            }
        });

        rightPanel.add(score);

        titleLabel = new JLabel("WZORCE", SwingConstants.CENTER);
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


        instructionLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "Wybierz poziom trudności oraz liczbę pytań<br>" +
                        "Odpowiedz na pytania i zdobądź punkty<br>" +
                        "</div></html>"
        );
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel questionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        questionsPanel.setOpaque(false);
        questionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel questionsLabel = new JLabel("Liczba pytań:");
        questionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));


        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(5, 5, 30, 1);
        questionsSpinner = new JSpinner(spinnerModel);
        questionsSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 16));


        JComponent editor = questionsSpinner.getEditor();
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
        spinnerEditor.getTextField().setEditable(false);
        spinnerEditor.getTextField().setColumns(4);


        questionsPanel.add(questionsLabel);
        questionsPanel.add(questionsSpinner);


        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.setOpaque(false);
        radioPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));


        easyRadio = new JRadioButton("Łatwy");
        easyRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        easyRadio.setOpaque(false);
        easyRadio.setFocusable(false);
        easyRadio.setAlignmentX(Component.CENTER_ALIGNMENT);
        easyRadio.setSelected(true);

        mediumRadio = new JRadioButton("Średni");
        mediumRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mediumRadio.setOpaque(false);
        mediumRadio.setFocusable(false);
        mediumRadio.setAlignmentX(Component.CENTER_ALIGNMENT);

        hardRadio = new JRadioButton("Trudny");
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

        start.setOpaque(true);
        start.setBorderPainted(false);
        start.setBackground(new Color(40, 217, 115));
        start.setForeground(Color.WHITE);
        start.setFocusable(false);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);


        start.addActionListener(e -> {
            int difficulty;
            int questions = (int) questionsSpinner.getValue();

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
            new PatternGame(parent, difficulty, questions);
        });


        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(questionsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
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