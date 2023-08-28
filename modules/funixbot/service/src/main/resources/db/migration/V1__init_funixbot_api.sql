create table if not exists funixbot_commands
(
    id         bigint generated by default as identity primary key,
    created_at timestamp    not null,
    updated_at timestamp,
    uuid       varchar(255) not null constraint UK_command_public_id unique,
    command    varchar(30)  not null constraint UK_command_name unique,
    message    varchar(255) not null
);

create table if not exists funixbot_users_experience
(
    id                bigint generated by default as identity primary key,
    created_at        timestamp    not null,
    updated_at        timestamp,
    uuid              varchar(255) not null constraint UK_fbot_user_public_id unique,
    last_message_date timestamp    not null,
    level             integer      not null,
    twitch_user_id    varchar(255) not null constraint UK_twitch_id_user unique,
    xp                integer      not null,
    xp_next_level     integer      not null
);