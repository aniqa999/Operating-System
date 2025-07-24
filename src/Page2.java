import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

class Page2 extends JFrame {
    private JLabel title;
    private Timer titleTimer;
    private int titleX = -200; // Title's initial position (off-screen)

    public Page2() {
        // Frame settings
        setTitle("Choose an Option");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Gradient Background (Dark Gray to Light Gray)
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.DARK_GRAY, 0, getHeight(), Color.LIGHT_GRAY);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setBounds(0, 0, 800, 600);
        setContentPane(gradientPanel);
        setLayout(null);

        // Title Label with transition
        title = new JLabel("Choose Your Action");
        title.setFont(new Font("Serif", Font.ITALIC, 30));
        title.setForeground(Color.ORANGE);
        title.setBounds(titleX, 30, 400, 50);
        add(title);

        // Timer for title animation
        titleTimer = new Timer(10, e -> {
            if (titleX < 270) { // Move title to the target position
                titleX += 5;
                title.setBounds(titleX, 30, 400, 50);
            } else {
                titleTimer.stop();
            }
        });
        titleTimer.start();

        // Buttons for Options
        String[] buttonNames = {"Process Management",  "Memory Management", "Fork()", "Bnakers ALgorithm"};
        for (int i = 0; i < buttonNames.length; i++) {
            JButton button = new JButton(buttonNames[i]);
            button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.WHITE);
            button.setBounds(300, 120 + i * 80, 200, 50);
            button.setFocusPainted(false);

            // Add hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(255, 140, 0));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(Color.ORANGE);
                }
            });

            // Action listener for "Process Management" button
            if (button.getText().equals("Process Management")) {
                button.addActionListener(e -> openProcessManagementPage());

//            else if (button.getText().equals("Process Scheduling")) {
//                button.addActionListener(e -> openPagingPage());
            } else if (button.getText().equals("Memory Management")) {
                button.addActionListener(e -> openMemoryManagementPage());
            } else if (button.getText().equals("Fork()")) {
                button.addActionListener(e -> openForkPage());
            } else if (button.getText().equals("Bnakers ALgorithm")) {
                button.addActionListener(e -> openBankersAlgoPage());
            }

            add(button);
        }

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        backButton.setBackground(Color.GRAY);
        backButton.setForeground(Color.ORANGE);
        backButton.setBounds(50, 500, 100, 40);
        backButton.setFocusPainted(false);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.LIGHT_GRAY);
                backButton.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.GRAY);
                backButton.setForeground(Color.ORANGE);

            }
        });

        // Navigate back to Page1 on button click
        backButton.addActionListener(e -> {
            new Page1(); // Open Page1
            dispose();   // Close the current page (Page2)
        });

        add(backButton);

        setVisible(true);
    }

    // Method to open the Process Management Page
    private void openProcessManagementPage() {
        new ProcessCreationPage(); // Open ProcessManagementPage
        dispose(); // Close current page (Page2)
    }

    // Method to open Memory Management Page
    private void openMemoryManagementPage() {
        new MemoryManagement();
        dispose();
    }

    // Method to open Fork Page
    private void openForkPage() {
      new fork().setVisible(true);
      dispose();
    }

    // Method to open Bankers Algorithm Page
    private void openBankersAlgoPage() {
         new bankersalgo().setVisible(true);
         dispose();
    }

    public static void main(String[] args) {
        new Page2(); // Launch the second page
    }
}