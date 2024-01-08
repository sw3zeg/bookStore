package com.example.bookstore.app.service;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.model.CustomerRole.CustomerRole_entity;
import com.example.bookstore.app.model.customer.Customer_EditDto;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.model.customer.Customer_view;
import com.example.bookstore.app.model.role.Role_entity;
import com.example.bookstore.app.repository.CustomerDao;
import com.example.bookstore.app.repository.CustomerRoleDao;
import com.example.bookstore.app.repository.RoleDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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

    public Customer_entity findCustomerByUsername(String username) {
        return customerRepository.getCustomerByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {

        Customer_entity customer_entity = findCustomerByUsername(username);

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

        customerRoleRepository.addRoleToCustomer(
                new CustomerRole_entity(customer_id, AppConstants.ROLE_USER));
    }



    public ResponseEntity<String> editCustomer(Customer_EditDto customer) {

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.editCustomer(customer);

        return new ResponseEntity<>(
                "Customer was changes successful",
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> deleteCustomer(Long customer_id) {

        customerRepository.deleteCustomer(customer_id);

        return new ResponseEntity<>(
                "Customer was deleted successful",
                HttpStatus.OK
        );
    }

    public ResponseEntity<Collection<Customer_entity>> getCustomers(Long offset, Long limit, String query) {

        return new ResponseEntity<>(
                customerRepository.getCustomers(offset, limit, query),
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> addBalance(Long customerId, Long balance) {

        Long newBalance = customerRepository.addBalance(customerId, balance);

        return new ResponseEntity<>(
                "Money was added. Balance now - %s".formatted(newBalance),
                HttpStatus.OK
        );
    }

    public void reduceBalance(Long customerId, Long balance) {
        customerRepository.reduceBalance(customerId, balance);
    }

    public Long indexOfCustomerByName(String username) {
        return customerRepository.indexOfCustomerByUsername(username);
    }

}
