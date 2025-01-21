CREATE TABLE profile
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_profile PRIMARY KEY (id)
);

CREATE TABLE user
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255)          NOT NULL,
    last_name  VARCHAR(255)          NOT NULL,
    email      VARCHAR(255)          NOT NULL,
    password   VARCHAR(255)          NOT NULL,
    roles      VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_profile
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NOT NULL,
    profile_id BIGINT                NOT NULL,
    CONSTRAINT pk_userprofile PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user_profile
    ADD CONSTRAINT FK_USERPROFILE_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES profile (id);

ALTER TABLE user_profile
    ADD CONSTRAINT FK_USERPROFILE_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);