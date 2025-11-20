package RepositoryClass;

import java.util.*;

// implementation of IUserRepository
import EntityClass.User;
import EntityClass.CompanyRep;

public class UserRepository implements IUserRepository {

    private final Map<String, User> users = new HashMap<>();

    // track which CompanyReps are approved by CareerStaff
    private final Set<String> approvedCompanyRepIds = new HashSet<>();

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
}
