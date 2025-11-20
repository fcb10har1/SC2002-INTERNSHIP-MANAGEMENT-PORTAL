package RepositoryClass;

import java.util.*;
import java.util.stream.Collectors;

import EntityClass.InternshipOpportunity;

public class OpportunityRepository implements IOpportunityRepository {

    private final Map<String, InternshipOpportunity> store = new HashMap<>();

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
}
