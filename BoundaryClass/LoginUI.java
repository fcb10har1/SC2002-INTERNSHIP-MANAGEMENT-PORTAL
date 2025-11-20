package BoundaryClass;

import java.util.Optional;
import java.util.Scanner;

import ControlClass.AuthService;
import ControlClass.UserManager;
import EntityClass.User;
import EntityClass.CompanyRep;

public class LoginUI {

    private final AuthService authService;
    private final UserManager userManager;
    private final Scanner scanner;

    public LoginUI(AuthService authService, UserManager userManager, Scanner scanner) {
        this.authService = authService;
        this.userManager = userManager;
        this.scanner = scanner;
    }

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
                return Optional.empty(); // Return to menu after registration
            case "0":
                return Optional.empty(); // Exit
            default:
                System.out.println("Invalid choice.");
                return Optional.empty();
        }
    }

    private Optional<User> handleLogin() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        Optional<User> loggedIn = authService.login(userId, password);
        
        if (loggedIn.isPresent()) {
            User user = loggedIn.get();
            
            // Check if CompanyRep is approved
            if (user instanceof CompanyRep) {
                CompanyRep rep = (CompanyRep) user;
                if (!authService.isApprovedCompanyRep(rep)) {
                    System.out.println("Your account is pending approval by Career Center Staff.");
                    System.out.println("Please wait for approval before logging in.");
                    return Optional.empty();
                }
            }
            
            System.out.println("Login successful.");
            return loggedIn;
        } else {
            System.out.println("Login failed. Invalid user ID or password.");
            return Optional.empty();
        }
    }

    private void handleCompanyRepRegistration() {
        System.out.println("\n=== Company Representative Registration ===");
        
        System.out.print("Enter desired user ID: ");
        String userId = scanner.nextLine().trim();
        
        // Check if ID already exists
        if (authService.getUserRepository().findById(userId).isPresent()) {
            System.out.println("Error: User ID already exists. Please choose a different ID.");
            return;
        }
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter company name: ");
        String companyName = scanner.nextLine().trim();
        
        System.out.print("Enter department: ");
        String department = scanner.nextLine().trim();
        
        System.out.print("Enter position: ");
        String position = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        // Create new CompanyRep (not approved yet)
        CompanyRep newRep = new CompanyRep(userId, name, companyName, department, position);
        newRep.changePassword(password); // Set custom password
        
        // Add to repository
        authService.getUserRepository().add(newRep);
        
        System.out.println("\nRegistration successful!");
        System.out.println("Your account is pending approval by Career Center Staff.");
        System.out.println("You will be able to log in once approved.");
        System.out.println("User ID: " + userId);
    }
}
