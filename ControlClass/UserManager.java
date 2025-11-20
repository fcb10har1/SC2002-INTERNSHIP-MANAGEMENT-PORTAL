package ControlClass;

import java.util.Optional;

import EntityClass.User;
import EntityClass.Student;
import EntityClass.CompanyRep;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;

/*
 * Manages user-related operations such as password changes and CompanyRep approvals
 */
public class UserManager {

    private final IUserRepository userRepository;

    public UserManager(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserManager() {
        this(new UserRepository());
    }

    /*
     * Changes the password for a user identified by userId
     */
    public boolean changePassword(String userId, String newPwd) {
        Optional<User> opt = userRepository.findById(userId);
        if (!opt.isPresent()) {
            System.out.println("User not found: " + userId);
            return false;
        }
        User u = opt.get();
        u.changePassword(newPwd);
        userRepository.update(u);
        System.out.println("Password changed for user " + userId);
        return true;
    }
    
    /*
     * Changes the password for a student
     */
    public void changeStudentPassword(Student s, String newPwd) {
        s.changePassword(newPwd);
        userRepository.update(s);
        System.out.println("Password changed for student " + s.getUserId());
    }

    /*
     * Approves a CompanyRep by a CareerStaff member
     */
    public boolean approveCompanyRep(CareerStaff approver, CompanyRep rep) {
        if (approver == null || rep == null) {
            return false;
        }

        if (!(userRepository instanceof UserRepository)) {
            System.out.println("Repository does not support approval tracking.");
            return false;
        }

        UserRepository concrete = (UserRepository) userRepository;
        concrete.markCompanyRepApproved(rep);
        userRepository.update(rep);
        System.out.println("CompanyRep " + rep.getUserId()
                + " approved by staff " + approver.getUserId());
        return true;
    }

    /*
     * Rejects a CompanyRep by a CareerStaff member
     */
    public boolean rejectCompanyRep(CareerStaff approver, CompanyRep rep) {
        if (approver == null || rep == null) {
            return false;
        }
        if (!(userRepository instanceof UserRepository)) {
            System.out.println("Repository does not support rejection tracking.");
            return false;
        }
        UserRepository concrete = (UserRepository) userRepository;
        concrete.markCompanyRepRejected(rep);
        userRepository.update(rep);
        System.out.println("CompanyRep " + rep.getUserId() + " rejected by staff " + approver.getUserId());
        return true;
    }

    /*
     * Returns the user repository
     */
    public IUserRepository getUserRepository() {
        return userRepository;
    }
}
