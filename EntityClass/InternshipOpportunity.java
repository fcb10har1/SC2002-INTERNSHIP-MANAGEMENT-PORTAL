package EntityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import EntityClass.Enums.InternshipLevel;
import EntityClass.Enums.OpportunityStatus;

/**
 * Represents an internship opportunity in the internship management portal.
 */

public class InternshipOpportunity {
    private String opportunityID;
    private String title;
    private String description;
    private String preferredMajor;
    private InternshipLevel level;
    private LocalDate openDate;
    private LocalDate closeDate;
    private OpportunityStatus status = OpportunityStatus.Pending;
    private boolean visible = false;
    private String companyName;
    private CompanyRep owner;
    private int slotCap;
    private List<Application> applications = new ArrayList<>();

    /**
     * Creates a new internship opportunity with full details.
     * @param opportunityID unique id
     * @param title title
     * @param description description
     * @param preferredMajor preferred major or null
     * @param level internship level
     * @param openDate opening date
     * @param closeDate closing date
     * @param companyName company name
     * @param owner owner representative
     * @param slotCap slot capacity
     */
    public InternshipOpportunity(String opportunityID, String title, String description, String preferredMajor,
            InternshipLevel level, LocalDate openDate, LocalDate closeDate, String companyName, CompanyRep owner, int slotCap) {
        this.opportunityID = opportunityID;
        this.title = title;
        this.description = description;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.companyName = companyName;
        this.owner = owner;
        this.slotCap = slotCap;
    }

    /**
     * Returns the opportunity ID.
     * @return id string
     */
    public String getOpportunityID() {
        return opportunityID;
    }

    /**
     * Returns title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns preferred major.
     * @return preferred major or null
     */
    public String getPreferredMajor() {
        return preferredMajor;
    }

    /**
     * Returns internship level.
     * @return level enum or null
     */
    public InternshipLevel getLevel() {
        return level;
    }

    /**
     * Returns opening date.
     * @return opening date or null
     */
    public LocalDate getOpenDate() {
        return openDate;
    }

    /**
     * Returns closing date.
     * @return closing date or null
     */
    public LocalDate getCloseDate() {
        return closeDate;
    }

    /**
     * Returns status.
     * @return opportunity status
     */
    public OpportunityStatus getStatus() {
        return status;
    }       
    /**
     * Returns whether opportunity is visible.
     * @return true if visible
     */
    public boolean getVisible() {
        return visible;
    }
    /**
     * Returns company name.
     * @return company name
     */
    public String getCompanyName() {
        return companyName;
    }
    /**
     * Returns owner representative.
     * @return owner
     */
    public CompanyRep getOwner() {
        return owner;
    }
    /**
     * Returns slot capacity.
     * @return slot cap integer
     */
    public int getSlotCap() {
        return slotCap;
    }
    /**
     * Returns applications linked to this opportunity.
     * @return list of applications
     */
    public List<Application> getApplications() {
        return applications;
    }
    /**
     * Sets status.
     * @param status new status value
     */
    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }       
    /**
     * Sets visibility.
     * @param visible new visibility flag
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Determines if all slots are filled.
     * @return true if confirmed applications reach cap
     */
    public boolean slotsFilled() {
        return confirmedCount() >= slotCap;
    }
    
    /**
     * Sets window dates.
     * @param openDate opening date
     * @param closeDate closing date
     */
    public void setWindows(LocalDate openDate, LocalDate closeDate) {
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    /**
     * Sets basic properties.
     * @param title title
     * @param description description
     * @param preferredMajor preferred major
     * @param level level
     * @param slotCap slot capacity
     */
    public void setBasics(String title, String description, String preferredMajor, InternshipLevel level, int slotCap) {
        this.title = title;
        this.description = description;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.slotCap = slotCap;
    }

    /**
     * Counts confirmed applications.
     * @return count of confirmed apps
     */
    public long confirmedCount() {
        return applications.stream()
            .filter(Application::isConfirmed)
            .count();
    }

    /**
     * Adds an application.
     * @param app application instance
     */
    public void addApplication(Application app) {
        applications.add(app);
    }
    
    /**
     * Returns visibility state.
     * @return true if visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Returns remaining available slots.
     * @return available slot count
     */
    public int getSlots() {
        return slotCap - (int) confirmedCount();
}   
    /**
     * Creates a new internship opportunity with minimal draft details.
     * @param owner owner representative
     * @param companyName company name
     * @param slotCap slot capacity
     */
    public InternshipOpportunity(CompanyRep owner, String companyName, int slotCap) {
        this.opportunityID = java.util.UUID.randomUUID().toString();
        this.title = "";
        this.description = "";
        this.preferredMajor = null;
        this.level = null;
        this.openDate = null;
        this.closeDate = null;
        this.companyName = companyName;
        this.owner = owner;
        this.slotCap = slotCap;
}   

}