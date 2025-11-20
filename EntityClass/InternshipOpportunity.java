package EntityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import EntityClass.Enums.InternshipLevel;
import EntityClass.Enums.OpportunityStatus;

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

    public String getOpportunityID() {
        return opportunityID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public boolean getVisible() {
        return visible;
    }

    public String getCompanyName() {
        return companyName;
    }

    public CompanyRep getOwner() {
        return owner;
    }

    public int getSlotCap() {
        return slotCap;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }       

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean slotsFilled() {
        return confirmedCount() >= slotCap;
    }

    public void setWindows(LocalDate openDate, LocalDate closeDate) {
        this.openDate = openDate;
        this.closeDate = closeDate;
    }
    
    public void setBasics(String title, String description, String preferredMajor, InternshipLevel level, int slotCap) {
        this.title = title;
        this.description = description;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.slotCap = slotCap;
    }

    public long confirmedCount() {
        return applications.stream()
            .filter(Application::isConfirmed)
            .count();
    }

    public void addApplication(Application app) {
        applications.add(app);
    }

    public boolean isVisible() {
        return visible;
    }

    public int getSlots() {
        return slotCap - (int) confirmedCount();
}   
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