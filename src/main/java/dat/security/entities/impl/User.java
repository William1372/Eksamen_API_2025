package dat.security.entities.impl;

import dat.security.entities.ISecurityUser;
import dat.security.entities.impl.Role;
import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "users")
public class User implements ISecurityUser {
    @Id
    @Column(nullable = false)
    private String username;
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "rolename")
    )
    private Set<Role> roles = new HashSet<>();


    // Konstruktør til at hashe passwords
    public User(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.username = username;
        this.password = hashed;
        this.roles = new HashSet<>();
    }


    @Override
    public boolean verifyPassword(String candidate) {
        if (BCrypt.checkpw(candidate, password))
            return true;
        else
            return false;
    }

    @Override
    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    @Override
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
}