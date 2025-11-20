package BoundaryClass;

import java.util.Optional;
import java.util.Scanner;

import ControlClass.AuthService;
import ControlClass.CSVImporter;
import ControlClass.UserManager;
import EntityClass.User;
import EntityClass.CompanyRep;

/**
 * LoginUI handles user login and registration interactions via CLI.
 */
public class LoginUI {

    private final AuthService authService;
    private final UserManager userManager;
    private final Scanner scanner;

    /**
     * Constructs the login UI with required services and input scanner.
     * @param authService authentication service
     * @param userManager user manager
     * @param scanner interactive input source
     */
    public LoginUI(AuthService authService, UserManager userManager, Scanner scanner) {
        this.authService = authService;
        this.userManager = userManager;
        this.scanner = scanner;
    }
    
    /**
     * Displays the login and registration menu and handles user input.
     * @return an Optional containing the logged-in User, or empty if exiting.
     */
    public Optional<User> promptLogin() {
        System.out.println("\n=== Login / Registration Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register as Company Representative");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                return handleLogin();
            case "2":
                handleCompanyRepRegistration();
                return promptLogin(); // Return to menu after registration instead of exiting
            case "0":
                return Optional.empty(); // Exit
            default:
                System.out.println("Invalid choice.");
                return promptLogin(); // Return to menu for invalid choices too
        }
    }

    /**
     * Handles user login process.
     * @return optional containing authenticated user or empty if login failed
     */
    private Optional<User> handleLogin() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // First check if user exists
        Optional<User> userOpt = authService.getUserRepository().findById(userId);
        
        if (!userOpt.isPresent()) {
            System.out.println("Login failed. User ID not found.");
            return Optional.empty();
        }
        
        User user = userOpt.get();
        
        // Check if CompanyRep and if approved
        if (user instanceof CompanyRep) {
            CompanyRep rep = (CompanyRep) user;
            if (!authService.isApprovedCompanyRep(rep)) {
                System.out.println("Login failed. Your Company Representative account is pending approval by Career Center Staff.");
                System.out.println("Please wait for approval before logging in.");
                return Optional.empty();
            }
        }
        
        // Now verify password
        if (!user.login(userId, password)) {
            System.out.println("Login failed. Incorrect password.");
            return Optional.empty();
        }
        
        System.out.println("Login successful.");
        return Optional.of(user);
    }

    /**
     * Handles registration process for Company Representatives.
     */
    private void handleCompanyRepRegistration() {
        System.out.println("\n=== Company Representative Registration ===");
        System.out.println("Note: Your User ID must be your company email address.");
        
        System.out.print("Enter your company email address (this will be your User ID): ");
        String userId = scanner.nextLine().trim();
        
        // Validate email format
        if (!userId.contains("@") || !userId.contains(".")) {
            System.out.println("Error: Invalid email address format.");
            return;
        }
        
        // Check if ID already exists
        if (authService.getUserRepository().findById(userId).isPresent()) {
            System.out.println("Error: This email is already registered. Please use a different email.");
            return;
        }
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        
        // Email will be the same as userId for company reps
        String email = userId;
        
        System.out.print("Enter company name: ");
        String companyName = scanner.nextLine().trim();
        
        System.out.print("Enter department: ");
        String department = scanner.nextLine().trim();
        
        System.out.print("Enter position: ");
        String position = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        // Create new CompanyRep (not approved yet)
        CompanyRep newRep = new CompanyRep(userId, name, email, companyName, department, position);
        newRep.changePassword(password); // Set custom password
        
        // Add to repository
        authService.getUserRepository().add(newRep);
        
        // Export to CSV file to persist registration
        boolean exported = CSVImporter.exportCompanyRep("companyreps.csv", newRep);
        
        System.out.println("\nRegistration successful!");
        System.out.println("Your account is pending approval by Career Center Staff.");
        System.out.println("You will be able to log in once approved.");
        System.out.println("User ID: " + userId);
        
        if (!exported) {
            System.out.println("Note: Registration saved to session but may not persist after restart.");
        }
    }
}
