INSERT INTO PUBLIC.dealer (name, phone, email, tier)
VALUES ( 'RentalCars', '514-509-6995', 'service@rentalcars.com', 4);

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model, short_description, long_description)
VALUES ( 1, 4500, CURRENT_DATE(), 'PUBLISHED', 'Toyota', '2020', 'Selling Toyota 2020', 'Sellling Toyota 2020, you can ask for a drive test for free');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model, short_description, long_description)
VALUES ( 1, 4200, CURRENT_DATE(), 'PUBLISHED', 'Chevrolett', '2019', 'Selling Chevrolett 2019', 'Sellling Chevrolett 2019, you can ask for a drive test for free');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model, short_description, long_description)
VALUES ( 1, 3800, CURRENT_DATE(), 'PUBLISHED', 'Toyota', '2018', 'Selling Toyota 2018', 'Sellling Toyota 2018, you can ask for a drive test for free');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model, short_description, long_description)
VALUES ( 1, 4500, CURRENT_DATE(), 'DRAFT', 'Chevrolett', '2021', 'Selling Chevrolett 2021', 'Sellling Chevrolett 2021, you can ask for a drive test for free');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model, short_description, long_description)
VALUES ( 1, 5000, CURRENT_DATE(), 'DRAFT', 'Toyota', '2020', 'Selling Toyota 2020', 'Sellling Toyota 2020, you can ask for a drive test for free');

INSERT INTO PUBLIC.listing (dealer, price, posting_date, state, brand, model, short_description, long_description)
VALUES ( 1, 7000, CURRENT_DATE(), 'DRAFT', 'Chevrolett', '2019', 'Selling Chevrolett 2019', 'Sellling Chevrolett 2019, you can ask for a drive test for free');