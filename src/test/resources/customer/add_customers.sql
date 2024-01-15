

insert into customer (email, username, password)
values ('admin@admin', 'admin', '$2a$10$avesy5DnKxXKRyXE2ycaOOF/fOl72rS0dSjasAB8VhOCYtHT7C0c6'),-- p
       ('customer@customer', 'customer', '$2a$10$avesy5DnKxXKRyXE2ycaOOF/fOl72rS0dSjasAB8VhOCYtHT7C0c6');-- p

insert into role (name)
values ('ROLE_USER'), ('ROLE_ADMIN');

insert into customer_role (customer_username, role_name)
values ('admin', 'ROLE_ADMIN'),
       ('admin', 'ROLE_USER'),
       ('customer', 'ROLE_USER');
