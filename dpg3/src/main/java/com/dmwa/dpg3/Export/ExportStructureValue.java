package com.dmwa.dpg3.Export;

import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.models.DBLog;
import com.dmwa.dpg3.QueryProcessing.enums.Path;

import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pavithra Gunasekaran
 */
public class ExportStructureValue {

    public static HashMap<String,String> getTablesFromMetaData(String dbName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Path.ROOT+"/"+"metaData.txt"));
        /**
         * TODO - get tables from different vm as per the meta data
         */
        String metaData = br.lines().collect(Collectors.joining("\n"));
        String[] metaDataArray = metaData.split("\n\n");
        String[] tableInfoArray;
        HashMap<String, String> tableInfo = new HashMap<String, String>();
        for(int i=0;i< metaDataArray.length;i++) {
            tableInfoArray=metaDataArray[i].split("\n");
            String columns="";
            for(int j=1;j< tableInfoArray.length;j++){
                columns+=tableInfoArray[j]+",";
            }
            int index = columns.lastIndexOf(",");
            StringBuilder sb = new StringBuilder(columns);
            sb.setCharAt(index, ' ');
            columns = sb.toString();
            tableInfo.put(tableInfoArray[0],columns);

        }
        return  tableInfo;

    }

    public static List<String> getTableValues(String dbName, String tableName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Path.ROOT+"/"+dbName+"/"+tableName+".txt"));
        br.readLine();
        String splitTableValues = br.lines().collect(Collectors.joining("\n"));
        String[] values = splitTableValues.split("\n");
        List<String> tableValues = new ArrayList<>();
        if(values.length!=0) {
            for (String val : values) {
                val = val.replaceAll("\\|", ",");
                tableValues.add(val);
                //System.out.println(val);
            }
        }
        else{
            tableValues= Collections.emptyList();
        }
        System.out.println("tablevalues - "+tableValues);
        return tableValues;
        //System.out.println(tableValues);
    }

    public static void getExportStructureValue(String dbName, VMInstance vmInstance) throws IOException {
        /**
         * TODO
         */
        DBLog dbLog = new DBLog();
        //String vm = vmInstance.toString();
        String file=Path.ROOT+"/"+dbName+"/"+dbName+".sql";
        File myObj = new File(file);
        myObj.createNewFile();
        FileWriter dumpFile=new FileWriter(file);
//        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//        bw.write();
        dumpFile.write("--------Database dump-----------");
        dumpFile.write("\n");
        dumpFile.write("-----------------------------------------------");
        dumpFile.write("\n");
        dumpFile.write("Host : "+vmInstance.toString()+"\t Database : "+dbName);
        dumpFile.write("\n\n");

        HashMap<String,String> tableInfo = getTablesFromMetaData(dbName);

        for (Map.Entry<String, String> entry : tableInfo.entrySet()) {
            //System.out.println(entry.getKey() + ", columns : " + entry.getValue());
            dumpFile.write("------------\n");
            dumpFile.write("Table structure for "+entry.getKey().split("\\.")[1]+"\n");
            dumpFile.write("------------\n");
            dumpFile.write("DROP TABLE IF EXISTS "+entry.getKey().split("\\.")[1]+";");
            dumpFile.write("\n");
            dumpFile.write("CREATE TABLE "+entry.getKey()+"\n(\n"+entry.getValue()+"\n)");
            dumpFile.write(" ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
            dumpFile.write("\n");
            String[] dbTable = entry.getKey().split("\\.");
            List<String> tableValues = getTableValues(dbTable[0],dbTable[1]);
            if(tableValues.size()>0) {
                dumpFile.write("------------\n");
                dumpFile.write("Dumping data for table "+dbTable[1]+"\n");
                dumpFile.write("------------\n");
                dumpFile.write("LOCK TABLES "+dbTable[1] +" WRITE;\n");
                dumpFile.write("/*!40000 ALTER TABLE `"+dbTable[1]+"` DISABLE KEYS */\n");
                dumpFile.write("INSERT INTO " + dbTable[1] + " VALUES ");
                for (int i = 0; i < tableValues.size(); i++) {
                    if(i==tableValues.size()-1){
                        dumpFile.write("(" + tableValues.get(i) + ")");
                    }
                    else {
                        dumpFile.write("(" + tableValues.get(i) + "),");
                    }
                }
                dumpFile.write(";");
                dumpFile.write("\n");
                dumpFile.write("/*!40000 ALTER TABLE `"+dbTable[1]+"` ENABLE KEYS */\n");

                dumpFile.write("UNLOCK TABLES;");
            }

            dumpFile.write("\n\n");


        }
        dumpFile.write("-----Dump completed on "+new Timestamp(new Date().getTime()));

        dumpFile.close();

    }
}
