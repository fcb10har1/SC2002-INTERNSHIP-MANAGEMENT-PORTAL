package EntityClass;

import java.util.List;
import java.util.ArrayList;

public class CompanyRep extends User {
    private String companyName;
    private String department;
    private String position;
    private List<InternshipOpportunity> opportunities;
    
    // shd opportunities be here in constructor?
    public CompanyRep(String userID, String name, String companyName, String department, String position) {
        super(userID, name);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.opportunities = new ArrayList<>();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName; 
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<InternshipOpportunity> getOpportunities() {
        return opportunities;
    }
    
}
