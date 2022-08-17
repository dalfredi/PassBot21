create schema if not exists passbot;
DROP TABLE IF EXISTS passbot.users ;
DROP TABLE IF EXISTS passbot.orders;
CREATE TABLE IF NOT EXISTS passbot.users (
                        user_id      INT GENERATED ALWAYS AS IDENTITY,
                        name         text not null,
                        surname      text,
                        patronymic   text,
                        role         text,
                        PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS passbot.orders (
                        order_id     INT GENERATED ALWAYS AS IDENTITY,
                        number       integer,
                        status       text,
                        start_time   timestamp without time zone,
                        end_time     timestamp without time zone,
                        duration     integer,
                        peer_id      INT,
                        guest_id     INT,
                        admin_id     INT,
                        PRIMARY KEY(order_id),
                        CONSTRAINT fk_peer
                            FOREIGN KEY (peer_id)
                                REFERENCES passbot.users(user_id),
                        CONSTRAINT fk_guest
                            FOREIGN KEY (guest_id)
                                REFERENCES passbot.users(user_id),
                        CONSTRAINT fk_admin
                            FOREIGN KEY (admin_id)
                                REFERENCES passbot.users(user_id)
);