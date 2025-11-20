package RepositoryClass;

import java.util.List;
import java.util.Optional;

import EntityClass.InternshipOpportunity;

public interface IOpportunityRepository {

    void add(InternshipOpportunity io);

    Optional<InternshipOpportunity> findById(String id);

    List<InternshipOpportunity> findVisibleFor(String major, int year);

    void update(InternshipOpportunity io);

    List<InternshipOpportunity> all();
}
