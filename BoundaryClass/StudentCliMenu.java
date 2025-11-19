package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import ControlClass.WithdrawalManager;
import EntityClass.Application;
import EntityClass.Student;

import java.util.List;
import java.util.Scanner;

public class StudentCliMenu {

    private final Scanner scanner;
    private final ApplicationManager applicationManager;
    private final OpportunityManager opportunityManager;
    private final WithdrawalManager withdrawalManager;

    public StudentCliMenu(Scanner scanner,
                          ApplicationManager appMgr,
                          OpportunityManager oppMgr,
                          WithdrawalManager wdMgr) {
        this.scanner = scanner;
        this.applicationManager = appMgr;
        this.opportunityManager = oppMgr;
        this.withdrawalManager = wdMgr;
    }

    public void showOptions(Student student) {
        int choice;
        do {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View eligible opportunities");
            System.out.println("2. View my applications");
            System.out.println("3. Request withdrawal");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    break;
                case 2:
                    List<Application> apps = student.getApplications();
                    apps.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("Enter application ID to withdraw: ");
                    String appId = scanner.nextLine();
                    System.out.print("Enter reason: ");
                    String reason = scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }
}