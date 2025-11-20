package RepositoryClass;

import java.util.List;
import java.util.Optional;

import EntityClass.User;

/**
 * Contract for User data persistence and retrieval operations.
 */
public interface IUserRepository {

    /**
     * Adds new user.
     * @param u user instance
     */
    void add(User u);

    /**
     * Finds user by id.
     * @param id user id
     * @return optional user
     */
    Optional<User> findById(String id);

    /**
     * Updates existing user.
     * @param u user instance
     */
    void update(User u);

    /**
     * Returns all users.
     * @return list of users
     */
    List<User> all();
}