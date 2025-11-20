package ControlClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import EntityClass.Application;
import EntityClass.Enums.InternshipLevel;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.OpportunityStatus;
import RepositoryClass.IApplicationRepository;
import RepositoryClass.IOpportunityRepository;

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

    // Comprehensive aggregated report for career staff
    public String generateComprehensiveReport() {
    List<InternshipOpportunity> allOpps = opportunityRepository.all();
    List<Application> allApps = applicationRepository.all();

    StringBuilder sb = new StringBuilder();
    sb.append("=== Comprehensive Internship Report ===\n");
    sb.append("Total Opportunities: ").append(allOpps.size()).append('\n');

    // Status counts
    java.util.Map<OpportunityStatus, Long> statusCounts = allOpps.stream()
        .collect(Collectors.groupingBy(InternshipOpportunity::getStatus, Collectors.counting()));
    for (OpportunityStatus st : OpportunityStatus.values()) {
        sb.append("  ").append(st).append(": ")
          .append(statusCounts.getOrDefault(st, 0L)).append('\n');
    }

    // Filled vs Open slots summary
    long filled = allOpps.stream().filter(InternshipOpportunity::slotsFilled).count();
    long withRemaining = allOpps.stream().filter(o -> !o.slotsFilled()).count();
    sb.append("Filled Opportunities: ").append(filled).append("\n");
    sb.append("Open (slots remaining): ").append(withRemaining).append("\n");

    // Major distribution
    sb.append("\nBy Preferred Major:\n");
    java.util.Map<String, Long> byMajor = allOpps.stream()
        .collect(Collectors.groupingBy(o -> o.getPreferredMajor() == null ? "(Unspecified)" : o.getPreferredMajor(), Collectors.counting()));
    byMajor.forEach((m, c) -> sb.append("  ").append(m).append(": ").append(c).append('\n'));

    // Level distribution
    sb.append("\nBy Level:\n");
    java.util.Map<InternshipLevel, Long> byLevel = allOpps.stream()
        .filter(o -> o.getLevel() != null)
        .collect(Collectors.groupingBy(InternshipOpportunity::getLevel, Collectors.counting()));
    for (InternshipLevel lvl : InternshipLevel.values()) {
        sb.append("  ").append(lvl).append(": ")
          .append(byLevel.getOrDefault(lvl, 0L)).append('\n');
    }

    // Application summary
    sb.append("\nApplications Summary:\n");
    sb.append("Total Applications: ").append(allApps.size()).append('\n');
    java.util.Map<EntityClass.Enums.ApplicationStatus, Long> appStatusCounts = allApps.stream()
        .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));
    for (EntityClass.Enums.ApplicationStatus as : EntityClass.Enums.ApplicationStatus.values()) {
        sb.append("  ").append(as).append(": ")
          .append(appStatusCounts.getOrDefault(as, 0L)).append('\n');
    }
    long confirmed = allApps.stream().filter(Application::isConfirmed).count();
    sb.append("Confirmed (student accepted successful offers): ").append(confirmed).append('\n');

    // Per company summary (top 5 by opportunity count)
    sb.append("\nTop Companies by Opportunity Count:\n");
    java.util.Map<String, Long> byCompany = allOpps.stream()
        .collect(Collectors.groupingBy(InternshipOpportunity::getCompanyName, Collectors.counting()));
    byCompany.entrySet().stream()
        .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed())
        .limit(5)
        .forEach(e -> sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append('\n'));

    sb.append("\nDetailed Opportunities (remaining slots):\n");
    for (InternshipOpportunity o : allOpps) {
        sb.append("- ").append(o.getTitle())
          .append(" [").append(o.getCompanyName()).append("] ")
          .append(o.getStatus()).append(" | Level=").append(o.getLevel())
          .append(" | Major=").append(o.getPreferredMajor())
          .append(" | Remaining/Cap=").append(o.getSlots()).append('/')
          .append(o.getSlotCap())
          .append("\n");
    }
    return sb.toString();
    }
}
