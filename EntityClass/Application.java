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
     * @param student submitting student
     * @param target targeted internship opportunity
     */
    public Application(Student student, InternshipOpportunity target) {
        this.student = student;
        this.target = target;
    }

    /**
     * Returns the current status of this application.
     * @return application status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this application.
     * @param status new status
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * Returns whether the student has accepted this application offer.
     * @return true if accepted by student
     */
    public boolean isAcceptedByStudent() {
        return acceptedByStudent;
    }

    /**
     * Returns the student who submitted this application.
     * @return student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the internship opportunity that this application targets.
     * @return target opportunity
     */
    public InternshipOpportunity getTarget() {
        return target;
    }

    /**
     * Checks whether the application is fully confirmed (successful and accepted).
     * @return true if confirmed
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
    /**
     * Revokes student acceptance.
     */
    public void revokeStudentAcceptance() {
        this.acceptedByStudent = false;
    }

}