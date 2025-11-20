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
    */
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.filters = new FilterSettings();
    }
    
    /**
     * Logs in the user with the given credentials.
     */

    public boolean login(String userId, String pwd) {
        return this.userId.equals(userId) && this.password.equals(pwd);
    }

    /**
     * Logs out the user.
     */
    public void logout() {
        System.out.println(name + " logged out.");
    }

    /**
     * Changes the user's password.
     */
    public void changePassword(String newPwd) {
        this.password = newPwd;
    }

    /**
     * Returns the user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }   

    /**
     * Returns the name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     */
    public FilterSettings getFilters() {
        return filters;
    }

    /**
     * Sets the filter settings for the user.
     */
    public void setFilters(FilterSettings f) {
        this.filters = f;
    }

    /**  
     *Returns a string representation of the user.
     */
    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name + ", Email: " + email;
    }
}


