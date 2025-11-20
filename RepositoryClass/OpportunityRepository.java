package RepositoryClass;

import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import java.util.*;
import java.util.stream.Collectors;

public class OpportunityRepository implements IOpportunityRepository {

    private final Map<String, InternshipOpportunity> store = new HashMap<>();

    @Override
    public void save(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    @Override
    public void add(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    @Override
    public Optional<InternshipOpportunity> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<InternshipOpportunity> findVisibleFor(String major, int year) {
        return store.values().stream()
                .filter(io -> io.getVisible())
                .filter(io -> major == null || major.equalsIgnoreCase(io.getPreferredMajor()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(InternshipOpportunity io) {
        store.put(io.getOpportunityID(), io);
    }

    @Override
    public List<InternshipOpportunity> all() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<InternshipOpportunity> findVisible() {
        return store.values().stream()
            .filter(InternshipOpportunity::getVisible)
            .collect(Collectors.toList());
    }

    @Override
    public List<InternshipOpportunity> findByOwner(CompanyRep owner) {
        return store.values().stream()
            .filter(opp -> opp.getOwner().equals(owner))
            .collect(Collectors.toList());
    }

    @Override
    public List<InternshipOpportunity> findByStatus(OpportunityStatus status) {
        return store.values().stream()
            .filter(opp -> opp.getStatus() == status)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(String opportunityID) {
        store.remove(opportunityID);
    }
}
