package com.example.bookstore.app.model.CustomerRole;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CustomerRole_entity {

    private Long customer_id;
    private Long role_id;

    public CustomerRole_entity(Long customer_id, Long role_id) {
        this.customer_id = customer_id;
        this.role_id = role_id;
    }

    public CustomerRole_entity() {

    }
}
