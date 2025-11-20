package ControlClass;

import java.util.Optional;

import EntityClass.User;
import EntityClass.Student;
import EntityClass.CompanyRep;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;

/**
 * Provides authentication (login/logout) and basic role verification for users.
 */
public class AuthService {

    private final IUserRepository userRepository;

    /**
     * Constructs an AuthService with the given user repository.
     * @param userRepository backing repository
     */
    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Attempts to authenticate a user by ID and password.
     * @param userId user identifier
     * @param password plaintext password attempt
     * @return Optional containing authenticated User or empty if failed
     */
    public Optional<User> login(String userId, String password) {
        return userRepository.findById(userId)
                .filter(u -> u.login(userId, password));
    }

    /**
     * Logs out the given user.
     * @param user authenticated user
     */
    public void logout(User user) {
        user.logout();
    }

    /**
     * Verifies if the user instance matches the textual role.
     * @param user user instance
     * @param role role string (Student|CompanyRep|CareerStaff)
     * @return true if role matches, false otherwise
     */
    public boolean verifyRole(User user, String role) {
        switch (role) {
            case "Student":
                return user instanceof Student;
            case "CompanyRep":
                return user instanceof CompanyRep;
            case "CareerStaff":
                return user instanceof CareerStaff;
            default:
                return false;
        }
    }

    /**
     * Checks if a company representative has been approved.
     * @param rep company representative
     * @return true if approved, false otherwise
     */
    public boolean isApprovedCompanyRep(CompanyRep rep) {
        if (userRepository instanceof UserRepository) {
            UserRepository concrete = (UserRepository) userRepository;
            return concrete.isCompanyRepApproved(rep);
        }
        return false;
    }

    /**
     * Returns the backing user repository.
     * @return user repository
     */
    public IUserRepository getUserRepository() {
        return userRepository;
    }
}
