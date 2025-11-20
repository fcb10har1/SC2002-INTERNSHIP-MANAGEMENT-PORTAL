package ControlClass;

import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.Enums.OpportunityStatus;
import EntityClass.Enums.InternshipLevel;
import Repository.IOpportunityRepository;

import java.util.List;

public class OpportunityManager {

    private final IOpportunityRepository opportunityRepository;

    public OpportunityManager(IOpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    // -----------------------------------------------------
    // CompanyRep actions
    // -----------------------------------------------------

    public InternshipOpportunity createDraft(CompanyRep owner, String company, int slots) {
        InternshipOpportunity opportunity = new InternshipOpportunity(owner, company, slots);
        opportunity.setStatus(OpportunityStatus.Draft);
        opportunity.setVisible(false); // drafts should not be visible
        opportunityRepository.save(opportunity);
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

    // Toggle visibility ONLY for approved opportunities
    public void toggleVisibility(InternshipOpportunity opportunity) {
        if (opportunity.getStatus() != OpportunityStatus.Approved) {
            throw new IllegalStateException("Only approved opportunities can toggle visibility.");
        }
        opportunity.setVisible(!opportunity.isVisible());
        opportunityRepository.update(opportunity);
    }

    // -----------------------------------------------------
    // CareerStaff actions
    // -----------------------------------------------------

    public void approve(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be approved.");
        }
        io.setStatus(OpportunityStatus.Approved);
        io.setReviewedBy(staff);
        io.setVisible(true);
        opportunityRepository.update(io);
    }

    public void reject(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be rejected.");
        }
        io.setStatus(OpportunityStatus.Rejected);
        io.setReviewedBy(staff);
        io.setVisible(false);
        opportunityRepository.update(io);
    }

    // -----------------------------------------------------
    // Listing
    // -----------------------------------------------------

    public List<InternshipOpportunity> listVisibleFor(Student student) {
        return opportunityRepository.findVisibleForStudent(student);
    }

    // -----------------------------------------------------
    // Eligibility check
    // -----------------------------------------------------

    public boolean checkEligibility(InternshipOpportunity opportunity, Student student) {

        // 1. Must be approved & visible
        if (opportunity.getStatus() != OpportunityStatus.Approved || !opportunity.isVisible()) {
            return false;
        }

        // 2. Student has already applied for this same opportunity
        if (student.getApplications().stream()
                .anyMatch(app -> app.getOpportunity().equals(opportunity))) {
            return false;
        }

        // 3. Max 5 applications rule
        if (student.getApplications().size() >= 5) {
            return false;
        }

        // 4. Check level requirement (your enum is Basic/Intermediate/Advanced)
        InternshipLevel level = opportunity.getLevel();

        switch (level) {
            case Basic:
                break; // everyone can apply

            case Intermediate:
                if (student.getYearOfStudy() < 2) return false;
                break;

            case Advanced:
                if (student.getYearOfStudy() < 3) return false;
                break;
        }

        // 5. Check slots
        if (opportunity.getSlots() <= 0) {
            return false;
        }

        return true;
    }
}
