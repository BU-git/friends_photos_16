-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Ноя 15 2015 г., 22:17
-- Версия сервера: 5.5.46
-- Версия PHP: 5.3.10-1ubuntu3.21

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `friendsphotos`
--

-- --------------------------------------------------------

--
-- Структура таблицы `accounts`
--
-- Создание: Ноя 15 2015 г., 20:04
--

CREATE TABLE IF NOT EXISTS `accounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `email` varchar(255),
  `fb_id` varchar(255) DEFAULT NULL,
  `fb_profile_url` varchar(255) DEFAULT NULL,
  `fb_token` varchar(255) DEFAULT NULL,
  `guest` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `vk_id` varchar(255) DEFAULT NULL,
  `vk_profile_url` varchar(255) DEFAULT NULL,
  `vk_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Структура таблицы `accounts_groups`
--
-- Создание: Ноя 15 2015 г., 20:15
--

CREATE TABLE IF NOT EXISTS `accounts_groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL DEFAULT '1',
  `account_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk8hu2h76qobbyl54968qygbk` (`account_id`),
  KEY `FKklf0v50gkwrrr6ondf993ixm9` (`group_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `comments`
--
-- Создание: Ноя 15 2015 г., 20:04
--

CREATE TABLE IF NOT EXISTS `comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkme10xlodeckscmx4urfb50q5` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `groups`
--
-- Создание: Ноя 15 2015 г., 20:04
--

CREATE TABLE IF NOT EXISTS `groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `expire_date` date DEFAULT NULL,
  `group_type` varchar(255) DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `visible` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm8hjkaiexhs4km0ewx539sk1k` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `groups_comments`
--
-- Создание: Ноя 15 2015 г., 20:04
--

CREATE TABLE IF NOT EXISTS `groups_comments` (
  `group_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_5n6j5b6piu0ilsl2ytw25k9r` (`comments_id`),
  KEY `FK7olox9xr2lb2n0r9y56wpliap` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `photos`
--
-- Создание: Ноя 15 2015 г., 20:04
--

CREATE TABLE IF NOT EXISTS `photos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `preview_url` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKii7cq96qtw9ddno2hcemefp8a` (`group_id`),
  KEY `FKgy507q953m6ba1ik8vo9ew368` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `photos_comments`
--
-- Создание: Ноя 15 2015 г., 20:05
--

CREATE TABLE IF NOT EXISTS `photos_comments` (
  `photo_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_b6d2eeh2ccei3nbrot0fce39g` (`comments_id`),
  KEY `FK75om0dg3qmoti4upba83j54ob` (`photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `roles`
--
-- Создание: Ноя 15 2015 г., 20:08
--

CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `accounts_groups`
--
ALTER TABLE `accounts_groups`
ADD CONSTRAINT `accounts_groups_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
ADD CONSTRAINT `FKk8hu2h76qobbyl54968qygbk` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
ADD CONSTRAINT `FKklf0v50gkwrrr6ondf993ixm9` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`);

--
-- Ограничения внешнего ключа таблицы `comments`
--
ALTER TABLE `comments`
ADD CONSTRAINT `FKkme10xlodeckscmx4urfb50q5` FOREIGN KEY (`author_id`) REFERENCES `accounts` (`id`);

--
-- Ограничения внешнего ключа таблицы `groups`
--
ALTER TABLE `groups`
ADD CONSTRAINT `FKm8hjkaiexhs4km0ewx539sk1k` FOREIGN KEY (`owner_id`) REFERENCES `accounts` (`id`);

--
-- Ограничения внешнего ключа таблицы `groups_comments`
--
ALTER TABLE `groups_comments`
ADD CONSTRAINT `FK7olox9xr2lb2n0r9y56wpliap` FOREIGN KEY (`Group_id`) REFERENCES `groups` (`id`),
ADD CONSTRAINT `FKr3fj48hy5rn73obot6niilhpa` FOREIGN KEY (`comments_id`) REFERENCES `comments` (`id`);

--
-- Ограничения внешнего ключа таблицы `photos`
--
ALTER TABLE `photos`
ADD CONSTRAINT `FKgy507q953m6ba1ik8vo9ew368` FOREIGN KEY (`owner_id`) REFERENCES `accounts` (`id`),
ADD CONSTRAINT `FKii7cq96qtw9ddno2hcemefp8a` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`);

--
-- Ограничения внешнего ключа таблицы `photos_comments`
--
ALTER TABLE `photos_comments`
ADD CONSTRAINT `FK75om0dg3qmoti4upba83j54ob` FOREIGN KEY (`Photo_id`) REFERENCES `photos` (`id`),
ADD CONSTRAINT `FKqubsxk100fwyde607ip2neeiy` FOREIGN KEY (`comments_id`) REFERENCES `comments` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
