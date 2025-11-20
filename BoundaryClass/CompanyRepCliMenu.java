package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Application;
import EntityClass.Student;
import EntityClass.FilterSettings;
import EntityClass.Enums.InternshipLevel;
import EntityClass.Enums.OpportunityStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/*
 * CLI menu for Company Representative users to manage internship opportunities.
 */
public class CompanyRepCliMenu {

    private final Scanner scanner;
    private final ApplicationManager applicationManager;
    private final OpportunityManager opportunityManager;
    private FilterSettings filterSettings = new FilterSettings(); // Persists across menu navigation

    public CompanyRepCliMenu(Scanner scanner,
                             ApplicationManager appMgr,
                             OpportunityManager oppMgr) {
        this.scanner = scanner;
        this.applicationManager = appMgr;
        this.opportunityManager = oppMgr;
    }

    /*
     * Displays the Company Representative menu and handles user input.
     */
    public void showOptions(CompanyRep rep) {
        int choice;
        do {
            System.out.println("\n=== Company Representative Menu ===");
            System.out.println("1. Create new internship opportunity");
            System.out.println("2. List my opportunities");
            System.out.println("3. Edit opportunity");
            System.out.println("4. Toggle opportunity visibility");
            System.out.println("5. Review applications for an opportunity");
            System.out.println("6. Delete opportunity");
            System.out.println("7. Change password");
            // Report generation removed (career staff only)
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    createOpportunity(rep);
                    break;
                case 2:
                    listMyOpportunities(rep);
                    break;
                case 3:
                    editOpportunity(rep);
                    break;
                case 4:
                    toggleVisibility(rep);
                    break;
                case 5:
                    reviewApplications(rep);
                    break;
                case 6:
                    deleteOpportunity(rep);
                    break;
                case 7:
                    if (changePassword(rep)) {
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
     * Create a new internship opportunity.
     */
    private void createOpportunity(CompanyRep rep) {
        // Check if rep has reached max opportunities (5)
        List<InternshipOpportunity> myOpps = opportunityManager.listOwnedOpps(rep);
        if (myOpps.size() >= 5) {
            System.out.println("Error: You have reached the maximum of 5 opportunities.");
            return;
        }

        System.out.println("\n=== Create New Internship Opportunity ===");
        
        System.out.print("Internship Title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Internship Level (Basic/Intermediate/Advanced): ");
        String levelStr = scanner.nextLine().trim();
        InternshipLevel level;
        try {
            level = InternshipLevel.valueOf(levelStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid level. Using Basic as default.");
            level = InternshipLevel.Basic;
        }
        
        System.out.print("Preferred Major (e.g., Computer Science, EEE, MAE): ");
        String preferredMajor = scanner.nextLine().trim();
        
        System.out.print("Application Opening Date (YYYY-MM-DD): ");
        String openDateStr = scanner.nextLine().trim();
        LocalDate openDate;
        try {
            openDate = LocalDate.parse(openDateStr);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Using today as default.");
            openDate = LocalDate.now();
        }
        
        System.out.print("Application Closing Date (YYYY-MM-DD): ");
        String closeDateStr = scanner.nextLine().trim();
        LocalDate closeDate;
        try {
            closeDate = LocalDate.parse(closeDateStr);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Using 30 days from now as default.");
            closeDate = LocalDate.now().plusDays(30);
        }
        
        System.out.print("Number of slots (max 10): ");
        int slots;
        try {
            slots = Integer.parseInt(scanner.nextLine().trim());
            if (slots > 10) {
                System.out.println("Maximum 10 slots allowed. Setting to 10.");
                slots = 10;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using 1 slot as default.");
            slots = 1;
        }
        
        // Generate unique ID
        String oppId = "OPP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Create the opportunity
        InternshipOpportunity opp = new InternshipOpportunity(
            oppId,
            title,
            description,
            preferredMajor,
            level,
            openDate,
            closeDate,
            rep.getCompanyName(),
            rep,
            slots
        );
        
        // Set to Pending (needs approval from Career Staff)
        opp.setStatus(OpportunityStatus.Pending);
        opp.setVisible(false); // Not visible until approved
        
        // Save via OpportunityManager
        opportunityManager.getRepository().save(opp);
        
        System.out.println("\n✓ Opportunity created successfully!");
        System.out.println("Opportunity ID: " + oppId);
        System.out.println("Status: Pending (awaiting Career Staff approval)");
    }
    
    /*
     * List all internship opportunities owned by the company representative.
     */
    private void listMyOpportunities(CompanyRep rep) {
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
        
        List<InternshipOpportunity> myOpps = opportunityManager.listOwnedOpps(rep);
        
        // Apply filters
        List<InternshipOpportunity> filteredOpps = filterSettings.apply(myOpps);
        
        if (filteredOpps.isEmpty()) {
            System.out.println("\nNo opportunities match your current filters.");
            return;
        }
        
        System.out.println("\n=== My Internship Opportunities ===");
        System.out.println(filterSettings.getSummary());
        for (int i = 0; i < filteredOpps.size(); i++) {
            InternshipOpportunity opp = filteredOpps.get(i);
            System.out.printf("\n%d. %s (ID: %s)%n", i + 1, opp.getTitle(), opp.getOpportunityID());
            System.out.println("   Status: " + opp.getStatus());
            if (opp.getOwner() != null) {
                System.out.println("   Company Rep: " + opp.getOwner().getName() + " (" + opp.getOwner().getUserId() + ")");
            }
            System.out.println("   Level: " + opp.getLevel());
            System.out.println("   Slots (remaining/total): " + opp.getSlots() + "/" + opp.getSlotCap());
            System.out.println("   Visible: " + (opp.getVisible() ? "Yes" : "No"));
            System.out.println("   Open: " + opp.getOpenDate() + " | Close: " + opp.getCloseDate());
            System.out.println("   Description: " + opp.getDescription());
        }
    }
    
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
     * Edit an existing internship opportunity.
     */
    private void editOpportunity(CompanyRep rep) {
        List<InternshipOpportunity> myOpps = opportunityManager.listOwnedOpps(rep);
        
        if (myOpps.isEmpty()) {
            System.out.println("\nYou have no opportunities to edit.");
            return;
        }
        
        listMyOpportunities(rep);
        
        System.out.print("\nSelect opportunity number to edit (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (choice == 0 || choice < 0 || choice > myOpps.size()) {
            return;
        }
        
        InternshipOpportunity opp = myOpps.get(choice - 1);
        
        // Cannot edit filled or rejected opportunities
        if (opp.getStatus() == OpportunityStatus.Filled || opp.getStatus() == OpportunityStatus.Rejected) {
            System.out.println("Cannot edit a " + opp.getStatus().toString().toLowerCase() + " opportunity.");
            return;
        }
        
        boolean isApproved = (opp.getStatus() == OpportunityStatus.Approved);
        
        System.out.println("\n=== Edit Opportunity ===");
        if (isApproved) {
            System.out.println("Note: Opportunity is approved. Only description can be edited.");
        } else {
            System.out.println("Leave blank to keep current value");
        }
        
        // If Pending, allow full edit
        if (!isApproved) {
            System.out.print("New Title [" + opp.getTitle() + "]: ");
            String newTitle = scanner.nextLine().trim();
            
            System.out.print("New Description [" + opp.getDescription() + "]: ");
            String newDesc = scanner.nextLine().trim();
            
            System.out.print("New Preferred Major [" + opp.getPreferredMajor() + "]: ");
            String newMajor = scanner.nextLine().trim();
            
            System.out.print("New Level (Basic/Intermediate/Advanced) [" + opp.getLevel() + "]: ");
            String newLevelStr = scanner.nextLine().trim();
            InternshipLevel newLevel = opp.getLevel();
            if (!newLevelStr.isEmpty()) {
                try {
                    newLevel = InternshipLevel.valueOf(newLevelStr);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid level. Keeping current value.");
                }
            }
            
            System.out.print("New Number of Slots [" + opp.getSlotCap() + "]: ");
            String newSlotsStr = scanner.nextLine().trim();
            int newSlots = opp.getSlotCap();
            if (!newSlotsStr.isEmpty()) {
                try {
                    newSlots = Integer.parseInt(newSlotsStr);
                    if (newSlots > 10) {
                        System.out.println("Maximum 10 slots. Setting to 10.");
                        newSlots = 10;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Keeping current value.");
                }
            }
            
            System.out.print("New Opening Date (YYYY-MM-DD) [" + opp.getOpenDate() + "]: ");
            String newOpenStr = scanner.nextLine().trim();
            java.time.LocalDate newOpen = opp.getOpenDate();
            if (!newOpenStr.isEmpty()) {
                try {
                    newOpen = java.time.LocalDate.parse(newOpenStr);
                } catch (java.time.format.DateTimeParseException e) {
                    System.out.println("Invalid date. Keeping current value.");
                }
            }
            
            System.out.print("New Closing Date (YYYY-MM-DD) [" + opp.getCloseDate() + "]: ");
            String newCloseStr = scanner.nextLine().trim();
            java.time.LocalDate newClose = opp.getCloseDate();
            if (!newCloseStr.isEmpty()) {
                try {
                    newClose = java.time.LocalDate.parse(newCloseStr);
                } catch (java.time.format.DateTimeParseException e) {
                    System.out.println("Invalid date. Keeping current value.");
                }
            }
            
            // Apply changes
            if (!newTitle.isEmpty() || !newDesc.isEmpty() || !newMajor.isEmpty()) {
                opp.setBasics(
                    newTitle.isEmpty() ? opp.getTitle() : newTitle,
                    newDesc.isEmpty() ? opp.getDescription() : newDesc,
                    newMajor.isEmpty() ? opp.getPreferredMajor() : newMajor,
                    newLevel,
                    newSlots
                );
            } else {
                opp.setBasics(opp.getTitle(), opp.getDescription(), opp.getPreferredMajor(), newLevel, newSlots);
            }
            opp.setWindows(newOpen, newClose);
            
        } else {
            // If Approved, only allow description edit
            System.out.print("New Description [" + opp.getDescription() + "]: ");
            String newDesc = scanner.nextLine().trim();
            if (!newDesc.isEmpty()) {
                opp.setBasics(opp.getTitle(), newDesc, opp.getPreferredMajor(), opp.getLevel(), opp.getSlotCap());
            }
        }
        
        opportunityManager.getRepository().save(opp);
        System.out.println("✓ Opportunity updated successfully!");
    }

    /*
     * Toggle visibility of an internship opportunity.
     */
    private void toggleVisibility(CompanyRep rep) {
        List<InternshipOpportunity> myOpps = opportunityManager.listOwnedOpps(rep);
        
        if (myOpps.isEmpty()) {
            System.out.println("\nYou have no opportunities.");
            return;
        }
        
        listMyOpportunities(rep);
        
        System.out.print("\nSelect opportunity number to toggle visibility (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (choice == 0 || choice < 0 || choice > myOpps.size()) {
            return;
        }
        
        InternshipOpportunity opp = myOpps.get(choice - 1);
        
        if (opp.getStatus() != OpportunityStatus.Approved) {
            System.out.println("Error: Only approved opportunities can have visibility toggled.");
            return;
        }
        
        opp.setVisible(!opp.getVisible());
        opportunityManager.getRepository().save(opp);
        
        System.out.println("✓ Visibility toggled! Now: " + (opp.getVisible() ? "ON" : "OFF"));
    }

    /*
     * Review applications for a selected internship opportunity.
     */
    private void reviewApplications(CompanyRep rep) {
        // First, list the company rep's opportunities
        List<InternshipOpportunity> myOpps = opportunityManager.listOwnedOpps(rep);
        
        if (myOpps.isEmpty()) {
            System.out.println("\nYou have no opportunities to review applications for.");
            return;
        }
        
        System.out.println("\n=== Your Opportunities ===");
        for (int i = 0; i < myOpps.size(); i++) {
            InternshipOpportunity opp = myOpps.get(i);
            System.out.printf("%d. %s (ID: %s) - Status: %s%n",
                i + 1,
                opp.getTitle(),
                opp.getOpportunityID(),
                opp.getStatus()
            );
        }
        
        System.out.print("\nSelect opportunity number (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (choice == 0 || choice < 0 || choice > myOpps.size()) {
            return;
        }
        
        InternshipOpportunity selectedOpp = myOpps.get(choice - 1);
        
        // Find applications for this opportunity
        List<Application> applications = applicationManager.getApplicationRepository()
            .findByOpportunity(selectedOpp);
        
        if (applications.isEmpty()) {
            System.out.println("\nNo applications found for this opportunity.");
            return;
        }
        
        System.out.println("\n=== Applications for: " + selectedOpp.getTitle() + " ===");
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            Student student = app.getStudent();
            System.out.printf("\n%d. Student: %s (ID: %s)%n", i + 1, student.getName(), student.getUserId());
            System.out.println("   Email: " + student.getEmail());
            System.out.println("   Year of Study: " + student.getYearOfStudy());
            System.out.println("   Major: " + student.getMajor());
            System.out.println("   Application Status: " + app.getStatus());
            System.out.println("   Accepted by student: " + app.isAcceptedByStudent());
            if (app.getStatus() == EntityClass.Enums.ApplicationStatus.Pending) {
                System.out.println("   (Actionable)");
            } else {
                System.out.println("   (Final - no further actions)");
            }
        }
        
        System.out.print("\nSelect application number to review (0 to cancel): ");
        int appChoice;
        try {
            appChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (appChoice == 0 || appChoice < 0 || appChoice > applications.size()) {
            return;
        }
        
        Application selectedApp = applications.get(appChoice - 1);
        
        if (selectedApp.getStatus() != EntityClass.Enums.ApplicationStatus.Pending) {
            System.out.println("This application is no longer actionable (status: " + selectedApp.getStatus() + ").");
            return;
        }

        System.out.println("\n1. Approve application");
        System.out.println("2. Reject application");
        System.out.print("Choice: ");

        String action = scanner.nextLine().trim();

        try {
            if ("1".equals(action)) {
                applicationManager.repApprove(rep, selectedApp);
                System.out.println("✓ Application approved! Student can now accept the offer.");
            } else if ("2".equals(action)) {
                applicationManager.repReject(rep, selectedApp);
                System.out.println("✓ Application rejected.");
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*
     * Delete an internship opportunity.
     */
    private void deleteOpportunity(CompanyRep rep) {
        List<InternshipOpportunity> myOpps = opportunityManager.listOwnedOpps(rep);
        
        if (myOpps.isEmpty()) {
            System.out.println("\nYou have no opportunities to delete.");
            return;
        }
        
        listMyOpportunities(rep);
        
        System.out.print("\nSelect opportunity number to delete (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (choice == 0 || choice < 0 || choice > myOpps.size()) {
            return;
        }
        
        InternshipOpportunity opp = myOpps.get(choice - 1);
        
        // Can only delete Pending or Approved (not Filled or Rejected)
        if (opp.getStatus() != OpportunityStatus.Pending && opp.getStatus() != OpportunityStatus.Approved) {
            System.out.println("Cannot delete " + opp.getStatus().toString().toLowerCase() + " opportunities.");
            System.out.println("Only pending or approved opportunities can be deleted.");
            return;
        }
        
        System.out.print("Are you sure you want to delete '" + opp.getTitle() + "'? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            opportunityManager.getRepository().delete(opp.getOpportunityID());
            System.out.println("✓ Opportunity deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /*
     * Change password for the Company Representative user.
     */
    private boolean changePassword(CompanyRep rep) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine().trim();
        
        // Verify current password
        if (!rep.login(rep.getUserId(), currentPassword)) {
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
        
        rep.changePassword(newPassword);
        return true;
    }
}

