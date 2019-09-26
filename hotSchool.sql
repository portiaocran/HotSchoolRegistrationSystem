# Portia Ocran
# 991545021

DROP DATABASE IF EXISTS HotSummer;

CREATE DATABASE HotSummer;

USE HotSummer;

DROP TABLE IF EXISTS Student;

CREATE TABLE Student (
	id INT NOT NULL AUTO_INCREMENT,
	firstName VARCHAR(25) NOT NULL,
	lastName VARCHAR(25) NOT NULL,
	age INT,
	PRIMARY KEY (ID)
	);

DROP TABLE IF EXISTS Course;

CREATE TABLE Course (
	id INT NOT NULL AUTO_INCREMENT,
	courseName VARCHAR(25) NOT NULL,
	startTime TIME NOT NULL,
	PRIMARY KEY (id)
	);

DROP TABLE IF EXISTS StudentCourse;

CREATE TABLE StudentCourse(
	studentID INT,
	courseID INT,
	FOREIGN KEY (studentID) REFERENCES Student(id) ON DELETE CASCADE,
	FOREIGN KEY (courseID) REFERENCES Course(id) ON DELETE CASCADE
	);

INSERT INTO Student (firstName, lastName, age)
VALUES ("John","Johnson",10),
	("Bob","Bobson",11),
	("Maddie","Maddison", 15),
	("Mary","Molson",12),
	("Ed","Edison",13),
	("Mike","Molson",11);

INSERT INTO Course (courseName, startTime)
VALUES ("Swimming",'9:00:00'),
	("Tennis",'11:00:00'),
	("Soccer",'13:00:00');

INSERT INTO StudentCourse (studentID, courseID)
VALUES (1, 1),
	(1,2),
	(1,3),
	(2,1),
	(2,2),
	(3,1),
	(3,2),
	(3,3),
	(4,1),
	(4,3),
	(5,1),
	(5,3),
	(6,2),
	(6,3);
