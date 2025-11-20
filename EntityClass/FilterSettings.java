package EntityClass;

import java.time.LocalDate;
import java.util.List;
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
     * @return status value or null if unset
     */
    public OpportunityStatus getStatus() {
        return status;
    }

    /**
     * Sets the status filter.
     * @param status opportunity status to match (null clears)
     */
    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    /**
     * Returns the preferred major substring filter.
     * @return preferred major substring or null
     */
    public String getPreferredMajor() {
        return preferredMajor;
    }

    /**
     * Sets the preferred major substring filter.
     * @param preferredMajor major substring (case-insensitive) or null
     */
    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    /**
     * Returns the internship level filter.
     * @return internship level or null
     */
    public InternshipLevel getLevel() {
        return level;
    }

    /**
     * Sets the internship level filter.
     * @param level level to match (null clears)
     */
    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    /**
     * Returns the order by directive (e.g. TITLE_ASC).
     * @return orderBy directive string
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the order by directive.
     * @param orderBy directive (TITLE/COMPANY/LEVEL/CLOSEDATE + _ASC/_DESC)
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * Returns the closing-before date filter.
     * @return date or null if unset
     */
    public LocalDate getClosingBefore() {
        return closingBefore;
    }

    /**
     * Sets the closing-before date filter.
     * @param closingBefore date before which closing date must fall (null clears)
     */
    public void setClosingBefore(LocalDate closingBefore) {
        this.closingBefore = closingBefore;
    }

    /**
     * Returns a string representation of the filter settings.
     * @return summary string
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
     * Applies all active filters and sorting to the provided list of opportunities.
     * @param opportunities source list
     * @return filtered and sorted list
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
    
    /**
     * Creates a comparator based on orderBy field (TITLE/COMPANY/LEVEL/CLOSEDATE with ASC/DESC).
     * Defaults to TITLE_ASC if orderBy is null or invalid.
     * @return comparator for sorting opportunities
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
     * Clears all filter settings, restoring defaults.
     */
    public void clearAll() {
        status = null;
        preferredMajor = null;
        level = null;
        closingBefore = null;
        orderBy = "TITLE_ASC";
    }
    
    /**
     * Checks if any filters other than default sorting are active.
     * @return true if at least one filter value set
     */
    public boolean hasActiveFilters() {
        return status != null || 
               (preferredMajor != null && !preferredMajor.isEmpty()) || 
               level != null || 
               closingBefore != null;
    }
    
    /**
     * Returns a user-friendly summary of active filters.
     * @return summary of active filters and sort
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
