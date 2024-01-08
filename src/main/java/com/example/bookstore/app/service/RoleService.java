package com.example.bookstore.app.service;


import com.example.bookstore.app.model.role.Role_entity;
import com.example.bookstore.app.repository.RoleDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleDao roleRepository;

    public Collection<Role_entity> getRolesOfCustomer(Long customer_id) {
        return roleRepository.getRolesOFCustomer(customer_id);
    }
}
