package io.github.williamandradesantana.sports.domain.user;

import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPasswordException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionDescriptionException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidUsernameException;

import java.util.*;

public class User {
    private final UUID id;
    private String username;
    private String fullName;
    private String password;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private Set<Permission> permissions;

    public User(
            UUID id, String username, String fullName, String password,
            boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
            Set<Permission> permissions
    ) {
        this.id = id;
        setUsername(username);
        this.fullName = fullName;
        setPassword(password);
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) throw new InvalidUsernameException();
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 8) throw new InvalidPasswordException();
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void grantPermission(Permission permission) {
        if (permission == null) throw new InvalidPermissionException("Permission cannot be null");
        this.permissions.add(permission);
    }

    public void revokePermission(Permission permission) {
        if (permission == null) throw new InvalidPermissionException("Permission cannot be null");
        this.permissions.remove(permission);
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }
}
