# schoolmanagementsystem
 crud app as a school management system

CREATE DATABASE WITH THE FOLLOWNING STATEMENTS

CREATE DATABASE `student_hms` ;

CREATE TABLE `course` (
  `idcourse` int(11) NOT NULL,
  `course_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idcourse`)
);

CREATE TABLE `credentials` (
  `username` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `permission` int(11) DEFAULT NULL,
  PRIMARY KEY (`username`)
);

CREATE TABLE `grade` (
  `idgrade` int(11) NOT NULL,
  `score` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idgrade`)
);

CREATE TABLE `person_type` (
  `idperson_type` int(11) NOT NULL,
  `person_type_title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idperson_type`)
);

CREATE TABLE `person` (
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `personal_number` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `adress` varchar(45) DEFAULT NULL,
  `person_username` varchar(45) NOT NULL,
  `person_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`person_username`),
  KEY `person_type_idx` (`person_type_id`),
  CONSTRAINT `person_type` FOREIGN KEY (`person_type_id`) REFERENCES `person_type` (`idperson_type`),
  CONSTRAINT `person_username` FOREIGN KEY (`person_username`) REFERENCES `credentials` (`username`)
);

CREATE TABLE `program` (
  `idprogram` int(11) NOT NULL,
  `program_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idprogram`)
);

CREATE TABLE `program_course` (
  `idprogram_course_course_id` int(11) NOT NULL,
  `idprogram_course_program_id` int(11) NOT NULL,
  PRIMARY KEY (`idprogram_course_course_id`,`idprogram_course_program_id`)
);

CREATE TABLE `student` (
  `idstudent` varchar(55) NOT NULL,
  `student_program_id` int(11) DEFAULT NULL,
  `student_semester` int(11) DEFAULT NULL,
  PRIMARY KEY (`idstudent`),
  CONSTRAINT `idstudent` FOREIGN KEY (`idstudent`) REFERENCES `credentials` (`username`)
);

CREATE TABLE `student_grade` (
  `grade_idstudent` varchar(45) DEFAULT NULL,
  `grade_idcourse` int(11) DEFAULT NULL,
  `grade` varchar(45) DEFAULT NULL,
  `idgrade` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idgrade`),
  KEY `grade_idcourse_idx` (`grade_idcourse`)
);

