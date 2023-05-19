USE `ssafyproject`;
select * from board;
drop table if exists `like`;
drop table if exists `hotplace`;
drop table if exists `comment`;
drop table if exists `place`;
drop table if exists `plan`;
drop table if exists `board`;
drop table if exists `notice`;
drop table if exists `user`;


-- 유저 테이블
CREATE TABLE `user` (
  `id` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email_id` varchar(20) NOT NULL,
  `email_domain` varchar(25) NOT NULL,
  `grade` varchar(20) NOT NULL DEFAULT '일반 회원',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into user values ('admin', '1234', '관리자', 'admin', 'ssafy.com', '관리자');
insert into user values ('ssafy', '1234', '김싸피', 'ssafy', 'ssafy.com', '일반 회원');


-- 핫플레이스 테이블
CREATE TABLE IF NOT EXISTS `hotplace` (
  `user_id` VARCHAR(20) NOT NULL,
  `num` INT NOT NULL AUTO_INCREMENT,
  `image` VARCHAR(300) NOT NULL DEFAULT 'no-pictures.png',
  `title` VARCHAR(300) NOT NULL,
  `join_date` DATE NOT NULL DEFAULT (CURRENT_DATE),
  `desc` VARCHAR(1000) NOT NULL,
  `tag1` VARCHAR(100) NULL DEFAULT '',
  `tag2` VARCHAR(100) NULL DEFAULT '',
  `latitude` DECIMAL(20, 17) NULL,
  `longitude` DECIMAL(20, 17) NULL,
  `map_url` VARCHAR(100) NULL,
  `like` BOOLEAN DEFAULT FALSE,
  `like_cnt` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`num`),
  CONSTRAINT `f_user_id`
	FOREIGN KEY(`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


--  여행 계획 테이블
CREATE TABLE `plan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `user_id` VARCHAR(20) NOT NUll,	
  `hit` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 관광지 테이블
CREATE TABLE `place` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `plan_id` INT NOT NULL,
  `place_id` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `lat` DECIMAL(10, 8) NOT NULL,
  `lng` DECIMAL(11, 8) NOT NULL,
  `image_url` VARCHAR(255),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 여행 계획 댓글 테이블
CREATE TABLE `comment` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `content` VARCHAR(500) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `plan_id` INT NOT NULL,
    `user_id` VARCHAR(20) NOT NULL,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`plan_id`) REFERENCES `plan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 좋아요 테이블 (핫플레이스)
create table `like` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `hotplace_id` INT NOT NULL,
    `user_id` VARCHAR(20) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`),
    FOREIGN KEY (`hotplace_id`) REFERENCES `hotplace` (`num`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 게시판 테이블
CREATE TABLE `board` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(250) NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id` VARCHAR(20) NOT NULL,
    `hit` INT NOT NULL DEFAULT 0,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 공지사항 테이블
CREATE TABLE `notice` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(250) NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id` VARCHAR(20) NOT NULL,
    `hit` INT NOT NULL DEFAULT 0,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;