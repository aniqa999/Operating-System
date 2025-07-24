import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.List;

class ProcessCreationPage extends JFrame {
    // Lists to store the dispatched processes
    private List<String> RprocessIDs = new ArrayList<>();
    private List<Integer> RarrivalTimes = new ArrayList<>();
    private List<Integer> RburstTimes = new ArrayList<>();

    private DefaultTableModel blockedTableModel;
    private JTable blockedTable;
    private JTextField idField, arrivalField, burstField;
    private ArrayList<String> processIDs = new ArrayList<>();
    private ArrayList<Integer> arrivalTimes = new ArrayList<>();
    private ArrayList<Integer> burstTimes = new ArrayList<>();
    private JTable processTable, dispatchedTable, suspendedTable;
    private DefaultTableModel tableModel, dispatchTableModel, suspendTableModel;

    private static int processId = 1;

    public static int getProcessId() {
        return processId++;
    }

    public ProcessCreationPage() {

        setTitle("Process Management");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);

        // Labels and Fields
        JLabel idLabel = new JLabel("Process ID");
        idLabel.setBounds(50, 30, 100, 30);
        idLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        idLabel.setForeground(new Color(255, 102, 0));
        add(idLabel);

        String id = String.valueOf(getProcessId());
        System.out.printf(id);
        idField = new JTextField(id);
        idField.setBounds(150, 30, 200, 40);
        idField.setEditable(false);
        idField.setFocusable(false);
        add(idField);

        JLabel arrivalLabel = new JLabel("Arrival Time");
        arrivalLabel.setBounds(50, 70, 100, 30);
        arrivalLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        arrivalLabel.setForeground(new Color(255, 102, 0));
        add(arrivalLabel);

        arrivalField = new JTextField();
        arrivalField.setBounds(150, 70, 200, 40);
        add(arrivalField);

        JLabel burstLabel = new JLabel("Burst Time");
        burstLabel.setBounds(50, 110, 100, 30);
        burstLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        burstLabel.setForeground(new Color(255, 102, 0));
        add(burstLabel);

        burstField = new JTextField();
        burstField.setBounds(150, 110, 200, 30);
        add(burstField);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(400, 30, 350, 150);
        buttonPanel.setLayout(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        Color buttonColor = new Color(255, 102, 0);
        Color textColor = Color.WHITE;

        JButton createButton = new JButton("Create Process");
        JButton destroyButton = new JButton("Destroy Process");
        JButton dispatchButton = new JButton("Dispatch Process");
        JButton blockButton = new JButton("Block Process");
        JButton wakeupButton = new JButton(("Wakeup Process"));
        JButton suspendButton = new JButton("Suspend Process");
        JButton resumeButton = new JButton("Resume Process");
        JButton backButton = new JButton("Go Back");

        JButton[] buttons = {createButton, destroyButton, dispatchButton, suspendButton, resumeButton, backButton, blockButton, wakeupButton};
        for (JButton button : buttons) {
            styleButton(button, buttonColor, textColor);
            buttonPanel.add(button);
        }
        add(buttonPanel);

        createButton.addActionListener(e -> createProcess());
        destroyButton.addActionListener(e -> destroyProcess());
        dispatchButton.addActionListener(e -> dispatchProcess());
        blockButton.addActionListener(e -> blockProcess());
        wakeupButton.addActionListener(e -> wakeupBlockedProcesses());
        suspendButton.addActionListener(e -> suspendProcess());
        resumeButton.addActionListener(e -> resumeProcess());
        backButton.addActionListener(e -> {
            new Page2();
            dispose();
        });

        dispatchButton.addActionListener(e -> {
            new ProcessScheduling(tableModel); // Pass table model to ProcessScheduling
            dispose();
        });

// Tables Initialization and Adding Labels
// Process Table
        JLabel processTableLabel = new JLabel("Process Table");
        processTableLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        processTableLabel.setForeground(new Color(255, 102, 0));
        processTableLabel.setBounds(50, 200, 200, 20);
        add(processTableLabel);

        String[] columnNames = {"Process ID", "Arrival Time", "Burst Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(processTable);
        tableScrollPane.setBounds(50, 240, 350, 150);
        add(tableScrollPane);


// Dispatched Table

        JLabel lab = new JLabel("dispatch");
        JLabel dispatchedTableLabel = new JLabel("Dispatched Table");
        dispatchedTableLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        dispatchedTableLabel.setForeground(new Color(255, 102, 0));
        dispatchedTableLabel.setBounds(420, 200, 200, 20);
        dispatchedTableLabel.setVisible(true);
        add(dispatchedTableLabel);

        String[] dispatchColumnNames = {"Process ID", "Arrival Time", "Burst Time", "Status"};
        dispatchTableModel = new DefaultTableModel(dispatchColumnNames, 0);
        dispatchedTable = new JTable(dispatchTableModel);
        JScrollPane dispatchScrollPane = new JScrollPane(dispatchedTable);
        dispatchScrollPane.setBounds(420, 240, 350, 150);
        add(dispatchScrollPane);

// Suspended Table
        JLabel suspendedTableLabel = new JLabel("Suspended Table");
        suspendedTableLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        suspendedTableLabel.setForeground(new Color(255, 102, 0));
        suspendedTableLabel.setBounds(50, 450, 200, 20);
        add(suspendedTableLabel);

        String[] suspendColumnNames = {"Process ID", "Arrival Time", "Burst Time", "Status"};
        suspendTableModel = new DefaultTableModel(suspendColumnNames, 0);
        suspendedTable = new JTable(suspendTableModel);
        JScrollPane suspendScrollPane = new JScrollPane(suspendedTable);
        suspendScrollPane.setBounds(50, 500, 350, 150);
        add(suspendScrollPane);

        // Blocked Table
        JLabel blockedTableLabel = new JLabel("Blocked Table");
        blockedTableLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        blockedTableLabel.setForeground(new Color(255, 102, 0));
        blockedTableLabel.setBounds(420, 450, 200, 20);
        add(blockedTableLabel);

        String[] blockedColumnNames = {"Process ID", "Arrival Time", "Burst Time", "Status"};
        blockedTableModel = new DefaultTableModel(blockedColumnNames, 0);
        blockedTable = new JTable(blockedTableModel);
        JScrollPane blockedScrollPane = new JScrollPane(blockedTable);
        blockedScrollPane.setBounds(420, 500, 350, 150);
        add(blockedScrollPane);

    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void createProcess() {
        String id = idField.getText();
        if (id.isEmpty()) {
            Random random = new Random();
            id = "P" + random.nextInt(1000);
            idField.setText(id);
        } else if (processIDs.contains(id)) {
            JOptionPane.showMessageDialog(null, "Process ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int arrivalTime, burstTime;
        try {
            arrivalTime = Integer.parseInt(arrivalField.getText());
            burstTime = Integer.parseInt(burstField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Arrival or Burst Time.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        processIDs.add(id);
        arrivalTimes.add(arrivalTime);
        burstTimes.add(burstTime);
        tableModel.addRow(new Object[]{id, arrivalTime, burstTime, "Ready"});
        JOptionPane.showMessageDialog(null, "Process created successfully!");
        clearFields();
    }

    private void destroyProcess() {
        int selectedRow = processTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a process to destroy.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        processIDs.remove(selectedRow);
        arrivalTimes.remove(selectedRow);
        burstTimes.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        JOptionPane.showMessageDialog(null, "Process destroyed.");
    }

    private void dispatchProcess() {
        // Ensure a row is selected
        int selectedRow = processTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a process to dispatch.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract the process details from the selected row
        String id = (String) processTable.getValueAt(selectedRow, 0); // Process ID
        int arrivalTime = (int) processTable.getValueAt(selectedRow, 1); // Arrival Time
        int burstTime = (int) processTable.getValueAt(selectedRow, 2); // Burst Time

        // Add the selected process to the lists used by ProcessScheduling
        RprocessIDs.add(id);
        RarrivalTimes.add(arrivalTime);
        RburstTimes.add(burstTime);

        // Add the process to the dispatched table (UI update)
        dispatchTableModel.addRow(new Object[]{id, arrivalTime, burstTime, "Dispatched"});



        // Remove the process from the ready table (UI update)
        tableModel.removeRow(selectedRow);

        // Call the ProcessScheduling method to schedule and visualize the dispatched processes
        new ProcessScheduling(dispatchTableModel); // Assuming ProcessScheduling accepts a DefaultTableModel


        // Confirmation message
        JOptionPane.showMessageDialog(null, "Process dispatched.");
    }


    private void suspendProcess() {
        int selectedRow = processTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a process to suspend.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = processIDs.get(selectedRow);
        int arrivalTime = arrivalTimes.get(selectedRow);
        int burstTime = burstTimes.get(selectedRow);

        suspendTableModel.addRow(new Object[]{id, arrivalTime, burstTime, "Suspended"});
        tableModel.removeRow(selectedRow);
        processIDs.remove(selectedRow);
        arrivalTimes.remove(selectedRow);
        burstTimes.remove(selectedRow);

        JOptionPane.showMessageDialog(null, "Process suspended.");
    }

    private void resumeProcess() {
        int selectedRow = suspendedTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a suspended process to resume.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) suspendTableModel.getValueAt(selectedRow, 0);
        int arrivalTime = Integer.parseInt(suspendTableModel.getValueAt(selectedRow, 1).toString());
        int burstTime = Integer.parseInt(suspendTableModel.getValueAt(selectedRow, 2).toString());

        tableModel.addRow(new Object[]{id, arrivalTime, burstTime, "Ready"});
        suspendTableModel.removeRow(selectedRow);

        processIDs.add(id);
        arrivalTimes.add(arrivalTime);
        burstTimes.add(burstTime);

        JOptionPane.showMessageDialog(null, "Process resumed.");
    }
//    private void setupBlockedTable() {
//        String[] blockedColumnNames = {"Process ID", "Arrival Time", "Burst Time", "Status"};
//        blockedTableModel = new DefaultTableModel(blockedColumnNames, 0);
//        blockedTable = new JTable(blockedTableModel);
//
//        JScrollPane blockedScrollPane = new JScrollPane(blockedTable);
//        blockedScrollPane.setBounds(50, 710, 700, 150);
//        add(blockedScrollPane);
//    }

    private void blockProcess() {
        int selectedRow = processTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a process to block.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get process details
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int arrivalTime = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
        int burstTime = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());

        // Add process to blocked table
        blockedTableModel.addRow(new Object[]{id, arrivalTime, burstTime, "Blocked"});

        // Remove process from ready table
        tableModel.removeRow(selectedRow);
        processIDs.remove(selectedRow);
        arrivalTimes.remove(selectedRow);
        burstTimes.remove(selectedRow);

        JOptionPane.showMessageDialog(null, "Process blocked successfully.");
    }

    private void wakeupBlockedProcesses() {
        boolean foundBlocked = false;

        for (int i = 0; i < blockedTableModel.getRowCount(); i++) {
            String status = (String) blockedTableModel.getValueAt(i, 3);
            if ("Blocked".equals(status)) {
                foundBlocked = true;
                String id = (String) blockedTableModel.getValueAt(i, 0);
                int arrivalTime = Integer.parseInt(blockedTableModel.getValueAt(i, 1).toString());
                int burstTime = Integer.parseInt(blockedTableModel.getValueAt(i, 2).toString());

                tableModel.addRow(new Object[]{id, arrivalTime, burstTime, "Ready"});
                blockedTableModel.removeRow(i);
                processIDs.add(id);
                arrivalTimes.add(arrivalTime);
                burstTimes.add(burstTime);

                i--; // Adjust index after row removal
            }
        }

        if (foundBlocked) {
            JOptionPane.showMessageDialog(null, "All blocked processes are now ready.");
        } else {
            JOptionPane.showMessageDialog(null, "No processes are in a blocked state.");
        }
    }
    private void clearFields() {
        idField.setText(String.valueOf(getProcessId()));
        arrivalField.setText("");
        burstField.setText("");
    }

    public static void main(String[] args) {
        new ProcessCreationPage().setVisible(true);
    }
}