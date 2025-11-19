package EntityClass;

public class CareerStaff extends User {
    private String department;

    public CareerStaff(String userID, String name, String department) {
        super(userID, name);
        this.department = department;
    }

    //Unsure if we need get department and set department methods
    
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
}
