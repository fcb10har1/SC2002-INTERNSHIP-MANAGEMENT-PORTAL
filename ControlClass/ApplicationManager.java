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

/*
 * Manages application processes between Students and CompanyReps
 */
public class ApplicationManager {

    private final IApplicationRepository applicationRepo;
    @SuppressWarnings("unused")
    private final IOpportunityRepository opportunityRepo;

    /*
     * Constructor
     */
    public ApplicationManager(IApplicationRepository applicationRepo,
                              IOpportunityRepository opportunityRepo) {
        this.applicationRepo = applicationRepo;
        this.opportunityRepo = opportunityRepo;
    }

    /*
     * Student applies for an internship opportunity
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

    /*
     * CompanyRep approves an application
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
    /*
     * CompanyRep rejects an application
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

    /*
     * Student accepts an offer
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

    /*
     * Student rejects an offer
     */
    public void studentRejectOffer(Application application) {
        if (application.getStatus() != ApplicationStatus.Successful) {
            throw new IllegalStateException("Can only reject applications with status Successful.");
        }
        application.setStatus(ApplicationStatus.Unsuccessful);
        applicationRepo.update(application);
    }

    /*
     * Returns the application repository
     */
    public IApplicationRepository getApplicationRepository() {
        return applicationRepo;
    }
}

