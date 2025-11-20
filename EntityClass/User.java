package EntityClass;

/**
 * Represents a user in the internship management portal.
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password = "password";
    private FilterSettings filters;

    /**
     * Creates a new user with the given details.
     * @param userId unique id
     * @param name display name
     * @param email contact email
     */
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.filters = new FilterSettings();
    }
    
    /**
     * Attempts login with provided credentials.
     * @param userId user id
     * @param pwd password
     * @return true if credentials match stored values
     */

    public boolean login(String userId, String pwd) {
        return this.userId.equals(userId) && this.password.equals(pwd);
    }

    /**
     * Logs out the user (console side-effect only).
     */
    public void logout() {
        System.out.println(name + " logged out.");
    }

    /**
     * Changes the user's password.
     * @param newPwd new password
     */
    public void changePassword(String newPwd) {
        this.password = newPwd;
    }

    /**
     * Returns the user ID.
     * @return user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the email of the user.
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }   

    /**
     * Returns the name of the user.
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns active filter settings for this user.
     * @return filter settings object
     */
    public FilterSettings getFilters() {
        return filters;
    }

    /**
     * Replaces filter settings for the user.
     * @param f new filter settings
     */
    public void setFilters(FilterSettings f) {
        this.filters = f;
    }

    /**
     * Returns a string representation of the user.
     * @return formatted string
     */
    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name + ", Email: " + email;
    }
}


