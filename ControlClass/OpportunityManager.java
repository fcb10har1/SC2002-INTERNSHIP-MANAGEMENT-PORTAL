package ControlClass;

import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import Repository.IOpportunityRepository;

import java.util.List;

public class OpportunityManager {

    private final IOpportunityRepository oppRepo;

    public OpportunityManager(IOpportunityRepository oppRepo) {
        this.oppRepo = oppRepo;
    }

    // ---------------------------------------------------------
    // Company Representative Actions
    // ---------------------------------------------------------

    /**
     * CompanyRep creates a draft internship opportunity.
     */
    public void createDraft(CompanyRep rep,
                            String title,
                            String description,
                            int vacancies,
                            String level,
                            String startDate,
                            String endDate) {

        InternshipOpportunity opp = new InternshipOpportunity(
                title,
                description,
                rep,
                vacancies,
                level,
                startDate,
                endDate
        );

        opp.setStatus(OpportunityStatus.DRAFT);
        oppRepo.save(opp);
    }

    /**
     * Submit a draft for approval (changes status from DRAFT → PENDING).
     */
    public void submitForApproval(CompanyRep rep, InternshipOpportunity opp) {
        if (!opp.getOwner().equals(rep))
            throw new IllegalStateException("You do not own this internship opportunity.");

        if (opp.getStatus() != OpportunityStatus.DRAFT)
            throw new IllegalStateException("Only DRAFT opportunities may be submitted.");

        opp.setStatus(OpportunityStatus.PENDING);
        oppRepo.save(opp);
    }

    /**
     * Edit an existing draft.
     */
    public void editDraft(CompanyRep rep,
                          InternshipOpportunity opp,
                          String title,
                          String description,
                          int vacancies,
                          String level,
                          String startDate,
                          String endDate) {

        if (!opp.getOwner().equals(rep))
            throw new IllegalStateException("Only the owner may edit this draft.");

        if (opp.getStatus() != OpportunityStatus.DRAFT)
            throw new IllegalStateException("Only DRAFT opportunities can be edited.");

        opp.setTitle(title);
        opp.setDescription(description);
        opp.setVacancies(vacancies);
        opp.setLevel(level);
        opp.setStartDate(startDate);
        opp.setEndDate(endDate);

        oppRepo.save(opp);
    }

    // ---------------------------------------------------------
    // CareerStaff Approval Actions
    // ---------------------------------------------------------

    /**
     * Staff approves an opportunity (PENDING → APPROVED).
     */
    public void approve(CareerStaff staff, InternshipOpportunity opp) {
        if (opp.getStatus() != OpportunityStatus.PENDING)
            throw new IllegalStateException("Only PENDING opportunities can be approved.");

        opp.setStatus(OpportunityStatus.APPROVED);
        oppRepo.save(opp);
    }

    /**
     * Staff rejects an opportunity (PENDING → REJECTED).
     */
    public void reject(CareerStaff staff, InternshipOpportunity opp) {
        if (opp.getStatus() != OpportunityStatus.PENDING)
            throw new IllegalStateException("Only PENDING opportunities can be rejected.");

        opp.setStatus(OpportunityStatus.REJECTED);
        oppRepo.save(opp);
    }

    // ---------------------------------------------------------
    // Listing & Viewing Methods
    // ---------------------------------------------------------

    /**
     * List all opportunities with PUBLIC = true.
     */
    public List<InternshipOpportunity> listVisibleOpps() {
        return oppRepo.findVisible();
    }

    /**
     * List all opportunities that belong to the logged-in company rep.
     */
    public List<InternshipOpportunity> listOwnedOpps(CompanyRep rep) {
        return oppRepo.findByOwner(rep);
    }

    /**
     * For CareerStaff – view all draft and pending opportunities.
     */
    public List<InternshipOpportunity> listPendingOpps() {
        return oppRepo.findByStatus(OpportunityStatus.PENDING);
    }
}
