package ControlClass;

import java.util.Optional;

import EntityClass.User;
import EntityClass.CompanyRep;
import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;

public class AuthService {

    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthService() {
        this(new UserRepository());
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
                return user instanceof EntityClass.Student;
            case "CompanyRep":
                return user instanceof CompanyRep;
            case "CareerStaff":
                return user instanceof EntityClass.CareerStaff;
            default:
                return false;
        }
    }

    public boolean isApprovedCompanyRep(CompanyRep rep) {
        if (userRepository instanceof UserRepository concrete) {
            return concrete.isCompanyRepApproved(rep);
        }
        return false;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }
}