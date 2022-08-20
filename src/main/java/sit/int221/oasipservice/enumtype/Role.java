package sit.int221.oasipservice.enumtype;

public enum Role {
    ADMIN("admin"),
    LECTURER("lecturer"),
    STUDENT("student");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
