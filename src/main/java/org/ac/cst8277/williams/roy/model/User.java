package org.ac.cst8277.williams.roy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private Integer id;
    private String username;

    @PersistenceConstructor
    public User(String username) {
        this.username = username;
    }
}