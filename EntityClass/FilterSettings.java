package EntityClass;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import EntityClass.Enums.OpportunityStatus;
import EntityClass.Enums.InternshipLevel;

/**
 * Represents filter settings for querying internship opportunities.
 */

public class FilterSettings {

    private OpportunityStatus status;      
    private String preferredMajor;         
    private InternshipLevel level;         
    private String orderBy = "TITLE_ASC";  
    private LocalDate closingBefore;       

    /**
     * Returns the status filter.
     */
    public OpportunityStatus getStatus() {
        return status;
    }

    /**
     * Sets the status filter.
     */
    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    /**
     * Returns the preferred major filter.
     */
    public String getPreferredMajor() {
        return preferredMajor;
    }

    /**
     * Sets the preferred major filter.
     */
    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    /**
     * Returns the internship level filter.
     */
    public InternshipLevel getLevel() {
        return level;
    }

    /**
     * Sets the internship level filter.
     */
    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    /**
     * Returns the order by filter.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the order by filter.
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * Returns the closing before date filter.
     */
    public LocalDate getClosingBefore() {
        return closingBefore;
    }

    /**
     * Sets the closing before date filter.
     */
    public void setClosingBefore(LocalDate closingBefore) {
        this.closingBefore = closingBefore;
    }

    /**  
     * Returns a string representation of the filter settings.
     */
    @Override
    public String toString() {
        return "FilterSettings{" +
                "status=" + status +
                ", preferredMajor='" + preferredMajor + '\'' +
                ", level=" + level +
                ", orderBy='" + orderBy + '\'' +
                ", closingBefore=" + closingBefore +
                '}';
    }
    
    /**
     * Apply all filters and sorting to a list of opportunities.
     */
    public List<InternshipOpportunity> apply(List<InternshipOpportunity> opportunities) {
        return opportunities.stream()
            .filter(opp -> status == null || opp.getStatus() == status)
            .filter(opp -> preferredMajor == null || preferredMajor.isEmpty() || 
                          (opp.getPreferredMajor() != null && opp.getPreferredMajor().toLowerCase().contains(preferredMajor.toLowerCase())))
            .filter(opp -> level == null || opp.getLevel() == level)
            .filter(opp -> closingBefore == null || 
                          (opp.getCloseDate() != null && !opp.getCloseDate().isAfter(closingBefore)))
            .sorted(getComparator())
            .collect(java.util.stream.Collectors.toList());
    }
    
    /*
     * Creates a comparator based on orderBy field (TITLE/COMPANY/LEVEL/CLOSEDATE with ASC/DESC).
     * Defaults to TITLE_ASC if orderBy is null or invalid.
     */
    private java.util.Comparator<InternshipOpportunity> getComparator() {
        if (orderBy == null) orderBy = "TITLE_ASC";
        switch (orderBy.toUpperCase()) {
            case "TITLE_ASC":
                return java.util.Comparator.comparing(InternshipOpportunity::getTitle);
            case "TITLE_DESC":
                return java.util.Comparator.comparing(InternshipOpportunity::getTitle).reversed();
            case "COMPANY_ASC":
                return java.util.Comparator.comparing(InternshipOpportunity::getCompanyName);
            case "COMPANY_DESC":
                return java.util.Comparator.comparing(InternshipOpportunity::getCompanyName).reversed();
            case "LEVEL_ASC":
                return java.util.Comparator.comparing(InternshipOpportunity::getLevel);
            case "LEVEL_DESC":
                return java.util.Comparator.comparing(InternshipOpportunity::getLevel).reversed();
            case "CLOSEDATE_ASC":
                return java.util.Comparator.comparing(InternshipOpportunity::getCloseDate, 
                    java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
            case "CLOSEDATE_DESC":
                return java.util.Comparator.comparing(InternshipOpportunity::getCloseDate, 
                    java.util.Comparator.nullsFirst(java.util.Comparator.reverseOrder()));
            default:
                return java.util.Comparator.comparing(InternshipOpportunity::getTitle);
        }
    }
    
    /**
     * Clears all filter settings.
     */
    public void clearAll() {
        status = null;
        preferredMajor = null;
        level = null;
        closingBefore = null;
        orderBy = "TITLE_ASC";
    }
    
    /**
     * Checks if any filters are active.
     */
    public boolean hasActiveFilters() {
        return status != null || 
               (preferredMajor != null && !preferredMajor.isEmpty()) || 
               level != null || 
               closingBefore != null;
    }
    
    /**
     * Returns a user-friendly summary of active filters.
     */
    public String getSummary() {
        if (!hasActiveFilters()) {
            return "No filters active | Sort by: " + orderBy;
        }
        StringBuilder sb = new StringBuilder("Active Filters: ");
        if (status != null) sb.append("Status=").append(status).append(", ");
        if (preferredMajor != null && !preferredMajor.isEmpty()) sb.append("Major contains '").append(preferredMajor).append("', ");
        if (level != null) sb.append("Level=").append(level).append(", ");
        if (closingBefore != null) sb.append("Closing before ").append(closingBefore).append(", ");
        sb.setLength(sb.length() - 2); // Remove trailing comma
        sb.append(" | Sort by: ").append(orderBy);
        return sb.toString();
    }
}
