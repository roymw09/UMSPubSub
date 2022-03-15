package org.ac.cst8277.williams.roy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private Integer id;
    private String email;
    private String username;
    private String password;
    private UUID token;

    public User() {
        this.token = new UUID(2, 10);
    }
}