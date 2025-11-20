package EntityClass;
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password = "password";
    private FilterSettings filters;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.filters = new FilterSettings();
    }

    public boolean login(String userId, String pwd) {
        return this.userId.equals(userId) && this.password.equals(pwd);
    }

    public void logout() {
        System.out.println(name + " logged out.");
    }

    public void changePassword(String newPwd) {
        this.password = newPwd;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public FilterSettings getFilters() {
        return filters;
    }

    public void setFilters(FilterSettings f) {
        this.filters = f;
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name + ", Email: " + email;
    }
}


