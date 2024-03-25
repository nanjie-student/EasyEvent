DROP TABLE IF EXISTS event;
CREATE TABLE event(
    id SERIAL,
    title varchar(100) NOT NULL,
    desciption varchar(255) NOT NULL,
    price float NOT NULl,
    date timestamp  not null,
    primary key (id)
)
