package ControlClass;

import EntityClass.Application;
import EntityClass.CompanyRep;
import EntityClass.Enums.ApplicationStatus;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import Repository.IApplicationRepository;
import Repository.IInternshipRepository;

public class ApplicationManager {

    private final IApplicationRepository applicationRepo;
    private final IInternshipRepository internshipRepo;

    public ApplicationManager(IApplicationRepository applicationRepo,
                              IInternshipRepository internshipRepo) {
        this.applicationRepo = applicationRepo;
        this.internshipRepo = internshipRepo;
    }

    // Student applies for an internship opportunity
    public Application apply(Student student, InternshipOpportunity opportunity) {
        int count = applicationRepo.countByStudent(student);
        if (count >= 5) {
            throw new IllegalStateException("Student has reached maximum number of applications (5).");
        }

        Application app = new Application(student, opportunity);
        applicationRepo.save(app);   // or add(app)
        return app;
    }

    public void repApprove(CompanyRep rep, Application application) {
        if (!application.getOpportunity().getOwner().equals(rep)) {
            throw new IllegalStateException("Only the owner company representative can approve this application.");
        }
        if (application.getStatus() != ApplicationStatus.Pending) {
        throw new IllegalStateException("Only PENDING applications can be approved.");
    }
        application.setStatus(ApplicationStatus.Successful);
        applicationRepo.save(application);  
    }

    // Student accepts an offer
    public void studentAcceptOffer(Application application) {
        if (application.getStatus() != ApplicationStatus.Successful) {
            throw new IllegalStateException("Can only accept applications with status OFFERED.");
        }

        // update entity state
        application.isAcceptedByStudent();      // or application.studentAccept();
        application.setStatus(ApplicationStatus.Successful);

        // persist change
        applicationRepo.save(application);
    }

    // Student rejects an offer
    public void studentRejectOffer(Application application) {
        if (application.getStatus() != ApplicationStatus.Successful) {
            throw new IllegalStateException("Can only reject applications with status OFFERED.");
        }
        application.setStatus(ApplicationStatus.Unsuccessful);
        applicationRepo.save(application);
    }
}
