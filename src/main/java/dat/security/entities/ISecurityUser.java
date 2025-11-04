package dat.security.entities;


import dat.security.entities.impl.Role;

public interface ISecurityUser {
    boolean verifyPassword(String pw);
    void addRole(Role role);
    void removeRole(Role role);
}