create table public.test
(
    id    serial
        primary key
        unique,
    hello varchar
);

alter table public.test
    owner to trainlab;

create table frontend_data
(
    id         serial
        primary key
        unique,
    front_id   real                       not null,
    text       varchar                    not null,
    created    timestamp(6) default now() not null,
    changed    timestamp(6)               not null,
    is_deleted boolean      default false not null
);

create index frontend_data_created_index
    on frontend_data (created desc);

create index frontend_data_front_id_index
    on frontend_data (front_id);

alter table frontend_data
    add unique (front_id);



create table users
(
    id         bigint                     not null
        constraint users_pk
            primary key
        constraint users_sessions_user_id_fk
            references sessions (user_id),
    username   varchar(256)               not null,
    email      varchar(256)               not null,
    password   varchar(256)               not null,
    role_id    integer
        constraint users_roles_id_fk
            references roles,
    created    timestamp(6) default now() not null,
    changed    timestamp(6)               not null,
    is_deleted boolean      default false not null
);

alter table users
    owner to trainlab;

create unique index users_email_uindex
    on users (email);

create unique index users_username_uindex
    on users (username);


create table roles
(
    id         integer      not null
        constraint roles_pk
            primary key,
    role_name  varchar(256) not null,
    created    timestamp(6) not null,
    changed    timestamp(6) not null,
    is_deleted boolean
);

alter table roles
    owner to trainlab;

create table public.sessions
(
    id            bigserial
        primary key
        unique,
    user_id       bigint
        constraint sessions_users_id_fk
            references public.users,
    session_token varchar(256)               not null
        unique,
    created       timestamp(6) default now() not null,
    changed       timestamp(6)               not null,
    is_deleted    boolean      default false not null,
    session_id    varchar
);

alter table public.sessions
    owner to trainlab;

