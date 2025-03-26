

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




-- test data

INSERT INTO genre (title)
VALUES
    ('Фантастика'),
    ('Детектив'),
    ('Роман'),
    ('Ужасы'),
    ('Приключения');


INSERT INTO author (fio, biography, photo)
VALUES
    ('Иван Иванов', 'Иван Иванов — российский писатель, известный научно-фантастическими произведениями.', 'ivanov.jpg'),
    ('Анна Петрова', 'Анна Петрова — автор детективных историй, живёт в Санкт-Петербурге.', 'petrova.jpg'),
    ('Александр Сидоров', 'Александр Сидоров специализируется на романтической прозе и классической литературе.', 'sidorov.jpg'),
    ('Мария Смирнова', 'Мария Смирнова — автор историй в жанре ужасов и мистики.', 'smirnova.jpg'),
    ('Дмитрий Кузнецов', 'Дмитрий Кузнецов пишет приключенческие рассказы для молодёжи.', 'kuznetsov.jpg');


INSERT INTO book (
    title,
    description,
    pages,
    score_sum,
    score_count,
    image,
    author_id,
    genre_id,
    price
)
VALUES
    ('Звёздный путь', 'Космическая эпопея о путешествии к далеким звездам.', 350, 80, 10, 'starpath.jpg', 1, 1, 350),
    ('Загадка старого дома', 'Детективная история о таинственных событиях в старом поместье.', 280, 90, 15, 'oldhouse.jpg', 2, 2, 400),
    ('Любовь и судьба', 'Роман о непростых отношениях и судьбах двух молодых людей.', 500, 120, 20, 'lovefate.jpg', 3, 3, 450),
    ('Тьма во тьме', 'История о страшных событиях и паранормальных явлениях.', 220, 60, 12, 'darkness.jpg', 4, 4, 300),
    ('Пираты Золотого моря', 'Приключение в поисках затерянных сокровищ и далеких земель.', 380, 100, 25, 'pirates.jpg', 5, 5, 500);


INSERT INTO customer (email, username, password, balance)
VALUES
    ('john@example.com', 'john_smith', 'pass123', 1000),
    ('anna@example.com', 'anna_ivanova', 'qweasd', 2000),
    ('mike@example.com', 'mike_jones', 'mkpass', 1500),
    ('alice@example.com', 'alice_lee', 'alicepwd', 3000),
    ('bob@example.com', 'bob_petrov', 'bobpass', 500);


INSERT INTO review (
    text,
    mark,
    customer_username,
    book_id
)
VALUES
    ('Очень увлекательная книга, советую всем!', 9, 'john_smith', 1),
    ('Неоднозначное впечатление, но сюжет держит в напряжении.', 7, 'anna_ivanova', 1),
    ('Люблю детективы, книга увлекла с первых страниц.', 8, 'mike_jones', 2),
    ('Мрачно и местами жутковато, но мне понравилось.', 6, 'alice_lee', 4),
    ('Отличная история про пиратов, хочется продолжения!', 9, 'bob_petrov', 5);


INSERT INTO customer_book (customer_username, book_id)
VALUES
    ('john_smith', 1),
    ('john_smith', 2),
    ('anna_ivanova', 1),
    ('mike_jones', 2),
    ('bob_petrov', 5);


insert into role (name)
values ('ROLE_USER'), ('ROLE_ADMIN');


insert into customer (email, username, password)
values ('admin@admin', 'admin', '$2a$10$avesy5DnKxXKRyXE2ycaOOF/fOl72rS0dSjasAB8VhOCYtHT7C0c6'),-- p
       ('customer@customer', 'customer', '$2a$10$avesy5DnKxXKRyXE2ycaOOF/fOl72rS0dSjasAB8VhOCYtHT7C0c6');-- p


insert into customer_role (customer_username, role_name)
values ('admin', 'ROLE_ADMIN'),
       ('admin', 'ROLE_USER'),
       ('customer', 'ROLE_USER');