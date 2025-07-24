import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;


    public class MemoryManagement {
        private JFrame frame;
        private DefaultTableModel model;

        public MemoryManagement() {
            frame = new JFrame("Memory Management");
            frame.setSize(550, 600);
            frame.setLayout(null);
            frame.getContentPane().setBackground(Color.GRAY);// Background color

            // Adding a title label
            JLabel titleLabel = new JLabel("Memory Management System");
            titleLabel.setBounds(-500, 50, 500, 45); // Initially off-screen
            titleLabel.setBackground(Color.YELLOW);
            titleLabel.setForeground(Color.ORANGE);
            titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(titleLabel);

            // Buttons
            JButton LRU = createButton("Perform LRU", 180);
            JButton Paging = createButton("Perform Paging", 250); // Adjusted Y position for spacing
            JButton Segment = createButton("Segmentation", 320);
            JButton backBtn = createButton("Go Back", 390); // Adjusted Y position for spacing

            // Add buttons to frame
            frame.add(LRU);
            frame.add(Paging);
            frame.add(Segment);
            frame.add(backBtn);

            // Transitions
            animateLabel(titleLabel);
            addHoverEffect(LRU);
            addHoverEffect(Paging);
            addHoverEffect(Segment);
            addHoverEffect(backBtn);

            // Button Actions
            backBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Page2();
                }
            });

            LRU.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    PerFormeLRU();
                }
            });

            Paging.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose(); // Close the current frame
                    new Paging(); // Open the Paging frame
                }
            });

            Segment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    new segmentation();
                }
            });

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

        // Method to create a button
        private JButton createButton(String text, int y) {
            JButton button = new JButton(text);
            button.setBounds(500 / 2 - 110, y, 220, 45);// Adjusted width and alignment
            button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            button.setBackground(new Color(255, 102, 0));
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);


            // Add shadow to button border
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.ORANGE, 2), // Corrected line
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            return button;
        }

        // Function to animate the title label
        private void animateLabel(JLabel label) {
            Timer timer = new Timer(10, new ActionListener() {
                int x = -500;

                @Override
                public void actionPerformed(ActionEvent e) {
                    x += 5;
                    label.setBounds(x, 50, 500, 45);
                    if (x >= 0) {
                        ((Timer) e.getSource()).stop();
                    }
                }
            });
            timer.start();
        }

        // Function to add hover effect on buttons
        private void addHoverEffect(JButton button) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(255, 153, 51));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(255, 102, 0));
                }
            });
        }

        public void PerFormeLRU() {
            frame = new JFrame("LRU Page Replacement");
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            // Input panel styling
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.setBackground(Color.decode("#F3E5AB")); // Light pastel background for input panel

            JLabel lblRefString = new JLabel("Reference String (separated by space): ");
            lblRefString.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            lblRefString.setForeground(Color.decode("#FF4500")); // Bold and bright color for labels
            inputPanel.add(lblRefString);

            JTextField txtRefString = new JTextField();
            txtRefString.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            txtRefString.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            txtRefString.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.decode("#FFA500"), 3),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            txtRefString.setBackground(Color.decode("#FFF8DC")); // Soft yellow background
            txtRefString.setForeground(Color.decode("#000000"));
            txtRefString.setToolTipText("Enter the page reference string here."); // Tooltip for guidance
            inputPanel.add(txtRefString);

            JLabel lblNoFrames = new JLabel("Number of Frames: ");
            lblNoFrames.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            lblNoFrames.setForeground(Color.decode("#FF4500"));
            inputPanel.add(lblNoFrames);

            JTextField txtNoFrames = new JTextField();
            txtNoFrames.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            txtNoFrames.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            txtNoFrames.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.decode("#FFA500"), 3),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            txtNoFrames.setBackground(Color.decode("#FFF8DC"));
            txtNoFrames.setForeground(Color.decode("#000000"));
            txtNoFrames.setToolTipText("Enter the number of frames."); // Tooltip for guidance
            inputPanel.add(txtNoFrames);

            // Add some spacing between components
            inputPanel.add(Box.createVerticalStrut(10));

            // Process Button
            JButton btnProcess = new JButton("Process");
            btnProcess.setForeground(Color.WHITE);
            btnProcess.setBackground(Color.orange);
            btnProcess.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            btnProcess.setFocusPainted(false);
            btnProcess.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
            btnProcess.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnProcess.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnProcess.setBackground(Color.YELLOW);
                    btnProcess.setFont(new Font("Verdana", Font.BOLD, 16));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnProcess.setBackground(new Color(255, 140, 0));
                    btnProcess.setFont(new Font("Verdana", Font.BOLD, 16));
                }
            });
            btnProcess.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    processLRU(txtRefString, txtNoFrames);
                }
            });
            inputPanel.add(btnProcess);

            frame.add(inputPanel, BorderLayout.NORTH);

            // Table Styling
            model = new DefaultTableModel();
            JTable table = new JTable(model);
            table.setBackground(Color.WHITE);
            table.setForeground(Color.BLACK);
            table.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            table.setRowHeight(30);
            table.getTableHeader().setBackground(Color.decode("#FFA500"));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane, BorderLayout.CENTER);

            // Back Button Styling
            JButton backBtn = new JButton("Go Back");
            backBtn.setBounds(350, 450, 100, 50);
            backBtn.setPreferredSize(new Dimension(150, 40));
            backBtn.setBackground(Color.decode("#FF6347")); // Tomato color for the button
            backBtn.setForeground(Color.WHITE);
            backBtn.setBackground(Color.ORANGE);
            backBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            backBtn.setFocusPainted(false);
            backBtn.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Add Hover Effect
            backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    backBtn.setBackground(new Color(255, 140, 0));
                    backBtn.setFont(new Font("Verdana", Font.BOLD, 20));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    backBtn.setBackground(Color.YELLOW);
                    backBtn.setFont(new Font("Verdana", Font.BOLD, 18));
                }
            });

            backBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    new MemoryManagement();
                }
            });

            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.decode("#F3E5AB"));
            bottomPanel.add(backBtn);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            // Frame settings
            frame.getContentPane().setBackground(Color.decode("#FFF8DC"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

            private void processLRU(JTextField txtRefString, JTextField txtNoFrames) {
            String[] refString = txtRefString.getText().trim().split("\\s+");
            int noFrames = Integer.parseInt(txtNoFrames.getText().trim());

            int[] pages = new int[refString.length];
            for (int i = 0; i < refString.length; i++) {
                pages[i] = Integer.parseInt(refString[i]);
            }

            int[] frame = new int[noFrames];
            Arrays.fill(frame, -1); // Initialize all frames as empty
            int[] lastUsed = new int[noFrames];
            Arrays.fill(lastUsed, -1); // Initialize all last used times as -1

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Page Reference");
            for (int i = 0; i < noFrames; i++) {
                columnNames.add("Frame " + (i + 1));
            }
            columnNames.add("Page Fault");
            model.setColumnIdentifiers(columnNames);

            int count = 0; // Count of page faults
            for (int i = 0; i < pages.length; i++) {
                int page = pages[i];
                int minLastUsedIndex = -1;
                boolean pageFault = true;

                for (int j = 0; j < noFrames; j++) {
                    if (frame[j] == page) {
                        lastUsed[j] = i;
                        pageFault = false;
                        break;
                    }
                    if (minLastUsedIndex == -1 || lastUsed[j] < lastUsed[minLastUsedIndex]) {
                        minLastUsedIndex = j;
                    }
                }

                if (pageFault) {
                    frame[minLastUsedIndex] = page;
                    lastUsed[minLastUsedIndex] = i;
                    count++;
                }

                Vector<Object> row = new Vector<>();
                row.add(page);
                for (int j = 0; j < noFrames; j++) {
                    row.add(frame[j] == -1 ? "Empty" : frame[j]);
                }
                row.add(pageFault ? "Yes" : "No");
                model.addRow(row);
            }
        }


        public static void main(String[] args) {

            new MemoryManagement();
        }
    }