package EntityClass;

import EntityClass.Enums.*;

/**
 * Represents a student's application to a specific internship opportunity.
 */
public class Application {

    private Student student;
    private InternshipOpportunity target;
    private ApplicationStatus status = ApplicationStatus.Pending;
    private boolean acceptedByStudent = false;

    /**
     * Creates a new application for the given student and internship opportunity.
     */
    public Application(Student student, InternshipOpportunity target) {
        this.student = student;
        this.target = target;
    }

    /**
     * Returns the current status of this application.
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this application.
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * Returns whether the student has accepted this application offer.
     */
    public boolean isAcceptedByStudent() {
        return acceptedByStudent;
    }

    /**
     * Returns the student who submitted this application.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the internship opportunity that this application targets.
     */
    public InternshipOpportunity getTarget() {
        return target;
    }

    /**
     * Checks whether the application is fully confirmed.
     */
    public boolean isConfirmed() {
        return this.status == ApplicationStatus.Successful && this.acceptedByStudent;
    }

    /**
     * Marks this application as accepted by the student.
     */
    public void studentAccept() {
        this.acceptedByStudent = true;
    }
    // Revoke acceptance (e.g., after approved withdrawal)
    public void revokeStudentAcceptance() {
        this.acceptedByStudent = false;
    }

}