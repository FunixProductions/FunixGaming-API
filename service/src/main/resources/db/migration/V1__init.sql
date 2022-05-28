create table api_users
(
    id         bigint auto_increment primary key,
    created_at datetime(6)   not null,
    updated_at datetime(6)   null,
    uuid       varchar(255)  not null,
    banned     bit           not null,
    email      varchar(255)  not null,
    password   varchar(2000) not null,
    role       varchar(255)  not null,
    username   varchar(255)  not null,
    constraint UK_user_username unique (username),
    constraint UK_user_public_id unique (uuid)
);

create table api_users_tokens
(
    id              bigint auto_increment primary key,
    created_at      datetime(6)   not null,
    updated_at      datetime(6)   null,
    uuid            varchar(255)  not null,
    expiration_date datetime(6)   null,
    token           varchar(2000) not null,
    user_id         bigint        not null,
    constraint UK_api_token unique (token) using hash,
    constraint UK_token_public_id unique (uuid),
    constraint token_link_user foreign key (user_id) references api_users (id)
);

create table funixbot_commands
(
    id         bigint auto_increment primary key,
    created_at datetime(6)  not null,
    updated_at datetime(6)  null,
    uuid       varchar(255) not null,
    command    varchar(30)  not null,
    message    varchar(255) not null,
    constraint UK_command_name unique (command),
    constraint UK_command_public_id unique (uuid)
);

create table funixbot_users_experience
(
    id                bigint auto_increment primary key,
    created_at        datetime(6)  not null,
    updated_at        datetime(6)  null,
    uuid              varchar(255) not null,
    last_message_date datetime(6)  not null,
    level             int          not null,
    twitch_user_id    varchar(255) not null,
    xp                int          not null,
    xp_next_level     int          not null,
    constraint UK_fbot_user_public_id unique (uuid),
    constraint UK_twitch_id_user unique (twitch_user_id)
);

create table funix_api_mails
(
    id         bigint auto_increment primary key,
    created_at datetime(6)    not null,
    updated_at datetime(6)    null,
    uuid       varchar(255)   not null,
    from_mail  varchar(255)   not null,
    subject    varchar(255)   not null,
    text       varchar(10000) not null,
    to_mail    varchar(255)   not null,
    send       bit            not null,
    constraint UK_mail_id_public unique (uuid)
);
