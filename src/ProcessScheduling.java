import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessScheduling {

    private DefaultTableModel tableModel;
    private List<String> RprocessIDs = new ArrayList<>();
    private List<Integer> RarrivalTimes = new ArrayList<>();
    private List<Integer> RburstTimes = new ArrayList<>();

    public ProcessScheduling(DefaultTableModel tableModel) {
        this.tableModel = tableModel;

        if (tableModel.getColumnCount() == 4) {
            tableModel.addColumn("CT");
            tableModel.addColumn("TAT");
            tableModel.addColumn("WT");
        }

        if (tableModel.getColumnCount() == 0) {
            tableModel.addColumn("Process ID");
            tableModel.addColumn("Arrival Time");
            tableModel.addColumn("Burst Time");
            tableModel.addColumn("Process Status");
            tableModel.addColumn("Completion Time");
            tableModel.addColumn("Waiting Time");
            tableModel.addColumn("Turnaround Time");
        }

        for (int i = 0; i < RprocessIDs.size(); i++) {
            Object[] rowData = {RprocessIDs.get(i), RarrivalTimes.get(i), RburstTimes.get(i), "Ready", "N/A", "N/A", "N/A"};
            tableModel.addRow(rowData);
        }

        initUI();
    }

    private void initUI() {
        JFrame frame = new JFrame();
        frame.setTitle("Process Scheduling");
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.lightGray);
        frame.setLayout(null);

        JTable schedulingTable = new JTable(tableModel);
        schedulingTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        schedulingTable.setRowHeight(30);
        schedulingTable.setForeground(new Color(255, 102, 0));
        schedulingTable.setBackground(Color.white);
        schedulingTable.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        schedulingTable.getTableHeader().setForeground(Color.WHITE);
        schedulingTable.getTableHeader().setBackground(new Color(255, 102, 0));

        JScrollPane scrollPane = new JScrollPane(schedulingTable);
        scrollPane.setBounds(110, 170, 850, 300);
        scrollPane.setBorder(new LineBorder(new Color(255, 102, 0), 2));
        frame.add(scrollPane);

        JButton backButton = createStyledButton("Go Back", 740, 50);
        JButton FCFSButton = createStyledButton("FCFS", 30, 90);
        JButton SJFButton = createStyledButton("SJF", 200, 90);
        JButton SJFPreemptiveButton = createStyledButton("SJF Preemptive", 400, 90);

        frame.add(backButton);
        frame.add(FCFSButton);
        frame.add(SJFButton);
        frame.add(SJFPreemptiveButton);

       // backButton.addActionListener(e -> new ProcessCreationPage());
        backButton.addActionListener(e -> {
            frame.dispose();
            new Page2();
        });


        FCFSButton.addActionListener(e -> {
            int completionTime = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int arrivalTime = (int) tableModel.getValueAt(i, 1);
                int burstTime = (int) tableModel.getValueAt(i, 2);

                if (arrivalTime > completionTime) {
                    completionTime = arrivalTime;
                }

                completionTime += burstTime;
                int turnaroundTime = completionTime - arrivalTime;
                int waitingTime = turnaroundTime - burstTime;

                tableModel.setValueAt(completionTime, i, 4);
                tableModel.setValueAt(waitingTime, i, 5);
                tableModel.setValueAt(turnaroundTime, i, 6);
                tableModel.setValueAt("Completed", i, 3);
            }
        });

        SJFButton.addActionListener(e -> {
            List<Object[]> processes = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                processes.add(new Object[]{
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2)
                });
            }
            processes.sort((p1, p2) -> Integer.compare((int) p1[2], (int) p2[2]));
            tableModel.setRowCount(0);
            int completionTime = 0;
            for (Object[] process : processes) {
                String id = (String) process[0];
                int arrivalTime = (int) process[1];
                int burstTime = (int) process[2];

                if (arrivalTime > completionTime) {
                    completionTime = arrivalTime;
                }

                completionTime += burstTime;
                int turnaroundTime = completionTime - arrivalTime;
                int waitingTime = turnaroundTime - burstTime;

                tableModel.addRow(new Object[]{id, arrivalTime, burstTime, "Completed", completionTime, waitingTime, turnaroundTime});
            }
        });

        SJFPreemptiveButton.addActionListener(e -> {
            int currentTime = 0;
            List<Integer> remainingBurstTimes = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                remainingBurstTimes.add((int) tableModel.getValueAt(i, 2));
            }

            while (remainingBurstTimes.stream().anyMatch(bt -> bt > 0)) {
                int shortestIndex = -1;
                int shortestBurst = Integer.MAX_VALUE;

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int arrivalTime = (int) tableModel.getValueAt(i, 1);
                    int burstTime = remainingBurstTimes.get(i);

                    if (arrivalTime <= currentTime && burstTime < shortestBurst && burstTime > 0) {
                        shortestBurst = burstTime;
                        shortestIndex = i;
                    }
                }

                if (shortestIndex == -1) {
                    currentTime++;
                    continue;
                }

                remainingBurstTimes.set(shortestIndex, remainingBurstTimes.get(shortestIndex) - 1);
                currentTime++;

                if (remainingBurstTimes.get(shortestIndex) == 0) {
                    int arrivalTime = (int) tableModel.getValueAt(shortestIndex, 1);
                    int completionTime = currentTime;
                    int turnaroundTime = completionTime - arrivalTime;
                    int waitingTime = turnaroundTime - (int) tableModel.getValueAt(shortestIndex, 2);

                    tableModel.setValueAt(completionTime, shortestIndex, 4);
                    tableModel.setValueAt(waitingTime, shortestIndex, 5);
                    tableModel.setValueAt(turnaroundTime, shortestIndex, 6);
                    tableModel.setValueAt("Completed", shortestIndex, 3);
                }
            }
        });

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 150, 40);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 102, 0));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.ORANGE, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.ORANGE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 102, 0));
            }
        });
        return button;
    }

    public static void main(String[] args) {
        DefaultTableModel tableModel = new DefaultTableModel();
        new ProcessScheduling(tableModel);
    }
}
