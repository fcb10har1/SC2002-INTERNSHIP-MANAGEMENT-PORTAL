package BoundaryClass;

import ControlClass.OpportunityManager;
import ControlClass.ReportManager;
import ControlClass.UserManager;
import ControlClass.WithdrawalManager;
import EntityClass.CareerStaff;

import java.util.Scanner;

public class CareerStaffCliMenu {

    private final Scanner scanner;
    private final OpportunityManager opportunityManager;
    private final WithdrawalManager withdrawalManager;
    private final ReportManager reportManager;
    private final UserManager userManager;

    public CareerStaffCliMenu(Scanner scanner,
                              OpportunityManager oppMgr,
                              WithdrawalManager wdMgr,
                              ReportManager reportMgr,
                              UserManager userMgr) {
        this.scanner = scanner;
        this.opportunityManager = oppMgr;
        this.withdrawalManager = wdMgr;
        this.reportManager = reportMgr;
        this.userManager = userMgr;
    }

    public void showOptions(CareerStaff staff) {
        int choice;
        do {
            System.out.println("\n=== Career Staff Menu ===");
            System.out.println("1. Approve/reject company representatives");
            System.out.println("2. Approve/reject opportunities");
            System.out.println("3. Approve/reject withdrawal requests");
            System.out.println("4. Generate reports");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    // call userManager.approveCompanyRep()
                    break;
                case 2:
                    // call opportunityManager.approve() OR reject()
                    break;
                case 3:
                    // use withdrawalManager.approveWithdrawal() ORRRR rejectWithdrawal()
                    break;
                case 4:
                    // reuse ReportManager 
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