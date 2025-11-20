package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import ControlClass.WithdrawalManager;
import EntityClass.Application;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;
import EntityClass.FilterSettings;
import EntityClass.Enums.ApplicationStatus;
import EntityClass.Enums.InternshipLevel;
import EntityClass.Enums.OpportunityStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * StudentCliMenu provides a command-line interface for student users
 * to interact with the internship placement management system.
 */
public class StudentCliMenu {

    private final Scanner scanner;
    private final ApplicationManager applicationManager;
    private final OpportunityManager opportunityManager;
    private final WithdrawalManager withdrawalManager;
    private FilterSettings filterSettings = new FilterSettings(); // Persists across menu navigation

    /*     
     * Constructor initializes the CLI menu with required managers and scanner.
     */
    public StudentCliMenu(Scanner scanner,
                          ApplicationManager appMgr,
                          OpportunityManager oppMgr,
                          WithdrawalManager wdMgr) {
        this.scanner = scanner;
        this.applicationManager = appMgr;
        this.opportunityManager = oppMgr;
        this.withdrawalManager = wdMgr;
    }

    /*     
     * Displays the student menu options and handles user input.
     */
    public void showOptions(Student student) {
        int choice;
        do {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View and apply for opportunities");
            System.out.println("2. View my applications");
            System.out.println("3. Accept/reject offers");
            System.out.println("4. Request withdrawal");
            System.out.println("5. Change password");
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
                case 5:
                    if (changePassword(student)) {
                        System.out.println("Password changed successfully. Please log in again with your new password.");
                        return; // Force logout
                    }
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    /*     
     * Displays available opportunities and allows the student to apply.
     */
    private void viewAndApplyOpportunities(Student student) {
        // Manage filters
        System.out.println("\n=== Filter Options ===");
        System.out.println("Current: " + filterSettings.getSummary());
        System.out.println("1. View opportunities with current filters");
        System.out.println("2. Modify filters");
        System.out.println("3. Clear all filters");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        
        String filterChoice = scanner.nextLine().trim();
        
        if ("0".equals(filterChoice)) {
            return;
        } else if ("2".equals(filterChoice)) {
            configureFilters();
        } else if ("3".equals(filterChoice)) {
            filterSettings.clearAll();
            System.out.println("✓ All filters cleared.");
        }
        
        // Get eligible opportunities
        List<InternshipOpportunity> eligibleOpps = opportunityManager.listVisibleFor(student);
        
        // Apply filters
        List<InternshipOpportunity> filteredOpps = filterSettings.apply(eligibleOpps);
        
        if (filteredOpps.isEmpty()) {
            System.out.println("\nNo eligible opportunities match your current filters.");
            return;
        }
        
        System.out.println("\n=== Eligible Internship Opportunities ===");
        System.out.println(filterSettings.getSummary());
        for (int i = 0; i < filteredOpps.size(); i++) {
            InternshipOpportunity opp = filteredOpps.get(i);
            System.out.printf("\n%d. %s%n", i + 1, opp.getTitle());
            System.out.println("   Company: " + opp.getCompanyName());
            if (opp.getOwner() != null) {
                System.out.println("   Company Rep: " + opp.getOwner().getName() + " (" + opp.getOwner().getUserId() + ")");
            }
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
        
        if (oppChoice == 0 || oppChoice < 0 || oppChoice > filteredOpps.size()) {
            return;
        }
        
        InternshipOpportunity selectedOpp = filteredOpps.get(oppChoice - 1);
        
        try {
            Application app = applicationManager.apply(student, selectedOpp);
            System.out.println("✓ Application submitted successfully!");
            System.out.println("Your application status: " + app.getStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /*     
     * Allows the student to configure filter settings for viewing opportunities.
     */
    private void configureFilters() {
        System.out.println("\n=== Configure Filters ===");
        
        System.out.print("Filter by status (Pending/Approved/Rejected/Filled, or leave blank): ");
        String statusStr = scanner.nextLine().trim();
        if (!statusStr.isEmpty()) {
            try {
                filterSettings.setStatus(OpportunityStatus.valueOf(statusStr));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status, filter not applied.");
            }
        } else {
            filterSettings.setStatus(null);
        }
        
        System.out.print("Filter by major (substring, or leave blank): ");
        String major = scanner.nextLine().trim();
        filterSettings.setPreferredMajor(major.isEmpty() ? null : major);
        
        System.out.print("Filter by level (Basic/Intermediate/Advanced, or leave blank): ");
        String levelStr = scanner.nextLine().trim();
        if (!levelStr.isEmpty()) {
            try {
                filterSettings.setLevel(InternshipLevel.valueOf(levelStr));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid level, filter not applied.");
            }
        } else {
            filterSettings.setLevel(null);
        }
        
        System.out.print("Filter by closing date before (YYYY-MM-DD, or leave blank): ");
        String dateStr = scanner.nextLine().trim();
        if (!dateStr.isEmpty()) {
            try {
                filterSettings.setClosingBefore(LocalDate.parse(dateStr));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format, filter not applied.");
            }
        } else {
            filterSettings.setClosingBefore(null);
        }
        
        // Sorting removed for simplicity (default alphabetical by title retained)
        
        System.out.println("\n✓ Filters configured: " + filterSettings.getSummary());
    }

    /*     
     * Displays the student's applications.
     */
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

    /*     
     * Allows the student to accept or reject offers.
     */
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

    /*     
     * Allows the student to request withdrawal from an application.
     */
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

    /*     
     * Allows the student to change their password.
     */
    private boolean changePassword(Student student) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine().trim();
        
        // Verify current password
        if (!student.login(student.getUserId(), currentPassword)) {
            System.out.println("Error: Current password is incorrect.");
            return false;
        }
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();
        
        if (newPassword.isEmpty()) {
            System.out.println("Error: Password cannot be empty.");
            return false;
        }
        
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Error: Passwords do not match.");
            return false;
        }
        
        student.changePassword(newPassword);
        return true;
    }
}
