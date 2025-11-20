package RepositoryClass;

import java.util.*;

// implementation of IUserRepository
import EntityClass.User;
import EntityClass.CompanyRep;

public class UserRepository implements IUserRepository {

    private final Map<String, User> users = new HashMap<>();

    // track which CompanyReps are approved by CareerStaff
    private final Set<String> approvedCompanyRepIds = new HashSet<>();
    // track which CompanyReps are rejected (persistent so they don't reappear)
    private final Set<String> rejectedCompanyRepIds = new HashSet<>();

    @Override
    public void add(User u) {
        users.put(u.getUserId(), u);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void update(User u) {
        users.put(u.getUserId(), u);
    }

    @Override
    public List<User> all() {
        return new ArrayList<>(users.values());
    }

    public void markCompanyRepApproved(CompanyRep rep) {
        approvedCompanyRepIds.add(rep.getUserId());
    }

    public boolean isCompanyRepApproved(CompanyRep rep) {
        return approvedCompanyRepIds.contains(rep.getUserId());
    }

    public void markCompanyRepRejected(CompanyRep rep) {
        // ensure exclusivity: if rejected, remove any prior approval
        approvedCompanyRepIds.remove(rep.getUserId());
        rejectedCompanyRepIds.add(rep.getUserId());
    }

    public boolean isCompanyRepRejected(CompanyRep rep) {
        return rejectedCompanyRepIds.contains(rep.getUserId());
    }
}
