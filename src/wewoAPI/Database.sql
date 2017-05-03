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
    views INTEGER(8) NOT NULL default 0,
	street VARCHAR(128) NOT NULL,
    zipcode INTEGER(8) NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    edited TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
	RatingID INTEGER(32) NOT NULL AUTO_INCREMENT,
	rating INTEGER(1) NOT NULL, 
    raterID VARCHAR(50) NOT NULL,
    rateeID VARCHAR(50) NOT NULL,
    description VARCHAR(256),
	primary key(RatingID),
    foreign key(raterID) REFERENCES Users(userID),
    foreign key(rateeID) REFERENCES Users(userID)
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
    reason INTEGER(32),
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
    primary key(CommentID),
    foreign key(TaskID) REFERENCES Tasks(ID)
);

CREATE TABLE Tags
(
	ID INTEGER(16) AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(32) NOT NULL, 
	parentTagID INTEGER(16),
    foreign key(parentTagID) REFERENCES Tags(ID)
);


CREATE TABLE TaskTags
(
	TagID INTEGER(16) NOT NULL,
    TaskID INTEGER(32) NOT NULL,
    primary key(TagID, TaskID),
    foreign key(TagID) REFERENCES Tags(ID),
    foreign key(TaskID) REFERENCES Tasks(ID) 
);
INSERT INTO `Users` (`userID`,`firstName`,`middleName`,`lastName`,`avatarS3path`,`street`,`zipcode`,`email`,`sex`,`aboutme`) VALUES ('23',',awæd','kamldaw','kamwld','klamwdlk','lkmwad',0,'klmawd','','klmawd');
INSERT INTO `Users` (`userID`,`firstName`,`middleName`,`lastName`,`avatarS3path`,`street`,`zipcode`,`email`,`sex`,`aboutme`) VALUES ('1','Thomas','Bech','Madsen','odkwapwod','akdmwwakl',3460,'dkmakdal','male','Min krop er i smerte');
INSERT INTO `Users` (`userID`,`firstName`,`middleName`,`lastName`,`avatarS3path`,`street`,`zipcode`,`email`,`sex`,`aboutme`) VALUES ('2','oakdopaw','oakdpowa','okmadwkl','kfmlksef','apowdk',0492,'adkmawk','female','poakwdpowakpdkaowdkopwakdpwkaodkwakdpoawkdopkawopdkaskgoimroijgroeijrekg');
INSERT INTO `Users` (`userID`,`firstName`,`middleName`,`lastName`,`avatarS3path`,`street`,`zipcode`,`email`,`sex`,`aboutme`) VALUES ('3','Gunnery','Sergeant','Hartmann','oakwdp','okaapowd',5678,'opkaowkd','male','TEXAS! Holy cow! Only steers and queers come from Texas, and you dont look like a steer to me so that kind of narrows it down!');
INSERT INTO `Users` (`userID`,`firstName`,`middleName`,`lastName`,`avatarS3path`,`street`,`zipcode`,`email`,`sex`,`aboutme`) VALUES ('4','l,daæld,wal','kmawdklmawd','klmawdklmawldk','akwldmawlkdm','dkawmlkad',3456,'jdiwajio','dgsrdgrdrd','awdawdawdawd');
INSERT INTO `Phone` (`ownerID`,`number`,`type`) VALUES ('1',210389021,'bby plz holla');
INSERT INTO `Phone` (`ownerID`,`number`,`type`) VALUES ('3',128397,'phone');
INSERT INTO `Phone` (`ownerID`,`number`,`type`) VALUES ('2',9128309,'fax');
INSERT INTO `Validations` (`userID`) VALUES ('1');
INSERT INTO `Validations` (`userID`) VALUES ('2');
INSERT INTO `Validations` (`userID`) VALUES ('3');
INSERT INTO `Validations` (`userID`) VALUES ('4');
INSERT INTO `Tags` (`ID`,`name`,`parentTagID`) VALUES (1,'cleaning',NULL);
INSERT INTO `Tags` (`ID`,`name`,`parentTagID`) VALUES (2,'carpentry',NULL);
INSERT INTO `Tags` (`ID`,`name`,`parentTagID`) VALUES (3,'compute',NULL);
INSERT INTO `Tags` (`ID`,`name`,`parentTagID`) VALUES (4,'transport',NULL);
INSERT INTO `Tags` (`ID`,`name`,`parentTagID`) VALUES (5,'unicycling',4);
INSERT INTO `Tasks` (`ID`,`title`,`description`,`price`,`ECT`,`supplies`,`urgent`,`Views`,`street`,`zipcode`,`created`,`edited`,`creatorID`) VALUES (5,'kawmdl','lkamwd',0,0,0,0,0,'kawd',0,NULL,NULL,'klaw');
INSERT INTO `Tasks` (`ID`,`title`,`description`,`price`,`ECT`,`supplies`,`urgent`,`Views`,`street`,`zipcode`,`created`,`edited`,`creatorID`) VALUES (6,'Title1','Test0',55,30,0,0,0,'Allegade',2000,NULL,NULL,'Jeiner');
INSERT INTO `Tasks` (`ID`,`title`,`description`,`price`,`ECT`,`supplies`,`urgent`,`Views`,`street`,`zipcode`,`created`,`edited`,`creatorID`) VALUES (7,'Title1','Test0',55,30,0,0,0,'Allegade',2000,NULL,NULL,'Jeiner');
INSERT INTO `Tasks` (`ID`,`title`,`description`,`price`,`ECT`,`supplies`,`urgent`,`Views`,`street`,`zipcode`,`created`,`edited`,`creatorID`) VALUES (1,'Slå naboens græs','Slå min nabos græs',200,9,1,0,1,'awdawd',1234,NULL,NULL,'1');
INSERT INTO `Tasks` (`ID`,`title`,`description`,`price`,`ECT`,`supplies`,`urgent`,`Views`,`street`,`zipcode`,`created`,`edited`,`creatorID`) VALUES (2,'Bring pizza','Peperoni og med ekstra ost',100,8,0,1,3,'awdwad',4321,NULL,NULL,'2');
INSERT INTO `Tasks` (`ID`,`title`,`description`,`price`,`ECT`,`supplies`,`urgent`,`Views`,`street`,`zipcode`,`created`,`edited`,`creatorID`) VALUES (3,'Lav en ikk noget joke','På Mikkel',5,6,0,1,789,'awdawd',2231,NULL,NULL,'3');
INSERT INTO `Comments` (`CommentID`,`Commenter`,`message`,`TaskID`,`submitDate`) VALUES (1,'2','Texas!? Holy cow! Only steers and queers come from Texas and you don\'t look much like a steer so that kind of narrows it down!',3,'1970-01-01');
INSERT INTO `Comments` (`CommentID`,`Commenter`,`message`,`TaskID`,`submitDate`) VALUES (2,'3','Oh SPAM, why dont you love me for who I am?',2,'1970-01-01');
INSERT INTO `Comments` (`CommentID`,`Commenter`,`message`,`TaskID`,`submitDate`) VALUES (3,'1','Hallo?',1,'1970-01-01');
INSERT INTO `Comments` (`CommentID`,`Commenter`,`message`,`TaskID`,`submitDate`) VALUES (4,'3','Hvad?',1,'0');
INSERT INTO `Comments` (`CommentID`,`Commenter`,`message`,`TaskID`,`submitDate`) VALUES (5,'1','Ikk noget',1,'1970-01-01');
INSERT INTO `RejectReasons` (`ID`,`reason`) VALUES (1,'EZ LYFE');
INSERT INTO `RejectReasons` (`ID`,`reason`) VALUES (2,'No');
INSERT INTO `RejectReasons` (`ID`,`reason`) VALUES (3,'Yes');
INSERT INTO `Appliers` (`TaskID`,`ApplierID`,`applierMessage`,`status`,`reason`) VALUES (1,'2','come at me bro','Applied',3);
INSERT INTO `Appliers` (`TaskID`,`ApplierID`,`applierMessage`,`status`,`reason`) VALUES (2,'1','get rekt m8','Rejected',2);
INSERT INTO `Appliers` (`TaskID`,`ApplierID`,`applierMessage`,`status`,`reason`) VALUES (3,'1','???','Accepted',1);
INSERT INTO `TaskFlags` (`flagID`,`description`) VALUES (2,'ITS A TRAP');
INSERT INTO `TaskFlags` (`flagID`,`description`) VALUES (1,'This task sux');
INSERT INTO `TaskFlags` (`flagID`,`description`) VALUES (3,'TOO EZ');
INSERT INTO `TaskFlaggings` (`TaskID`,`flaggerID`,`description`,`flagID`) VALUES (1,'1',',adwådl',2);
INSERT INTO `TaskFlaggings` (`TaskID`,`flaggerID`,`description`,`flagID`) VALUES (1,'2','fmoeioeo',3);
INSERT INTO `Compentencies` (`userID`) VALUES ('1');
INSERT INTO `Compentencies` (`userID`) VALUES ('2');
INSERT INTO `UserRatings` (`rating`,`raterID`,`rateeID`,`description`) VALUES (10,'1','2','10/10 would do again');
INSERT INTO `UserRatings` (`rating`,`raterID`,`rateeID`,`description`) VALUES (0,'3','2','0/10 the common cold sux');
INSERT INTO `UserRatings` (`rating`,`raterID`,`rateeID`,`description`) VALUES (5,'1','4','What does this button do?');
INSERT INTO `UserFlags` (`ID`,`description`) VALUES (1,'I am flag, yes');
INSERT INTO `UserFlags` (`ID`,`description`) VALUES (2,'Fliggity flaggity, you are a faggotty');
INSERT INTO `UserFlaggings` (`userID`,`flaggerID`,`description`,`flagID`) VALUES ('1','2','I AM REPORTING YOU TO THEADMINS',1);
INSERT INTO `UserFlaggings` (`userID`,`flaggerID`,`description`,`flagID`) VALUES ('2','1','WELL GUESS WHAT',2);
