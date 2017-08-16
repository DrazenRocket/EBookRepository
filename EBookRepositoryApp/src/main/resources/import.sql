insert into language (language_name) values ('English');
insert into language (language_name) values ('Serbian');

insert into category (category_name) values ('IT');
insert into category (category_name) values ('Math');

insert into user (user_first_name, user_last_name, user_username, user_password, user_type, user_category)
    values ('Pera', 'Perić', 'pera.peric', 'pera123', 'ROLE_ADMIN', null);
insert into user (user_first_name, user_last_name, user_username, user_password, user_type, user_category)
    values ('Marko', 'Marković', 'marko.markovic', '123marko', 'ROLE_SUBSCRIBER', null);
insert into user (user_first_name, user_last_name, user_username, user_password, user_type, user_category)
    values ('Imenko', 'Prezimenko', 'imenko.prezimenko', 'imePrezime', 'ROLE_SUBSCRIBER', 1);
