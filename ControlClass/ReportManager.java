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

    // Filtered composite report with aggregates limited to filtered subset
    public String generateFilteredCompositeReport(Map<String,String> filters) {
        List<InternshipOpportunity> base = opportunityRepository.all();
        java.util.stream.Stream<InternshipOpportunity> stream = base.stream();

        if (filters.containsKey("status")) {
            try {
                OpportunityStatus st = OpportunityStatus.valueOf(filters.get("status"));
                stream = stream.filter(o -> o.getStatus() == st);
            } catch (IllegalArgumentException ignored) {}
        }
        if (filters.containsKey("major")) {
            String major = filters.get("major").toLowerCase();
            stream = stream.filter(o -> o.getPreferredMajor() != null && o.getPreferredMajor().toLowerCase().contains(major));
        }
        if (filters.containsKey("company")) {
            String company = filters.get("company").toLowerCase();
            stream = stream.filter(o -> o.getCompanyName() != null && o.getCompanyName().toLowerCase().contains(company));
        }
        if (filters.containsKey("title")) {
            String title = filters.get("title").toLowerCase();
            stream = stream.filter(o -> o.getTitle() != null && o.getTitle().toLowerCase().contains(title));
        }
        if (filters.containsKey("level")) {
            try {
                InternshipLevel lvl = InternshipLevel.valueOf(filters.get("level"));
                stream = stream.filter(o -> o.getLevel() == lvl);
            } catch (IllegalArgumentException ignored) {}
        }
        if (filters.containsKey("placement")) {
            String placement = filters.get("placement").toLowerCase();
            if ("filled".equals(placement)) {
                stream = stream.filter(InternshipOpportunity::slotsFilled);
            } else if ("open".equals(placement)) {
                stream = stream.filter(o -> !o.slotsFilled());
            }
        }

        List<InternshipOpportunity> filtered = stream.collect(Collectors.toList());

        // Applications limited to those whose opportunity is in filtered set
        List<Application> apps = applicationRepository.all().stream()
                .filter(a -> filtered.contains(a.getTarget()))
                .collect(Collectors.toList());
        if (filters.containsKey("appStatus")) {
            String raw = filters.get("appStatus");
            try {
                EntityClass.Enums.ApplicationStatus desired = EntityClass.Enums.ApplicationStatus.valueOf(raw);
                apps = apps.stream().filter(a -> a.getStatus() == desired).collect(Collectors.toList());
            } catch (IllegalArgumentException ignored) {}
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Filtered Internship Report ===\n");
    sb.append("Applied Filters: ").append(filters.isEmpty() ? "(none)" : filters.toString()).append('\n');
    sb.append("Filtered Opportunity Count: ").append(filtered.size()).append('\n');

        // Status counts within filtered set
    java.util.Map<OpportunityStatus, Long> statusCounts = filtered.stream()
                .collect(Collectors.groupingBy(InternshipOpportunity::getStatus, Collectors.counting()));
        for (OpportunityStatus st : OpportunityStatus.values()) {
            sb.append("  ").append(st).append(": ")
              .append(statusCounts.getOrDefault(st, 0L)).append('\n');
        }

    long filled = filtered.stream().filter(InternshipOpportunity::slotsFilled).count();
    long open = filtered.size() - filled;
        sb.append("Filled: ").append(filled).append(" | Open: ").append(open).append('\n');

        // Major distribution
        sb.append("\nBy Preferred Major:\n");
    java.util.Map<String, Long> byMajor = filtered.stream()
                .collect(Collectors.groupingBy(o -> o.getPreferredMajor() == null ? "(Unspecified)" : o.getPreferredMajor(), Collectors.counting()));
        byMajor.forEach((m,c) -> sb.append("  ").append(m).append(": ").append(c).append('\n'));

        // Level distribution
        sb.append("\nBy Level:\n");
    java.util.Map<InternshipLevel, Long> byLevel = filtered.stream()
                .filter(o -> o.getLevel() != null)
                .collect(Collectors.groupingBy(InternshipOpportunity::getLevel, Collectors.counting()));
        for (InternshipLevel lvl : InternshipLevel.values()) {
            sb.append("  ").append(lvl).append(": ")
              .append(byLevel.getOrDefault(lvl, 0L)).append('\n');
        }

        // Application summary within filtered set
        sb.append("\nApplications Summary (filtered):\n");
        sb.append("Total Applications: ").append(apps.size()).append('\n');
        java.util.Map<EntityClass.Enums.ApplicationStatus, Long> appStatusCounts = apps.stream()
                .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));
        for (EntityClass.Enums.ApplicationStatus as : EntityClass.Enums.ApplicationStatus.values()) {
            sb.append("  ").append(as).append(": ")
              .append(appStatusCounts.getOrDefault(as, 0L)).append('\n');
        }
        long confirmed = apps.stream().filter(Application::isConfirmed).count();
        sb.append("Confirmed (student accepted successful offers): ").append(confirmed).append('\n');

        // Detailed list
        sb.append("\nDetailed Opportunities (remaining slots):\n");
                for (InternshipOpportunity o : filtered) {
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
