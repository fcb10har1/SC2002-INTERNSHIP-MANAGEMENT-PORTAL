package EntityClass;

import java.time.LocalDate;
import EntityClass.Enums.OpportunityStatus;
import EntityClass.Enums.InternshipLevel;
// every user's saved filter settings for viewing internships

public class FilterSettings {

    private OpportunityStatus status;      
    private String preferredMajor;         
    private InternshipLevel level;         
    private String orderBy = "TITLE_ASC";  
    private LocalDate closingBefore;       

    public OpportunityStatus getStatus() {
        return status;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public LocalDate getClosingBefore() {
        return closingBefore;
    }

    public void setClosingBefore(LocalDate closingBefore) {
        this.closingBefore = closingBefore;
    }

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
