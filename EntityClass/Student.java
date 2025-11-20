package EntityClass;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student in the internship management portal.
 */
public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<Application> applications;

    /**
     * Creates a new student with the given details.
     */
    public Student(String userId, String name, String email, int yearOfStudy, String major) {
        super(userId, name, email);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
    }
    
    /**
     * Returns the year of study of the student.
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * Returns the major of the student.
     */
    public String getMajor() {
        return major;
    }

    /**
     * Returns the list of applications submitted by the student.
     */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * Submits a new application for the student.
     */
    public void submitApplication(Application app) {
        if (applications.size() < 3) {
            applications.add(app);
        } else {
            System.out.println("You can only apply for up to 3 internships.");
        }
    }

    /**
     * Returns a string representation of the student.
     */
    @Override
    public String toString() {
        return super.toString() + " | Year: " + yearOfStudy + " | Major: " + major;
    }
}

