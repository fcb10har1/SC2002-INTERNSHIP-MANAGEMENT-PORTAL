package RepositoryClass;

import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory repository for InternshipOpportunity entities with various query helpers.
 */
public class OpportunityRepository implements IOpportunityRepository {

    private final Map<String, InternshipOpportunity> store = new HashMap<>();

    /**
     * Saves an internship opportunity, overwriting prior state.
     * @param io opportunity instance
     */
    @Override
    public void save(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    /**
     * Adds a new internship opportunity (alias of save).
     * @param io opportunity instance
     */
    @Override
    public void add(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    /**
     * Finds an internship opportunity by id.
     * @param id opportunity id
     * @return optional opportunity
     */
    @Override
    public Optional<InternshipOpportunity> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Finds visible opportunities for a major/year.
     * @param major preferred major or null for any
     * @param year academic year (currently not filtered)
     * @return list of visible opportunities
     */
    @Override
    public List<InternshipOpportunity> findVisibleFor(String major, int year) {
        return store.values().stream()
                .filter(io -> io.getVisible())
                .filter(io -> major == null || major.equalsIgnoreCase(io.getPreferredMajor()))
                .collect(Collectors.toList());
    }

    /**
     * Updates existing opportunity state.
     * @param io opportunity instance
     */
    @Override
    public void update(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    /**
     * Returns all stored opportunities.
     * @return list of opportunities
     */
    @Override
    public List<InternshipOpportunity> all() {
        return new ArrayList<>(store.values());
    }

    /**
     * Returns all currently visible opportunities.
     * @return list of visible opportunities
     */
    @Override
    public List<InternshipOpportunity> findVisible() {
        return store.values().stream()
            .filter(InternshipOpportunity::getVisible)
            .collect(Collectors.toList());
    }

    /**
     * Finds opportunities owned by the given company representative.
     * @param owner representative
     * @return list of owned opportunities
     */
    @Override
    public List<InternshipOpportunity> findByOwner(CompanyRep owner) {
        return store.values().stream()
            .filter(opp -> opp.getOwner().equals(owner))
            .collect(Collectors.toList());
    }

    /**
     * Finds opportunities by status.
     * @param status status value
     * @return list of matching opportunities
     */
    @Override
    public List<InternshipOpportunity> findByStatus(OpportunityStatus status) {
        return store.values().stream()
            .filter(opp -> opp.getStatus() == status)
            .collect(Collectors.toList());
    }

    /**
     * Deletes opportunity by id.
     * @param opportunityID id of opportunity
     */
    @Override
    public void delete(String opportunityID) {
        store.remove(opportunityID);
    }
}
