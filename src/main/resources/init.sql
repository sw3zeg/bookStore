create table genre(
                      id          bigserial       not null ,
                      title       varchar(50)     not null ,

                      primary key (id)
);

create table author(
                       id          bigserial       not NULL ,
                       fio        varchar(100)    not null ,
                       biography   varchar(500)    not null ,
                       photo       varchar(100)    not null ,

                       primary key (id)
);

create table book(
                     id            bigserial                        not NULL ,
                     title         varchar(50)                      not null ,
                     description   varchar(500)                     not null ,
                     pages         INT                              not null ,
                     score_sum     INT                              not null ,
                     score_count   INT                              not null ,
                     release       timestamp without time zone      not null     default now(),
                     image         varchar(100)                     not null ,
                     author_id     INT                              not null ,
                     genre_id      INT                              not null ,
                     price         INT                              NOT NULL default 0,

                     CHECK ( price > 0 ),
                     primary key (id),
                     foreign key (author_id) REFERENCES author(id),
                     foreign key (genre_id) REFERENCES genre(id)
);

create table customer(
                         id          bigserial       not null ,
                         email       varchar(50)     not null ,
                         username    varchar(50)     not null unique ,
                         password    varchar     not null ,
                         balance     INT        not null default 0,

                         CHECK ( balance > 0 ),
                         primary key (id)
);

create table review(
                       id            bigserial       not null ,
                       text          varchar(500)    not null ,
                       customer_id   INT             not null ,
                       book_id       INT             not null ,

                       primary key (id),
                       foreign key (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
                       foreign key (book_id) REFERENCES book(id) ON DELETE CASCADE
);

create table customer_book(
                              customer_id       INT        not null ,
                              book_id           INT        not null ,

                              primary key (customer_id, book_id),
                              FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
                              FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

create table role(
    id bigserial ,
    name varchar ,
    primary key (id)
);

create table customer_role(
                              customer_id       INT        not null ,
                              role_id           INT        not null ,

                              primary key (customer_id, role_id),
                              FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
                              FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);