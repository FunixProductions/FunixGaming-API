ALTER TABLE funixbot_users_experience ADD COLUMN twitch_username VARCHAR(200) DEFAULT '' NOT NULL constraint funixbot_users_experience_key_twitch_username_unique unique;
