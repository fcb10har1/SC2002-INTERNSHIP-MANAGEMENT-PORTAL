package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import ControlClass.ReportManager;
import ControlClass.UserManager;
import ControlClass.WithdrawalManager;
import EntityClass.User;

import java.util.Optional;
import java.util.Scanner;

// main app using CLI
public class MainApp {

    public static void main(String[] args) {
        new MainApp().startCLI();
    }

    private final Scanner scanner = new Scanner(System.in);

    // boundaries
    private final DataStore dataStore = new DataStore();
    private final LoginUI loginUI = new LoginUI(scanner);               
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
        userManager = new UserManager(/*userRepo*/);
        opportunityManager = new OpportunityManager(/*oppRepo, userRepo*/);
        applicationManager = new ApplicationManager(/*appRepo, oppRepo*/);
        withdrawalManager = new WithdrawalManager();
        reportManager = new ReportManager(/*oppRepo, appRepo*/);

        studentMenu = new StudentCliMenu(scanner, applicationManager, opportunityManager, withdrawalManager);
        companyRepMenu = new CompanyRepCliMenu(scanner, applicationManager, opportunityManager, reportManager);
        careerStaffMenu = new CareerStaffCliMenu(scanner, opportunityManager, withdrawalManager, reportManager, userManager);
    }

    public void startCLI() {
        System.out.println("Welcome to the Internship Placement Management System!");

        while (true) {
            Optional<User> loggedIn = loginUI.promptLogin(userManager);
            if (!loggedIn.isPresent()) {
                System.out.println("Exiting system.");
                break;
            }

            User user = loggedIn.get();
            System.out.println("Hello, " + user.getUserId());

            if (user instanceof EntityClass.Student) {
                studentMenu.showOptions((EntityClass.Student) user);
            } else if (user instanceof EntityClass.CompanyRep) {
                companyRepMenu.showOptions((EntityClass.CompanyRep) user);
            } else if (user instanceof EntityClass.CareerStaff) {
                careerStaffMenu.showOptions((EntityClass.CareerStaff) user);
            }

            // after menu returns, loop back to login
        }
    }
}
