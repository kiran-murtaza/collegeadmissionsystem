package AdminSetup.EntryTest;

import java.io.*;
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
        private List<String> subjects;

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

        // With appropriate getters and setters
        public List<String> getSubjects() { return subjects; }
        public void setSubjects(List<String> subjects) { this.subjects = subjects; }

    }

    /**
     * Saves or updates the given entry test record.
     */
    public void saveRecord(EntryTestRecord record) {
        List<EntryTestRecord> records = loadAllRecordsIncludingUnpaid();

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

    /**
     * Get record for given applicant ID (no fee check here).
     */
    public EntryTestRecord getRecordById(String applicantId) {
        List<EntryTestRecord> records = loadAllRecordsIncludingUnpaid();

        for (EntryTestRecord record : records) {
            if (record.getApplicantId().equalsIgnoreCase(applicantId)) {
                return record;
            }
        }
        return null; // Not found
    }
//

    /**
     * Loads only fee-paid applicants' test records.
     */
    public List<EntryTestRecord> loadAllRecords() {
        List<EntryTestRecord> all = loadAllRecordsIncludingUnpaid();
        List<EntryTestRecord> paidOnly = new ArrayList<>();

        for (EntryTestRecord r : all) {
            if (AdminSetup.PaymentManager.isFeePaid(r.getApplicantId())) {
                paidOnly.add(r);
            }
        }
        return paidOnly;
    }

    /**
     * Loads all test records (regardless of fee).
     */
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
                    String dateTimeStr = parts[1].trim();
                    LocalDateTime dateTime = null;

                    // Handle "N/A" or "Not Set" or empty string date values safely
                    if (!dateTimeStr.equalsIgnoreCase("N/A") &&
                            !dateTimeStr.equalsIgnoreCase("Not Set") &&
                            !dateTimeStr.isEmpty()) {
                        dateTime = LocalDateTime.parse(dateTimeStr);
                    }

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
