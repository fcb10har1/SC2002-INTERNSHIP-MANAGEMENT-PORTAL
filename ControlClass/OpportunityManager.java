package ControlClass;

import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.Enums.OpportunityStatus;
import EntityClass.Enums.InternshipLevel;
import RepositoryClass.IOpportunityRepository;

import java.util.List;

public class OpportunityManager {

    private final IOpportunityRepository opportunityRepository;

    public OpportunityManager(IOpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    // CompanyRep actions
    public InternshipOpportunity createDraft(CompanyRep owner, String company, int slots) {
        InternshipOpportunity opportunity =
                new InternshipOpportunity(/*id*/ "", "", "", null,
                        InternshipLevel.Basic, null, null, company, owner, slots);
        opportunity.setStatus(OpportunityStatus.Draft);
        opportunity.setVisible(false);
        opportunityRepository.add(opportunity);
        return opportunity;
    }

    public void submitForApproval(InternshipOpportunity opportunity) {
        if (opportunity.getStatus() != OpportunityStatus.Draft) {
            throw new IllegalStateException("Only Draft opportunities can be submitted.");
        }
        opportunity.setStatus(OpportunityStatus.Pending);
        opportunity.setVisible(false);
        opportunityRepository.update(opportunity);
    }

    public void toggleVisibility(InternshipOpportunity opportunity) {
        if (opportunity.getStatus() != OpportunityStatus.Approved) {
            throw new IllegalStateException("Only approved opportunities can toggle visibility.");
        }
        opportunity.setVisible(!opportunity.getVisible());
        opportunityRepository.update(opportunity);
    }

    // CareerStaff actions
    public void approve(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be approved.");
        }
        io.setStatus(OpportunityStatus.Approved);
        io.setVisible(true);
        opportunityRepository.update(io);
    }

    public void reject(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be rejected.");
        }
        io.setStatus(OpportunityStatus.Rejected);
        io.setVisible(false);
        opportunityRepository.update(io);
    }

    // Listing
    public List<InternshipOpportunity> listVisibleFor(Student student) {
        return opportunityRepository.findVisibleFor(student.getMajor(), student.getYearOfStudy());
    }

    // Eligibility check – simplified
    public boolean checkEligibility(InternshipOpportunity opportunity, Student student) {
        if (opportunity.getStatus() != OpportunityStatus.Approved || !opportunity.getVisible()) {
            return false;
        }
        if (student.getApplications().stream()
                .anyMatch(app -> app.getTarget().equals(opportunity))) {
            return false;
        }
        if (student.getApplications().size() >= 5) {
            return false;
        }

        InternshipLevel level = opportunity.getLevel();
        switch (level) {
            case Basic:
                break;
            case Intermediate:
                if (student.getYearOfStudy() < 2) return false;
                break;
            case Advanced:
                if (student.getYearOfStudy() < 3) return false;
                break;
        }

        if (opportunity.confirmedCount() >= opportunity.getSlotCap()) {
            return false;
        }

        return true;
    }
}
