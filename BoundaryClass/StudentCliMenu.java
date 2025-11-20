package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import ControlClass.WithdrawalManager;
import EntityClass.Application;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.Enums.ApplicationStatus;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            System.out.println("1. View and apply for opportunities");
            System.out.println("2. View my applications");
            System.out.println("3. Accept/reject offers");
            System.out.println("4. Request withdrawal");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    viewAndApplyOpportunities(student);
                    break;
                case 2:
                    viewMyApplications(student);
                    break;
                case 3:
                    acceptRejectOffers(student);
                    break;
                case 4:
                    requestWithdrawal(student);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void viewAndApplyOpportunities(Student student) {
        List<InternshipOpportunity> eligibleOpps = opportunityManager.listVisibleFor(student);
        
        if (eligibleOpps.isEmpty()) {
            System.out.println("\nNo eligible opportunities available at this time.");
            return;
        }
        
        System.out.println("\n=== Eligible Internship Opportunities ===");
        for (int i = 0; i < eligibleOpps.size(); i++) {
            InternshipOpportunity opp = eligibleOpps.get(i);
            System.out.printf("\n%d. %s%n", i + 1, opp.getTitle());
            System.out.println("   Company: " + opp.getCompanyName());
            System.out.println("   Level: " + opp.getLevel());
            System.out.println("   Major: " + opp.getPreferredMajor());
            System.out.println("   Slots: " + opp.getSlotCap());
            System.out.println("   Open: " + opp.getOpenDate() + " | Close: " + opp.getCloseDate());
            System.out.println("   Description: " + opp.getDescription());
        }
        
        System.out.print("\nSelect opportunity to apply (0 to cancel): ");
        int oppChoice;
        try {
            oppChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (oppChoice == 0 || oppChoice < 0 || oppChoice > eligibleOpps.size()) {
            return;
        }
        
        InternshipOpportunity selectedOpp = eligibleOpps.get(oppChoice - 1);
        
        try {
            Application app = applicationManager.apply(student, selectedOpp);
            System.out.println("✓ Application submitted successfully!");
            System.out.println("Your application status: " + app.getStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewMyApplications(Student student) {
        List<Application> apps = student.getApplications();
        
        if (apps.isEmpty()) {
            System.out.println("\nYou have no applications yet.");
            return;
        }
        
        System.out.println("\n=== My Applications ===");
        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            System.out.printf("\n%d. %s at %s%n", 
                i + 1,
                app.getTarget().getTitle(),
                app.getTarget().getCompanyName()
            );
            System.out.println("   Status: " + app.getStatus());
            System.out.println("   Accepted by you: " + app.isAcceptedByStudent());
        }
    }

    private void acceptRejectOffers(Student student) {
        List<Application> successfulApps = student.getApplications().stream()
            .filter(app -> app.getStatus() == ApplicationStatus.Successful)
            .filter(app -> !app.isAcceptedByStudent())
            .collect(Collectors.toList());
        
        if (successfulApps.isEmpty()) {
            System.out.println("\nYou have no pending offers to accept/reject.");
            return;
        }
        
        System.out.println("\n=== Pending Offers ===");
        for (int i = 0; i < successfulApps.size(); i++) {
            Application app = successfulApps.get(i);
            System.out.printf("\n%d. %s at %s%n",
                i + 1,
                app.getTarget().getTitle(),
                app.getTarget().getCompanyName()
            );
            System.out.println("   Level: " + app.getTarget().getLevel());
            System.out.println("   Slots: " + app.getTarget().getSlotCap());
        }
        
        System.out.print("\nSelect offer to respond (0 to cancel): ");
        int offerChoice;
        try {
            offerChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (offerChoice == 0 || offerChoice < 0 || offerChoice > successfulApps.size()) {
            return;
        }
        
        Application selectedApp = successfulApps.get(offerChoice - 1);
        
        System.out.println("\n1. Accept offer");
        System.out.println("2. Reject offer");
        System.out.print("Choice: ");
        
        String action = scanner.nextLine().trim();
        
        try {
            if ("1".equals(action)) {
                applicationManager.studentAcceptOffer(selectedApp);
                System.out.println("✓ Offer accepted! Congratulations!");
            } else if ("2".equals(action)) {
                applicationManager.studentRejectOffer(selectedApp);
                System.out.println("✓ Offer rejected.");
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void requestWithdrawal(Student student) {
        List<Application> apps = student.getApplications();
        
        if (apps.isEmpty()) {
            System.out.println("\nYou have no applications to withdraw from.");
            return;
        }
        
        System.out.println("\n=== Your Applications ===");
        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            System.out.printf("%d. %s at %s - Status: %s%n",
                i + 1,
                app.getTarget().getTitle(),
                app.getTarget().getCompanyName(),
                app.getStatus()
            );
        }
        
        System.out.print("\nSelect application to withdraw (0 to cancel): ");
        int appChoice;
        try {
            appChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (appChoice == 0 || appChoice < 0 || appChoice > apps.size()) {
            return;
        }
        
        Application selectedApp = apps.get(appChoice - 1);
        
        System.out.print("Enter reason for withdrawal: ");
        String reason = scanner.nextLine();
        
        try {
            withdrawalManager.requestWithdrawal(student, selectedApp, reason);
            System.out.println("✓ Withdrawal request submitted successfully!");
            System.out.println("Application: " + selectedApp.getTarget().getTitle());
            System.out.println("Reason: " + reason);
            System.out.println("Career staff will review your request.");
        } catch (Exception e) {
            System.out.println("Error submitting withdrawal request: " + e.getMessage());
        }
    }
}
