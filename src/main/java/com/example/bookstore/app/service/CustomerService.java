package com.example.bookstore.app.service;


import com.example.bookstore.app.constants.AppConstants;
import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.customer.Customer_entity;
import com.example.bookstore.app.model.customer.Customer_model;
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

    public Customer_entity findCustomerByUsername(String username) {

        Optional<Customer_entity> customer = customerRepository.getCustomerByUsername(username);

        if (customer.isEmpty()) {
            throw new BadRequestException("Customer '%s' doesnt exists".formatted(username));
        }

        return customer.get();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {

        Customer_entity customer = findCustomerByUsername(username);

        Collection<Role_entity> roles = roleService.getRolesOfCustomer(username);

        return new User(
                customer.getUsername(),
                customer.getPassword(),
                roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    @Transactional
    public void createNewUser(Customer_model customer) {

        if (customerRepository.isCustomerExists(customer.getUsername())) {
            throw new BadRequestException("Customer '%s' already exists".formatted(customer.getUsername()));
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerRepository.createCustomer(customer);

        customerRoleRepository.addRoleToCustomer(customer.getUsername(), AppConstants.ROLE_USER);
    }



    public ResponseEntity<String> editCustomer(Customer_model customer) {

        if (customerRepository.isCustomerExists(customer.getUsername())) {
            throw new BadRequestException("Customer '%s' already exists".formatted(customer.getUsername()));
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerRepository.editCustomer(customer);

        return new ResponseEntity<>(
                "Customer was changes successful",
                HttpStatus.OK
        );
    }

    public ResponseEntity<String> deleteCustomer(String username) {

        if (customerRepository.isCustomerExists(username)) {
            throw new BadRequestException("Customer '%s' already exists".formatted(username));
        }

        customerRepository.deleteCustomer(username);

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

    public ResponseEntity<String> addBalance(String username, Long balance) {

        if (customerRepository.isCustomerExists(username)) {
            throw new BadRequestException("Customer '%s' already exists".formatted(username));
        }

        Long newBalance = customerRepository.addBalance(username, balance);

        return new ResponseEntity<>(
                "Money was added. Balance now - %s".formatted(newBalance),
                HttpStatus.OK
        );
    }

    public void reduceBalance(String username, Long balance) {

        if (customerRepository.getBalance(username) < balance) {
            throw new BadRequestException("You have no money");
        }

        customerRepository.reduceBalance(username, balance);
    }

}
