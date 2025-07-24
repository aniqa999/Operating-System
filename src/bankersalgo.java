import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class bankersalgo extends JFrame {
    private JTextField[][] allocationTable, maxTable, availableTable;
    private JLabel[][] resultTable;
    private int[][] allocation, max, need;
    private int[] available;
    private int processCount, resourceCount;
    private JTextArea logArea;

    public bankersalgo() {
        setTitle(" Banker's Algorithm Simulator");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(250, 250, 250));

        JPanel inputPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Configuration"));
        inputPanel.setForeground(new Color(255, 165, 0)); // Orange color

        JTextField processField = new JTextField();
        JTextField resourceField = new JTextField();
        JButton initializeButton = createStyledButton("Initialize Tables");
        JButton calculateButton = createStyledButton("Calculate Safe Sequence");
        JButton updateAvailableButton = createStyledButton("Update Availability");
        JButton backbutton = createStyledButton("Go Back");

        backbutton.addActionListener(e -> {
            new Page2(); // Open Page1
            dispose();   // Close the current page
        });

        calculateButton.setEnabled(false);
        updateAvailableButton.setEnabled(false);

        inputPanel.add(new JLabel("Number of Processes:"));
        inputPanel.add(processField);
        inputPanel.add(new JLabel("Number of Resources:"));
        inputPanel.add(resourceField);
        inputPanel.add(initializeButton);
        inputPanel.add(calculateButton);
        inputPanel.add(backbutton);

        add(inputPanel, BorderLayout.NORTH);

        // Tables Panel
        JPanel tablesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        tablesPanel.setBackground(new Color(245, 245, 245));
        add(tablesPanel, BorderLayout.CENTER);

        // Log Area
        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        logArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        logArea.setForeground(new Color(255, 102, 0));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Execution Log"));
        add(logScrollPane, BorderLayout.SOUTH);

        // Button Actions
        initializeButton.addActionListener(e -> {
            try {
                processCount = Integer.parseInt(processField.getText().trim());
                resourceCount = Integer.parseInt(resourceField.getText().trim());

                if (processCount <= 0 || resourceCount <= 0) {
                    throw new NumberFormatException();
                }

                initializeTables(tablesPanel);
                calculateButton.setEnabled(true);
                updateAvailableButton.setEnabled(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid positive integers for processes and resources.");
            }
        });

        calculateButton.addActionListener(e -> calculateSafeSequence());
        updateAvailableButton.addActionListener(e -> updateAvailableResources());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 102, 0));
        button.setForeground(Color.white);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void initializeTables(JPanel tablesPanel) {
        tablesPanel.removeAll();

        allocationTable = new JTextField[processCount][resourceCount];
        maxTable = new JTextField[processCount][resourceCount];
        availableTable = new JTextField[1][resourceCount];

        tablesPanel.add(createTablePanel("Allocation Table", allocationTable));
        tablesPanel.add(createTablePanel("Max Table", maxTable));
        tablesPanel.add(createTablePanel("Available Resources", availableTable));

        tablesPanel.revalidate();
        tablesPanel.repaint();
    }

    private JPanel createTablePanel(String title, JTextField[][] table) {
        JPanel panel = new JPanel(new GridLayout(table.length + 1, table[0].length + 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel());
        for (int j = 0; j < table[0].length; j++) {
            panel.add(new JLabel("R" + j, SwingConstants.CENTER));
        }

        for (int i = 0; i < table.length; i++) {
            panel.add(new JLabel("P" + i, SwingConstants.CENTER));
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = new JTextField();
                table[i][j].setHorizontalAlignment(JTextField.CENTER);
                table[i][j].setBackground(new Color(245, 255, 250));
                panel.add(table[i][j]);
            }
        }

        return panel;
    }

    private void calculateSafeSequence() {
        try {
            allocation = parseTable(allocationTable);
            max = parseTable(maxTable);
            available = parseArray(availableTable);

            need = new int[processCount][resourceCount];
            for (int i = 0; i < processCount; i++) {
                for (int j = 0; j < resourceCount; j++) {
                    need[i][j] = max[i][j] - allocation[i][j];
                }
            }

            boolean[] finished = new boolean[processCount];
            int[] safeSequence = new int[processCount];
            int index = 0;

            logArea.setText("Calculating safe sequence...\n\n");

            while (index < processCount) {
                boolean found = false;

                for (int i = 0; i < processCount; i++) {
                    if (!finished[i] && canProceed(i)) {
                        logArea.append("Process P" + i + " can proceed.\n");
                        for (int j = 0; j < resourceCount; j++) {
                            available[j] += allocation[i][j];
                        }
                        safeSequence[index++] = i;
                        finished[i] = true;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    logArea.append("System is in an unsafe state! Deadlock detected.\n");
                    return;
                }
            }

            logArea.append("\nSafe sequence found: " + Arrays.toString(safeSequence) + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please fill all tables with valid integers.");
        }
    }

    private int[][] parseTable(JTextField[][] table) {
        int[][] parsedTable = new int[table.length][ table[0].length];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                parsedTable[i][j] = Integer.parseInt(table[i][j].getText().trim());
            }
        }
        return parsedTable;
    }

    private int[] parseArray(JTextField[][] array) {
        int[] parsedArray = new int[array[0].length];
        for (int j = 0; j < array[0].length; j++) {
            parsedArray[j] = Integer.parseInt(array[0][j].getText().trim());
        }
        return parsedArray;
    }

    private boolean canProceed(int process) {
        for (int j = 0; j < resourceCount; j++) {
            if (need[process][j] > available[j]) {
                return false;
            }
        }
        return true;
    }

    private void updateAvailableResources() {
        try {
            available = parseArray(availableTable);
            logArea.append("\nAvailable resources updated: " + Arrays.toString(available) + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please fill all available resources with valid integers.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            bankersalgo frame = new bankersalgo();
            frame.setVisible(true);
        });
    }
}
