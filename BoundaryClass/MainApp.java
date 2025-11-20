package BoundaryClass;

import EntityClass.User;
import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import ControlClass.ReportManager;
import ControlClass.UserManager;
import ControlClass.WithdrawalManager;
import ControlClass.AuthService;

import EntityClass.Student;
import EntityClass.CompanyRep;
import EntityClass.CareerStaff;

import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;
import RepositoryClass.IOpportunityRepository;
import RepositoryClass.OpportunityRepository;
import RepositoryClass.IApplicationRepository;
import RepositoryClass.ApplicationRepository;

import java.util.Optional;
import java.util.Scanner;

// main app using CLI
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

    public MainApp() {
        userManager = new UserManager(userRepository);
        opportunityManager = new OpportunityManager(opportunityRepository, userRepository); // assuming this ctor
        applicationManager = new ApplicationManager(applicationRepository, opportunityRepository);
        withdrawalManager = new WithdrawalManager();
        reportManager = new ReportManager(opportunityRepository, applicationRepository);

        // auth + login UI
        AuthService authService = new AuthService(userRepository);
        loginUI = new LoginUI(authService, scanner);

        // menus
        studentMenu = new StudentCliMenu(scanner, applicationManager, opportunityManager, withdrawalManager);
        companyRepMenu = new CompanyRepCliMenu(scanner, applicationManager, opportunityManager, reportManager);
        careerStaffMenu = new CareerStaffCliMenu(scanner, opportunityManager, withdrawalManager, reportManager, userManager);
    }

    public void startCLI() {
        System.out.println("Welcome to the Internship Placement Management System!");

        while (true) {
            Optional<User> loggedIn = loginUI.promptLogin();   // ✅ no args
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

            // after menu returns, loop back to login
        }
    }
}