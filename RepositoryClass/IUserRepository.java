package RepositoryClass;

import java.util.List;
import java.util.Optional;

import EntityClass.User;

/*
 * IUserRepository defines the contract for User data storage and retrieval.
 */
public interface IUserRepository {

    void add(User u);

    Optional<User> findById(String id);

    void update(User u);

    List<User> all();
}