insert into user (id, email, first_name, last_name, roles, password) values (1, 'jbonham@ledzeppelin.com', 'John', 'Bonham', 'USER', '{bcrypt}$2a$10$53M8JoFnJrvA6.2fY1xr7uKV1bVFJQHtvftaXDj2Hfsr5BMbnAU/W');
insert into user (id, email, first_name, last_name, roles, password) values (2, 'dmustaine@megadeth.com', 'Dave', 'Mustaine', 'USER', '{bcrypt}$2a$10$53M8JoFnJrvA6.2fY1xr7uKV1bVFJQHtvftaXDj2Hfsr5BMbnAU/W');
insert into profile (id, description, name) values (1, 'Manages everything', 'Admin');
insert into user_profile (id, user_id, profile_id) values (1, 1, 1);
insert into user_profile (id, user_id, profile_id) values (2, 2, 1);