package RepositoryClass;

import java.util.*;


import EntityClass.User;
import EntityClass.CompanyRep;

/*
 * UserRepository provides in-memory storage and retrieval of User entities,
 * along with tracking approval and rejection status for CompanyReps.
 */
public class UserRepository implements IUserRepository {

    private final Map<String, User> users = new HashMap<>();

    // track which CompanyReps are approved by CareerStaff
    private final Set<String> approvedCompanyRepIds = new HashSet<>();
    // track which CompanyReps are rejected (persistent so they don't reappear)
    private final Set<String> rejectedCompanyRepIds = new HashSet<>();

    /*     
     * Adds a new User to the repository.
     */
    @Override
    public void add(User u) {
        users.put(u.getUserId(), u);
    }

    /*     
     * Finds a User by their ID.
     */
    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    /*     
     * Updates an existing User in the repository.
     */
    @Override
    public void update(User u) {
        users.put(u.getUserId(), u);
    }

    /*     
     * Retrieves all Users in the repository.
     */
    @Override
    public List<User> all() {
        return new ArrayList<>(users.values());
    }

    /*     
     * Marks a CompanyRep as approved.
     */
    public void markCompanyRepApproved(CompanyRep rep) {
        approvedCompanyRepIds.add(rep.getUserId());
    }

    /*     
     * Checks if a CompanyRep is approved.
     */
    public boolean isCompanyRepApproved(CompanyRep rep) {
        return approvedCompanyRepIds.contains(rep.getUserId());
    }

    /*     
     * Marks a CompanyRep as rejected.
     */
    public void markCompanyRepRejected(CompanyRep rep) {
        // ensure exclusivity: if rejected, remove any prior approval
        approvedCompanyRepIds.remove(rep.getUserId());
        rejectedCompanyRepIds.add(rep.getUserId());
    }

    /*     
     * Checks if a CompanyRep is rejected.
     */
    public boolean isCompanyRepRejected(CompanyRep rep) {
        return rejectedCompanyRepIds.contains(rep.getUserId());
    }
}
