package EntityClass;

public class Application {
    Student student;
    InternshipOpportunity target;
    ApplicationStatus status = ApplicationStatus.PENDING;
    boolean acceptedByStudent = false;
}

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public boolean isAcceptedByStudent() {
        return acceptedByStudent;
    }

    


