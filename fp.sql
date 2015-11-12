CREATE DATABASE  IF NOT EXISTS `friendsphotos` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `friendsphotos`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win32 (AMD64)
--
-- Host: 127.0.0.1    Database: friendsphotos
-- ------------------------------------------------------
-- Server version	5.7.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_group`
--

DROP TABLE IF EXISTS `account_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_group` (
  `id` bigint(20) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk8hu2h76qobbyl54968qygbk` (`account_id`),
  KEY `FKklf0v50gkwrrr6ondf993ixm9` (`group_id`),
  CONSTRAINT `FKk8hu2h76qobbyl54968qygbk` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `FKklf0v50gkwrrr6ondf993ixm9` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_group`
--

LOCK TABLES `account_group` WRITE;
/*!40000 ALTER TABLE `account_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkme10xlodeckscmx4urfb50q5` (`author_id`),
  CONSTRAINT `FKkme10xlodeckscmx4urfb50q5` FOREIGN KEY (`author_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `expire_date` date DEFAULT NULL,
  `group_type` varchar(255) DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm8hjkaiexhs4km0ewx539sk1k` (`owner_id`),
  CONSTRAINT `FKm8hjkaiexhs4km0ewx539sk1k` FOREIGN KEY (`owner_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups_comments`
--

DROP TABLE IF EXISTS `groups_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups_comments` (
  `Group_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_5n6j5b6piu0ilsl2ytw25k9r` (`comments_id`),
  KEY `FK7olox9xr2lb2n0r9y56wpliap` (`Group_id`),
  CONSTRAINT `FK7olox9xr2lb2n0r9y56wpliap` FOREIGN KEY (`Group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `FKr3fj48hy5rn73obot6niilhpa` FOREIGN KEY (`comments_id`) REFERENCES `comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups_comments`
--

LOCK TABLES `groups_comments` WRITE;
/*!40000 ALTER TABLE `groups_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `groups_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `photos`
--

DROP TABLE IF EXISTS `photos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `preview_url` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKii7cq96qtw9ddno2hcemefp8a` (`group_id`),
  KEY `FKgy507q953m6ba1ik8vo9ew368` (`owner_id`),
  CONSTRAINT `FKgy507q953m6ba1ik8vo9ew368` FOREIGN KEY (`owner_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `FKii7cq96qtw9ddno2hcemefp8a` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `photos`
--

LOCK TABLES `photos` WRITE;
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `photos_comments`
--

DROP TABLE IF EXISTS `photos_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photos_comments` (
  `Photo_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_b6d2eeh2ccei3nbrot0fce39g` (`comments_id`),
  KEY `FK75om0dg3qmoti4upba83j54ob` (`Photo_id`),
  CONSTRAINT `FK75om0dg3qmoti4upba83j54ob` FOREIGN KEY (`Photo_id`) REFERENCES `photos` (`id`),
  CONSTRAINT `FKqubsxk100fwyde607ip2neeiy` FOREIGN KEY (`comments_id`) REFERENCES `comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `photos_comments`
--

LOCK TABLES `photos_comments` WRITE;
/*!40000 ALTER TABLE `photos_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `photos_comments` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-12  4:27:09
