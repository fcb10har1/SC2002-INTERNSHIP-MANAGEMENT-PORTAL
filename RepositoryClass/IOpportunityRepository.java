package RepositoryClass;

import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import java.util.List;
import java.util.Optional;

/**
 * Contract for InternshipOpportunity data persistence and retrieval operations.
 */
public interface IOpportunityRepository {
    /**
     * Saves (persists) opportunity, overwriting prior state.
     * @param opportunity opportunity instance
     */
    void save(InternshipOpportunity opportunity);
    /**
     * Adds new opportunity (alias of save).
     * @param io opportunity instance
     */
    void add(InternshipOpportunity io);
    /**
     * Finds opportunity by id.
     * @param id opportunity id
     * @return optional opportunity
     */
    Optional<InternshipOpportunity> findById(String id);
    /**
     * Finds visible opportunities for a major/year.
     * @param major preferred major or null
     * @param year academic year (currently unused eligibility dimension)
     * @return list of visible opportunities
     */
    List<InternshipOpportunity> findVisibleFor(String major, int year);
    /**
     * Updates existing opportunity.
     * @param io opportunity
     */
    void update(InternshipOpportunity io);
    /**
     * Returns all opportunities.
     * @return list of opportunities
     */
    List<InternshipOpportunity> all();
    /**
     * Returns approved and visible opportunities.
     * @return list of opportunities
     */
    List<InternshipOpportunity> findVisible();
    /**
     * Finds opportunities owned by representative.
     * @param owner company representative
     * @return list of owned opportunities
     */
    List<InternshipOpportunity> findByOwner(CompanyRep owner);
    /**
     * Finds opportunities by status.
     * @param status opportunity status
     * @return list of matching opportunities
     */
    List<InternshipOpportunity> findByStatus(OpportunityStatus status);
    /**
     * Deletes opportunity by id.
     * @param opportunityID opportunity id
     */
    void delete(String opportunityID);
}
