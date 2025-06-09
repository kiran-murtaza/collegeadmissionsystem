package AdminSetup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PaymentManager {
    private static final String APPLICATION_FILE = "all_applications.txt";

    public static boolean isFeePaid(String applicantId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 18) continue; // 18 fields = last index is 17

                String id = parts[0].trim();
                String feeStatus = parts[17].trim(); // Fixed index

                if (id.equalsIgnoreCase(applicantId)) {
                    return feeStatus.equalsIgnoreCase("PAID");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Default: unpaid if not found
    }



}
