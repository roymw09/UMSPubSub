package org.ac.cst8277.williams.roy.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Subscriber {
    @Id
    private Integer id;
    private Integer user_id;
}
