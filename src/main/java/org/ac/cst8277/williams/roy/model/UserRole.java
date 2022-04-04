package org.ac.cst8277.williams.roy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("user_roles")
public class UserRole {
    private String role_id;
    private String role;
    private String description;

    @Override
    public String toString() {
        return "roleId: " + role_id + ",\n" + "role: " + role + ",\n" + "description: " + description;
    }
}
