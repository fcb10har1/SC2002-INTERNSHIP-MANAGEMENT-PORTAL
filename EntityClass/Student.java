package EntityClass;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<Application> applications;

    public Student(String userId, String name, int yearOfStudy, String major) {
        super(userId, name);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public List<Application> getApplications() {
        return applications;
    }

    // Add a new application (max 3 applications allowed)
    public void submitApplication(Application app) {
        if (applications.size() < 3) {
            applications.add(app);
        } else {
            System.out.println("You can only apply for up to 3 internships.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " | Year: " + yearOfStudy + " | Major: " + major;
    }
}

