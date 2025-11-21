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
 * It initializes repositories, managers, and user interfaces, imports users from CSV files for students and career staff,
 * and starts the main interaction loop. Company representatives are registered interactively by user input.
 */
public class MainApp {

    /**
     * Entry point for the application.
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        new MainApp().startCLI();
    }

    private final Scanner scanner = new Scanner(System.in);

    // repositories
    private final IUserRepository userRepository = new UserRepository();
    private final IOpportunityRepository opportunityRepository = new OpportunityRepository();
    private final IApplicationRepository applicationRepository = new ApplicationRepository();

    // boundaries
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

    /**
     * Constructs MainApp initializing all repositories, managers, UIs, and imports users from CSV files.
     * Students and career staff are loaded from CSV files; company representatives are registered interactively.
     */
    public MainApp() {
        // managers
        userManager = new UserManager(userRepository);
        opportunityManager = new OpportunityManager(opportunityRepository);
        applicationManager = new ApplicationManager(applicationRepository, opportunityRepository);
        withdrawalManager = new WithdrawalManager(opportunityRepository, applicationRepository);
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
        
        // Company representatives start empty - they register through the UI
        System.out.println("✓ Company representatives: 0 (will be registered through UI)");
        
        System.out.println();
    }



    /**
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
