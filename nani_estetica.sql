CREATE DATABASE  IF NOT EXISTS `nani_estetica` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `nani_estetica`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: nani_estetica
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK7v5154akc3klprxpsrkb72eae` FOREIGN KEY (`id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('1'),('3fc499c8-66f1-472c-8aaa-7db5796039fb'),('43b50956-9f8a-4850-b5cd-86c5061b6cb3'),('71e9a22d-74e6-4693-a8a5-4a64c7506e0f'),('c76a1861-af3a-4ed5-9e0b-a29688505632'),('cb4efa15-17ec-4f92-94b4-281c6e6ad174'),('f6bbb537-2d5a-4b94-98e3-4f2221814e31');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id` varchar(255) NOT NULL,
  `alcohol` varchar(255) DEFAULT NULL,
  `deportes` varchar(255) DEFAULT NULL,
  `drogas` varchar(255) DEFAULT NULL,
  `ejercicios` varchar(255) DEFAULT NULL,
  `fomulario_datos` bit(1) DEFAULT NULL,
  `fumador` varchar(255) DEFAULT NULL,
  `medicamentos` varchar(255) DEFAULT NULL,
  `nombre_medicamento` varchar(255) DEFAULT NULL,
  `alteracion_hormonal` varchar(255) DEFAULT NULL,
  `amamantando` varchar(255) DEFAULT NULL,
  `ciclo_menstrual` varchar(255) DEFAULT NULL,
  `cirugia_estetica` varchar(255) DEFAULT NULL,
  `consumo_carbohidratos` varchar(255) DEFAULT NULL,
  `corticoides` varchar(255) DEFAULT NULL,
  `cual_enfermedad` varchar(255) DEFAULT NULL,
  `cuidado_de_piel` varchar(255) DEFAULT NULL,
  `embarazo` varchar(255) DEFAULT NULL,
  `exposicion_sol` varchar(255) DEFAULT NULL,
  `formulario_datos` bit(1) DEFAULT NULL,
  `fractura_facial` varchar(255) DEFAULT NULL,
  `horas_sueno` varchar(255) DEFAULT NULL,
  `hormonas` varchar(255) DEFAULT NULL,
  `indique_cirugia_estetica` varchar(255) DEFAULT NULL,
  `marca_pasos` varchar(255) DEFAULT NULL,
  `metodo_anticonceptivo` varchar(255) DEFAULT NULL,
  `motivo_consulta` varchar(255) DEFAULT NULL,
  `paciente_oncologica` varchar(255) DEFAULT NULL,
  `protector_solar` varchar(255) DEFAULT NULL,
  `reaplica_protector` varchar(255) DEFAULT NULL,
  `resultados_tratamiento_anterior` varchar(255) DEFAULT NULL,
  `sufre_enfermedad` varchar(255) DEFAULT NULL,
  `tiene_implantes` varchar(255) DEFAULT NULL,
  `tiroides` varchar(255) DEFAULT NULL,
  `tratamientos_faciales_anteriores` varchar(255) DEFAULT NULL,
  `vitaminas` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKkpvkbjg32bso6riqge70hwcel` FOREIGN KEY (`id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('263cbc34-ea31-4e77-b6d5-0b5f6aab3082',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('7a3d0005-9b18-4775-b66b-089e69b33870',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('8955757e-d788-4266-9004-0746a1f9a536',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('9bb258b4-94ba-4a45-8e92-21605941dd17',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('9e0528c3-f901-40c4-bfca-b37d4c0c1a29','Poco','NO','NO','NO',_binary '\0','NO','NO','xxxx','NO','NO','Irregular','xxxx','Poco','NO','xxxx','con agua y jabon','NO','3',NULL,'NO','Menos de ocho horas','NO','xxxx','NO','NO','Prevencion','NO','NO','NO','Malo','NO','NO','NO','NO','NO'),('a6282491-a0f6-4daf-81e8-3482148d02b8',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('b6d284ea-a24f-45d1-a556-302944d5234b',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('c5edc92d-91a9-440f-842f-ba83f3dac078',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('dc2dba35-4588-43d9-9859-75e76d5ce126',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consulta`
--

DROP TABLE IF EXISTS `consulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consulta` (
  `id` varchar(255) NOT NULL,
  `calificacion` int DEFAULT NULL,
  `diagnostico` varchar(6000) DEFAULT NULL,
  `fecha` varchar(255) DEFAULT NULL,
  `horario` int DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `cliente_id` varchar(255) DEFAULT NULL,
  `profesional_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3v3mb9gajlhqgbavxh67248bp` (`cliente_id`),
  UNIQUE KEY `UK_1qkmcqs5t9ure40fqopuyke2f` (`profesional_id`),
  CONSTRAINT `FK4lr0iihp4q619eishm0rh5g0u` FOREIGN KEY (`profesional_id`) REFERENCES `profesional` (`id`),
  CONSTRAINT `FKniywxol34kw4bcbesfdpu42wh` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consulta`
--

LOCK TABLES `consulta` WRITE;
/*!40000 ALTER TABLE `consulta` DISABLE KEYS */;
/*!40000 ALTER TABLE `consulta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `apellido` varchar(255) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `domicilio` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `ocupacion` varchar(255) DEFAULT NULL,
  `sexo` enum('FEMENINO','MASCULINO','OTRO') DEFAULT NULL,
  `telefono` int DEFAULT NULL,
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKe6dw7hkl2jsqqcecbmtwdplxb` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES ('celular','77779999','las casuarinas','2023-12-11','Celular',NULL,'OTRO',111,'0bd18ef3-8ca6-46e1-809c-05e60fe49996'),('pepitona','4444444','PEPITONA','2023-12-20','pepitona',NULL,'MASCULINO',66473476,'0fe65e26-11ae-4bd3-8e9e-55753a85ff05'),('admin','1111111','SARMIENTO','1987-07-16','admin','ADMINISTRADORPRUEBA','FEMENINO',222222,'1'),('cliente2','1122333','cliente2',NULL,'cliente2','cliente2','OTRO',332211,'263cbc34-ea31-4e77-b6d5-0b5f6aab3082'),('profesional2','222222','PROFESIONAL2','2023-12-20','profesional2',NULL,'MASCULINO',1232122,'2a53a56a-d043-4aee-8909-72a04e61e9a8'),('Cortesia','19114383','Sarmiento 675','1987-07-16','Alvaro','Ingeniero','MASCULINO',1171511537,'3cee5e81-450e-418f-8590-7434f3a2e01e'),('Cortesia','95999888','SARMIENTO888','2014-07-14','Santiago','GAMERS','MASCULINO',101010,'3fc499c8-66f1-472c-8aaa-7db5796039fb'),('nuevoAdmin','9999999','nuevoAdmin','2023-12-22','nuevoAdmin','nuevoAdmin','MASCULINO',8888888,'43b50956-9f8a-4850-b5cd-86c5061b6cb3'),('pro','111222','Av. siempre viva 123','2023-12-01','pro',NULL,'MASCULINO',10101,'527bbfc9-c2ee-4c62-a076-fac8d5f24e34'),('cliente5','99887766','cliente5',NULL,'cliente5','cliente5','MASCULINO',1112233,'71e9a22d-74e6-4693-a8a5-4a64c7506e0f'),('Benitez','95969342','Sarmiento 675','1991-07-09','Nani','Esteticista','FEMENINO',1171717171,'74a71dfd-44f8-4e89-91b7-841ed76c9074'),('cliente3','365820765','CLIENTE3','2023-12-11','cliente','CLIENTE3','MASCULINO',73637299,'7a3d0005-9b18-4775-b66b-089e69b33870'),('edad3','6453933','edad3',NULL,'edad3','edad3','FEMENINO',7472343,'8955757e-d788-4266-9004-0746a1f9a536'),('cliente1','0986780','cliente1','2023-12-18','cliente','cliente1','FEMENINO',7764677,'9bb258b4-94ba-4a45-8e92-21605941dd17'),('Adriana','546879654','sarmiento 675',NULL,'Adriana','Adriana','FEMENINO',115588990,'9e0528c3-f901-40c4-bfca-b37d4c0c1a29'),('cliente2','76540292','CLIENTE2PRUEBA','2023-12-20','cliente','CLIENTE2','MASCULINO',635300,'a6282491-a0f6-4daf-81e8-3482148d02b8'),('profesional3','012012012','profesional3','2023-11-27','profesional3',NULL,'FEMENINO',54376352,'a88f4c6d-19f8-4fe2-ad54-66d4884677f8'),('formulario','123456789','formulario','2023-12-21','formulario','formulario','OTRO',767676,'b6d284ea-a24f-45d1-a556-302944d5234b'),('edad','11223344','edad',NULL,'edad','edad','FEMENINO',443311,'c5edc92d-91a9-440f-842f-ba83f3dac078'),('admin3','12034010','admin3','2023-12-26','admin3','admin3','MASCULINO',5432726,'c76a1861-af3a-4ed5-9e0b-a29688505632'),('administrador','0000000','administrador','2023-12-16','administrador','administrador','MASCULINO',101010,'cb4efa15-17ec-4f92-94b4-281c6e6ad174'),('edad2','7766554','edad2',NULL,'edad2',NULL,'FEMENINO',535262,'d1c519fd-ba05-4aaf-8856-72caac5d5faa'),('profesional','12322312','profesional','2023-12-06','profesional',NULL,'MASCULINO',8238998,'db296c05-574d-498b-b6aa-1753610234a4'),('Telefono','22222222','TELEFONODIRECCION','2023-12-13','Telefono','NUEVOTELEFONO','FEMENINO',1111111,'dc2dba35-4588-43d9-9859-75e76d5ce126'),('play5','112235422','Sarmiento 675',NULL,'play5','Medico','MASCULINO',112233442,'f6bbb537-2d5a-4b94-98e3-4f2221814e31');
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesional`
--

DROP TABLE IF EXISTS `profesional`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profesional` (
  `calificacion` double DEFAULT NULL,
  `especialidad` varchar(255) DEFAULT NULL,
  `matricula` varchar(255) DEFAULT NULL,
  `precio_consulta` double DEFAULT NULL,
  `provincia` enum('BUENOS_AIRES','CATAMARCA','CHACO','CHUBUT','CORDOBA','CORRIENTES','ENTRE_RIOS','FORMOSA','JUJUY','LA_PAMPA','LA_RIOJA','MENDOZA','MISIONES','NEUQUEN','RIO_NEGRO','SALTA','SANTA_CRUZ','SANTA_FE','SANTIAGO_DEL_ESTERO','SAN_JUAN','SAN_LUIS','TIERRA_DEL_FUEGO','TUCUMAN') DEFAULT NULL,
  `tratamientos` enum('FACIALES_ANTIACNE','FACIALES_ANTIAGE','FACIALES_DESPIGMENTANTES','FACIALES_HIDRATANTES','FACIALES_ROSACEAS','HYDRALIPS','LAMINADO_CEJAS','LIFTING_PESTAÑAS','LIMPIEZA_ESPALDA','PERFILADO_CEJAS') DEFAULT NULL,
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK7epsp7iy5f4xuk8odb3t6x69o` FOREIGN KEY (`id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesional`
--

LOCK TABLES `profesional` WRITE;
/*!40000 ALTER TABLE `profesional` DISABLE KEYS */;
INSERT INTO `profesional` VALUES (NULL,'celularpro','PPP-89',NULL,NULL,NULL,'0bd18ef3-8ca6-46e1-809c-05e60fe49996'),(NULL,NULL,NULL,NULL,NULL,NULL,'0fe65e26-11ae-4bd3-8e9e-55753a85ff05'),(NULL,'profesional2','BCC-321',NULL,NULL,NULL,'2a53a56a-d043-4aee-8909-72a04e61e9a8'),(NULL,'pro','BGF-654',NULL,NULL,NULL,'527bbfc9-c2ee-4c62-a076-fac8d5f24e34'),(NULL,'profesional3','BGC-6453',NULL,NULL,NULL,'a88f4c6d-19f8-4fe2-ad54-66d4884677f8'),(NULL,'edad2','PPP-777',NULL,NULL,NULL,'d1c519fd-ba05-4aaf-8856-72caac5d5faa'),(NULL,'profesional','ABC-123',NULL,NULL,NULL,'db296c05-574d-498b-b6aa-1753610234a4');
/*!40000 ALTER TABLE `profesional` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` varchar(255) NOT NULL,
  `validacion_form` bit(1) DEFAULT NULL,
  `activo` bit(1) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `rol` enum('ADMIN','CLIENTE','PROFESIONAL') DEFAULT NULL,
  `persona_id` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c30a561f4mytkssjk76n0c1b3` (`persona_id`),
  CONSTRAINT `FKlse7lqghmt3r1sp298ss9s5bc` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('0bd18ef3-8ca6-46e1-809c-05e60fe49996',_binary '',_binary '','QWEasd12','CELULAR@CELULAR.COM','PROFESIONAL',NULL,NULL),('0fe65e26-11ae-4bd3-8e9e-55753a85ff05',_binary '',_binary '','QWEasd12','PEPITONA@PEPITONA.COM','PROFESIONAL',NULL,NULL),('1',_binary '',_binary '','QWEasd12','ADMIN@ADMINnuevo.COM','ADMIN',NULL,NULL),('263cbc34-ea31-4e77-b6d5-0b5f6aab3082',_binary '',_binary '','POIñlk09','CLIENTE2@CLIENTE2.COM','CLIENTE',NULL,NULL),('2a53a56a-d043-4aee-8909-72a04e61e9a8',_binary '',_binary '','QWEasd12','PROFESIONAL2@PROFESIONAL2.COM','PROFESIONAL',NULL,NULL),('3cee5e81-450e-418f-8590-7434f3a2e01e',_binary '',_binary '','ALVAro17','alvaro@claro.com','ADMIN',NULL,NULL),('3fc499c8-66f1-472c-8aaa-7db5796039fb',_binary '',_binary '','POIñlk09','SANTI@SANTI.COM','ADMIN',NULL,NULL),('43b50956-9f8a-4850-b5cd-86c5061b6cb3',_binary '',_binary '','QWEasd12','nike@nike.com','ADMIN',NULL,NULL),('527bbfc9-c2ee-4c62-a076-fac8d5f24e34',_binary '',_binary '','POIñlk09','PRO@PRO.COM','PROFESIONAL',NULL,NULL),('71e9a22d-74e6-4693-a8a5-4a64c7506e0f',_binary '\0',_binary '','QWEasd12','cliente5@cliente5.com','CLIENTE',NULL,'2010-12-12'),('74a71dfd-44f8-4e89-91b7-841ed76c9074',_binary '',_binary '','QWEasd12','nani@nani.com','ADMIN',NULL,NULL),('7a3d0005-9b18-4775-b66b-089e69b33870',_binary '',_binary '','QWEasd12','CLIENTE3@CLIENTE.COM','CLIENTE',NULL,NULL),('8955757e-d788-4266-9004-0746a1f9a536',_binary '',_binary '','QWEasd12','edad3@edad3.com','CLIENTE',NULL,'2009-06-06'),('9bb258b4-94ba-4a45-8e92-21605941dd17',_binary '',_binary '','QWEasd12','cliente1@cliente.com','CLIENTE',NULL,NULL),('9e0528c3-f901-40c4-bfca-b37d4c0c1a29',_binary '',_binary '','QWEasd12','adriana@adriana.com','CLIENTE',NULL,'1991-06-10'),('a6282491-a0f6-4daf-81e8-3482148d02b8',_binary '',_binary '','QWEasd12','CLIENTE2@CLIENTE.COM','CLIENTE',NULL,NULL),('a88f4c6d-19f8-4fe2-ad54-66d4884677f8',_binary '',_binary '','QWEasd12','profesional3@pro.com','PROFESIONAL',NULL,NULL),('b6d284ea-a24f-45d1-a556-302944d5234b',_binary '',_binary '','QWEasd12','fomulario@formulario.com','CLIENTE',NULL,NULL),('c5edc92d-91a9-440f-842f-ba83f3dac078',_binary '',_binary '','QWEasd12','EDAD@EDAD.COM','CLIENTE',NULL,'2005-11-17'),('c76a1861-af3a-4ed5-9e0b-a29688505632',_binary '',_binary '','QWEasd12','admin3@admin.com','ADMIN',NULL,NULL),('cb4efa15-17ec-4f92-94b4-281c6e6ad174',_binary '',_binary '','QWEasd12','administrador@administrador.com','ADMIN',NULL,NULL),('d1c519fd-ba05-4aaf-8856-72caac5d5faa',_binary '',_binary '','QWEasd12','EDAD2@EDAD2.COM','PROFESIONAL',NULL,'2009-07-21'),('db296c05-574d-498b-b6aa-1753610234a4',_binary '',_binary '','QWEasd12','profesional@profesional.com','PROFESIONAL',NULL,NULL),('dc2dba35-4588-43d9-9859-75e76d5ce126',_binary '\0',_binary '','QWEasd12','TELEFONO@TELEFONONUEVO.COM','ADMIN',NULL,NULL),('f6bbb537-2d5a-4b94-98e3-4f2221814e31',_binary '',_binary '','QWEasd12','PLAY5@PLAY.COM','ADMIN',NULL,'2007-06-05');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-24 23:14:51
