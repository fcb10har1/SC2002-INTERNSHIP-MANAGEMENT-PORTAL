package RepositoryClass;

import java.util.*;


import EntityClass.User;
import EntityClass.CompanyRep;

/**
 * In-memory repository for User entities including tracking of CompanyRep approval/rejection state.
 */
public class UserRepository implements IUserRepository {

    private final Map<String, User> users = new HashMap<>();

    // track which CompanyReps are approved by CareerStaff
    private final Set<String> approvedCompanyRepIds = new HashSet<>();
    // track which CompanyReps are rejected (persistent so they don't reappear)
    private final Set<String> rejectedCompanyRepIds = new HashSet<>();

    /**
     * Adds a new user.
     * @param u user instance
     */
    @Override
    public void add(User u) {
        users.put(u.getUserId(), u);
    }

    /**
     * Finds user by id.
     * @param id user id
     * @return optional user
     */
    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * Updates existing user state.
     * @param u user instance
     */
    @Override
    public void update(User u) {
        users.put(u.getUserId(), u);
    }

    /**
     * Returns all stored users.
     * @return list of users
     */
    @Override
    public List<User> all() {
        return new ArrayList<>(users.values());
    }

    /**
     * Marks company representative as approved.
     * @param rep company representative
     */
    public void markCompanyRepApproved(CompanyRep rep) {
        approvedCompanyRepIds.add(rep.getUserId());
    }

    /**
     * Checks if representative is approved.
     * @param rep company representative
     * @return true if approved
     */
    public boolean isCompanyRepApproved(CompanyRep rep) {
        return approvedCompanyRepIds.contains(rep.getUserId());
    }

    /**
     * Marks company representative as rejected.
     * @param rep company representative
     */
    public void markCompanyRepRejected(CompanyRep rep) {
        // ensure exclusivity: if rejected, remove any prior approval
        approvedCompanyRepIds.remove(rep.getUserId());
        rejectedCompanyRepIds.add(rep.getUserId());
    }

    /**
     * Checks if representative is rejected.
     * @param rep company representative
     * @return true if rejected
     */
    public boolean isCompanyRepRejected(CompanyRep rep) {
        return rejectedCompanyRepIds.contains(rep.getUserId());
    }
}
