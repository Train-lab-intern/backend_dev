create table public.test
(
    id    serial
        primary key
        unique,
    hello varchar
);

alter table public.test
    owner to trainlab;
