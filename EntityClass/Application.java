package EntityClass;

import EntityClass.Enums.*;

public class Application {
    private Student student;
    private InternshipOpportunity target;
    private ApplicationStatus status = ApplicationStatus.Pending;
    private boolean acceptedByStudent = false;

    public Application(Student student, InternshipOpportunity target) {
        this.student = student;
        this.target = target;
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

    public Student getStudent() {
        return student;
    }

    public InternshipOpportunity getTarget() {
        return target;
    }

    public boolean isconfirmed() {
        return this.status == ApplicationStatus.Successful && this.acceptedByStudent;
    }

    public void studentAccept() {
        this.acceptedByStudent = true;
    }
    

}
    


