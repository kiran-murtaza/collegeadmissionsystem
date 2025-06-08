package AdminSetup;

import java.io.*;

public class PaymentManager {
    private static final String APPLICATION_FILE = "all_applications.txt";

    public static boolean isFeePaid(String applicantId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 15) continue;

                String id = parts[0].trim();
                String status = parts[14].trim().toUpperCase();

                if (id.equals(applicantId)) {
                    return status.equals("PAYMENT_CLEARED");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading payment status: " + e.getMessage());
        }
        return false;
    }
}

