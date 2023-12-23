package user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {
    public boolean isAdminConnected = false;
    private boolean isFirstAdmin = true;
    private final Set<String> usersSet;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public void changeFirstAdmin(){
        isFirstAdmin = false;
    }

    public boolean isFirstAdmin() {
        return isFirstAdmin;
    }
}
