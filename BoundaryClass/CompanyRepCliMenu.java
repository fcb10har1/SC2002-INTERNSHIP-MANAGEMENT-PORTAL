package BoundaryClass;

import ControlClass.ApplicationManager;
import ControlClass.OpportunityManager;
import EntityClass.CompanyRep;
import EntityClass.InternshipOpportunity;
import EntityClass.Application;
import EntityClass.Enums.InternshipLevel;
import EntityClass.Enums.OpportunityStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CompanyRepCliMenu {

    private final Scanner scanner;
    private final ApplicationManager applicationManager;
    private final OpportunityManager opportunityManager;

    public CompanyRepCliMenu(Scanner scanner,
                             ApplicationManager appMgr,
                             OpportunityManager oppMgr) {
        this.scanner = scanner;
        this.applicationManager = appMgr;
        this.opportunityManager = oppMgr;
    }

    public void showOptions(CompanyRep rep) {
        int choice;
        do {
            System.out.println("\n=== Company Representative Menu ===");
            System.out.println("1. Create new internship opportunity");
            System.out.println("2. List my opportunities");
            System.out.println("3. Edit opportunity");
            System.out.println("4. Toggle opportunity visibility");
            System.out.println("5. Review applications for an opportunity");
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
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

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

    private void listMyOpportunities(CompanyRep rep) {
        List<InternshipOpportunity> myOpps = opportunityManager.listOwnedOpps(rep);
        
        if (myOpps.isEmpty()) {
            System.out.println("\nYou have no internship opportunities yet.");
            return;
        }
        
        System.out.println("\n=== My Internship Opportunities ===");
        for (int i = 0; i < myOpps.size(); i++) {
            InternshipOpportunity opp = myOpps.get(i);
            System.out.printf("\n%d. %s (ID: %s)%n", i + 1, opp.getTitle(), opp.getOpportunityID());
            System.out.println("   Status: " + opp.getStatus());
            System.out.println("   Level: " + opp.getLevel());
            System.out.println("   Slots (remaining/total): " + opp.getSlots() + "/" + opp.getSlotCap());
            System.out.println("   Visible: " + (opp.getVisible() ? "Yes" : "No"));
            System.out.println("   Open: " + opp.getOpenDate() + " | Close: " + opp.getCloseDate());
        }
    }

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
        
        // Can only edit if status is Pending or Approved (not Filled/Rejected)
        if (opp.getStatus() == OpportunityStatus.Filled) {
            System.out.println("Cannot edit a filled opportunity.");
            return;
        }
        
        System.out.println("\n=== Edit Opportunity ===");
        System.out.println("Leave blank to keep current value");
        
        System.out.print("New Title [" + opp.getTitle() + "]: ");
        String newTitle = scanner.nextLine().trim();
        if (!newTitle.isEmpty()) {
            opp.setBasics(newTitle, opp.getDescription(), opp.getPreferredMajor(), opp.getLevel(), opp.getSlotCap());
        }
        
        System.out.print("New Description [" + opp.getDescription() + "]: ");
        String newDesc = scanner.nextLine().trim();
        if (!newDesc.isEmpty()) {
            opp.setBasics(opp.getTitle(), newDesc, opp.getPreferredMajor(), opp.getLevel(), opp.getSlotCap());
        }
        
        opportunityManager.getRepository().save(opp);
        System.out.println("✓ Opportunity updated successfully!");
    }

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
            System.out.printf("\n%d. Student: %s%n", i + 1, app.getStudent().getUserId());
            System.out.println("   Status: " + app.getStatus());
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

    // Report generation removed from CompanyRep interface (restricted to CareerStaff)
}

