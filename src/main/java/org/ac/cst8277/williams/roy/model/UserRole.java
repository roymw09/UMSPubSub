package org.ac.cst8277.williams.roy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_roles")
public class UserRole {
    @Id
    private Integer id;
    private Integer user_id;
    private Integer role_id;
    private String role;
    private String description;
    private String refreshToken;
}
