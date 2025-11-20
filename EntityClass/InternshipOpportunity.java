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
     * Creates a new internship opportunity with the given details.
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
     */
    public String getOpportunityID() {
        return opportunityID;
    }

    /**
     * Returns the title of the internship opportunity.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the internship opportunity.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the preferred major for the internship opportunity.
     */
    public String getPreferredMajor() {
        return preferredMajor;
    }

    /**
     * Returns the internship level for the internship opportunity.
     */
    public InternshipLevel getLevel() {
        return level;
    }

    /**
     * Returns the open date for the internship opportunity.
     */
    public LocalDate getOpenDate() {
        return openDate;
    }

    /**
     * Returns the close date for the internship opportunity.
     */
    public LocalDate getCloseDate() {
        return closeDate;
    }

    /**
     * Returns the status of the internship opportunity.
     */
    public OpportunityStatus getStatus() {
        return status;
    }       
    /**
     * Returns whether the internship opportunity is visible.
     */
    public boolean getVisible() {
        return visible;
    }
    /**
     * Returns the company name offering the internship opportunity.
     */
    public String getCompanyName() {
        return companyName;
    }
    /**
     * Returns the owner (company representative) of the internship opportunity.
     */
    public CompanyRep getOwner() {
        return owner;
    }
    /**
     * Returns the slot capacity of the internship opportunity.
     */
    public int getSlotCap() {
        return slotCap;
    }
    /**
     * Returns the list of applications for the internship opportunity.
     */
    public List<Application> getApplications() {
        return applications;
    }
    /**
     * Sets the status of the internship opportunity.
     */
    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }       
    /**
     * Sets the visibility of the internship opportunity.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Checks whether the slots for the internship opportunity are filled.
     */
    public boolean slotsFilled() {
        return confirmedCount() >= slotCap;
    }
    
    /**
     * Sets the open and close dates for the internship opportunity.
     */
    public void setWindows(LocalDate openDate, LocalDate closeDate) {
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    /**
     * Sets the basic details of the internship opportunity.
     */
    public void setBasics(String title, String description, String preferredMajor, InternshipLevel level, int slotCap) {
        this.title = title;
        this.description = description;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.slotCap = slotCap;
    }

    /**
     * Returns the count of confirmed applications for the internship opportunity.
     */
    public long confirmedCount() {
        return applications.stream()
            .filter(Application::isConfirmed)
            .count();
    }

    /**
     * Adds an application to the internship opportunity.
     */
    public void addApplication(Application app) {
        applications.add(app);
    }
    
    /**
     * Returns whether the internship opportunity is visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Returns the number of available slots for the internship opportunity.
     */
    public int getSlots() {
        return slotCap - (int) confirmedCount();
}   
    /**
     * Creates a new internship opportunity with minimal details.
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