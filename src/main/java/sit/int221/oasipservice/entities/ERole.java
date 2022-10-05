package sit.int221.oasipservice.entities;

public enum ERole {
    ROLE_ADMIN("admin"),
    ROLE_LECTURER("lecturer"),
    ROLE_STUDENT("student");

    private final String role;

    ERole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
