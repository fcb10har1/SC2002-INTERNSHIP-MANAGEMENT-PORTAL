package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.AuthService;
import ControlClass.CSVImporter;
import ControlClass.OpportunityManager;
import ControlClass.ReportManager;
import ControlClass.UserManager;
import ControlClass.WithdrawalManager;
import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import EntityClass.Student;
import EntityClass.User;
import RepositoryClass.IApplicationRepository;
import RepositoryClass.IOpportunityRepository;
import RepositoryClass.IUserRepository;
import RepositoryClass.ApplicationRepository;
import RepositoryClass.OpportunityRepository;
import RepositoryClass.UserRepository;

import java.util.Optional;
import java.util.Scanner;

/**
 * MainApp is the entry point for the Internship Placement Management System CLI application.
 * It initializes repositories, managers, and user interfaces, and starts the main interaction loop.
 */
public class MainApp {

    public static void main(String[] args) {
        new MainApp().startCLI();
    }

    private final Scanner scanner = new Scanner(System.in);

    // repositories
    private final IUserRepository userRepository = new UserRepository();
    private final IOpportunityRepository opportunityRepository = new OpportunityRepository();
    private final IApplicationRepository applicationRepository = new ApplicationRepository();

    // boundaries
    private final DataStore dataStore = new DataStore();
    private final LoginUI loginUI;
    private final StudentCliMenu studentMenu;
    private final CompanyRepCliMenu companyRepMenu;
    private final CareerStaffCliMenu careerStaffMenu;

    // managers
    private final UserManager userManager;
    private final OpportunityManager opportunityManager;
    private final ApplicationManager applicationManager;
    private final WithdrawalManager withdrawalManager;
    private final ReportManager reportManager;

    /*     
     * Constructor initializes repositories, managers, UIs, and imports users from CSV files.
     */
    public MainApp() {
        // managers
        userManager = new UserManager(userRepository);
        opportunityManager = new OpportunityManager(opportunityRepository);
        applicationManager = new ApplicationManager(applicationRepository, opportunityRepository);
        withdrawalManager = new WithdrawalManager();
        reportManager = new ReportManager(opportunityRepository, applicationRepository);

        // auth + login UI
        AuthService authService = new AuthService(userRepository);
        loginUI = new LoginUI(authService, userManager, scanner);

        // menus
        studentMenu = new StudentCliMenu(scanner, applicationManager, opportunityManager, withdrawalManager);
    companyRepMenu = new CompanyRepCliMenu(scanner, applicationManager, opportunityManager);
        careerStaffMenu = new CareerStaffCliMenu(scanner, opportunityManager, withdrawalManager, reportManager, userManager);

        // Import users from CSV files
        importUsersFromCSV();
        
        // seed additional demo users for testing (if CSV import fails or for testing)
        seedDemoUsers();
    }

    /*     
     * Imports users from predefined CSV files using CSVImporter.
     */
    private void importUsersFromCSV() {
        System.out.println("=== Importing Users from CSV Files ===");
        
        // Import students
        int studentCount = CSVImporter.importStudents("students.csv", userRepository);
        System.out.println("✓ Imported " + studentCount + " students from students.csv");
        
        // Import career staff
        int staffCount = CSVImporter.importCareerStaff("careerstaff.csv", userRepository);
        System.out.println("✓ Imported " + staffCount + " career staff from careerstaff.csv");
        
        // Import company representatives
        int repCount = CSVImporter.importCompanyReps("companyreps.csv", userRepository);
        System.out.println("✓ Imported " + repCount + " company representatives from companyreps.csv");
        
        System.out.println();
    }

    /*     
     * Seeds demo users into the user repository for testing purposes.
     */
    private void seedDemoUsers() {
        // Create demo users with email - all with default password "password"
        // Only add if they don't already exist (to avoid duplicates from CSV)
        
        // Student ID format: U followed by 7 digits and ends with a letter (e.g., U2345123F)
        if (!userRepository.findById("U9999999Z").isPresent()) {
            Student student = new Student("U9999999Z", "Alice Demo Student", "alice.demo@e.ntu.edu.sg", 2, "Computer Science");
            userRepository.add(student);
            System.out.println("Demo student created: ID=U9999999Z, password=password");
        }
        
        // Company Representative ID is their company email address
        if (!userRepository.findById("demo.rep@techcorp.com").isPresent()) {
            CompanyRep companyRep = new CompanyRep("demo.rep@techcorp.com", "Bob Demo CompanyRep", "demo.rep@techcorp.com", "TechCorp", "Engineering", "HR Manager");
            userRepository.add(companyRep);
            System.out.println("Demo CompanyRep created: ID=demo.rep@techcorp.com, password=password");
        }
        
        // Career Center Staff's ID is their NTU account
        if (!userRepository.findById("demo001").isPresent()) {
            CareerStaff careerStaff = new CareerStaff("demo001", "Carol Demo Staff", "demo001@ntu.edu.sg", "Career Services");
            userRepository.add(careerStaff);
            System.out.println("Demo CareerStaff created: ID=demo001, password=password");
        }
        
        System.out.println();
    }

    /*     
     * Starts the main CLI interaction loop.
     */
    public void startCLI() {
        System.out.println("Welcome to the Internship Placement Management System!");

        while (true) {
            Optional<User> loggedIn = loginUI.promptLogin();  // no param
            if (!loggedIn.isPresent()) {
                System.out.println("Exiting system.");
                break;
            }

            User user = loggedIn.get();
            System.out.println("Hello, " + user.getUserId());

            if (user instanceof Student) {
                studentMenu.showOptions((Student) user);
            } else if (user instanceof CompanyRep) {
                companyRepMenu.showOptions((CompanyRep) user);
            } else if (user instanceof CareerStaff) {
                careerStaffMenu.showOptions((CareerStaff) user);
            }
            // then loop back to login
        }
    }
}
