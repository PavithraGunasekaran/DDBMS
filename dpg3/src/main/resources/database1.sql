--------Database dump-----------
-----------------------------------------------
Host : FIRST	 Database : database1

------------
Table structure for student
------------
DROP TABLE IF EXISTS student;
CREATE TABLE database1.student
(
STUDID INT NOT NULL,NAME VARCHAR(80),PRIMARY KEY-STUDID 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
------------
Dumping data for table student
------------
LOCK TABLES student WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */
INSERT INTO student VALUES (3,rrr),(4,ghu),(3,rrr),(4,ghu);
/*!40000 ALTER TABLE `student` ENABLE KEYS */
UNLOCK TABLES;

------------
Table structure for student1
------------
DROP TABLE IF EXISTS student1;
CREATE TABLE database1.student1
(
STUDID INT NOT NULL,NAME VARCHAR(80),PRIMARY KEY-STUDID 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-----Dump completed on 2022-03-31 23:23:23.698