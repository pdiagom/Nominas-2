-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.39 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para empresa
CREATE DATABASE IF NOT EXISTS `empresa` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `empresa`;

-- Volcando estructura para procedimiento empresa.calcular_nomina
DELIMITER //
CREATE PROCEDURE `calcular_nomina`(
	IN `categoria` INT,
	IN `anyos` INT,
	OUT `sueldo` DECIMAL(10,2)
)
BEGIN
 DECLARE SUELDO_BASE DECIMAL(10, 2);

    -- Asignar el sueldo base según la categoría
    CASE categoria
        WHEN 1 THEN SET SUELDO_BASE = 50000;
        WHEN 2 THEN SET SUELDO_BASE = 70000;
        WHEN 3 THEN SET SUELDO_BASE = 90000;
        WHEN 4 THEN SET SUELDO_BASE = 110000;
        WHEN 5 THEN SET SUELDO_BASE = 130000;
        WHEN 6 THEN SET SUELDO_BASE = 150000;
        WHEN 7 THEN SET SUELDO_BASE = 170000;
        WHEN 8 THEN SET SUELDO_BASE = 190000;
        WHEN 9 THEN SET SUELDO_BASE = 210000;
        WHEN 10 THEN SET SUELDO_BASE = 230000;
        ELSE SET SUELDO_BASE = 0; -- Valor por defecto si la categoría no es válida
    END CASE;

    -- Calcular el sueldo sumando el sueldo base y el incremento por años trabajados
    SET sueldo = SUELDO_BASE + (5000 * anyos);
END//
DELIMITER ;

-- Volcando estructura para tabla empresa.empleados
CREATE TABLE IF NOT EXISTS `empleados` (
  `dni` varchar(9) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `genero` char(1) DEFAULT NULL,
  `categoria` int DEFAULT '1',
  `anyos_trabajados` int DEFAULT '0',
  PRIMARY KEY (`dni`),
  CONSTRAINT `empleados_chk_1` CHECK ((`genero` in (_utf8mb4'M',_utf8mb4'F'))),
  CONSTRAINT `empleados_chk_2` CHECK (((`categoria` >= 1) and (`categoria` <= 10))),
  CONSTRAINT `empleados_chk_3` CHECK ((`anyos_trabajados` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla empresa.nominas
CREATE TABLE IF NOT EXISTS `nominas` (
  `id_nomina` int NOT NULL AUTO_INCREMENT,
  `dni` varchar(9) DEFAULT NULL,
  `sueldo` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_nomina`),
  KEY `fk_empleado` (`dni`),
  CONSTRAINT `fk_empleado` FOREIGN KEY (`dni`) REFERENCES `empleados` (`dni`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para disparador empresa.empleados_after_insert
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `empleados_after_insert` AFTER INSERT ON `empleados` FOR EACH ROW BEGIN
 DECLARE sueldo DECIMAL(10, 2);
    
    -- Llamamos al procedimiento para calcular el sueldo
    CALL calcular_nomina(NEW.categoria, NEW.anyos_trabajados, sueldo);

    -- Insertamos la nómina en la tabla nominas
    INSERT INTO nominas (dni, sueldo) 
    VALUES (NEW.dni, sueldo);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador empresa.empleados_after_update
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `empleados_after_update` AFTER UPDATE ON `empleados` FOR EACH ROW BEGIN
   DECLARE sueldo_calculado DECIMAL(10, 2);

    -- Llamamos al procedimiento para calcular el sueldo
    CALL calcular_nomina(NEW.categoria, NEW.anyos_trabajados, sueldo_calculado);

    -- Actualizamos la nómina en la tabla nominas
    UPDATE nominas 
    SET sueldo = sueldo_calculado
    WHERE dni = NEW.dni;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
