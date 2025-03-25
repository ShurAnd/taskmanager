create table if not exists users(
    id serial unique primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    username varchar(20) not null unique,
    password varchar(100) not null
);

create table if not exists authorities(
    id serial unique primary key,
    authority varchar(50) not null,
    user_id bigint not null references users(id)
);

create table if not exists tasks(
    id serial unique primary key,
    title varchar(50) not null,
    description text not null,
    status smallint not null,
    priority smallint not null,
    author_id bigint not null references users(id),
    performer_id bigint references users(id)
);

create table if not exists comments(
    id serial unique primary key,
    task_id bigint not null references tasks(id),
    comment text not null,
    author_id bigint not null references users(id)
);