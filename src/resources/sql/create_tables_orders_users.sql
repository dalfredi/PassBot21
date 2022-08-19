drop schema if exists bot cascade;
create schema if not exists bot;

drop table if exists bot.users cascade;
drop table if exists bot.orders cascade;

create table if not exists bot.users (
                        user_id      serial primary key,
                        chat_id      bigint,
                        login        text,
                        email        text,
                        campus       text,
                        name         text,
                        surname      text,
                        patronymic   text,
                        role         text,
                        registered   boolean
);

create table if not exists bot.orders (
                        order_id     serial primary key,
                        status       text,
                        start_time   timestamp,
                        end_time     timestamp,
                        duration     integer,
                        peer_id      integer references bot.users(user_id),
                        guest_id     integer references bot.users(user_id),
                        admin_id     integer references bot.users(user_id)
);