INSERT INTO tb_client (name, register_Date, img_Url) VALUES ('Alex', TIMESTAMP WITHOUT TIME ZONE '2023-07-24T11:15:00', 'https://xsgames.co/randomusers/assets/avatars/male/47.jpg');
INSERT INTO tb_client (name, register_Date, img_Url) VALUES ('Maria', TIMESTAMP WITHOUT TIME ZONE '2023-08-10T15:45:14', 'https://i.pinimg.com/originals/76/ef/b7/76efb7c94755748d695d3d46cf11d08d.jpg');

INSERT INTO tb_user (cpf, name, client_id, password) VALUES ('123.456.789-00', 'Alex', 1, '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (cpf, name, client_id, password) VALUES ('000.123.456-78', 'Maria', 2, '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_role (authority) VALUES ('ROLE_CLIENT');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_cpf, role_id) VALUES ('123.456.789-00', 1);
INSERT INTO tb_user_role (user_cpf, role_id) VALUES ('000.123.456-78', 1);
INSERT INTO tb_user_role (user_cpf, role_id) VALUES ('000.123.456-78', 2);

INSERT INTO tb_address (client_id, street, city, neighborhood, complement, tag) VALUES (1, 'Rua Assis Brasil, 465', 'Bento Gonçalves', 'Centro', null, 'Tag1');
INSERT INTO tb_address (client_id, street, city, neighborhood, complement, tag) VALUES (2, 'Rua Ramiro Barcelos, 123', 'Bento Gonçalves', 'Centro', 2, 'Tag2');

INSERT INTO tb_contact (client_id, tag, type, value) VALUES (1, 'Tag1', true, 'alex@gmail.com');
INSERT INTO tb_contact (client_id, tag, type, value) VALUES (2, 'Tag1', false, '(54)99999-9999');

INSERT INTO tb_breed (description) VALUES ('Pitbull');
INSERT INTO tb_breed (description) VALUES ('Golden Retriever');

INSERT INTO tb_pet (client_id, breed_id, name, birth_Date, img_Url) VALUES (1, 1, 'Myke', TIMESTAMP WITHOUT TIME ZONE '2016-01-11', 'https://www.petlove.com.br/images/breeds/193221/profile/original/pitbull-p.jpg?1532539293');
INSERT INTO tb_pet (client_id, breed_id, name, birth_Date, img_Url) VALUES (2, 2, 'Sia', TIMESTAMP WITHOUT TIME ZONE '2022-10-20', 'https://www.petlove.com.br/images/breeds/193223/profile/original/golden_retriever-p.jpg?1532539102');

INSERT INTO tb_assistance (pet_id, description, value, date) VALUES (1, 'Regular exams', 50.0, TIMESTAMP WITHOUT TIME ZONE '2023-09-20T13:45:10');
INSERT INTO tb_assistance (pet_id, description, value, date) VALUES (1, 'Monthly exams', 120.0, TIMESTAMP WITHOUT TIME ZONE '2023-11-02T10:20:51');