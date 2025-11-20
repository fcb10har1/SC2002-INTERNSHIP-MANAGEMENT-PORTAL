package RepositoryClass;

import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import java.util.*;
import java.util.stream.Collectors;

/*
 * OpportunityRepository provides in-memory storage and retrieval of InternshipOpportunity entities.
 */
public class OpportunityRepository implements IOpportunityRepository {

    private final Map<String, InternshipOpportunity> store = new HashMap<>();

    /*     
     * Saves an InternshipOpportunity to the repository.
     */
    @Override
    public void save(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    /*     
     * Adds a new InternshipOpportunity to the repository.
     */
    @Override
    public void add(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    /*     
     * Finds an InternshipOpportunity by its ID.
     */
    @Override
    public Optional<InternshipOpportunity> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /*     
     * Finds all visible InternshipOpportunities for a given major and year.
     */
    @Override
    public List<InternshipOpportunity> findVisibleFor(String major, int year) {
        return store.values().stream()
                .filter(io -> io.getVisible())
                .filter(io -> major == null || major.equalsIgnoreCase(io.getPreferredMajor()))
                .collect(Collectors.toList());
    }

    /*     
     * Updates an existing InternshipOpportunity in the repository.
     */
    @Override
    public void update(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    /*     
     * Retrieves all InternshipOpportunities in the repository.
     */
    @Override
    public List<InternshipOpportunity> all() {
        return new ArrayList<>(store.values());
    }

    /*     
     * Finds all visible InternshipOpportunities.
     */
    @Override
    public List<InternshipOpportunity> findVisible() {
        return store.values().stream()
            .filter(InternshipOpportunity::getVisible)
            .collect(Collectors.toList());
    }

    /*     
     * Finds all InternshipOpportunities owned by a specific CompanyRep.
     */
    @Override
    public List<InternshipOpportunity> findByOwner(CompanyRep owner) {
        return store.values().stream()
            .filter(opp -> opp.getOwner().equals(owner))
            .collect(Collectors.toList());
    }

    /*     
     * Finds all InternshipOpportunities with a specific status.
     */
    @Override
    public List<InternshipOpportunity> findByStatus(OpportunityStatus status) {
        return store.values().stream()
            .filter(opp -> opp.getStatus() == status)
            .collect(Collectors.toList());
    }

    /*     
     * Deletes an InternshipOpportunity by its ID.
     */
    @Override
    public void delete(String opportunityID) {
        store.remove(opportunityID);
    }
}
