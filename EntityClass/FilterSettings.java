package EntityClass;

import java.time.LocalDate;
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
}
