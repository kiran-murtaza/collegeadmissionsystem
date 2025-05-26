package Applicant;

import AdminSetup.Program.Program;
import Authentication.Gender;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PaymentPortal_Panel {

    private Applicant applicant;

    public PaymentPortal_Panel(Applicant applicant) {
        this.applicant = applicant;
        showFeePortal();
    }

    private void showFeePortal() {
        JFrame frame = new JFrame("Fee Payment Portal");
        frame.setSize(650, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columns = {"Program", "Fee", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        ArrayList<ApplicationFormData> approvedApplications = new ArrayList<>();

        for (ApplicationFormData app : applicant.getSubmittedApplications()) {
            if (app.getStatus().equals(Status.APPROVED)) {
                approvedApplications.add(app);
                tableModel.addRow(new Object[]{
                        app.getSelectedProgram().getName(),
                        "Rs. " + app.getSelectedProgram().getFee(),
                        applicant.getFeeStatus()
                });
            }
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton payFeeButton = new JButton("Pay Fee");
        payFeeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a row to pay the fee.");
                return;
            }

            ApplicationFormData selectedApp = approvedApplications.get(selectedRow);
            openPaymentDialog(selectedApp, tableModel, selectedRow);
        });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(payFeeButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void openPaymentDialog(ApplicationFormData appData, DefaultTableModel tableModel, int row) {
        JFrame popup = new JFrame("Enter Payment Details");
        popup.setSize(300, 200);
        popup.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Card Number:"));
        JTextField cardField = new JTextField();
        panel.add(cardField);

        panel.add(new JLabel("Amount (Rs):"));
        JTextField amountField = new JTextField(String.valueOf(appData.getSelectedProgram().getFee()));
        panel.add(amountField);

        JButton confirm = new JButton("Pay Fee");
        confirm.addActionListener(e -> {
            String card = cardField.getText().trim();
            String amtText = amountField.getText().trim();

            if (card.length() < 8 || amtText.isEmpty()) {
                JOptionPane.showMessageDialog(popup, "Invalid input.");
                return;
            }

            try {
                double amount = Double.parseDouble(amtText);
                if (amount < appData.getSelectedProgram().getFee()) {
                    JOptionPane.showMessageDialog(popup, "Amount is less than required.");
                    return;
                }

                // Update fee status
                applicant.setFeeStatus(FeeStatus.PAID);
                tableModel.setValueAt(FeeStatus.PAID, row, 2);
                JOptionPane.showMessageDialog(popup, "Payment successful.");
                popup.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Enter a valid number.");
            }
        });

        panel.add(new JLabel()); // empty
        panel.add(confirm);

        popup.add(panel);
        popup.setVisible(true);
    }



}
