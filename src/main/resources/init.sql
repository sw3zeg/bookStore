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

                     primary key (id),
                     foreign key (author_id) REFERENCES author(id),
                     foreign key (genre_id) REFERENCES genre(id)
);

create table review(
                       id            bigserial       not null ,
                       name          varchar(100)    not null ,
                       text          varchar(500)    not null ,

                       primary key (id)
);

create table book_review(
                            book_id     INT       not null ,
                            review_id   INT       not null ,

                            primary key (book_id, review_id),
                            FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
                            FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
);

create table customer(
                         id          bigserial       not null ,
                         mail        varchar(50)     not null ,
                         name        varchar(50)     not null ,
                         password    varchar(50)     not null ,

                         primary key (id)
);

create table customer_book(
                          customer_id       INT        not null ,
                          book_id           INT        not null ,

                          primary key (customer_id, book_id),
                          FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
                          FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);