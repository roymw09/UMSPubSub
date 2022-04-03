package org.ac.cst8277.williams.roy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.relational.core.mapping.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table("users")
public class User {
    @Id
    private Integer id;
    private String username;
    @Column(columnDefinition = "text[]")
    @Type(type = "org.ac.cst8277.williams.roy.util.CustomArrayType")
    private String[] roles;

    public User(String username) {
        this.username = username;
    }

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}