package ControlClass;

import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.Enums.OpportunityStatus;
import EntityClass.Enums.InternshipLevel;
import RepositoryClass.IOpportunityRepository;

import java.util.List;

/*
 * Manages internship opportunities between CompanyReps, CareerStaff, and Students
 */
public class OpportunityManager {

    private final IOpportunityRepository opportunityRepository;

    public OpportunityManager(IOpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    /*
    * CompanyRep creates a draft internship opportunity
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

    /*
     * CompanyRep submits a draft internship opportunity for approval
     */
    public void submitForApproval(InternshipOpportunity opportunity) {
        if (opportunity.getStatus() != OpportunityStatus.Draft) {
            throw new IllegalStateException("Only Draft opportunities can be submitted.");
        }
        opportunity.setStatus(OpportunityStatus.Pending);
        opportunity.setVisible(false);
        opportunityRepository.update(opportunity);
    }

    /*
     * CompanyRep toggles visibility of an approved internship opportunity
     */
    public void toggleVisibility(InternshipOpportunity opportunity) {
        if (opportunity.getStatus() != OpportunityStatus.Approved) {
            throw new IllegalStateException("Only approved opportunities can toggle visibility.");
        }
        opportunity.setVisible(!opportunity.getVisible());
        opportunityRepository.update(opportunity);
    }

    /*
     * CareerStaff approves a pending internship opportunity
     */
    public void approve(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be approved.");
        }
        io.setStatus(OpportunityStatus.Approved);
        io.setVisible(true);
        opportunityRepository.update(io);
    }

    /*
     * CareerStaff rejects a pending internship opportunity
     */
    public void reject(CareerStaff staff, InternshipOpportunity io) {
        if (io.getStatus() != OpportunityStatus.Pending) {
            throw new IllegalStateException("Only Pending opportunities can be rejected.");
        }
        io.setStatus(OpportunityStatus.Rejected);
        io.setVisible(false);
        opportunityRepository.update(io);
    }

    /*
     * Lists internship opportunities visible to a student based on eligibility
     */
    public List<InternshipOpportunity> listVisibleFor(Student student) {
        // Apply eligibility rules explicitly: status Approved & visible, major match, level-year mapping, slots available
        int year = student.getYearOfStudy();
        String major = student.getMajor();
        return opportunityRepository.findVisible().stream()
                .filter(opp -> opp.getStatus() == OpportunityStatus.Approved && opp.isVisible())
                .filter(opp -> opp.getPreferredMajor() == null || major.equalsIgnoreCase(opp.getPreferredMajor()))
                .filter(opp -> {
                    switch (opp.getLevel()) {
                        case Basic: return year == 1; // Year 1 only
                        case Intermediate: return year >= 2 && year <= 3; // Years 2-3
                        case Advanced: return year >= 4; // Years 4-5 (assuming 5 possible)
                        default: return false;
                    }
                })
                .filter(opp -> opp.confirmedCount() < opp.getSlotCap())
                .filter(opp -> student.getApplications().stream().noneMatch(app -> app.getTarget().equals(opp)))
                .collect(java.util.stream.Collectors.toList());
    }
    /*
     * Lists internship opportunities owned by a CompanyRep
     */
    public List<InternshipOpportunity> listOwnedOpps(CompanyRep rep) {
        return opportunityRepository.findByOwner(rep);
    }
    /*
     * Gets the opportunity repository
     */
    public IOpportunityRepository getRepository() {
        return opportunityRepository;
    }

    /*
     * Checks if a student is eligible to apply for a given internship opportunity
     */
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
        int year = student.getYearOfStudy();
        switch (level) {
            case Basic:
                if (year != 1) return false;
                break;
            case Intermediate:
                if (year < 2 || year > 3) return false;
                break;
            case Advanced:
                if (year < 4) return false; // Advanced only for year 4-5
                break;
        }

        if (opportunity.confirmedCount() >= opportunity.getSlotCap()) {
            return false;
        }

        return true;
    }
}
