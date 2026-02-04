create table if not exists public.announcement
(
    announcement_id    uuid    not null
        primary key,
    announcement_body  text    not null,
    announcement_type  varchar not null,
    announcement_date  date    not null,
    announcement_photo varchar,
    add_event          boolean default false
);


create table if not exists public.news
(
    news_id    uuid not null
        primary key,
    news_body  text not null,
    news_data  date not null,
    news_photo varchar
);


create table if not exists public.person
(
    user_id           uuid    not null
        primary key,
    user_name         varchar not null
        unique,
    user_email        varchar not null
        unique,
    user_library_code varchar
        unique
);


create table if not exists public.expected_book
(
    expected_book_id          uuid      not null
        primary key,
    expected_book_name        varchar   not null,
    expected_book_year        integer   not null,
    expected_book_publication varchar   not null,
    expected_book_language    varchar   not null,
    expected_book_image       varchar,
    expected_book_genre       varchar   not null,
    expected_book_added_at    timestamp not null,
    expected_book_author      varchar   not null,
    expected_book_pieces      integer   not null
);

create table if not exists public.books
(
    book_id          uuid    not null
        primary key,
    book_author      varchar not null,
    book_year        integer not null,
    book_publication varchar,
    book_language    varchar not null,
    book_pieces      integer,
    book_image       varchar,
    book_genre       varchar not null,
    book_added_at    timestamp,
    book_name        varchar
);

create table if not exists public.reservation
(
    reservation_id        uuid      not null
        primary key,
    reservation_date      date      not null,
    created_at            timestamp not null,
    reservation_book_id   uuid
                                    references public.books
                                        on delete set null,
    reservation_person_id uuid
                                    references public.person
                                        on delete set null,
    reservation_status    varchar default 'CREATED'::character varying
);


create table if not exists public.report_availability
(
    id               uuid                                         not null
        primary key,
    user_id          uuid
                                                                  references public.person
                                                                      on delete set null,
    expected_book_id uuid
                                                                  references public.expected_book
                                                                      on delete set null,
    user_email       varchar
                                                                  references public.person (user_email)
                                                                      on delete set null,
    status           varchar default 'CREATED'::character varying not null,
    username         varchar                                      not null
);


create table if not exists public.report_availability_errors
(
    id               uuid                                         not null
        primary key,
    user_id          uuid
                                                                  references public.person
                                                                      on delete set null,
    expected_book_id uuid
                                                                  references public.expected_book
                                                                      on delete set null,
    user_email       varchar
                                                                  references public.person (user_email)
                                                                      on delete set null,
    status           varchar default 'CREATED'::character varying not null,
    error            varchar                                      not null,
    username         varchar                                      not null
);


create table if not exists public.auth_user
(
    id            uuid    not null
        primary key,
    user_name     varchar not null
        unique,
    user_password varchar not null,
    user_role     varchar not null,
    user_email    varchar not null
);


