package ControlClass;

import java.util.Optional;

import EntityClass.User;
import EntityClass.Student;
import EntityClass.CompanyRep;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;

public class UserManager {

    private final IUserRepository userRepository;

    public UserManager(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserManager() {
        this(new UserRepository());
    }

    public boolean changePassword(String userId, String newPwd) {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isEmpty()) {
            System.out.println("User not found: " + userId);
            return false;
        }
        User u = opt.get();
        u.changePassword(newPwd);
        userRepository.update(u);
        System.out.println("Password changed for user " + userId);
        return true;
    }

    public void changeStudentPassword(Student s, String newPwd) {
        s.changePassword(newPwd);
        userRepository.update(s);
        System.out.println("Password changed for student " + s.getUserId());
    }

    public boolean approveCompanyRep(CareerStaff approver, CompanyRep rep) {
        if (approver == null || rep == null) {
            return false;
        }

        if (!(userRepository instanceof UserRepository concrete)) {
            System.out.println("Repository does not support approval tracking.");
            return false;
        }

        concrete.markCompanyRepApproved(rep);
        userRepository.update(rep); // keep repo in sync
        System.out.println("CompanyRep " + rep.getUserId()
                + " approved by staff " + approver.getUserId());
        return true;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }
}
