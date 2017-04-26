DROP TABLE IF EXISTS Appliers;
DROP TABLE IF EXISTS RejectReasons;
DROP TABLE IF EXISTS TaskFlaggings;
DROP TABLE IF EXISTS TaskFlags;

DROP TABLE IF EXISTS UserFlaggings;
DROP TABLE IF EXISTS UserFlags;
DROP TABLE IF EXISTS UserRatingMapper;
DROP TABLE IF EXISTS UserRatings;
DROP TABLE IF EXISTS Phone;
DROP TABLE IF EXISTS Compentencies;
DROP TABLE IF EXISTS Validations;
DROP TABLE IF EXISTS TaskTags;
DROP TABLE IF EXISTS Tags;
DROP TABLE IF EXISTS Login;
DROP TABLE IF EXISTS Comments;
DROP TABLE IF EXISTS Tasks;
DROP TABLE IF EXISTS Users;



CREATE TABLE Users (
    userID VARCHAR(50) NOT NULL,
    firstName VARCHAR(128) NOT NULL,
    middleName VARCHAR(128),
    lastName VARCHAR(128) NOT NULL,
    avatarS3path VARCHAR(128),
    street VARCHAR(128) NOT NULL,
    zipcode INTEGER(8) NOT NULL,
    email VARCHAR(128) NOT NULL ,
    sex ENUM('male', 'female') NOT NULL,
    aboutme VARCHAR(512),
    PRIMARY KEY (userID)
);

CREATE TABLE Tasks
(
	ID INTEGER(32) AUTO_INCREMENT PRIMARY KEY NOT NULL, 
	title VARCHAR(32) NOT NULL,
    description VARCHAR(256) NOT NULL,
    price INTEGER(8) NOT NULL,
    ECT INTEGER(8) NOT NULL, -- Estimated complition time(minutes)
	supplies BOOLEAN NOT NULL,
    urgent BOOLEAN NOT NULL,
    views INTEGER(8) NOT NULL,
	street VARCHAR(128) NOT NULL,
    zipcode INTEGER(8) NOT NULL,
    created TIMESTAMP,
    edited TIMESTAMP,
    creatorID VARCHAR(50) NOT NULL
   -- foreign key(creatorID) REFERENCES Users(userID)
);


CREATE TABLE Validations
(
	userID VARCHAR(50) NOT NULL,
	FOREIGN KEY(userID) REFERENCES Users(userid)
);


CREATE TABLE Compentencies
(
	userID VARCHAR(50) NOT NULL,
	FOREIGN KEY(userID) REFERENCES Users(userID)
);

CREATE TABLE Phone
(
	ownerID VARCHAR(50) NOT NULL,
	number INTEGER(8) NOT NULL,
	type ENUM('Home', 'Mobile', 'Work') NOT NULL,
	foreign key(ownerID) REFERENCES Users(userID)
);
 
 
 
CREATE TABLE UserRatings
(
	rating INTEGER(1) NOT NULL, 
    raterID VARCHAR(50) NOT NULL,
    rateeID VARCHAR(50) NOT NULL,
    description VARCHAR(256),
    foreign key(raterID) REFERENCES Users(userID),
    foreign key(rateeID) REFERENCES Users(userID)
);

CREATE TABLE UserRatingMapper
 (
	RatingID INTEGER NOT NULL AUTO_INCREMENT,
    raterID VARCHAR(50) NOT NULL,
    rateeID VARCHAR(50) NOT NULL,
    primary key(RatingID),
    foreign key(raterID) REFERENCES UserRatings(raterID),
    foreign key(rateeID) REFERENCES UserRatings(raterID)
 );
CREATE TABLE UserFlags
(
	ID INTEGER(3) PRIMARY KEY NOT NULL,
    description VARCHAR(32)
);

CREATE TABLE UserFlaggings
(
	userID VARCHAR(50) NOT NULL,
    flaggerID VARCHAR(50) NOT NULL,
    description VARCHAR(256),
    flagID INTEGER(3) NOT NULL,
    foreign key(userID) REFERENCES Users(userID),
	foreign key(flaggerID) REFERENCES Users(userID),
	foreign key(flagID) REFERENCES UserFlags(ID)
);

CREATE TABLE TaskFlags
(
	flagID INTEGER(2) PRIMARY KEY NOT NULL,
    description VARCHAR(16) NOT NULL
);

CREATE TABLE TaskFlaggings
(
	TaskID INTEGER(32) NOT NULL,
    flaggerID VARCHAR(50) NOT NULL,
	description VARCHAR(64) NOT NULL,
    flagID INTEGER(2) NOT NULL,
    foreign key (TaskID) REFERENCES Tasks(ID),
    foreign key(FlaggerID) REFERENCES Users(userID),
    foreign key(flagID) REFERENCES TaskFlags(flagID)
);

CREATE TABLE RejectReasons
(
	ID INTEGER(3) PRIMARY KEY NOT NULL,
    reason VARCHAR(32) NOT NULL
);

CREATE TABLE Appliers
(
	TaskID INTEGER(32) NOT NULL,
    ApplierID VARCHAR(50) NOT NULL,
    applierMessage VARCHAR(256),
    status ENUM('Applied', 'Rejected', 'Accepted', 'Closed') NOT NULL,
    reason INTEGER(3),
	foreign key(TaskID) REFERENCES Tasks(ID),
    foreign key(ApplierID) REFERENCES Users(userid),
    foreign key(reason) REFERENCES RejectReasons(ID)
);

CREATE TABLE Comments
(
    CommentID INTEGER(32) NOT NULL, 
    Commenter VARCHAR(50) NOT NULL,
    message VARCHAR(1024) NOT NULL,
    TaskID INTEGER(32) NOT NULL,
    submitDate DATE NOT NULL,
    foreign key(TaskID) REFERENCES Tasks(ID)
);

CREATE TABLE Tags
(
	ID INTEGER(16) AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(32) NOT NULL, 
	parentTagID INTEGER(16),
    foreign key(parentTagID) REFERENCES Tags(ID)
);
insert into Tags VALUES(1, 'cleaning', null), (2, 'carpentry', null), (3, 'compute', null), (4, 'transport', null);


CREATE TABLE TaskTags
(
	TagID INTEGER(16) NOT NULL,
    TaskID INTEGER(32) NOT NULL,
    primary key(TagID, TaskID),
    foreign key(TagID) REFERENCES Tags(ID),
    foreign key(TaskID) REFERENCES Tasks(ID) 
);

DROP TRIGGER IF EXISTS UpdateDate;
DELIMITER \\
CREATE TRIGGER UpdateDate BEFORE INSERT ON Tasks FOR EACH ROW
BEGIN
	SET NEW.edited = NOW();
	SET NEW.created = NOW();
END;\\


#SET @handy := (SELECT LAST_INSERT_ID());
#insert into Tags VALUES(null, "Handy", null), (null, "Move", null),(null, "Clean", null), (null, "Cook", null), (null, "Outdoor", null), (null, "Service", null), (null, "IT", null), (null, "Study", null), (null, "Other", null);
#insert into Tags VALUES("Samle møbler", @handy), ("Ikea møbler", LAST_INSERTID()-1);
