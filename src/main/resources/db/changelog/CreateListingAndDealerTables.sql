CREATE TABLE dealer (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(20) NOT NULL,
    tier int NOT NULL,
    email varchar(50) NOT NULL,
    phone varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE listing (
    id int NOT NULL AUTO_INCREMENT,
    dealer int NOT NULL,
    price varchar(50) NOT NULL,
    posting_date date,
    state varchar(10) NOT NULL,
    brand varchar(20) NOT NULL,
    model varchar(4) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (dealer) REFERENCES dealer(id)
);

CREATE TABLE unpublish_reason (
    id int NOT NULL AUTO_INCREMENT,
    listing int NOT NULL,
    reason varchar(50) NOT NULL,
    unposting_date date,
    posting_date date,
    PRIMARY KEY (id),
    FOREIGN KEY (listing) REFERENCES listing(id)
);