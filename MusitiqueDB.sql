/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

# -----------------------------------------
# SQL script to create the tables in the 
# Musitique Database (MusitiqueDB)
# Created by Joe Conwell, Vibhavi Peiris, Mehmet Oguz Yardimci for Musitique app
# -----------------------------------------

/*
Tables to be dropped must be listed in a logical order based on dependency.
UserFile and UserPhoto depend on User. Therefore, they must be dropped before User.
*/
DROP TABLE IF EXISTS UserFile, UserPhoto, User;

/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(256) NOT NULL,          /* To store Salted and Hashed Password Parts */
    first_name VARCHAR(32) NOT NULL,
    middle_name VARCHAR(32),
    last_name VARCHAR(32) NOT NULL,
    address1 VARCHAR(128) NOT NULL,
    address2 VARCHAR(128),
    city VARCHAR(64) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,            /* e.g., 24060-1804 */
    security_question_number INT NOT NULL,   /* Refers to the number of the selected security question */
    security_answer VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,      
    PRIMARY KEY (id)
);

/* The UserPhoto table contains attributes of interest of a user's photo. */
CREATE TABLE UserPhoto
(
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
	extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
	user_id INT UNSIGNED,
	FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

/* The UserComment table contains user comments on songs/albums/artists. */
CREATE TABLE UserComment
(
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
	user_id INT UNSIGNED,
	entity_id VARCHAR(256),
	entity_type ENUM('ALBUM', 'ARTIST', 'TRACK') NOT NULL,
	comment VARCHAR(256) NOT NULL,
	date DATETIME,
	FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

/* The UserRating table contains user ratings for songs/albums/artists. */
CREATE TABLE UserRating
(
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
	user_id INT UNSIGNED,
	entity_id VARCHAR(256),
	entity_type ENUM('ALBUM', 'ARTIST', 'TRACK') NOT NULL,
	rating INT UNSIGNED,
	date DATETIME,
	FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);


/* The UserGenres table contains user genre preferences */
CREATE TABLE UserGenre
(
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
	user_id INT UNSIGNED,
	genre VARCHAR(256),
	FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

/* The UserFavoriteArtists table contains user's Followed Artists */
CREATE TABLE UserFavoriteArtist
(
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
	user_id INT UNSIGNED,
	entity_id VARCHAR(256),
	FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);
