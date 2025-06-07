//package Applicant;
//
//import Applicant.ApplicationFormData;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.*;
//import java.util.ArrayList;
//import Applicant.Applicant;
//
//
//public class PaymentPortal_Panel extends JPanel {
//    private Applicant userInfo;
//    private ArrayList<ApplicationFormData> offeredApplications;
//    private JComboBox<String> appSelector;
//    private JLabel feeLabel, dueDateLabel, statusLabel;
//    private JButton payButton;
//
//    public PaymentPortal_Panel(Applicant userInfo) {
//        this.userInfo = userInfo;
//        this.offeredApplications = new ArrayList<>();
//        setLayout(new BorderLayout());
//        setBackground(Color.WHITE);
//
//        loadApplications();
//
//        if (offeredApplications.isEmpty()) {
//            JLabel noAppsLabel = new JLabel("No applications are accepted yet for payment.");
//            noAppsLabel.setFont(new Font("Arial", Font.BOLD, 18));
//            noAppsLabel.setHorizontalAlignment(SwingConstants.CENTER);
//            noAppsLabel.setForeground(Color.RED);
//            add(noAppsLabel, BorderLayout.CENTER);
//        } else {
//            buildUI();
//        }
//    }
//
//    private void loadApplications() {
//        try (BufferedReader reader = new BufferedReader(new FileReader("all_applications.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 18) {
//                    String userId = parts[0];
//                    String appId = parts[1];
//                    String address = parts[3];
//                    String board10 = parts[4];
//                    String year10 = parts[5];
//                    String percent10 = parts[6];
//                    String stream10 = parts[7];
//                    String board12 = parts[8];
//                    String year12 = parts[9];
//                    String percent12 = parts[10];
//                    String stream12 = parts[11];
//                    Program program = new Program(parts[12]);
//                    College college = new College(parts[13]);
//                    String email = parts[14];
//                    String status = parts[15];
//
//                    if ((email.equals(userInfo.getEmail()) || userId.equals(userInfo.getUserID()))
//                            && "Admission Offered".equalsIgnoreCase(status)) {
//                        ApplicationFormData app = new ApplicationFormData();
//                        app.setApplicationId(appId);
//                        app.setEmail(email);
//                        app.setAddress(address);
//                        app.setBoard10(board10);
//                        app.setYear10(year10);
//                        app.setPercent10(percent10);
//                        app.setStream10(stream10);
//                        app.setBoard12(board12);
//                        app.setYear12(year12);
//                        app.setPercent12(percent12);
//                        app.setStream12(stream12);
//                        app.setSelectedProgram(program);
//                        app.setSelectedCollege(college);
//                        app.setStatus(ApplicationStatus.ADMISSION_OFFERED);
//                        app.setUsers(userInfo);
//                        offeredApplications.add(app);
//                    }
//                } else {
//                    System.err.println("Skipping malformed application line: " + line);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void buildUI() {
//        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
//        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        appSelector = new JComboBox<>();
//        for (ApplicationFormData app : offeredApplications) {
//            appSelector.addItem(app.getApplicationId() + " - " + app.getSelectedProgram().getName());
//        }
//
//        feeLabel = new JLabel();
//        dueDateLabel = new JLabel();
//        statusLabel = new JLabel("Fee Status: Unpaid");
//
//        payButton = new JButton("Pay Now");
//        payButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int index = appSelector.getSelectedIndex();
//                if (index >= 0) {
//                    ApplicationFormData selectedApp = offeredApplications.get(index);
//                    handlePayment(selectedApp);
//                }
//            }
//        });
//
//        appSelector.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int index = appSelector.getSelectedIndex();
//                if (index >= 0) {
//                    updateFeeDetails(offeredApplications.get(index));
//                }
//            }
//        });
//
//        formPanel.add(new JLabel("Select Application:"));
//        formPanel.add(appSelector);
//        formPanel.add(new JLabel("Fee Amount:"));
//        formPanel.add(feeLabel);
//        formPanel.add(new JLabel("Due Date:"));
//        formPanel.add(dueDateLabel);
//        formPanel.add(statusLabel);
//        formPanel.add(payButton);
//
//        add(formPanel, BorderLayout.CENTER);
//        appSelector.setSelectedIndex(0);
//        updateFeeDetails(offeredApplications.get(0));
//    }
//
//    private void updateFeeDetails(ApplicationFormData app) {
//        String collegeName = app.getSelectedCollege().getName();
//        String programName = app.getSelectedProgram().getName();
//        double fee = 0.0;
//
//        try (BufferedReader reader = new BufferedReader(new FileReader("colleges.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 3 && parts[0].equalsIgnoreCase(collegeName)
//                        && parts[1].equalsIgnoreCase(programName)) {
//                    fee = Double.parseDouble(parts[2]);
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        boolean hasScholarship = false;
//        try (BufferedReader reader = new BufferedReader(new FileReader("allscholarship.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 3 && parts[0].equalsIgnoreCase(app.getApplicationId())
//                        && "Approved".equalsIgnoreCase(parts[2])) {
//                    hasScholarship = true;
//                    fee *= 0.5;
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        feeLabel.setText("Rs. " + fee);
//        dueDateLabel.setText("Due: 5 days after offer");
//
//        if (app.getStatus() == ApplicationStatus.ADMISSION_ACCEPTED) {
//            statusLabel.setText("Fee Status: Paid");
//            statusLabel.setForeground(new Color(0, 128, 0));
//            payButton.setEnabled(false);
//        } else {
//            statusLabel.setText("Fee Status: Unpaid");
//            statusLabel.setForeground(Color.RED);
//            payButton.setEnabled(true);
//        }
//    }
//
//    private void handlePayment(ApplicationFormData app) {
//        int confirm = JOptionPane.showConfirmDialog(this, "Proceed with payment for: " + app.getApplicationId(),
//                "Confirm Payment", JOptionPane.YES_NO_OPTION);
//        if (confirm == JOptionPane.YES_OPTION) {
//            updateApplicationStatus(app);
//            app.setStatus(ApplicationStatus.ADMISSION_ACCEPTED);
//            updateFeeDetails(app);
//            JOptionPane.showMessageDialog(this, "Payment Successful. Admission Accepted.");
//        }
//    }
//
//    private void updateApplicationStatus(ApplicationFormData targetApp) {
//        File inputFile = new File("all_applications.txt");
//        File tempFile = new File("temp_applications.txt");
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 16 && parts[1].equals(targetApp.getApplicationId())) {
//                    parts[15] = "Admission Accepted";
//                    if (parts.length < 18) {
//                        String[] extended = new String[18];
//                        System.arraycopy(parts, 0, extended, 0, parts.length);
//                        for (int i = parts.length; i < 18; i++) {
//                            extended[i] = "";
//                        }
//                        extended[17] = "Paid";
//                        line = String.join(",", extended);
//                    } else {
//                        parts[17] = "Paid";
//                        line = String.join(",", parts);
//                    }
//                }
//                writer.write(line);
//                writer.newLine();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        inputFile.delete();
//        tempFile.renameTo(inputFile);
//    }
//}
