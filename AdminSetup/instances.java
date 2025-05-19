package AdminSetup;


interface AdmissionCriteria {
    boolean isEligible(Student student);
    String getDescription();
}

interface ScholarshipCriteria {
    boolean isEligible(Student student);
    String getDescription();
}

class MeritBasedCriteria implements AdmissionCriteria {
    private int minScore;

    public MeritBasedCriteria(int minScore) {
        this.minScore = minScore;
    }

    public boolean isEligible(Student student) {
        return student.getScore() >= minScore;
    }

    public String getDescription() {
        return "Min Merit Score: " + minScore;
    }
}

class MeritScholarshipCriteria implements ScholarshipCriteria {
    private int minScore;

    public MeritScholarshipCriteria(int minScore) {
        this.minScore = minScore;
    }

    public boolean isEligible(Student student) {
        return student.getScore() >= minScore;
    }

    public String getDescription() {
        return "Merit >= " + minScore;
    }
}

enum Status {
    PENDING, ADMITTED, REJECTED
}

