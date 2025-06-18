package AdminSetup.EntryTest;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Applicant.Status;

public class EntryTestRecordManager {
    private final String FILE_PATH = "EntryTestRecords.txt";

    public static class EntryTestRecord {
        private String applicantId;
        private LocalDateTime testDateTime;
        private boolean attempted;
        private int score;
        private ArrayList<String> subjects;

        private Status status;
        private ArrayList<String> attemptedSubjects = new ArrayList<>();
        private int totalScore = 0;
        private boolean englishTaken = false;
        private boolean mathTaken = false;
        private boolean biologyTaken = false;
        private boolean advMathTaken = false;



        public EntryTestRecord(String applicantId, LocalDateTime testDateTime, boolean attempted, int score) {
            this.applicantId = applicantId;
            this.testDateTime = testDateTime;
            this.attempted = attempted;
            this.score = score;
        }


        public ArrayList<String> getAttemptedSubjects() {
            return attemptedSubjects;
        }



        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }

        public void addToScore(int subjectScore) {
            this.totalScore += subjectScore;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
        public boolean isEnglishTaken() {
            return englishTaken;
        }

        public void setEnglishTaken(boolean englishTaken) {
            this.englishTaken = englishTaken;
        }

        public boolean isMathTaken() {
            return mathTaken;
        }

        public void setMathTaken(boolean mathTaken) {
            this.mathTaken = mathTaken;
        }

        public boolean isBiologyTaken() {
            return biologyTaken;
        }

        public void setBiologyTaken(boolean biologyTaken) {
            this.biologyTaken = biologyTaken;
        }

        public boolean isAdvMathTaken() {
            return advMathTaken;
        }

        public void setAdvMathTaken(boolean advMathTaken) {
            this.advMathTaken = advMathTaken;
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
            return applicantId + "," + testDateTime + "," + attempted + "," + score + "," +
                    (subjects != null ? String.join(";", subjects) : "Not Set");
        }

        public ArrayList<String> getSubjects() { return subjects; }
        public void setSubjects(ArrayList<String> subjects) { this.subjects = subjects; }

        public boolean isAllSubjectsCompleted() {
            return (isEnglishTaken() || !subjects.contains("English")) &&
                    (isMathTaken() || !subjects.contains("Math")) &&
                    (isBiologyTaken() || !subjects.contains("Biology")) &&
                    (isAdvMathTaken() || !subjects.contains("Add Math") && !subjects.contains("Advanced Math"));
        }
    }


    public void saveRecord(EntryTestRecord record) {
        ArrayList<EntryTestRecord> records = loadAllRecordsIncludingUnpaid();

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
        }

        catch (IOException e) {
            System.err.println("Failed to save test records: " + e.getMessage());
        }
    }

    public EntryTestRecord getRecordById(String applicantId) {
        ArrayList<EntryTestRecord> records = loadAllRecordsIncludingUnpaid();

        for (EntryTestRecord record : records) {
            if (record.getApplicantId().equalsIgnoreCase(applicantId)) {
                return record;
            }
        }
        return null;
    }



    public ArrayList<EntryTestRecord> loadAllRecords() {
        ArrayList<EntryTestRecord> all = loadAllRecordsIncludingUnpaid();
        ArrayList<EntryTestRecord> paidOnly = new ArrayList<>();

        for (EntryTestRecord r : all) {
            if (AdminSetup.PaymentManager.isFeePaid(r.getApplicantId())) {
                paidOnly.add(r);
            }
        }
        return paidOnly;
    }


    private ArrayList<EntryTestRecord> loadAllRecordsIncludingUnpaid() {
        ArrayList<EntryTestRecord> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",", 5);
                    if (parts.length < 4) continue;

                    String applicantId = parts[0].trim();
                    String dateTimeStr = parts[1].trim();
                    LocalDateTime dateTime = null;

                    if (!dateTimeStr.equalsIgnoreCase("N/A") &&
                            !dateTimeStr.equalsIgnoreCase("Not Set") &&
                            !dateTimeStr.isEmpty()) {
                        dateTime = LocalDateTime.parse(dateTimeStr);
                    }

                    boolean attempted = Boolean.parseBoolean(parts[2].trim());
                    int score = Integer.parseInt(parts[3].trim());

                    EntryTestRecord record = new EntryTestRecord(applicantId, dateTime, attempted, score);

                    if (parts.length == 5) {
                        String subjectStr = parts[4].trim();
                        if (!subjectStr.equalsIgnoreCase("Not Set") && !subjectStr.isEmpty()) {
                            ArrayList<String> subjectList = new ArrayList<>();
                            for (String sub : subjectStr.split(";")) {
                                subjectList.add(sub.trim());
                            }
                            record.setSubjects(subjectList);
                        }
                    }

                    list.add(record);

                }
                catch (Exception ex) {
                    System.err.println("Skipped invalid record: " + line);
                }
            }
        }

        catch (IOException e) {
//            System.err.println("Error reading test records: " + e.getMessage());
        }

        return list;
    }

}
