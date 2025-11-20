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
     * Creates a new student.
     * @param userId unique id
     * @param name student name
     * @param email email address
     * @param yearOfStudy academic year
     * @param major major field of study
     */
    public Student(String userId, String name, String email, int yearOfStudy, String major) {
        super(userId, name, email);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
    }
    
    /**
     * Returns the year of study of the student.
     * @return year of study
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * Returns the major of the student.
     * @return major string
     */
    public String getMajor() {
        return major;
    }

    /**
     * Returns applications submitted by the student.
     * @return list of applications
     */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * Submits new application (max 3 enforced).
     * @param app application instance
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
     * @return formatted string
     */
    @Override
    public String toString() {
        return super.toString() + " | Year: " + yearOfStudy + " | Major: " + major;
    }
}

