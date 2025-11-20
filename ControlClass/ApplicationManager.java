package ControlClass;

import EntityClass.Application;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.Enums.ApplicationStatus;
import EntityClass.Enums.OpportunityStatus;
import RepositoryClass.IApplicationRepository;
import RepositoryClass.IOpportunityRepository;

import java.util.List;

/**
 * Manages application lifecycle operations between Students and Company Representatives,
 * including applying, rep approval/rejection, and student acceptance/rejection of offers.
 */
public class ApplicationManager {

    private final IApplicationRepository applicationRepo;
    @SuppressWarnings("unused")
    private final IOpportunityRepository opportunityRepo;

    /**
     * Constructs an ApplicationManager with the required repositories.
     * @param applicationRepo repository for persisting applications
     * @param opportunityRepo repository for accessing opportunities (may be used for side-effects)
     */
    public ApplicationManager(IApplicationRepository applicationRepo,
                              IOpportunityRepository opportunityRepo) {
        this.applicationRepo = applicationRepo;
        this.opportunityRepo = opportunityRepo;
    }

    /**
     * Creates a new application for the given student and opportunity enforcing
     * a maximum of 3 concurrent applications.
     * @param student the student applying
     * @param opportunity the opportunity being applied to
     * @return the created Application
     * @throws IllegalStateException if the student already has 3 applications
     */
    public Application apply(Student student, InternshipOpportunity opportunity) {
        List<Application> existing = applicationRepo.findByStudent(student);
        int count = existing.size();
        if (count >= 3) {
            throw new IllegalStateException("Student has reached maximum number of applications (3).");
        }

        Application app = new Application(student, opportunity);
        applicationRepo.add(app);
        student.submitApplication(app);
        opportunity.addApplication(app);
        return app;
    }

    /**
     * Approves a pending application by the owning company representative.
     * @param rep the company representative performing approval
     * @param application the target application
     * @throws IllegalStateException if rep is not owner or application not Pending
     */
    public void repApprove(CompanyRep rep, Application application) {
        if (!application.getTarget().getOwner().equals(rep)) {
            throw new IllegalStateException("Only the owner company representative can approve this application.");
        }
        if (application.getStatus() != ApplicationStatus.Pending) {
            throw new IllegalStateException("Only PENDING applications can be approved.");
        }
        application.setStatus(ApplicationStatus.Successful);
        applicationRepo.update(application);
    }
    /**
     * Rejects a pending application by the owning company representative.
     * @param rep the company representative performing rejection
     * @param application the target application
     * @throws IllegalStateException if rep is not owner or application not Pending
     */
    public void repReject(CompanyRep rep, Application application) {
        if (!application.getTarget().getOwner().equals(rep)) {
            throw new IllegalStateException("Only the owner company representative can reject this application.");
        }
        if (application.getStatus() != ApplicationStatus.Pending) {
            throw new IllegalStateException("Only PENDING applications can be rejected.");
        }
        application.setStatus(ApplicationStatus.Unsuccessful);
        applicationRepo.update(application);
    }

    /**
     * Accepts a successful application offer on behalf of the student, auto-rejecting all other
     * outstanding successful/pending applications and updating opportunity fill status.
     * @param application the successful application being accepted
     * @throws IllegalStateException if application status is not Successful
     */
    public void studentAcceptOffer(Application application) {
        if (application.getStatus() != ApplicationStatus.Successful) {
            throw new IllegalStateException("Can only accept applications with status Successful.");
        }
        // Mark this application as accepted by student
        application.studentAccept();
        applicationRepo.update(application);
        // Auto-reject all other outstanding successful offers for this student
        Student s = application.getStudent();
        s.getApplications().stream()
                .filter(other -> other != application)
                .filter(other -> other.getStatus() == ApplicationStatus.Pending || other.getStatus() == ApplicationStatus.Successful)
                .filter(other -> !other.isAcceptedByStudent())
                .forEach(other -> {
                    other.setStatus(ApplicationStatus.Unsuccessful);
                    applicationRepo.update(other);
                });
        // After acceptance, check if opportunity is now filled
        InternshipOpportunity opp = application.getTarget();
        if (opp.confirmedCount() >= opp.getSlotCap()) {
            opp.setStatus(OpportunityStatus.Filled);
            try {
                opportunityRepo.update(opp);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Rejects a successful application offer on behalf of the student.
     * @param application the successful application being rejected
     * @throws IllegalStateException if application status is not Successful
     */
    public void studentRejectOffer(Application application) {
        if (application.getStatus() != ApplicationStatus.Successful) {
            throw new IllegalStateException("Can only reject applications with status Successful.");
        }
        application.setStatus(ApplicationStatus.Unsuccessful);
        applicationRepo.update(application);
    }

    /**
     * Returns the backing application repository.
     * @return application repository
     */
    public IApplicationRepository getApplicationRepository() {
        return applicationRepo;
    }
}

