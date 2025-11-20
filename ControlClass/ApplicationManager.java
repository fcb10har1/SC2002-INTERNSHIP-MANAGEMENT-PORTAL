package ControlClass;

import EntityClass.Application;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.Enums.ApplicationStatus;
import RepositoryClass.IApplicationRepository;
import RepositoryClass.IOpportunityRepository;

import java.util.List;

public class ApplicationManager {

    private final IApplicationRepository applicationRepo;
    @SuppressWarnings("unused")
    private final IOpportunityRepository opportunityRepo;

    public ApplicationManager(IApplicationRepository applicationRepo,
                              IOpportunityRepository opportunityRepo) {
        this.applicationRepo = applicationRepo;
        this.opportunityRepo = opportunityRepo;
    }

    // Student applies for an internship opportunity
    public Application apply(Student student, InternshipOpportunity opportunity) {
        List<Application> existing = applicationRepo.findByStudent(student);
        int count = existing.size();
        if (count >= 5) {
            throw new IllegalStateException("Student has reached maximum number of applications (5).");
        }

        Application app = new Application(student, opportunity);
        applicationRepo.add(app);
        student.submitApplication(app);
        opportunity.addApplication(app);
        return app;
    }

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

    // Student accepts an offer
    public void studentAcceptOffer(Application application) {
        if (application.getStatus() != ApplicationStatus.Successful) {
            throw new IllegalStateException("Can only accept applications with status Successful.");
        }
        application.studentAccept();
        applicationRepo.update(application);
    }

    // Student rejects an offer
    public void studentRejectOffer(Application application) {
        if (application.getStatus() != ApplicationStatus.Successful) {
            throw new IllegalStateException("Can only reject applications with status Successful.");
        }
        application.setStatus(ApplicationStatus.Unsuccessful);
        applicationRepo.update(application);
    }

    public IApplicationRepository getApplicationRepository() {
        return applicationRepo;
    }
}

