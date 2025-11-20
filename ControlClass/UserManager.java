package ControlClass;

import java.util.Optional;

import EntityClass.User;
import EntityClass.Student;
import EntityClass.CompanyRep;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;
import RepositoryClass.UserRepository;

/**
 * Manages user-related operations such as password changes and CompanyRep approval/rejection tracking.
 */
public class UserManager {

    private final IUserRepository userRepository;

    /**
     * Constructs a UserManager with the given repository.
     * @param userRepository user repository
     */
    public UserManager(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Constructs a UserManager with a new default UserRepository instance.
     */
    public UserManager() {
        this(new UserRepository());
    }

    /**
     * Changes the password of the user identified by the supplied ID.
     * @param userId user ID
     * @param newPwd new password value
     * @return true if password changed, false if user not found
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
    
    /**
     * Changes the password for a student entity.
     * @param s student whose password will change
     * @param newPwd new password value
     */
    public void changeStudentPassword(Student s, String newPwd) {
        s.changePassword(newPwd);
        userRepository.update(s);
        System.out.println("Password changed for student " + s.getUserId());
    }

    /**
     * Marks a company representative as approved.
     * @param approver career staff performing approval
     * @param rep company representative
     * @return true if approved, false otherwise
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

    /**
     * Marks a company representative as rejected.
     * @param approver career staff performing rejection
     * @param rep company representative
     * @return true if rejected, false otherwise
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

    /**
     * Returns the backing user repository.
     * @return user repository
     */
    public IUserRepository getUserRepository() {
        return userRepository;
    }
}
