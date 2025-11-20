package BoundaryClass;

import ControlClass.OpportunityManager;
import ControlClass.ReportManager;
import ControlClass.UserManager;
import ControlClass.WithdrawalManager;
import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import EntityClass.FilterSettings;
import EntityClass.InternshipOpportunity;
import EntityClass.Enums.InternshipLevel;
import EntityClass.Enums.OpportunityStatus;
import RepositoryClass.IUserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
 * CLI menu for Career Staff users to manage company representatives,
 */
public class CareerStaffCliMenu {

    private final Scanner scanner;
    private final OpportunityManager opportunityManager;
    private final WithdrawalManager withdrawalManager;
    private final ReportManager reportManager;
    private final UserManager userManager;
    private FilterSettings filterSettings = new FilterSettings(); // Persists across menu navigation

    /*
     * Constructor for CareerStaffCliMenu.
     */
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

    /*
     * Displays the Career Staff menu and handles user input.
     */
    public void showOptions(CareerStaff staff) {
        int choice;
        do {
            System.out.println("\n=== Career Staff Menu ===");
            System.out.println("1. Approve/reject company representatives");
            System.out.println("2. Approve/reject opportunities");
            System.out.println("3. Approve/reject withdrawal requests");
            System.out.println("4. Generate reports");
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
                    approveRejectCompanyReps(staff);
                    break;
                case 2:
                    approveRejectOpportunities(staff);
                    break;
                case 3:
                    approveRejectWithdrawals(staff);
                    break;
                case 4:
                    generateReports();
                    break;
                case 5:
                    if (changePassword(staff)) {
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
     * Approve or reject company representatives.
     */
    private void approveRejectCompanyReps(CareerStaff staff) {
        IUserRepository userRepo = userManager.getUserRepository();
        
        // Get all CompanyReps
        List<CompanyRep> allReps = userRepo.all().stream()
            .filter(u -> u instanceof CompanyRep)
            .map(u -> (CompanyRep) u)
            .collect(Collectors.toList());
        
        if (allReps.isEmpty()) {
            System.out.println("No company representatives registered.");
            return;
        }
        
        System.out.println("\n=== Company Representatives ===");
        for (int i = 0; i < allReps.size(); i++) {
            CompanyRep rep = allReps.get(i);
            boolean approved = false;
            boolean rejected = false;
            if (userManager.getUserRepository() instanceof RepositoryClass.UserRepository) {
                RepositoryClass.UserRepository repo = (RepositoryClass.UserRepository) userManager.getUserRepository();
                approved = repo.isCompanyRepApproved(rep);
                rejected = repo.isCompanyRepRejected(rep);
            }
            String status = rejected ? "REJECTED" : (approved ? "APPROVED" : "PENDING");
            System.out.printf("%d. %s (ID: %s) - Company: %s - Status: %s%n",
                    i + 1,
                    rep.getUserId(),
                    rep.getUserId(),
                    rep.getCompanyName(),
                    status);
        }
        
        System.out.print("\nSelect representative number (0 to cancel): ");
        int repChoice;
        try {
            repChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (repChoice == 0 || repChoice < 0 || repChoice > allReps.size()) {
            return;
        }
        
        CompanyRep selectedRep = allReps.get(repChoice - 1);
        
    System.out.println("\n1. Approve");
    System.out.println("2. Reject");
        System.out.print("Choice: ");
        
        String action = scanner.nextLine().trim();
        
        if ("1".equals(action)) {
            boolean success = userManager.approveCompanyRep(staff, selectedRep);
            System.out.println(success ? "Company Representative approved successfully!" : "Failed to approve representative.");
        } else if ("2".equals(action)) {
            boolean success = userManager.rejectCompanyRep(staff, selectedRep);
            System.out.println(success ? "Company Representative rejected." : "Failed to reject representative.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /*
     * Approve or reject internship opportunities.
     */
    private void approveRejectOpportunities(CareerStaff staff) {
        // Filter options
        System.out.println("\n=== View All Opportunities (with filters) ===");
        System.out.println("Current: " + filterSettings.getSummary());
        System.out.println("1. View opportunities with current filters");
        System.out.println("2. Modify filters");
        System.out.println("3. Clear all filters");
        System.out.println("4. Approve/reject pending opportunities (no filters)");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        
        String filterChoice = scanner.nextLine().trim();
        
        if ("0".equals(filterChoice)) {
            return;
        } else if ("2".equals(filterChoice)) {
            configureFilters();
            return;
        } else if ("3".equals(filterChoice)) {
            filterSettings.clearAll();
            System.out.println("✓ All filters cleared.");
            return;
        } else if ("4".equals(filterChoice)) {
            // Original behavior - approve/reject pending
            approvePendingOpportunities(staff);
            return;
        }
        
        // Show all opportunities with filters
        List<InternshipOpportunity> allOpps = opportunityManager.getRepository().all();
        List<InternshipOpportunity> filteredOpps = filterSettings.apply(allOpps);
        
        if (filteredOpps.isEmpty()) {
            System.out.println("\nNo opportunities match your current filters.");
            return;
        }
        
        System.out.println("\n=== All Internship Opportunities (Filtered) ===");
        System.out.println(filterSettings.getSummary());
        for (int i = 0; i < filteredOpps.size(); i++) {
            InternshipOpportunity opp = filteredOpps.get(i);
            System.out.printf("\n%d. %s (ID: %s)%n", i + 1, opp.getTitle(), opp.getOpportunityID());
            System.out.println("   Company: " + opp.getCompanyName());
            if (opp.getOwner() != null) {
                System.out.println("   Company Rep: " + opp.getOwner().getName() + " (" + opp.getOwner().getUserId() + ")");
            }
            System.out.println("   Status: " + opp.getStatus());
            System.out.println("   Level: " + opp.getLevel());
            System.out.println("   Slots (remaining/total): " + opp.getSlots() + "/" + opp.getSlotCap());
            System.out.println("   Preferred Major: " + opp.getPreferredMajor());
            System.out.println("   Open: " + opp.getOpenDate() + " | Close: " + opp.getCloseDate());
        }
    }
    
    private void approvePendingOpportunities(CareerStaff staff) {
        List<InternshipOpportunity> pendingOpps = 
            opportunityManager.getRepository().findByStatus(OpportunityStatus.Pending);
        
        if (pendingOpps.isEmpty()) {
            System.out.println("\nNo pending opportunities to review.");
            return;
        }
        
        System.out.println("\n=== Pending Internship Opportunities ===");
        for (int i = 0; i < pendingOpps.size(); i++) {
            InternshipOpportunity opp = pendingOpps.get(i);
            System.out.printf("\n%d. %s (ID: %s)%n", i + 1, opp.getTitle(), opp.getOpportunityID());
            System.out.println("   Company: " + opp.getCompanyName());
            if (opp.getOwner() != null) {
                System.out.println("   Company Rep: " + opp.getOwner().getName() + " (" + opp.getOwner().getUserId() + ")");
            }
            System.out.println("   Level: " + opp.getLevel());
            System.out.println("   Slots (remaining/total): " + opp.getSlots() + "/" + opp.getSlotCap());
            System.out.println("   Preferred Major: " + opp.getPreferredMajor());
            System.out.println("   Open: " + opp.getOpenDate() + " | Close: " + opp.getCloseDate());
        }
        
        System.out.print("\nSelect opportunity number (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        if (choice == 0 || choice < 0 || choice > pendingOpps.size()) {
            return;
        }
        
        InternshipOpportunity selectedOpp = pendingOpps.get(choice - 1);
        
        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.print("Choice: ");
        
        String action = scanner.nextLine().trim();
        
        try {
            if ("1".equals(action)) {
                opportunityManager.approve(staff, selectedOpp);
                System.out.println("✓ Opportunity approved! Now visible to eligible students.");
            } else if ("2".equals(action)) {
                opportunityManager.reject(staff, selectedOpp);
                System.out.println("✓ Opportunity rejected.");
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
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
     * Generate comprehensive internship opportunities report.
     */
    private void generateReports() {
        System.out.println("\n=== Generate & Filter Internship Report ===");
        System.out.println("Press Enter to skip a filter.");
        java.util.Map<String,String> filters = new java.util.HashMap<>();

        System.out.print("Filter by status (Pending/Approved/Rejected/Filled/Draft): ");
        String status = scanner.nextLine().trim();
        if (!status.isEmpty()) filters.put("status", status);

    System.out.print("Filter by major (substring, case-insensitive): ");
        String major = scanner.nextLine().trim();
        if (!major.isEmpty()) filters.put("major", major);

    System.out.print("Filter by company (substring, case-insensitive): ");
    System.out.print("Filter by title (substring, case-insensitive): ");
    String title = scanner.nextLine().trim();
    if (!title.isEmpty()) filters.put("title", title);

    System.out.print("Filter by application status (Pending/Successful/Unsuccessful/Withdrawn): ");
    String appStatus = scanner.nextLine().trim();
    if (!appStatus.isEmpty()) filters.put("appStatus", appStatus);
        String company = scanner.nextLine().trim();
        if (!company.isEmpty()) filters.put("company", company);

        System.out.print("Filter by level (Basic/Intermediate/Advanced): ");
        String level = scanner.nextLine().trim();
        if (!level.isEmpty()) filters.put("level", level);

        System.out.print("Filter by placement (filled/open): ");
        String placement = scanner.nextLine().trim();
        if (!placement.isEmpty()) filters.put("placement", placement);

        System.out.print("Generate full aggregate anyway? (yes/no) [yes]: ");
        String fullAgg = scanner.nextLine().trim();
        boolean includeFull = fullAgg.isEmpty() || fullAgg.equalsIgnoreCase("yes") || fullAgg.equalsIgnoreCase("y");

        if (filters.isEmpty() && includeFull) {
            System.out.println("\n=== Comprehensive Internship Opportunities Report ===");
            System.out.println(reportManager.generateComprehensiveReport());
            return;
        }

        String filteredReport = reportManager.generateFilteredCompositeReport(filters);
        System.out.println(filteredReport);
        if (includeFull) {
            System.out.println("\n=== (Unfiltered Global Aggregate for Reference) ===");
            System.out.println(reportManager.generateComprehensiveReport());
        }
    }

    /*
     * Approve or reject withdrawal requests.
     */
    private void approveRejectWithdrawals(CareerStaff staff) {
        List<EntityClass.WithdrawalRequest> pending = withdrawalManager.getAllRequests().stream()
                .filter(r -> !r.isProcessed())
                .collect(java.util.stream.Collectors.toList());
        if (pending.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }
        System.out.println("\n=== Pending Withdrawal Requests ===");
        for (int i = 0; i < pending.size(); i++) {
            EntityClass.WithdrawalRequest r = pending.get(i);
            System.out.printf("%d. ID=%s | Student=%s | Opportunity=%s | Reason=%s%n", i + 1,
                    r.getRequestId(),
                    r.getApplication().getStudent().getUserId(),
                    r.getApplication().getTarget().getTitle(),
                    r.getReason());
        }
        System.out.print("Select request number (0 to cancel): ");
        int choice;
        try { choice = Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { System.out.println("Invalid."); return; }
        if (choice <= 0 || choice > pending.size()) return;
        EntityClass.WithdrawalRequest selected = pending.get(choice - 1);
        System.out.println("1. Approve withdrawal\n2. Reject withdrawal");
        System.out.print("Choice: ");
        String action = scanner.nextLine().trim();
        if ("1".equals(action)) {
            withdrawalManager.approveWithdrawal(staff, selected.getRequestId());
        } else if ("2".equals(action)) {
            withdrawalManager.rejectWithdrawal(staff, selected.getRequestId());
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /*
     * Change password for the Career Staff user.
     */
    private boolean changePassword(CareerStaff staff) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine().trim();
        
        // Verify current password
        if (!staff.login(staff.getUserId(), currentPassword)) {
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
        
        staff.changePassword(newPassword);
        return true;
    }
}
