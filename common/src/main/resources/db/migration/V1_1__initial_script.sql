create table if not exists public.users
(
    id            bigserial
        constraint users_pk
            primary key
        unique,
    username      varchar(256),
    email         varchar(256)               not null
        unique,
    user_password varchar(256)               not null,
    created       timestamp(6) default now() not null,
    changed       timestamp(6)               not null,
    is_deleted    boolean      default false not null,
    is_active        boolean      default false
);

alter table public.users
    owner to trainlab;

create unique index users_email_uindex
    on public.users (email);

create unique index users_username_uindex
    on public.users (username);

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
    session_id    varchar,
    ip_address    varchar
);

alter table public.sessions
    owner to trainlab;

create table if not exists public.roles
(
    id         serial
        constraint roles_pk
            primary key
        unique,
    role_name  varchar(256) not null,
    created    timestamp(6) not null,
    changed    timestamp(6) not null,
    is_deleted boolean
);

alter table public.roles
    owner to trainlab;

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