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

create table public.sessions
(
    id            serial
        primary key,
    user_id       integer                    not null
        unique,
    session_token varchar(255)               not null
        unique,
    created       timestamp(6) default now() not null,
    changed       timestamp(6)               not null,
    is_deleted    boolean      default false not null
);

alter table public.sessions
    owner to trainlab;

