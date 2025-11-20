package ControlClass;

import EntityClass.Application;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.InternshipLevel;
import EntityClass.Enums.OpportunityStatus;

import RepositoryClass.IOpportunityRepository;
import RepositoryClass.IApplicationRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// generating reports for career staff
public class ReportManager {

    private final IOpportunityRepository opportunityRepository;
    private final IApplicationRepository applicationRepository;

    public ReportManager(IOpportunityRepository oppRepo,
                         IApplicationRepository appRepo) {
        this.opportunityRepository = oppRepo;
        this.applicationRepository = appRepo;
    }

    public String generateReport(Map<String, String> filters) {
        List<InternshipOpportunity> opportunities = opportunityRepository.all();

        if (filters.containsKey("status")) {
            OpportunityStatus status =
                    OpportunityStatus.valueOf(filters.get("status"));
            opportunities = filterByStatus(status);
        }
        if (filters.containsKey("major")) {
            opportunities = filterByMajor(filters.get("major"));
        }
        if (filters.containsKey("level")) {
            InternshipLevel level =
                    InternshipLevel.valueOf(filters.get("level"));
            opportunities = filterByLevel(level);
        }
        if (filters.containsKey("company")) {
            opportunities = filterByCompany(filters.get("company"));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Internship Opportunities Report ===\n");
        for (InternshipOpportunity opp : opportunities) {
            sb.append(opp).append('\n');
        }
        return sb.toString();
    }

    public List<InternshipOpportunity> filterByStatus(OpportunityStatus status) {
        return opportunityRepository.all().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<InternshipOpportunity> filterByMajor(String major) {
        return opportunityRepository.all().stream()
                .filter(o -> major.equalsIgnoreCase(o.getPreferredMajor()))
                .collect(Collectors.toList());
    }

    public List<InternshipOpportunity> filterByLevel(InternshipLevel level) {
        return opportunityRepository.all().stream()
                .filter(o -> o.getLevel() == level)
                .collect(Collectors.toList());
    }

    public List<InternshipOpportunity> filterByCompany(String companyName) {
        return opportunityRepository.all().stream()
                .filter(o -> companyName.equalsIgnoreCase(o.getCompanyName()))
                .collect(Collectors.toList());
    }
}
