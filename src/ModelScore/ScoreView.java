package ModelScore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


/**
 * Okno dialogowe wyświetlające tabelę wyników graczy
 * Pokazuje imię, wynik i poziom trudności w formie tabeli
 *
 */
public class ScoreView extends JDialog {

    /** Tabela JTable wyświetlająca wyniki*/
    private JTable scoreTable;

    /**Model danych dla tabeli*/
    private DefaultTableModel tableModel;

    /**
     * Konstruktor tworzy okno z tabelą wyników
     *
     * @param parent okno nadrzędne
     * @param scores lista wyników do wyświetlenia
     */
    public ScoreView(JFrame parent, List<EntryScore> scores) {
        super(parent, "Ranking wyników", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        initTable(scores);
        initBottomPanel();

        setVisible(true);
    }

    /**
     * Inicjalizuje tabelę wyników i dodaje dane
     * Kolumny: Gracz, Wynik, Poziom
     * Tabela jest nieedytowalna
     * @param scores lista wyników do wyświetlenia
     */
    private void initTable(List<EntryScore> scores) {
        String[] columns = {"Gracz", "Wynik", "Poziom"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // brak edycji
            }
        };

        //Wypełnianie tabeli
        for (EntryScore entry : scores) {
            tableModel.addRow(new Object[]{
                    entry.getPlayerName(),
                    entry.getScore(),
                    entry.getDifficulty()

            });
        }

        scoreTable = new JTable(tableModel);
        scoreTable.setRowHeight(24);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scoreTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Inicjalizuje dolny panel z przyciskiem "Zamknij".
     */
    private void initBottomPanel() {
        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(e -> dispose());

        JPanel panel = new JPanel();
        panel.add(closeButton);

        add(panel, BorderLayout.SOUTH);
    }
}
