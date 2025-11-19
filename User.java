package EntityClass;
public abstract class User2 {
    protected String userId;
    protected String name;
    protected String password = "password";
    protected FilterSettings filters;

    public User2(String userId, String name) {
        this.userId = userId;
        this.name = name;
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

    public FilterSettings getFilters() {
        return filters;
    }

    public void setFilters(FilterSettings f) {
        this.filters = f;
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name;
    }
}


