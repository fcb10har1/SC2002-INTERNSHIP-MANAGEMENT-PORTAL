package ControlClass;

import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.Enums.OpportunityStatus;
import EntityClass.Enums.InternshipLevel;
import RepositoryClass.IOpportunityRepository;

import java.util.List;

/**
 * Manages lifecycle operations for internship opportunities including draft
 * creation, submission, approval/rejection, visibility toggling and eligibility checks.
 */
public class OpportunityManager {

    private final IOpportunityRepository opportunityRepository;

    /**
     * Constructs an OpportunityManager with the given repository.
     * @param opportunityRepository repository for opportunities
     */
    public OpportunityManager(IOpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    /**
     * Creates a new draft internship opportunity owned by the given company representative.
     * @param owner the owning company representative
     * @param company the company name
     * @param slots total available slots (cap)
     * @return the created draft InternshipOpportunity
     */
    public InternshipOpportunity createDraft(CompanyRep owner, String company, int slots) {
        InternshipOpportunity opportunity =
                new InternshipOpportunity(/*id*/ "", "", "", null,
                        InternshipLevel.Basic, null, null, company, owner, slots);
        opportunity.setStatus(OpportunityStatus.Draft);
        opportunity.setVisible(false);
        opportunityRepository.add(opportunity);
        return opportunity;
    }

    /**
     * Submits a draft internship opportunity for approval.
     * @param opportunity the draft opportunity
     * @throws IllegalStateException if the opportunity is not in Draft status
     */
    public void submitForApproval(InternshipOpportunity opportunity) {
        if (opportunity.getStatus() != OpportunityStatus.Draft) {
            throw new IllegalStateException("Only Draft opportunities can be submitted.");
        }
        opportunity.setStatus(OpportunityStatus.Pending);
        opportunity.setVisible(false);
        opportunityRepository.update(opportunity);
    }

    /**
     * Toggles visibility of an approved internship opportunity.
     * @param opportunity the approved opportunity to toggle
     * @throws IllegalStateException if opportunity not Approved
     */
    public void toggleVisibility(InternshipOpportunity opportunity) {
        if (opportunity.getStatus() != OpportunityStatus.Approved) {
            throw new IllegalStateException("Only approved opportunities can toggle visibility.");
        }
        opportunity.setVisible(!opportunity.getVisible());
        opportunityRepository.update(opportunity);
    }

    /**
     * Approves a pending internship opportunity making it visible.
     * @param staff approving career staff member
     * @param io the pending opportunity
     * @throws IllegalStateException if status not Pending
     */
    public void approve(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be approved.");
        }
        io.setStatus(OpportunityStatus.Approved);
        io.setVisible(true);
        opportunityRepository.update(io);
    }

    /**
     * Rejects a pending internship opportunity.
     * @param staff rejecting career staff member
     * @param io the pending opportunity
     * @throws IllegalStateException if status not Pending
     */
    public void reject(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be rejected.");
        }
        io.setStatus(OpportunityStatus.Rejected);
        io.setVisible(false);
        opportunityRepository.update(io);
    }

    /**
     * Returns a list of approved, visible internship opportunities the student is eligible to apply for.
     * @param student student whose eligibility is evaluated
     * @return list of eligible opportunities
     */
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
    /**
     * Returns a list of opportunities owned by the given company representative (any status).
     * @param rep owner representative
     * @return list of owned opportunities
     */
    public List<InternshipOpportunity> listOwnedOpps(CompanyRep rep) {
        return opportunityRepository.findByOwner(rep);
    }
    /**
     * Returns the backing opportunity repository.
     * @return opportunity repository
     */
    public IOpportunityRepository getRepository() {
        return opportunityRepository;
    }

    /**
     * Checks if a student is eligible to apply for the specified opportunity.
     * @param opportunity target opportunity
     * @param student candidate student
     * @return true if eligible, false otherwise
     */
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
