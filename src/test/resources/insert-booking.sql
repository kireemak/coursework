insert into cars(id, brand, model, year, rental_price, status)
    values (999999, 'testBrand', 'testModel', 2020, 60.0, 'Rented'),
           (1999999, 'testBrand2', 'testModel2', 2021, 85.0, 'Available'),
           (2999999, 'testBrand3', 'testModel3', 2022, 120.0, 'Rented');

insert into users(id, name, email, phone_number, password)
    values (999999, 'testName', 'testEmail', 'testPhone', 'password_hash'),
           (1999999, 'testName2', 'testEmail2', 'testPhone2', 'password_hash2');

insert into bookings(id, user_id, car_id, start_date, end_date, status)
    values (999999, 999999, 999999, '2024-10-17', '2024-11-26', 'Created'),
           (1999999, 999999, 2999999, '2024-01-01', '2025-10-10', 'Created');

insert into users_roles(user_id, role_id)
    values (999999, 1),
           (1999999, 1);
