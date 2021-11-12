DROP DATABASE IF EXISTS DISNEY;
CREATE DATABASE DISNEY;
USE DISNEY; 

drop table if exists Titles;
drop table if exists User;
drop table if exists Rental;
drop table if exists Billing;
drop table if exists Payment;

CREATE TABLE Titles (
show_id varchar(10),
type varchar(20),
title varchar(50) not null,
director varchar(50),
cast varchar(250),
country varchar(50),
release_year int,
rating varchar(20),
duration varchar(20),
description varchar(250),
copies int not null,
price float not null,
primary key (show_id),
updatedAt date
);

CREATE TABLE User (
uID int not null AUTO_INCREMENT,
name varchar(50),
age int,
rented int,
phone_number char(10),
primary key (uID)
);
ALTER table USER AUTO_INCREMENT = 1001;

SET SQL_MODE='ALLOW_INVALID_DATES';

CREATE TABLE Rental (
uID int,
show_id varchar(10),
check_out date default '0000-00-00',
overdue boolean default false,
FOREIGN KEY (show_id) references Titles(show_id),
FOREIGN KEY (uID) references User(uID),
primary key (uID, show_id)
);

CREATE TABLE BILLING (
uID int,
name varchar(50),
address varchar(50),
balance int,
FOREIGN KEY (uID) references User(uID),
primary key (uID)
);

CREATE TABLE PAYMENT (
pID int not null AUTO_INCREMENT,
uID int,
payment_date date,
payment_amount int,
FOREIGN KEY (uID) references User(uID),
primary key (pID)
);

CREATE TABLE Archive (
show_id varchar(10),
type varchar(20),
title varchar(50) not null,
director varchar(50),
cast varchar(250),
country varchar(50),
release_year int,
rating varchar(20),
duration varchar(20),
description varchar(250),
copies int not null,
price float not null,
primary key (show_id)
);

/*Trigger to decrease # of copies once title is rented */
DROP TRIGGER IF EXISTS DecreaseCopies;
DELIMITER //
CREATE TRIGGER DecreaseCopies
AFTER INSERT ON Rental
FOR EACH ROW
BEGIN
	IF (copies >0 and New.show_id=show_id) THEN
	UPDATE Titles SET copies= copies - 1 where show_id = new.show_id;
	END IF;
END;
//
DELIMITER ;

/*Trigger to remove rentals once title is returned */
DROP TRIGGER IF EXISTS RentalReturned
DELIMITER //
CREATE TRIGGER RentalReturned
AFTER DELETE ON Rental 
FOR EACH ROW
UPDATE Titles SET copies = copies + 1 where show_id  = old.show_id;
END;
//
DELIMITER ;

/*Trigger for updating date of updatedAt on insert */
DROP TRIGGER IF EXISTS UpdatedAtOnInsert;
DELIMITER //
CREATE TRIGGER UpdatedAtOnInsert
AFTER INSERT ON Titles
FOR EACH ROW
BEGIN
	Update Titles set updatedAt = current_date WHERE show_id = new.show_id;
END;
//
DELIMITER ;

/*Trigger for updating date of updatedAt when modified */
DROP TRIGGER IF EXISTS UpdatedAtOnUpdate;
DELIMITER //
CREATE TRIGGER UpdatedAtOnUpdate
AFTER UPDATE ON Titles
FOR EACH ROW
BEGIN
	Update Titles set updatedAt = current_date WHERE show_id = new.show_id;
END;
//
DELIMITER ;

/*Procedure for archiving data into the Archive relation */
DROP PROCEDURE IF EXISTS storedProcedure
DELIMITER //
CREATE PROCEDURE storedProcedure(IN cutoffDate DATE)
BEGIN 
	INSERT INTO Archive (SELECT * FROM Titles WHERE (updatedAt<cutoffDate));
	DELETE FROM Titles WHERE (updatedAt<cutoffDate);
END; //
DELIMITER ;

ALTER table USER AUTO_INCREMENT = 1;

insert into User (name, age, rented, phone_number) values ('John Smith', 18, 0, '5103456789');
insert into User (name, age, rented, phone_number) values ('Ella Kim', 25, 0, '4083456789');
insert into User (name, age, rented, phone_number) values ('Mary Martinez', 13, 0, '5108889999');

SET GLOBAL local_infile=1;

/* Change the source to your directory */
LOAD DATA LOCAL INFILE '/Users/Diane/CS157A/Project/disney_plus_titles.csv' INTO TABLE TITLES 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS (show_id,type,title,director,cast,country,release_year,rating,duration,description,copies,price);