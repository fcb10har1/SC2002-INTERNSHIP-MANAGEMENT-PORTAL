package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import ControlClass.ReportManager;
import EntityClass.CompanyRep;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CompanyRepCliMenu {

    private final Scanner scanner;
    private final ApplicationManager applicationManager;
    private final OpportunityManager opportunityManager;
    private final ReportManager reportManager;

    public CompanyRepCliMenu(Scanner scanner,
                             ApplicationManager appMgr,
                             OpportunityManager oppMgr,
                             ReportManager reportMgr) {
        this.scanner = scanner;
        this.applicationManager = appMgr;
        this.opportunityManager = oppMgr;
        this.reportManager = reportMgr;
    }

    public void showOptions(CompanyRep rep) {
        int choice;
        do {
            System.out.println("\n=== Company Representative Menu ===");
            System.out.println("1. List my opportunities");
            System.out.println("2. Review applications for an opportunity");
            System.out.println("3. Generate simple report for my company");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    break;
                case 2:
                    System.out.print("Enter opportunity ID: ");
                    String oppId = scanner.nextLine();
                    break;
                case 3:
                    Map<String, String> filters = new HashMap<>();
                    filters.put("company", rep.getCompanyName());
                    String report = reportManager.generateReport(filters);
                    System.out.println(report);
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
