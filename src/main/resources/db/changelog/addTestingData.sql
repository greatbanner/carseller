INSERT INTO PUBLIC.dealer (name, phone, email, tier)
VALUES ( 'RentalCars', '514-509-6995', 'service@rentalcars.com', 4);

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model)
VALUES ( 1, 4500, CURRENT_DATE(), 'PUBLISHED', 'Toyota', '2020');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model)
VALUES ( 1, 4200, CURRENT_DATE(), 'PUBLISHED', 'Chevrolett', '2019');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model)
VALUES ( 1, 3800, CURRENT_DATE(), 'PUBLISHED', 'Toyota', '2018');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model)
VALUES ( 1, 4500, CURRENT_DATE(), 'DRAFT', 'Chevrolett', '2021');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model)
VALUES ( 1, 5000, CURRENT_DATE(), 'DRAFT', 'Toyota', '2020');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model)
VALUES ( 1, 7000, CURRENT_DATE(), 'DRAFT', 'Chevrolett', '2019');