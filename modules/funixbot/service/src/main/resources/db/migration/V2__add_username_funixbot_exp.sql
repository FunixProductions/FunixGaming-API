ALTER TABLE funixbot_users_experience
    ADD COLUMN twitch_username VARCHAR(200) DEFAULT '' NOT NULL,
    ADD CONSTRAINT uk_funixbot_users_experience_twitch_username UNIQUE (twitch_username);
