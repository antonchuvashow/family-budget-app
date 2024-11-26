package antonchuvashov.familybudget;

public class AuthenticationState {

    private boolean isAuthenticated;
    private boolean isAdmin;
    private String username;

    private AuthenticationState() {
        this.isAuthenticated = false;
        this.isAdmin = false;
        this.username = null;
    }

    private static final class InstanceHolder {
        private static final AuthenticationState instance = new AuthenticationState();
    }

    public static AuthenticationState getInstance() {
        return InstanceHolder.instance;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void login(String username, boolean isAdmin) {
        this.isAuthenticated = true;
        this.isAdmin = isAdmin;
        this.username = username;
    }

    public void logout() {
        this.isAuthenticated = false;
        this.isAdmin = false;
        this.username = null;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
