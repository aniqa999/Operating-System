import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Page1 extends JFrame {

    public Page1() {
        // Frame settings
        setTitle("OS project");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background Gradient Effect
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

        // Title Label with Transition Effect
        JLabel title = new JLabel("OPERATING SYSTEM PROJECT");
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(Color.ORANGE);
        title.setBounds(80, 30, 650, 50);
        add(title);

        // Subtitle with Shadow Effect and Transition
        JLabel subtitle = new JLabel("WELCOME !!");
        subtitle.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        subtitle.setForeground(Color.WHITE);
        subtitle.setBounds(150, 80, 500, 40);
        subtitle.setOpaque(false);

        // Adding a fade-in transition to the subtitle text
        Timer subtitleTimer = new Timer(150, new ActionListener() {
            private int alpha = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha < 255) {
                    alpha += 5;
                    subtitle.setForeground(new Color(255, 255, 255, alpha));
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        subtitleTimer.start();

        add(subtitle);

        // Image with Border
        ImageIcon originalImage = new ImageIcon("C:\\Users\\aniqa\\IdeaProjects\\final 2\\final\\src\\image.png");
        Image scaledImage = originalImage.getImage().getScaledInstance(500, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedImage = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(resizedImage);
        imageLabel.setBounds(150, 150, 500, 200);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4, true));
        add(imageLabel);

        // Overlay Text with Transparent Background
        JLabel overlayText = new JLabel("Explore Your Potential!");
        overlayText.setFont(new Font("Arial", Font.BOLD, 20));
        overlayText.setForeground(Color.BLACK);  // Set color to black for visibility
        overlayText.setHorizontalAlignment(SwingConstants.CENTER);
        overlayText.setBounds(270, 230, 260, 30);
        overlayText.setOpaque(true);
        overlayText.setBackground(new Color(234, 255, 255, 150)); // White with transparency
        add(overlayText);

        // Text below the image (right side) with smaller font, italic, and Arabic-style font
        JLabel projectInfo = new JLabel("This project is by Rahman & Aniqa");
        projectInfo.setFont(new Font("Serif", Font.ITALIC, 14)); // Smaller, italic font
        projectInfo.setForeground(Color.BLACK);  // Black for contrast
        projectInfo.setBounds(430, 370, 300, 30); // Position text slightly to the left and ensure it's fully visible
        add(projectInfo);

        // Stylish "Next" Button with Hover Transition
        JButton nextButton = new JButton("Next");
        nextButton.setBackground(Color.ORANGE);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setBounds(350, 450, 100, 50);
        nextButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        // Add Hover Effect
        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nextButton.setBackground(new Color(255, 140, 0));
                nextButton.setFont(new Font("Verdana", Font.BOLD, 20));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                nextButton.setBackground(Color.ORANGE);
                nextButton.setFont(new Font("Verdana", Font.BOLD, 18));
            }
        });

        // Button Action
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Page2();
                dispose();
            }
        });
        add(nextButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Page1();
    }
}
