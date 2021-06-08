CREATE TABLE question (
    id int NOT NULL AUTO_INCREMENT,
    listing int NOT NULL,
    by varchar(50) NOT NULL, 
    message varchar(250) NOT NULL,
    posting_date date NOT NULL,
    response varchar(250),
    response_date date,
    PRIMARY KEY (id),
    FOREIGN KEY (listing) REFERENCES listing(id)
);