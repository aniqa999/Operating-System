import javax.swing.*;
import java.awt.*;

public class WelcomeScreen {

    public static void showWelcomeScreen() {
        // Create a frame
        JFrame welcome = new JFrame();
        welcome.setUndecorated(true); // Remove the default window decorations
        welcome.setSize(800, 600);
        welcome.setLocationRelativeTo(null);

        // Main container panel with an orange border
        JPanel container = new JPanel();
        container.setBackground(Color.ORANGE); // Border color
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createLineBorder(Color.BLUE, 6)); // Blue border with 6px thickness

        // Inner panel for the main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE); // White background for content
        container.add(contentPanel, BorderLayout.CENTER);

        // Left Panel with emoji and text
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.lightGray);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Emoji label
        JLabel emojiLabel = new JLabel("\uD83D\uDE0A", JLabel.CENTER);
        emojiLabel.setFont(new Font("Serif", Font.PLAIN, 80));
        emojiLabel.setForeground(Color.BLUE);
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(emojiLabel);

        // Placeholder text-like design
        JLabel textPlaceholder = new JLabel("GEN-OS by AnRah", JLabel.CENTER);
        textPlaceholder.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        textPlaceholder.setForeground(Color.BLUE);
        textPlaceholder.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        textPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(textPlaceholder);
        leftPanel.add(Box.createVerticalGlue());
        contentPanel.add(leftPanel, BorderLayout.WEST);

        // Right Panel with two overlapping images
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.lightGray);
        rightPanel.setLayout(null);

        // Add the first image with a blue border
        ImageIcon firstImageIcon = new ImageIcon("C:\\Users\\aniqa\\IdeaProjects\\azab\\final 2\\final\\src\\wel.png");
        Image firstImage = firstImageIcon.getImage().getScaledInstance(450, 600, Image.SCALE_SMOOTH);
        JLabel firstImageLabel = new JLabel(new ImageIcon(firstImage));
        firstImageLabel.setBounds(50, 150, 400, 350);
        firstImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4)); // Blue border
        rightPanel.add(firstImageLabel);

        // Add the second overlapping image with a blue border
        ImageIcon secondImageIcon = new ImageIcon("D:\\OTHERS\\OsPROJECT\\azab\\final 2\\final\\src\\image.png");

        Image secondImage = secondImageIcon.getImage().getScaledInstance(300, 500, Image.SCALE_SMOOTH);
        JLabel secondImageLabel = new JLabel(new ImageIcon(secondImage));
        secondImageLabel.setBounds(150, 100, 520, 400);
        secondImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4)); // Blue border
        rightPanel.add(secondImageLabel);

        contentPanel.add(rightPanel, BorderLayout.CENTER);

        // Add the container panel to the frame
        welcome.add(container);

        // Transition effect for the placeholder text
        new Thread(() -> {
            for (int i = 0; i <= 255; i += 5) {
                try {
                    Thread.sleep(50); // Adjust speed of fade-in
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int opacity = i;
                SwingUtilities.invokeLater(() -> {
                    textPlaceholder.setForeground(new Color(0, 0, 255, opacity));
                });
            }
        }).start();

        // Splash screen timer
        new Thread(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> {
                welcome.dispose();
                new Page1();
            });
        }).start();

        // Make the frame visible
        welcome.setVisible(true);
    }

    public static void main(String[] args) {
        showWelcomeScreen();
    }
}
