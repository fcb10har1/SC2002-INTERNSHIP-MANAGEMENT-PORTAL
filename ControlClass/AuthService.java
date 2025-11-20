package ControlClass;

import java.util.Optional;

import EntityClass.User;
import EntityClass.Student;
import EntityClass.CompanyRep;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;

public class AuthService {

    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String userId, String password) {
        return userRepository.findById(userId)
                .filter(u -> u.login(userId, password));
    }

    public void logout(User user) {
        user.logout();
    }

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

    public boolean isApprovedCompanyRep(CompanyRep rep) {
        if (userRepository instanceof UserRepository) {
            UserRepository concrete = (UserRepository) userRepository;
            return concrete.isCompanyRepApproved(rep);
        }
        return false;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }
}
