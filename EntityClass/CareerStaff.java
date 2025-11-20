package EntityClass;

/**
 * Represents a career staff member in the internship management portal.
 */
public class CareerStaff extends User {
    private String department;

    /**
     * Creates a new career staff member with the given details.
     */
    public CareerStaff(String userID, String name, String email, String department) {
        super(userID, name, email);
        this.department = department;
    }

    /**
     * Returns the department of this career staff member.
     */
    
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of this career staff member.
     */

    public void setDepartment(String department) {
        this.department = department;
    }
    
}
