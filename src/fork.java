import javax.swing.*;
import java.awt.*;

public class fork extends JFrame {
    private final JTextArea logArea;
    private final JButton startButton, clearButton, backbutton ;
    private final JProgressBar progressBar;
    private final JPanel treePanel;
    private int numForks;
    private int totalProcesses;

    public fork() {
        setTitle("Fork Simulation - Interactive");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 30));

        // Title Label
        JLabel titleLabel = new JLabel("Fork Simulation ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        titleLabel.setForeground(Color.ORANGE);
        add(titleLabel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        controlPanel.setBackground(new Color(30, 30, 30));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(30, 30, 30));

        JLabel inputLabel = new JLabel("Enter Number of Forks:");
        inputLabel.setForeground(Color.WHITE);

        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(150, 30));

        startButton = new JButton("Start Simulation");

        clearButton = new JButton("Clear Log");
        backbutton= new JButton("Go Back");

        backbutton.addActionListener(e -> {
            new Page2(); // Open Page1
            dispose();   // Close the current page (Page2)
        });

        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(startButton);
        inputPanel.add(clearButton);
        inputPanel.add(backbutton);


        // Progress Bar Panel
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(new Color(30, 30, 30));
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.ORANGE);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        controlPanel.add(inputPanel);
        controlPanel.add(progressPanel);

        add(controlPanel, BorderLayout.SOUTH);

        // Log Area with Scroll Pane
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        logArea.setBackground(new Color(40, 40, 40));
        logArea.setForeground(Color.WHITE);

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        logScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        logScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2),
                "Process Log",
                0, 0,
                new Font("Comic Sans MS", Font.BOLD, 18),
                Color.ORANGE
        ));

        // Tree Panel with Scroll Pane
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTree(g);
            }
        };
        treePanel.setBackground(new Color(40, 40, 40));
        treePanel.setPreferredSize(new Dimension(700, 600));
        treePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2),
                "Process Tree",
                0, 0,
                new Font("Arial", Font.BOLD, 16),
                Color.ORANGE
        ));

        JScrollPane treeScrollPane = new JScrollPane(treePanel);
        treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        treeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.add(logScrollPane);
        centerPanel.add(treeScrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // Button Actions
        startButton.addActionListener(e -> {
            try {
                numForks = Integer.parseInt(inputField.getText());
                if (numForks < 0) throw new NumberFormatException();
                startSimulation();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(fork.this, "Please enter a valid non-negative integer!");
            }
        });

        clearButton.addActionListener(e -> {
            logArea.setText("");
            progressBar.setValue(0);
            numForks = 0; // Reset forks count
            treePanel.repaint(); // Clear tree
        });
    }

    private void startSimulation() {
        logArea.setText("");
        progressBar.setValue(0);
        progressBar.setMaximum(numForks);
        treePanel.repaint();

        totalProcesses = (int) Math.pow(2, numForks);
        int totalChildren = totalProcesses - 1;

        logArea.append("Total Forks: " + numForks + "\n");
        logArea.append("Total Processes (Parent + Children): " + totalProcesses + "\n");
        logArea.append("Total Parents: 1\n");
        logArea.append("Total Children: " + totalChildren + "\n");
        logArea.append("Formula Used: Total Processes = 2^n (n = number of forks)\n\n");

        for (int i = 0; i < numForks; i++) {
            logArea.append("Fork " + (i + 1) + " executed, doubling processes.\n");
            progressBar.setValue(i + 1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        logArea.append("\nSimulation Complete!\n");
        treePanel.repaint();
    }

    private void drawTree(Graphics g) {
        if (numForks <= 0) return;

        int width = treePanel.getWidth();
        int height = treePanel.getHeight();
        int centerX = width / 2;
        int startY = 50;
        int levelHeight = 100;

        int processesAtLevel = 1;
        int gap = width / (processesAtLevel + 1);

        int[] prevX = new int[1];
        prevX[0] = centerX;

        for (int level = 0; level < numForks; level++) {
            int[] currX = new int[processesAtLevel * 2];
            gap = width / (currX.length + 1);

            for (int i = 0; i < currX.length; i++) {
                currX[i] = gap * (i + 1);
                g.setColor(Color.ORANGE);
                g.drawLine(prevX[i / 2], startY + (level * levelHeight), currX[i], startY + ((level + 1) * levelHeight));
                g.fillOval(currX[i] - 10, startY + ((level + 1) * levelHeight) - 10, 20, 20);

                g.setColor(Color.WHITE);
                String label = (i == 0 && level == 0) ? "Parent" : "Child";
                g.drawString(label + " " + (i + 1), currX[i] - 15, startY + ((level + 1) * levelHeight) - 15);
            }

            prevX = currX;
            processesAtLevel *= 2;
        }

        g.setColor(Color.WHITE);
        g.drawString("Parent", centerX - 20, startY - 10);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            fork frame = new fork();
            frame.setVisible(true);
        });
    }
}
