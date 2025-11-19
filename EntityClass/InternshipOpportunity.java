import java.time.LocalDate;
import java.util.List;

class InternshipOpportunity {
    String opportunityID;
    String title;
    String description;
    String preferredMajor;
    InternshipLevel level;
    LocalDate openDate;
    LocalDate closeDate;
    OpportunityStatus status = OpportunityStatus.PENDING;
    Boolean visible = false;
    String companyName;
    CompanyRep Owner;
    int Slotcap;
    List<Application> applications;

    public InternshipOpportunity(String opportunityID, String title, String description, String preferredMajor,
            InternshipLevel level, LocalDate openDate, LocalDate closeDate, String companyName, CompanyRep owner, int slotcap) {
        this.opportunityID = opportunityID;
        this.title = title;
        this.description = description;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.companyName = companyName;
        this.Owner = owner;
        this.Slotcap = slotcap;
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

    public Boolean getVisible() {
        return visible;
    }

    public String getCompanyName() {
        return companyName;
    }

    public CompanyRep getOwner() {
        return Owner;
    }

    public int getSlotcap() {
        return Slotcap;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }       

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void slotsFilled() {
        if (applications.size() >= Slotcap) {
            this.status = OpportunityStatus.FILLED;
        }
    }

    public setWindows(LocalDate openDate, LocalDate closeDate) {
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    //why ah?
    
    public setBasics(String title, String description, String preferredMajor, InternshipLevel level, int slotcap) {
        this.title = title;
        this.description = description;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.Slotcap = slotcap;
    }