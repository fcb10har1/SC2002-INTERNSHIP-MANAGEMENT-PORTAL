package BoundaryClass;

import java.util.Optional;
import java.util.Scanner;

import ControlClass.AuthService;
import EntityClass.User;

public class LoginUI {

    private final AuthService authService;
    private final Scanner scanner;

    public LoginUI(AuthService authService, Scanner scanner) {
        this.authService = authService;
        this.scanner = scanner;
    }

    public LoginUI(AuthService authService) {
        this(authService, new Scanner(System.in));
    }

    public Optional<User> promptLogin() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        Optional<User> loggedIn = authService.login(userId, password);
        displayLoginResult(loggedIn.isPresent());
        return loggedIn;
    }

    public void displayLoginResult(boolean success) {
        if (success) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Login failed. Invalid user ID or password.");
        }
    }
}