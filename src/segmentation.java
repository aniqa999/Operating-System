import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class segmentation {

    private JFrame frame;
    private JTable segmentTable;
    private DefaultTableModel tableModel;
    private JTextField segmentField, baseField, limitField, logicalSegmentField, offsetField;
    private JTextArea resultArea;
    private JPanel memoryPanel;
    private ArrayList<Segment> segments;

    public segmentation() {
        segments = new ArrayList<>();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Segmentation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Segment Input Panel
        JPanel segmentInputPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        segmentInputPanel.setBorder(BorderFactory.createTitledBorder("Add Segment"));

        segmentField = new JTextField();
        baseField = new JTextField();
        limitField = new JTextField();
        JButton addSegmentButton = new JButton("Add Segment");
        addSegmentButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        addSegmentButton.setBackground(new Color(255, 102, 0));
        addSegmentButton.setForeground(Color.WHITE);

        JLabel segmentLabel = new JLabel("Segment No");
        segmentLabel.setForeground(new Color(255, 102, 0));
        segmentLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        segmentInputPanel.add(segmentLabel);
        segmentInputPanel.add(segmentField);

        JLabel baseLabel = new JLabel("Base Address");
        baseLabel.setForeground(new Color(255, 102, 0));
        baseLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        segmentInputPanel.add(baseLabel);
        segmentInputPanel.add(baseField);

        JLabel limitLabel = new JLabel("Limit");
        limitLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        limitLabel.setForeground(new Color(255, 102, 0));
        segmentInputPanel.add(limitLabel);
        segmentInputPanel.add(limitField);

        segmentInputPanel.add(addSegmentButton);
        inputPanel.add(segmentInputPanel);


        // Logical Address Input Panel
        JPanel logicalAddressPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        logicalAddressPanel.setBorder(BorderFactory.createTitledBorder("Logical Address to Physical Address"));

        logicalSegmentField = new JTextField();
        offsetField = new JTextField();
        JButton calculateButton = new JButton("Calculate");
calculateButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
calculateButton.setBackground(new Color(255, 102, 0));
calculateButton.setForeground(Color.WHITE);

       // JPanel logicalAddressPanel = new JPanel(new GridLayout(1, 6, 5, 5));

        logicalAddressPanel.add(new JLabel("Segment No:"));
        JLabel segmentNoLabel = (JLabel) logicalAddressPanel.getComponent(0);
        segmentNoLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        segmentNoLabel.setForeground(new Color(255, 102, 0)); // Set color to orange

        logicalAddressPanel.setFont(new Font("Comic Sans MS", Font.BOLD, 14)); // Set font for the labels
        logicalAddressPanel.add(logicalSegmentField);

        logicalAddressPanel.add(new JLabel("Offset:"));
        JLabel offsetLabel = (JLabel) logicalAddressPanel.getComponent(2);
        offsetLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        offsetLabel.setForeground(new Color(255, 102, 0)); // Set color to orange

        logicalAddressPanel.add(offsetField);
        logicalAddressPanel.add(calculateButton);

        inputPanel.add(logicalAddressPanel);


        // Result Panel
        resultArea = new JTextArea(4, 20);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Result"));
        inputPanel.add(resultArea);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"Segment No", "Base Address", "Limit"}, 0);
        segmentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(segmentTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Segment Table"));

// Set orange color for table headers
        JTableHeader header = segmentTable.getTableHeader();
        header.setBackground(Color.WHITE); // Set background color to orange
        header.setForeground(new Color(255, 102, 0));  // Set text color to black
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        //header.setFont(new Font("Arial", Font.BOLD, 12)); // Optional: Customize font

// Add table to frame
        frame.add(tableScrollPane, BorderLayout.CENTER);


        // Memory Visualization Panel
        memoryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMemory(g);
            }
        };
        // Memory Panel
        memoryPanel.setPreferredSize(new Dimension(200, 400));
        memoryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 102, 0), 2), // Orange border
                "Memory Visualization",                                   // Title text
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,    // Title position
                javax.swing.border.TitledBorder.DEFAULT_POSITION,         // Default position
                new Font("Comic Sans MS", Font.BOLD, 15),                 // Font style
                new Color(255, 102, 0)                                    // Orange text color
        ));

// Add panel to the frame
        frame.add(memoryPanel, BorderLayout.EAST);

// Footer Panel with Back Button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton back = new JButton("Back");

// Customize the button with orange theme
        back.setBackground(new Color(255, 102, 0));
     //   back.setBounds(420, 450, 200, 20);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 14));
        back.setFocusPainted(true);
        back.setBorder(BorderFactory.createLineBorder(Color.YELLOW)); // Optional: border

        footerPanel.add(back);


        frame.add(footerPanel, BorderLayout.SOUTH);

        // Button Listeners
        addSegmentButton.addActionListener(e -> addSegment());
        calculateButton.addActionListener(e -> calculatePhysicalAddress());

        back.addActionListener(e -> {
            frame.dispose();
            new Page2();
        });

        frame.setVisible(true);
    }

    private void addSegment() {
        try {
            int segmentNo = Integer.parseInt(segmentField.getText());
            int base = Integer.parseInt(baseField.getText());
            int limit = Integer.parseInt(limitField.getText());

            // Check for duplicate segment or overlapping ranges
            for (Segment segment : segments) {
                if (segment.getSegmentNo() == segmentNo || (base >= segment.getBase() && base < segment.getBase() + segment.getLimit())) {
                    JOptionPane.showMessageDialog(frame, "Segment already allocated or overlapping!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            segments.add(new Segment(segmentNo, base, limit));
            tableModel.addRow(new Object[]{segmentNo, base, limit});
            segmentField.setText("");
            baseField.setText("");
            limitField.setText("");
            memoryPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for Segment, Base, and Limit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawMemory(Graphics g) {
        int y = 10;
        int panelHeight = memoryPanel.getHeight();
        int panelWidth = memoryPanel.getWidth();
        int totalLimit = segments.stream().mapToInt(Segment::getLimit).sum();

        if (totalLimit == 0) return;

        for (Segment segment : segments) {
            int segmentHeight = (int) ((double) segment.getLimit() / totalLimit * (panelHeight - 20));
            g.setColor(new Color((segment.getSegmentNo() * 1234567) % 0xFFFFFF));
            g.fillRect(4, y, panelWidth - 20, segmentHeight);
            g.setColor(Color.BLACK);
            g.drawRect(4, y, panelWidth - 20, segmentHeight);
            g.drawString("Segment " + segment.getSegmentNo(), 15, y + 15);
            y += segmentHeight + 5;
        }
    }

    private void calculatePhysicalAddress() {
        try {
            int segmentNo = Integer.parseInt(logicalSegmentField.getText());
            int offset = Integer.parseInt(offsetField.getText());

            if (offset < 0) {
                resultArea.setText("Error: Offset cannot be negative.");
                return;
            }

            for (Segment segment : segments) {
                if (segment.getSegmentNo() == segmentNo) {
                    if (offset < segment.getLimit()) {
                        int physicalAddress = segment.getBase() + offset;
                        resultArea.setText("Physical Address: " + physicalAddress);
                        return;
                    } else {
                        resultArea.setText("Error: Offset exceeds segment limit.");
                        return;
                    }
                }
            }

            resultArea.setText("Error: Segment not found.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for Segment and Offset.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new segmentation();
    }

    // Helper Class for Segment
    class Segment {
        private final int segmentNo;
        private final int base;
        private final int limit;

        public Segment(int segmentNo, int base, int limit) {
            this.segmentNo = segmentNo;
            this.base = base;
            this.limit = limit;
        }

        public int getSegmentNo() {
            return segmentNo;
        }

        public int getBase() {
            return base;
        }

        public int getLimit() {
            return limit;
        }
    }
}
