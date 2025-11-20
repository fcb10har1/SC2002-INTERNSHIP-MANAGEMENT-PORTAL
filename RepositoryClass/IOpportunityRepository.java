package RepositoryClass;

import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import java.util.List;
import java.util.Optional;

public interface IOpportunityRepository {
    void save(InternshipOpportunity opportunity);
    void add(InternshipOpportunity io);
    Optional<InternshipOpportunity> findById(String id);
    List<InternshipOpportunity> findVisibleFor(String major, int year);
    void update(InternshipOpportunity io);
    List<InternshipOpportunity> all();
    List<InternshipOpportunity> findVisible();
    List<InternshipOpportunity> findByOwner(CompanyRep owner);
    List<InternshipOpportunity> findByStatus(OpportunityStatus status);
    void delete(String opportunityID);
}
