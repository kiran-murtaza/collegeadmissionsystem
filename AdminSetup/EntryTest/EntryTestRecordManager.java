package AdminSetup.EntryTest;

import AdminSetup.PaymentManager;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntryTestRecordManager {
    private final String FILE_PATH = "EntryTestRecords.txt";

    public static class EntryTestRecord {
        private String applicantId;
        private LocalDateTime testDateTime;
        private boolean attempted;
        private int score;

        public EntryTestRecord(String applicantId, LocalDateTime testDateTime, boolean attempted, int score) {
            this.applicantId = applicantId;
            this.testDateTime = testDateTime;
            this.attempted = attempted;
            this.score = score;
        }

        public String getApplicantId() {
            return applicantId;
        }

        public LocalDateTime getTestDateTime() {
            return testDateTime;
        }

        public boolean isAttempted() {
            return attempted;
        }

        public int getScore() {
            return score;
        }

        public void setTestDateTime(LocalDateTime testDateTime) {
            this.testDateTime = testDateTime;
        }

        public void setAttempted(boolean attempted) {
            this.attempted = attempted;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return applicantId + "," + testDateTime + "," + attempted + "," + score;
        }
    }

    /**
     * Saves or updates the given entry test record.
     */
    public void saveRecord(EntryTestRecord record) {
        List<EntryTestRecord> records = loadAllRecordsIncludingUnpaid();  // Use internal method to include all
        boolean updated = false;

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getApplicantId().equals(record.getApplicantId())) {
                records.set(i, record);
                updated = true;
                break;
            }
        }

        if (!updated) {
            records.add(record);
        }

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH)))) {
            for (EntryTestRecord r : records) {
                writer.println(r.toString());
            }
        } catch (IOException e) {
            System.err.println("Failed to save test records: " + e.getMessage());
        }
    }

    public EntryTestRecord getRecordById(String applicantId) {
        if (!PaymentManager.isFeePaid(applicantId)) {
            return null;
        }

        return loadAllRecords().stream()
                .filter(r -> r.getApplicantId().equals(applicantId))
                .findFirst()
                .orElse(null);
    }

    public List<EntryTestRecord> loadAllRecords() {
        List<EntryTestRecord> all = loadAllRecordsIncludingUnpaid();
        List<EntryTestRecord> paidOnly = new ArrayList<>();

        for (EntryTestRecord r : all) {
            if (PaymentManager.isFeePaid(r.getApplicantId())) {
                paidOnly.add(r);
            }
        }
        return paidOnly;
    }

    private List<EntryTestRecord> loadAllRecordsIncludingUnpaid() {
        List<EntryTestRecord> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",", 4);
                    if (parts.length < 4) continue;

                    String applicantId = parts[0].trim();
                    LocalDateTime dateTime = LocalDateTime.parse(parts[1].trim());
                    boolean attempted = Boolean.parseBoolean(parts[2].trim());
                    int score = Integer.parseInt(parts[3].trim());

                    EntryTestRecord record = new EntryTestRecord(applicantId, dateTime, attempted, score);
                    list.add(record);
                } catch (Exception ex) {
                    System.err.println("Skipped invalid record: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading test records: " + e.getMessage());
        }

        return list;
    }
}






