package ControlClass;

import java.util.Optional;

import EntityClass.User;
import EntityClass.Student;
import EntityClass.CompanyRep;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;

/*
 * Manages authentication and authorization of users
 */
public class AuthService {

    private final IUserRepository userRepository;

    /*
     * Constructor
     */
    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Logs in a user with given credentials
     */
    public Optional<User> login(String userId, String password) {
        return userRepository.findById(userId)
                .filter(u -> u.login(userId, password));
    }

    /*
     * Logs out the given user
     */
    public void logout(User user) {
        user.logout();
    }

    /*
     * Verifies if the user has the specified role
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

    /*
     * Checks if a CompanyRep is approved
     */
    public boolean isApprovedCompanyRep(CompanyRep rep) {
        if (userRepository instanceof UserRepository) {
            UserRepository concrete = (UserRepository) userRepository;
            return concrete.isCompanyRepApproved(rep);
        }
        return false;
    }

    /*
     * Returns the user repository
     */
    public IUserRepository getUserRepository() {
        return userRepository;
    }
}
