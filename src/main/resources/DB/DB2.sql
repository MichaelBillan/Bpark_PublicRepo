CREATE DATABASE  IF NOT EXISTS `bpark` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bpark`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: bpark
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `late_parkings`
--

DROP TABLE IF EXISTS `late_parkings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `late_parkings` (
  `subId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parkingCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lateDuration` int NOT NULL,
  PRIMARY KEY (`subId`,`parkingCode`),
  KEY `parkingCode` (`parkingCode`),
  CONSTRAINT `late_parkings_ibfk_1` FOREIGN KEY (`subId`) REFERENCES `subscribers` (`subscriber_id`),
  CONSTRAINT `late_parkings_ibfk_2` FOREIGN KEY (`parkingCode`) REFERENCES `parking_sessions` (`parking_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `late_parkings`
--

LOCK TABLES `late_parkings` WRITE;
/*!40000 ALTER TABLE `late_parkings` DISABLE KEYS */;
/*!40000 ALTER TABLE `late_parkings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_sessions`
--

DROP TABLE IF EXISTS `parking_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_sessions` (
  `session_id` int NOT NULL AUTO_INCREMENT,
  `parking_code` varchar(50) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `duration` int NOT NULL,
  `last_extension_time` datetime DEFAULT NULL,
  `status` enum('RESERVED','ACTIVE','COMPLETED','CANCELLED') DEFAULT 'RESERVED',
  `extension_count` int DEFAULT '0',
  `space_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `subscriber_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`session_id`),
  UNIQUE KEY `parking_code` (`parking_code`),
  KEY `space_id` (`space_id`),
  KEY `subscriber_id` (`subscriber_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `parking_sessions_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `parkingspots` (`spot_id`),
  CONSTRAINT `parking_sessions_ibfk_2` FOREIGN KEY (`subscriber_id`) REFERENCES `subscribers` (`subscriber_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_sessions`
--

LOCK TABLES `parking_sessions` WRITE;
/*!40000 ALTER TABLE `parking_sessions` DISABLE KEYS */;
INSERT INTO `parking_sessions` VALUES (1,'BP-20250709-2516','2025-07-09 16:14:29',6,NULL,'COMPLETED',0,'A1','SUB-130F'),(3,'BP-20250709-1BAD','2025-07-09 16:26:10',31,NULL,'COMPLETED',0,'A1','SUB-130F'),(5,'BP-20250709-6CE8','2025-07-09 17:02:21',15,NULL,'COMPLETED',0,'A1','SUB-130F'),(8,'BP-20250709-AA4F','2025-07-09 17:44:26',1,NULL,'COMPLETED',0,'A1','SUB-6708'),(9,'BP-20250605-1A2B','2025-06-05 08:00:00',60,'2025-06-05 09:00:00','COMPLETED',1,'A1','SUB-130F'),(10,'BP-20250607-2C3D','2025-06-07 10:30:00',45,NULL,'COMPLETED',0,'A2','SUB-1CEE'),(11,'BP-20250610-3E4F','2025-06-10 14:00:00',120,'2025-06-10 15:30:00','COMPLETED',2,'A3','SUB-55CE'),(12,'BP-20250612-4G5H','2025-06-12 12:00:00',90,NULL,'COMPLETED',0,'B1','SUB-63C6'),(13,'BP-20250614-5J6K','2025-06-14 09:15:00',30,NULL,'COMPLETED',0,'B2','SUB-6708'),(14,'BP-20250616-6L7M','2025-06-16 15:00:00',60,'2025-06-16 15:30:00','COMPLETED',1,'B3','SUB-130F'),(15,'BP-20250618-7N8P','2025-06-18 17:20:00',120,NULL,'COMPLETED',0,'C1','SUB-1CEE'),(16,'BP-20250620-8Q9R','2025-06-20 08:45:00',60,NULL,'COMPLETED',0,'C2','SUB-55CE'),(17,'BP-20250623-9S0T','2025-06-23 13:10:00',45,NULL,'COMPLETED',0,'D1','SUB-63C6'),(18,'BP-20250628-0U1V','2025-06-28 19:30:00',90,'2025-06-28 20:00:00','COMPLETED',1,'D2','SUB-6708'),(19,'BP-20250702-A2B3','2025-07-02 08:00:00',60,'2025-07-02 09:00:00','COMPLETED',1,'A1','SUB-55CE'),(20,'BP-20250703-C4D5','2025-07-03 11:00:00',90,NULL,'COMPLETED',0,'A2','SUB-1CEE'),(21,'BP-20250706-E6F7','2025-07-06 13:30:00',60,'2025-07-06 14:30:00','COMPLETED',1,'A3','SUB-63C6'),(22,'BP-20250708-G8H9','2025-07-08 16:00:00',120,NULL,'COMPLETED',0,'B1','SUB-6708'),(23,'BP-20250711-J0K1','2025-07-11 09:45:00',45,NULL,'COMPLETED',0,'B2','SUB-130F'),(24,'BP-20250714-L2M3','2025-07-14 12:15:00',60,'2025-07-14 13:00:00','COMPLETED',1,'B3','SUB-1CEE'),(25,'BP-20250717-N4P5','2025-07-17 18:00:00',90,NULL,'COMPLETED',0,'C1','SUB-55CE'),(26,'BP-20250721-Q6R7','2025-07-21 07:50:00',120,NULL,'COMPLETED',0,'C2','SUB-63C6'),(27,'BP-20250724-S8T9','2025-07-24 14:40:00',60,NULL,'COMPLETED',0,'D1','SUB-6708'),(28,'BP-20250728-U0V1','2025-07-28 17:15:00',45,NULL,'COMPLETED',0,'D2','SUB-130F');
/*!40000 ALTER TABLE `parking_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parkingspots`
--

DROP TABLE IF EXISTS `parkingspots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parkingspots` (
  `spot_id` varchar(10) NOT NULL,
  `spot_status` enum('AVAILABLE','RESERVED','OCCUPIED') NOT NULL DEFAULT 'AVAILABLE',
  PRIMARY KEY (`spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parkingspots`
--

LOCK TABLES `parkingspots` WRITE;
/*!40000 ALTER TABLE `parkingspots` DISABLE KEYS */;
INSERT INTO `parkingspots` VALUES ('A1','OCCUPIED'),('A2','OCCUPIED'),('A3','OCCUPIED'),('B1','OCCUPIED'),('B2','OCCUPIED'),('B3','OCCUPIED'),('C1','OCCUPIED'),('C2','OCCUPIED'),('D1','OCCUPIED'),('D2','OCCUPIED');
/*!40000 ALTER TABLE `parkingspots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `res_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `confirmation_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `reservation_status` enum('PENDING','CONFIRMED','CANCELLED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `start_time` datetime NOT NULL,
  `duration` int NOT NULL,
  `sub_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `space_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`res_id`),
  UNIQUE KEY `confirmation_code_UNIQUE` (`confirmation_code`),
  KEY `sub_id` (`sub_id`),
  KEY `space_id` (`space_id`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`sub_id`) REFERENCES `subscribers` (`subscriber_id`),
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`space_id`) REFERENCES `parkingspots` (`spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES ('Res-2ecc51a7','Con-2ecc51a7','CONFIRMED','2025-07-11 07:00:00',540,'SUB-130F','A1'),('Res-aaa12345','Con-aaa12345','CONFIRMED','2025-06-14 10:00:00',120,'SUB-130F','A1'),('Res-bbb12345','Con-bbb12345','CANCELLED','2025-06-16 11:30:00',60,'SUB-1CEE','A2'),('Res-ccc12345','Con-ccc12345','CONFIRMED','2025-06-19 08:15:00',180,'SUB-55CE','A3'),('Res-ddd12345','Con-ddd12345','CANCELLED','2025-07-02 09:00:00',90,'SUB-63C6','B1'),('Res-eee12345','Con-eee12345','CONFIRMED','2025-07-05 14:45:00',240,'SUB-6708','B2'),('Res-fff12345','Con-fff12345','CANCELLED','2025-07-07 16:20:00',60,'SUB-130F','B3'),('Res-ggg12345','Con-ggg12345','CONFIRMED','2025-07-08 18:10:00',120,'SUB-1CEE','C1'),('Res-hhh12345','Con-hhh12345','CANCELLED','2025-07-08 19:00:00',90,'SUB-55CE','C2'),('Res-iii12345','Con-iii12345','PENDING','2025-07-10 10:00:00',120,'SUB-63C6','D1'),('Res-jjj12345','Con-jjj12345','PENDING','2025-07-11 12:00:00',90,'SUB-6708','D2'),('Res-kkk12345','Con-kkk12345','PENDING','2025-07-12 08:30:00',60,'SUB-130F','A1'),('Res-lll12345','Con-lll12345','PENDING','2025-07-13 13:45:00',180,'SUB-1CEE','A2'),('Res-mmm12345','Con-mmm12345','PENDING','2025-07-14 09:10:00',120,'SUB-55CE','A3'),('Res-nnn12345','Con-nnn12345','PENDING','2025-07-15 11:25:00',60,'SUB-63C6','B1'),('Res-ooo12345','Con-ooo12345','PENDING','2025-07-16 15:55:00',90,'SUB-6708','B2'),('Res-ppp12345','Con-ppp12345','PENDING','2025-07-17 16:40:00',60,'SUB-130F','B3'),('Res-qqq12345','Con-qqq12345','PENDING','2025-07-18 18:50:00',120,'SUB-1CEE','C1'),('Res-rrr12345','Con-rrr12345','PENDING','2025-07-19 20:00:00',180,'SUB-55CE','C2');
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscribers`
--

DROP TABLE IF EXISTS `subscribers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscribers` (
  `subscriber_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`subscriber_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscribers`
--

LOCK TABLES `subscribers` WRITE;
/*!40000 ALTER TABLE `subscribers` DISABLE KEYS */;
INSERT INTO `subscribers` VALUES ('SUB-130F','Michael Billan','0558813026','michael7billan@gmail.com'),('SUB-1CEE','Ofer Shwarts','0525493785','ofer@gmail.com'),('SUB-55CE','Elia Cohen','0549425673','elia@gmail.com'),('SUB-63C6','Danial Kats','0528594035','daniel@gmail.com'),('SUB-6708','Gyan Livy','0543328250','gyan812@gmail.com');
/*!40000 ALTER TABLE `subscribers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-09 23:33:57
