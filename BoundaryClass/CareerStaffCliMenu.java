package BoundaryClass;

import ControlClass.OpportunityManager;
import ControlClass.ReportManager;
import ControlClass.UserManager;
import ControlClass.WithdrawalManager;
import EntityClass.CareerStaff;
import EntityClass.CompanyRep;
import RepositoryClass.IUserRepository;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

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

    private void approveRejectOpportunities(CareerStaff staff) {
        List<EntityClass.InternshipOpportunity> pendingOpps = 
            opportunityManager.getRepository().findByStatus(EntityClass.Enums.OpportunityStatus.Pending);
        
        if (pendingOpps.isEmpty()) {
            System.out.println("\nNo pending opportunities to review.");
            return;
        }
        
        System.out.println("\n=== Pending Internship Opportunities ===");
        for (int i = 0; i < pendingOpps.size(); i++) {
            EntityClass.InternshipOpportunity opp = pendingOpps.get(i);
            System.out.printf("\n%d. %s (ID: %s)%n", i + 1, opp.getTitle(), opp.getOpportunityID());
            System.out.println("   Company: " + opp.getCompanyName());
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
        
        EntityClass.InternshipOpportunity selectedOpp = pendingOpps.get(choice - 1);
        
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

    private void generateReports() {
        System.out.println("\n=== Comprehensive Internship Opportunities Report ===");
        String report = reportManager.generateComprehensiveReport();
        System.out.println(report);
    }

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
}
