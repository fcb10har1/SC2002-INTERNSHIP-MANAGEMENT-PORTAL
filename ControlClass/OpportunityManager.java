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
        // Apply eligibility rules explicitly: status Approved & visible, major match, level-year mapping, slots available
        int year = student.getYearOfStudy();
        String major = student.getMajor();
        java.time.LocalDate today = java.time.LocalDate.now();
        return opportunityRepository.findVisible().stream()
                .filter(opp -> opp.getStatus() == OpportunityStatus.Approved && opp.isVisible())
                .filter(opp -> opp.getPreferredMajor() == null || major.equalsIgnoreCase(opp.getPreferredMajor()))
                .filter(opp -> {
                    // New eligibility: Year 1-2 students can ONLY apply for Basic.
                    // Year 3 and above can apply for any level.
                    if (year <= 2) {
                        return opp.getLevel() == InternshipLevel.Basic;
                    } else { // year >= 3
                        return true; // Any level permitted
                    }
                })
                .filter(opp -> opp.getCloseDate() == null || !opp.getCloseDate().isBefore(today)) // Not past closing date
                .filter(opp -> opp.getStatus() != OpportunityStatus.Filled) // Not filled
                .filter(opp -> opp.confirmedCount() < opp.getSlotCap()) // Has available slots
                .filter(opp -> student.getApplications().stream().noneMatch(app -> app.getTarget().equals(opp)))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<InternshipOpportunity> listOwnedOpps(CompanyRep rep) {
        return opportunityRepository.findByOwner(rep);
    }

    public IOpportunityRepository getRepository() {
        return opportunityRepository;
    }

    // Eligibility check – simplified
    public boolean checkEligibility(InternshipOpportunity opportunity, Student student) {
        if (opportunity.getStatus() != OpportunityStatus.Approved || !opportunity.getVisible()) {
            return false;
        }
        
        // Check closing date
        java.time.LocalDate today = java.time.LocalDate.now();
        if (opportunity.getCloseDate() != null && opportunity.getCloseDate().isBefore(today)) {
            return false;
        }
        
        // Check if opportunity is filled
        if (opportunity.getStatus() == OpportunityStatus.Filled) {
            return false;
        }
        
        if (student.getApplications().stream()
                .anyMatch(app -> app.getTarget().equals(opportunity))) {
            return false;
        }
        if (student.getApplications().size() >= 3) {
            return false;
        }

        int year = student.getYearOfStudy();
        if (year <= 2) { // Year 1-2 only Basic allowed
            if (opportunity.getLevel() != InternshipLevel.Basic) {
                return false;
            }
        } // Year 3+ any level allowed

        if (opportunity.confirmedCount() >= opportunity.getSlotCap()) {
            return false;
        }

        return true;
    }
}
