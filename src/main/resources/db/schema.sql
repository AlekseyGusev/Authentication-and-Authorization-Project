DROP TABLE users IF EXISTS;
DROP TABLE specialties IF EXISTS;
DROP TABLE trainers IF EXISTS;
DROP TABLE group_classes IF EXISTS;
DROP TABLE user_group_classes IF EXISTS;

CREATE TABLE administrations
(
    id           INTEGER IDENTITY PRIMARY KEY,
    first_name   VARCHAR (25),
    last_name    VARCHAR (25),
    username     VARCHAR (255),
    password     VARCHAR (255),
    role_name_id INTEGER NOT NULL,
    enabled      BOOLEAN NOT NULL
);

CREATE TABLE users
(
    id           INTEGER IDENTITY PRIMARY KEY,
    first_name   VARCHAR (25),
    last_name    VARCHAR (25),
    username     VARCHAR (255),
    password     VARCHAR (255),
    trainer_id   INTEGER,
    role_name_id INTEGER NOT NULL,
    enabled      BOOLEAN NOT NULL
);

CREATE TABLE trainers
(
    id           INTEGER IDENTITY PRIMARY KEY,
    first_name   VARCHAR (25),
    last_name    VARCHAR (25),
    username     VARCHAR (255),
    password     VARCHAR (255),
    role_name_id INTEGER NOT NULL,
    enabled      BOOLEAN NOT NULL
);
ALTER TABLE users ADD CONSTRAINT fk_users_trainer FOREIGN KEY (trainer_id) REFERENCES trainers (id);

CREATE TABLE specialties (
    id   INTEGER IDENTITY PRIMARY KEY,
    name VARCHAR(80)
);

CREATE TABLE trainer_specialties (
    trainer_id   INTEGER NOT NULL,
    specialty_id INTEGER NOT NULL
);
ALTER TABLE trainer_specialties ADD CONSTRAINT fk_trainer_specialties_trainers FOREIGN KEY (trainer_id) REFERENCES trainers (id);
ALTER TABLE trainer_specialties ADD CONSTRAINT fk_trainer_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

CREATE TABLE group_classes
(
    id         INTEGER IDENTITY PRIMARY KEY,
    name       VARCHAR (25),
    date       TIMESTAMP,
    trainer_id INTEGER
);
ALTER TABLE group_classes ADD CONSTRAINT fk_group_classes_trainer FOREIGN KEY (trainer_id) REFERENCES trainers (id);

CREATE TABLE user_group_classes
(
    group_class_id INTEGER NOT NULL,
    user_id      INTEGER NOT NULL
);
ALTER TABLE user_group_classes ADD CONSTRAINT fk_user_group_class FOREIGN KEY (group_class_id) REFERENCES group_classes (id);
ALTER TABLE user_group_classes ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE roles
(
    id        INTEGER IDENTITY PRIMARY KEY,
    role_name VARCHAR (25)
);
ALTER TABLE users ADD CONSTRAINT fk_user_role FOREIGN KEY (role_name_id) REFERENCES roles (id);
ALTER TABLE trainers ADD CONSTRAINT fk_trainer_role FOREIGN KEY (role_name_id) REFERENCES roles (id);