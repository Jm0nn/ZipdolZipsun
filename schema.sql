-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema zipdolzipsun
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `zipdolzipsun` ;

-- -----------------------------------------------------
-- Schema zipdolzipsun
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `zipdolzipsun` DEFAULT CHARACTER SET utf8mb4 ;
USE `zipdolzipsun` ;

-- -----------------------------------------------------
-- Table `zipdolzipsun`.`user_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zipdolzipsun`.`user_type` ;

CREATE TABLE IF NOT EXISTS `zipdolzipsun`.`user_type` (
  `user_type_no` INT NOT NULL,
  `user_type_name` VARCHAR(45) NULL,
  PRIMARY KEY (`user_type_no`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `zipdolzipsun`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zipdolzipsun`.`user` ;

CREATE TABLE IF NOT EXISTS `zipdolzipsun`.`user` (
  `user_no` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(32) NOT NULL,
  `user_name` VARCHAR(100) NOT NULL,
  `user_pass` VARCHAR(64) NOT NULL,
  `nick_name` VARCHAR(32) NOT NULL,
  `user_type_no` INT NOT NULL DEFAULT 1,
  `sex` ENUM('male', 'female') NOT NULL,
  `birth_date` VARCHAR(50) NULL,
  `join_date` TIMESTAMP NULL DEFAULT current_timestamp(),
  `email_id` VARCHAR(32) NULL,
  `email_domain` VARCHAR(32) NULL,
  `phone_number` VARCHAR(11) NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_no`),
  INDEX `user_type_no_idx` (`user_type_no` ASC) VISIBLE,
  CONSTRAINT `user_type_no`
    FOREIGN KEY (`user_type_no`)
    REFERENCES `zipdolzipsun`.`user_type` (`user_type_no`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `zipdolzipsun`.`board_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zipdolzipsun`.`board_type` ;

CREATE TABLE IF NOT EXISTS `zipdolzipsun`.`board_type` (
  `board_no` VARCHAR(10) NOT NULL,
  `board_name` VARCHAR(45) NULL,
  PRIMARY KEY (`board_no`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `zipdolzipsun`.`article_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zipdolzipsun`.`article_type` ;

CREATE TABLE IF NOT EXISTS `zipdolzipsun`.`article_type` (
  `article_type_no` INT NOT NULL,
  `article_type_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`article_type_no`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `zipdolzipsun`.`board`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zipdolzipsun`.`board` ;

CREATE TABLE IF NOT EXISTS `zipdolzipsun`.`board` (
  `article_no` INT NOT NULL AUTO_INCREMENT,
  `user_no` INT NOT NULL,
  `board_no` VARCHAR(10) NOT NULL,
  `subject` VARCHAR(100) NOT NULL,
  `content` VARCHAR(1000) NOT NULL,
  `regist_time` TIMESTAMP NOT NULL DEFAULT current_timestamp(),
  `hit` INT NULL DEFAULT 0,
  `likes` INT NULL DEFAULT 0,
  `article_type_no` INT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`article_no`),
  INDEX `board_no_idx` (`board_no` ASC) VISIBLE,
  INDEX `article_type_no_idx` (`article_type_no` ASC) VISIBLE,
  INDEX `user_no_idx` (`user_no` ASC) VISIBLE,
  CONSTRAINT `board_no`
    FOREIGN KEY (`board_no`)
    REFERENCES `zipdolzipsun`.`board_type` (`board_no`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `article_type_no`
    FOREIGN KEY (`article_type_no`)
    REFERENCES `zipdolzipsun`.`article_type` (`article_type_no`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `zipdolzipsun`.`comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zipdolzipsun`.`comment` ;

CREATE TABLE IF NOT EXISTS `zipdolzipsun`.`comment` (
  `comment_no` INT NOT NULL AUTO_INCREMENT,
  `article_no` INT NOT NULL,
  `nick_name` VARCHAR(32) NOT NULL,
  `content` VARCHAR(500) NOT NULL,
  `likes` INT NULL DEFAULT 0,
  `regist_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`comment_no`),
  INDEX `article_no_idx` (`article_no` ASC) VISIBLE,
  CONSTRAINT `article_no`
    FOREIGN KEY (`article_no`)
    REFERENCES `zipdolzipsun`.`board` (`article_no`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `zipdolzipsun`.`dongcode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zipdolzipsun`.`region` ;

CREATE TABLE IF NOT EXISTS `zipdolzipsun`.`region` (
  `region_code` VARCHAR(8) NOT NULL,
  `address_name` VARCHAR(50) NOT NULL,
  `local_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`region_code`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
