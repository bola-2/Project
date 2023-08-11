import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnifiedCitizenRecordSystemGUI extends JFrame {
    private final Map<String, String[]> organizationData;
    private int recordCounter;
    private final JTextField organizationInput;
    private final JTextArea recordOutput;

    public UnifiedCitizenRecordSystemGUI() {
        super("UNIFIED CITIZEN RECORD SYSTEM");

        getContentPane().setBackground(new Color(177, 230, 173));

        organizationData = new HashMap<>();
        organizationData.put("NIMC", new String[]{"name", "age", "gender"});
        organizationData.put("FRSC", new String[]{"name", "age", "driver_license"});
        organizationData.put("INEC", new String[]{"name", "age", "voter_id"});
        organizationData.put("Immigration Services", new String[]{"name", "age", "passport"});

        recordCounter = 0;

        organizationInput = new JTextField(20);
        recordOutput = new JTextArea(10, 40);
        recordOutput.setEditable(false);

//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the RIGHT

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        JButton createButton = new JButton("Create Record");
        createButton.addActionListener(new CreateRecordListener());

        JButton retrieveButton = new JButton("Check Records");
        retrieveButton.addActionListener(new RetrieveRecordsListener());
        panel.add(retrieveButton);

        panel.add(new JLabel("Select your organization:"));
        panel.add(organizationInput);
        panel.add(createButton);
        panel.add(retrieveButton);

        JScrollPane scrollPane = new JScrollPane(recordOutput);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

//    JButton retrieveButton = new JButton("Retrieve Records");
//        retrieveButton.addActionListener(new RetrieveRecordsListener());
//        panel.add(retrieveButton);

    private class CreateRecordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String organization = organizationInput.getText();
            String[] attributes = organizationData.get(organization);
            if (attributes != null) {
                Map<String, String> recordData = new HashMap<>();
                for (String attr : attributes) {
                    String value = JOptionPane.showInputDialog("Enter " + attr + ": ");
                    recordData.put(attr, value);
                }
                createRecord(organization, recordData);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid organization.");
            }
        }
    }

    private void createRecord(String organization, Map<String, String> recordData) {
        recordCounter++;
        String recordId = "Record " + recordCounter;
        StringBuilder recordInfo = new StringBuilder("Record created: " + recordId + "\n");
        recordInfo.append("Total records created: ").append(recordCounter).append("\n");

        try (FileWriter writer = new FileWriter("user_records.txt", true)) {
            writer.write(recordId + "\n");
            writer.write("Organization: " + organization + "\n");
            for (Map.Entry<String, String> entry : recordData.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
                recordInfo.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            writer.write("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        recordOutput.append(recordInfo.toString());
    }

    private class RetrieveRecordsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            retrieveRecordsFromFile();
        }
    }

    private void retrieveRecordsFromFile() {
        try {
            recordOutput.setText(""); // Clear the text area

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("user_records.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                recordOutput.append(line + "\n");
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
