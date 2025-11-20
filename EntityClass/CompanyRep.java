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
    
    /**
     * Creates a new company representative.
     * @param userID unique id
     * @param name name
     * @param email email
     * @param companyName company name
     * @param department department name
     * @param position job position
     */
    public CompanyRep(String userID, String name, String email, String companyName, String department, String position) {
        super(userID, name, email);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.opportunities = new ArrayList<>();
    }

    /**
     * Returns company name.
     * @return company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets company name.
     * @param companyName company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName; 
    }

    /**
     * Returns department.
     * @return department string
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets department.
     * @param department department name
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns position.
     * @return job title
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets position.
     * @param position job title
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Returns posted opportunities.
     * @return list of owned opportunities
     */
    public List<InternshipOpportunity> getOpportunities() {
        return opportunities;
    }
    
}
