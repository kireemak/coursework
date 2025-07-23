insert into cars(id, brand, model, year, rental_price, status)
    values (999, 'testBrand', 'testModel', 2020, 60.0, 'Rented'),
           (1999, 'testBrand2', 'testModel2', 2021, 85.0, 'Available');

insert into users(id, name, email, phone_number, password)
    values (999, 'testName', 'testEmail', 'testPhone', 'password_hash'),
           (1999, 'testName2', 'testEmail2', 'testPhone2', 'password_hash2');

insert into bookings(id, user_id, car_id, start_date, end_date, status)
    values (999, 999, 999, '2024-10-17', '2024-11-26', 'Created');

insert into users_roles(user_id, role_id)
    values (999, 1),
           (1999, 1);
