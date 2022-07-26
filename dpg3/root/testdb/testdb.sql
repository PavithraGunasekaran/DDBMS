--------Database dump-----------
-----------------------------------------------
Host : FIRST	 Database : testdb

------------
Table structure for financedepartment
------------
DROP TABLE IF EXISTS financedepartment;
CREATE TABLE testdb.financedepartment
(
DEPTID INT NOT NULL,NAME VARCHAR(80),PRIMARY KEY-DEPTID,FOREIGN KEY-DEPTID:DEPARTMENT(DEPTID) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
------------
Dumping data for table financedepartment
------------
LOCK TABLES financedepartment WRITE;
/*!40000 ALTER TABLE `financedepartment` DISABLE KEYS */
INSERT INTO financedepartment VALUES (1, e),(2, b);
/*!40000 ALTER TABLE `financedepartment` ENABLE KEYS */
UNLOCK TABLES;

------------
Table structure for department
------------
DROP TABLE IF EXISTS department;
CREATE TABLE testdb.department
(
DEPTID INT NOT NULL,NAME VARCHAR(80),PRIMARY KEY-DEPTID 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
------------
Dumping data for table department
------------
LOCK TABLES department WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */
INSERT INTO department VALUES (2 , b);
/*!40000 ALTER TABLE `department` ENABLE KEYS */
UNLOCK TABLES;

-----Dump completed on 2022-04-08 14:30:55.844