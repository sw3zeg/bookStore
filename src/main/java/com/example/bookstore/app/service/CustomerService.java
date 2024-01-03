package com.example.bookstore.app.service;


import com.example.bookstore.app.model.auth.RegistrationCustomerDto;
import com.example.bookstore.app.model.customer.Customer_model;
import com.example.bookstore.app.model.customer.Customer_view;
import com.example.bookstore.app.repository.CustomerDao;
import com.example.bookstore.app.repository.RoleDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService implements UserDetailsService {


    private final CustomerDao customerRepository;
    private final RoleDao roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public CustomerService(CustomerDao customerRepository, RoleDao roleRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Customer_view> findByUsername(String username) {
        return customerRepository.getCustomerByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer_view customer = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));

        customer.setRoles(roleRepository.getRolesOFCustomer(customer.getId()));

        return new User(
                customer.getUsername(),
                customer.getPassword(),
                customer.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public void createNewUser(RegistrationCustomerDto registrationCustomerDto) {

        //customer.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        Customer_model customer = new Customer_model();
        customer.setUsername(registrationCustomerDto.getUsername());
        customer.setEmail(registrationCustomerDto.getEmail());
        customer.setPassword(passwordEncoder.encode(registrationCustomerDto.getPassword()));

        Long id = customerRepository.createCustomer(customer);
//        customerRepository.

    }
}
