CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL CONSTRAINT users_pk PRIMARY KEY UNIQUE,
    username VARCHAR(256),
    email VARCHAR(256) NOT NULL UNIQUE,
    user_password VARCHAR(256) NOT NULL,
    created TIMESTAMP(6) DEFAULT now() NOT NULL,
    changed TIMESTAMP(6) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    is_active BOOLEAN DEFAULT FALSE
);

ALTER TABLE users OWNER TO trainlab;

CREATE UNIQUE INDEX users_email_uindex ON users(email);

CREATE UNIQUE INDEX users_username_uindex ON users(username);

CREATE TABLE IF NOT EXISTS roles(
    id SERIAL CONSTRAINT roles_pk PRIMARY KEY,
    role_name  VARCHAR(256) NOT NULL,
    created    TIMESTAMP(6) NOT NULL,
    changed    TIMESTAMP(6) NOT NULL,
    is_deleted BOOLEAN
);

ALTER TABLE roles OWNER TO trainlab;

CREATE INDEX roles_changed_id_index ON roles (changed asc, id desc);

CREATE TABLE IF NOT EXISTS users_roles(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id INT,
    created TIMESTAMP(6) DEFAULT now() NOT NULL,
    changed TIMESTAMP(6) DEFAULT now() NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);

ALTER TABLE users_roles OWNER TO trainlab;

CREATE TABLE frontend_data(
    id SERIAL PRIMARY KEY,
    front_id REAL NOT NULL UNIQUE,
    text VARCHAR NOT NULL,
    created TIMESTAMP(6) DEFAULT now() NOT NULL,
    changed TIMESTAMP(6) NOT NULL,
    is_deleted boolean DEFAULT FALSE NOT NULL
);

ALTER TABLE frontend_data OWNER TO trainlab;

CREATE INDEX frontend_data_created_index ON frontend_data(created desc);
CREATE INDEX frontend_data_front_id_index ON frontend_data (front_id);


create index roles_changed_id_index
    on public.roles (changed asc, id desc);

create table if not exists public.users_roles
(
    id         bigserial
        primary key
        unique,
    user_id    bigint                     not null,
    role_id    integer,
    created    timestamp(6) default now() not null,
    changed    timestamp(6) default now() not null,
    is_deleted boolean      default false not null
);

alter table public.users_roles
    owner to trainlab;

create table public.frontend_data
(
    id         serial
        primary key,
    front_id   real                       not null
        unique,
    text       varchar                    not null,
    created    timestamp(6) default now() not null,
    changed    timestamp(6)               not null,
    is_deleted boolean      default false not null
);

alter table public.frontend_data
    owner to trainlab;

create index frontend_data_created_index
    on public.frontend_data (created desc);

create index frontend_data_front_id_index
    on public.frontend_data (front_id);
