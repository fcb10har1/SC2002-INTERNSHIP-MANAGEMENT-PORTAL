package EntityClass;

/**
 * Represents a career staff member in the internship management portal.
 */
public class CareerStaff extends User {
    private String department;

    /**
     * Creates a new career staff member.
     * @param userID unique id
     * @param name staff name
     * @param email email
     * @param department department name
     */
    public CareerStaff(String userID, String name, String email, String department) {
        super(userID, name, email);
        this.department = department;
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
    
}
