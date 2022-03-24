package org.ac.cst8277.williams.roy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {
    @Id
    private Integer id;
    private String email;
    private String username;
    private String password;
    @Transient
    private List<UserRole> roles;

    @PersistenceConstructor
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = null;
    }
}