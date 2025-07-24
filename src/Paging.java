import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Properties;
import javax.swing.border.TitledBorder;

public class Paging extends JFrame {

    private JTextField processSizeField;
    private JLabel maxPageTableEntrySizeLabel;
    private JLabel pageTableSizeLabel;
    private JLabel totalFramesLabel;
    private int pageSizeKB = 4; // Default page size in KB

    private static final String CONFIG_FILE = "config.properties";

    public Paging() {
        loadPageSizeFromFile();
        createUI();
    }

    private void createUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255));
       // headerPanel.setBackground(Color.ORANGE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Paging Scheme", JLabel.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createTitledBorder("Input Process Details")));

        JLabel processSizeLabel = new JLabel("Enter the process size (in MB):");
        processSizeLabel.setForeground(new Color(255, 102, 0)); // Set the label color to orange
        inputPanel.add(processSizeLabel);

        processSizeField = new JTextField(10);
        processSizeField.setToolTipText("Enter the process size in MB (e.g., 64)");
        inputPanel.add(processSizeField);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBackground(new Color(255, 140, 0));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.addActionListener(this::calculate);
        inputPanel.add(new JLabel());
        inputPanel.add(calculateButton);

        // Result panel
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(3, 1, 10, 10));

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Results");
        titledBorder.setTitleColor(Color.black); // Set the title color to orange
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                titledBorder));

        maxPageTableEntrySizeLabel = new JLabel("Maximum page table entry size: ");
        maxPageTableEntrySizeLabel.setForeground(new Color(255, 102, 0));

        pageTableSizeLabel = new JLabel("Page table size: ");
        pageTableSizeLabel.setForeground(new Color(255, 102, 0));

        totalFramesLabel = new JLabel("Total number of frames: ");
        totalFramesLabel.setForeground(new Color(255, 102, 0));

        resultPanel.add(totalFramesLabel);
        resultPanel.add(maxPageTableEntrySizeLabel);
        resultPanel.add(pageTableSizeLabel);

        // Menu bar for configuration
        JMenuBar menuBar = new JMenuBar();
        JMenu configMenu = new JMenu("Configuration");
        configMenu.setForeground(new Color(255, 102, 0)); // Set the menu label color to orange

        JMenuItem setPageSizeItem = new JMenuItem("Set Page Size");
        setPageSizeItem.addActionListener(this::openPageSizeConfigDialog);
        configMenu.add(setPageSizeItem);

// Add a "Back" button in the menu bar
        JMenu navigationMenu = new JMenu("Navigation");
        navigationMenu.setForeground(new Color(255, 102, 0)); // Set the menu label color to orange

        JMenuItem backButton = new JMenuItem("Back");
        backButton.addActionListener(e -> {
            // Logic to go back or close the current frame
            this.dispose();
            new MemoryManagement();
        });
        navigationMenu.add(backButton);

        menuBar.add(configMenu);
        menuBar.add(navigationMenu);

        setJMenuBar(menuBar);

// Add components to the main frame
        add(headerPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void calculate(ActionEvent e) {
        try {
            int processSizeMB = Integer.parseInt(processSizeField.getText());

            if (processSizeMB <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive value for process size.");
                return;
            }

            int numberOfPages = (processSizeMB * 1024) / pageSizeKB;
            int maxPageTableEntrySize = calculateMaxPageTableEntrySize(numberOfPages);
            int pageTableSize = numberOfPages * maxPageTableEntrySize;

            maxPageTableEntrySizeLabel.setText("Maximum page table entry size: " + maxPageTableEntrySize + " bytes.");
            pageTableSizeLabel.setText("Page table size: " + pageTableSize + " bytes.");

            int totalFrames = (processSizeMB * 1024) / pageSizeKB;
            totalFramesLabel.setText("Total number of frames: " + totalFrames);

            new MemoryPanelFrame(totalFrames);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for process size.");
        }
    }

    private int calculateMaxPageTableEntrySize(int numberOfPages) {
        return Integer.SIZE / 8; // Assuming 32-bit address space
    }

    private void openPageSizeConfigDialog(ActionEvent e) {
        String newPageSize = JOptionPane.showInputDialog(this, "Enter page size in KB:", pageSizeKB);
        if (newPageSize != null) {
            try {
                pageSizeKB = Integer.parseInt(newPageSize);
                if (pageSizeKB <= 0) {
                    throw new NumberFormatException();
                }
                savePageSizeToFile();
                JOptionPane.showMessageDialog(this, "Page size updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid page size. Please enter a positive integer.");
            }
        }
    }

    private void loadPageSizeFromFile() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            pageSizeKB = Integer.parseInt(properties.getProperty("pageSizeKB", "4"));
        } catch (IOException | NumberFormatException e) {
            // Use default page size if file is missing or invalid
        }
    }

    private void savePageSizeToFile() {
        Properties properties = new Properties();
        properties.setProperty("pageSizeKB", String.valueOf(pageSizeKB));
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Paging Scheme Configuration");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save configuration: " + e.getMessage());
        }
    }

    class MemoryPanelFrame extends JFrame {

        private MemoryPanel memoryPanel;

        public MemoryPanelFrame(int numberOfFrames) {
            memoryPanel = new MemoryPanel();
            memoryPanel.setNumberOfFrames(numberOfFrames);

            JScrollPane scrollPane = new JScrollPane(memoryPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            JButton goBackButton = createGoBackButton();

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(goBackButton);

            add(scrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(500, 400);
            setLocationRelativeTo(null);
            setTitle("Memory Panel");
            setVisible(true);
        }

        private JButton createGoBackButton() {
            JButton goBackButton = new JButton("Go Back");
            goBackButton.setBackground(new Color(255, 69, 0)); // Vibrant orange
            goBackButton.setForeground(Color.WHITE);
            goBackButton.setFont(new Font("Arial", Font.BOLD, 14));
            goBackButton.setFocusPainted(false);
            goBackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            goBackButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    goBackButton.setBackground(new Color(255, 99, 71)); // Lighter orange on hover
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    goBackButton.setBackground(new Color(255, 69, 0)); // Original color
                }
            });

            goBackButton.addActionListener(e -> dispose());
            return goBackButton;
        }

        class MemoryPanel extends JPanel {
            private int numberOfFrames = 0;

            public void setNumberOfFrames(int numberOfFrames) {
                this.numberOfFrames = numberOfFrames;
                int preferredHeight = Math.max(200, 20 * numberOfFrames);
                setPreferredSize(new Dimension(400, preferredHeight));
                revalidate();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (numberOfFrames > 0) {
                    int width = getWidth();
                    int frameHeight = 20;
                    g.setColor(Color.ORANGE);

                    for (int i = 0; i < numberOfFrames; i++) {
                        int y = i * frameHeight;
                        g.drawRect(0, y, width - 1, frameHeight - 1);
                        g.drawString("Frame " + (i + 1) + " - Page Size: " + pageSizeKB + " KB", 5, y + frameHeight / 2 + 5);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Paging();
    }
}

