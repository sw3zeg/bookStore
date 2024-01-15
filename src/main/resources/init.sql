

create table genre(
    id      bigserial   not null ,
    title   varchar     not null ,

    primary key (id)
);

create table author(
    id              bigserial   not NULL ,
    fio             varchar     not null ,
    biography       varchar     not null ,
    photo           varchar     not null ,

    primary key (id)
);

create table book(
    id              bigserial                       not NULL ,
    title           varchar                         not null ,
    description     varchar                         not null ,
    pages           INT                             not null ,
    score_sum       INT                             not null ,
    score_count     INT                             not null ,
    release         timestamp without time zone     not null    default now(),
    image           varchar                         not null ,
    author_id       INT                             not null ,
    genre_id        INT                             not null ,
    price           INT                             NOT NULL    default 0,

    CHECK ( price > 0 ),
    primary key (id),
    foreign key (author_id) REFERENCES author(id),
    foreign key (genre_id) REFERENCES genre(id)
);

create table customer(
    email           varchar         not null ,
    username        varchar(50)     not null unique ,
    password        varchar         not null ,
    balance         INT             not null default 0,

    CHECK ( balance >= 0 ),
    primary key (username)
);

create table review(
    text                varchar                         not null ,
    created             timestamp without time zone     not null    default now(),
    updated             timestamp without time zone     not null    default now(),
    mark                INT                             not null ,
    customer_username   varchar(50)                     not null ,
    book_id             INT                             not null ,

    check ( mark >= 0 and mark <= 10 ),
    primary key (customer_username, book_id),
    foreign key (customer_username) REFERENCES customer(username) ON DELETE CASCADE,
    foreign key (book_id) REFERENCES book(id) ON DELETE CASCADE
);

create table customer_book(
    customer_username     varchar(50)     not null ,
    book_id               INT             not null ,

    primary key (customer_username, book_id),
    FOREIGN KEY (customer_username) REFERENCES customer(username) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

create table role(
    name varchar ,

    primary key (name)
);

create table customer_role(
    customer_username   varchar(50)     not null ,
    role_name           varchar         not null ,

    primary key (customer_username, role_name),
    FOREIGN KEY (customer_username) REFERENCES customer(username) ON DELETE CASCADE,
    FOREIGN KEY (role_name) REFERENCES role(name) ON DELETE CASCADE
);