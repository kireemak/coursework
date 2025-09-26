DELETE FROM users_roles;
DELETE FROM bookings;
DELETE FROM users;
DELETE FROM cars;
DELETE FROM roles;

insert into roles(id, name) values (1, 'ROLE_USER');
insert into roles(id, name) values (2, 'ROLE_ADMIN');

insert into cars(id, brand, model, year, rental_price, status) values (4, 'Chevrolet', 'Malibu', 2018, 35.0, 'Rented');
insert into cars(id, brand, model, year, rental_price, status) values (5, 'Tesla', 'Model 3', 2022, 100.0, 'Unavailable');
insert into cars(id, brand, model, year, rental_price, status) values (8, 'Mercedes', 'C-Class', 2021, 80.0, 'Rented');
insert into cars(id, brand, model, year, rental_price, status) values (7, 'Audi', 'A4', 2019, 65.0, 'Rented');
insert into cars(id, brand, model, year, rental_price, status) values (2, 'Honda', 'Civic', 2019, 45.0, 'Available');
insert into cars(id, brand, model, year, rental_price, status) values (3, 'Ford', 'Focus', 2021, 50.0, 'Available');
insert into cars(id, brand, model, year, rental_price, status) values (6, 'BMW', '320i', 2020, 70.0, 'Available');
insert into cars(id, brand, model, year, rental_price, status) values (1, 'Toyota', 'Corolla', 2020, 40.0, 'Available');
insert into cars(id, brand, model, year, rental_price, status) values (999999, 'testBrand', 'testModel', 2020, 60.0, 'Rented'),
                                                                      (1999999, 'testBrand2', 'testModel2', 2021, 85.0, 'Available'),
                                                                      (2999999, 'testBrand3', 'testModel3', 2022, 120.0, 'Rented'),
                                                                      (3999999, 'testBrand4', 'testModel4', 2022, 120.0, 'Available');

insert into users(id, name, email, phone_number, password) values (12, 'User123', 'user123@gmail.com', 3123548797, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (13, 'Admin12345', 'admin@admin.com', 5275742752, '$2a$10$ZsyR0FtDmps7LI3gF4Qf9.DwEJPOKm9SoPzI371VB7QqBFyZ4gCA2');
insert into users(id, name, email, phone_number, password) values (5, 'Charlie Davis', 'charlie.davis@example.com', 3333333333, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (3, 'Alice Johnson', 'alice.johnson@example.com', 5555555555, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (1, 'John Doe', 'john.doe@example.com', 1234567890, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (7, 'Evan Ford', 'evan.ford@example.com', 1111111111, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (8, 'Fiona Green', 'fiona.green@example.com', 9999999999, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (2, 'Jane Smith', 'jane.smith@example.com', 987654321, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (4, 'Bob Brown', 'bob.brown@example.com', 4444444444, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (6, 'Diana Evans', 'diana.evans@example.com', 2222222222, '$2a$10$Ia/FX.m1i/CXFy8lPgEqZexM8lyF56JAnNwmzuXvGB43BwGvx8/re');
insert into users(id, name, email, phone_number, password) values (999999, 'testName', 'testEmail', 'testPhone', 'password_hash'),
                                                                  (1999999, 'testName2', 'testEmail2', 'testPhone2', 'password_hash2'),
                                                                  (2999999, 'testName3', 'testEmail3', 'testPhone3', 'password_hash3'),
                                                                  (3999999, 'deleteTestName4', 'deleteTestEmail4', 'deleteTestPhone4', 'password_hash4');

insert into bookings(id, user_id, car_id, start_date, end_date, status) values (2, 2, 2, '2024-11-21', '2024-11-24', 'Created');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (3, 3, 3, '2024-11-22', '2024-11-26', 'Confirmed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (4, 4, 4, '2024-11-20', '2024-11-23', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (5, 5, 5, '2024-11-25', '2024-11-30', 'Created');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (6, 6, 6, '2024-11-26', '2024-12-01', 'Confirmed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (7, 7, 7, '2024-11-22', '2024-11-28', 'Cancelled');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (8, 8, 8, '2024-11-19', '2024-11-22', 'Confirmed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (9, 1, 7, '2024-11-19', '2024-11-20', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (10, 2, 7, '2020-06-06', '2020-07-07', 'Created');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (14, 1, 2, '2002-10-01', '2020-01-06', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (19, 1, 6, '2020-10-10', '2022-01-10', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (20, 1, 6, '2019-12-12', '2020-12-12', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (21, 13, 1, '2025-06-04', '2025-06-28', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (22, 13, 1, '2025-06-07', '2025-06-25', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (23, 13, 1, '2025-06-05', '2025-06-25', 'Completed');
insert into bookings(id, user_id, car_id, start_date, end_date, status) values (999999, 999999, 999999, '2024-10-17', '2024-11-26', 'Created'),
                                                                               (1999999, 999999, 2999999, '2024-01-01', '2025-10-10', 'Created');

insert into users_roles(user_id, role_id) values (12, 1);
insert into users_roles(user_id, role_id) values (13, 2);
insert into users_roles(user_id, role_id) values (999999, 1),
                                                 (1999999, 1),
                                                 (2999999, 2),
                                                 (3999999, 1);

select setval('cars_id_seq', (select max(id) from cars));
select setval('users_id_seq', (select max(id) from users));
select setval('bookings_id_seq', (select max(id) from bookings));