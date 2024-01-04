package com.example.bookstore.app.service;


import com.example.bookstore.app.model.CustomerRole.CustomerRole_entity;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.model.customer.Customer_view;
import com.example.bookstore.app.model.role.Role_entity;
import com.example.bookstore.app.repository.CustomerDao;
import com.example.bookstore.app.repository.CustomerRoleDao;
import com.example.bookstore.app.repository.RoleDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService implements UserDetailsService {


    private final CustomerDao customerRepository;
    private final CustomerRoleDao customerRoleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    //@Lazy чтобы не было циклической зависимости с securityConfiguration. чтобы passwordEncoder успел инициализироваться
    @Lazy
    public CustomerService(CustomerDao customerRepository, RoleDao roleRepository, CustomerRoleDao customerRoleRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.customerRoleRepository = customerRoleRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Customer_entity> findByUsername(String username) {
        return customerRepository.getCustomerByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var a = findByUsername(username);

        Customer_entity customer_entity = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));

        Collection<Role_entity> roles = roleService.getRolesOfCustomer(customer_entity.getId());

        Customer_view customer = new Customer_view(customer_entity, roles);

        return new User(
                customer.getUsername(),
                customer.getPassword(),
                customer.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    @Transactional
    public void createNewUser(Customer_model customer) {

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Long customer_id = customerRepository.createCustomer(customer);

        customerRoleRepository.addRoleToCustomer(new CustomerRole_entity(customer_id, 2L));
    }
}
