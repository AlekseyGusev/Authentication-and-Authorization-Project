INSERT INTO specialties (name) VALUES ('PERSONAL');
INSERT INTO specialties (name) VALUES ('GROUP');

INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_TRAINER');
INSERT INTO roles (role_name) VALUES ('ROLE_USER');

INSERT INTO trainers (first_name, last_name, username, password, role_name_id, enabled) VALUES ('Jack', 'Smith', 'smith', '$2a$10$kYiBkxwKpVZAhXVojlTv.eueDSrV.6PA8M9R79YeQC4WIRfuH2c.y', 2, true);
INSERT INTO trainers (first_name, last_name, username, password, role_name_id, enabled) VALUES ('Alice', 'Petterson', 'petterson', '$2a$10$sVZIfQui/7vI91b/kBQ6oeaTBXPClNRbvR5gTERrVkRtnEZI4Gb3e', 2, true);

INSERT INTO trainer_specialties VALUES (1, 1);
INSERT INTO trainer_specialties VALUES (2, 2);

INSERT INTO administrations (first_name, last_name, username, password, role_name_id, enabled) VALUES ('Admin', 'Admin', 'admin', '$2a$10$PdMTVB3nYno42eWo5BmcS.v4nD4EHC9d53ByRGWYgkimkF/cykT.K', 1, true);

INSERT INTO users (first_name, last_name, username, password, trainer_id, role_name_id, enabled) VALUES ('John', 'Watson', 'watson', '$2a$10$R9LibvwBYavDbsBcHysKT.J70eRSR8FpHL.uqgDBkXV2CGu5zF4dq', null, 3, true);
INSERT INTO users (first_name, last_name, username, password, trainer_id, role_name_id, enabled) VALUES ('Melany', 'Brook', 'brook', '$2a$10$60dLVLzcuGJyloLHyqBTze0WVt3hD2VlL3seJ7KrhmVRW8NymNR86', 1, 3, true);
INSERT INTO users (first_name, last_name, username, password, trainer_id, role_name_id, enabled) VALUES ('Steve', 'Green', 'green', '$2a$10$jn1/oYDLpJf694xZTPuHF.5GSNBFDj0jmognA6AbL.iLZp9XUFhGe', 2, 3, true);

INSERT INTO group_classes (name, date, trainer_id) VALUES ('PILATES', '2022-12-30 09:00', 2);
INSERT INTO group_classes (name, date, trainer_id) VALUES ('YOGA', '2022-12-30 10:00', 2);

INSERT INTO user_group_classes (group_class_id, user_id) VALUES (1, 1);
INSERT INTO user_group_classes (group_class_id, user_id) VALUES (1, 3);
INSERT INTO user_group_classes (group_class_id, user_id) VALUES (2, 1);
INSERT INTO user_group_classes (group_class_id, user_id) VALUES (2, 2);