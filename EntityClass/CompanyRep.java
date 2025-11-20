package EntityClass;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a company representative in the internship management portal.
 */

public class CompanyRep extends User {
    private String companyName;
    private String department;
    private String position;
    private List<InternshipOpportunity> opportunities;
    
    public CompanyRep(String userID, String name, String email, String companyName, String department, String position) {
        super(userID, name, email);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.opportunities = new ArrayList<>();
    }

    /**
     * Returns the company name of this company representative.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company name of this company representative.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName; 
    }

    /**
     * Returns the department of this company representative.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of this company representative.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns the position of this company representative.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the position of this company representative.
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Returns the list of internship opportunities posted by this company representative.
     */
    public List<InternshipOpportunity> getOpportunities() {
        return opportunities;
    }
    
}
