package AdminSetup;

import java.time.LocalDate;

public class EntryTestManager {
    private LocalDate commerceTestDate;
    private LocalDate engineeringTestDate;
    private LocalDate biologyTestDate;

    public EntryTestManager() {
        commerceTestDate = null;
        engineeringTestDate = null;
        biologyTestDate = null;
    }

    public void scheduleTest(String stream, LocalDate date) {
        switch (stream.toLowerCase()) {
            case "commerce":
                commerceTestDate = date;
                break;
            case "engineering":
                engineeringTestDate = date;
                break;
            case "biology":
                biologyTestDate = date;
                break;
            default:
                System.out.println("Invalid stream");
        }
    }

    public LocalDate getTestDate(String stream) {
        switch (stream.toLowerCase()) {
            case "commerce":
                return commerceTestDate;
            case "engineering":
                return engineeringTestDate;
            case "biology":
                return biologyTestDate;
            default:
                return null;
        }
    }

    public void resetTest(String stream) {
        switch (stream.toLowerCase()) {
            case "commerce":
                commerceTestDate = null;
                break;
            case "engineering":
                engineeringTestDate = null;
                break;
            case "biology":
                biologyTestDate = null;
                break;
            default:
                System.out.println("Invalid stream");
        }
    }
}
