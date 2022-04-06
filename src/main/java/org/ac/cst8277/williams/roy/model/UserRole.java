package org.ac.cst8277.williams.roy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_roles")
public class UserRole {
    private Integer user_id;
    private String role_id;
    private String role;
    private String description;
    private String refreshToken;
}
