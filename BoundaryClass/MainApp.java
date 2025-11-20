package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.AuthService;
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
        // managers
        userManager = new UserManager(userRepository);
        opportunityManager = new OpportunityManager(opportunityRepository);
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
