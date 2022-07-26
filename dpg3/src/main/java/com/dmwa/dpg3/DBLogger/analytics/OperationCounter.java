package com.dmwa.dpg3.DBLogger.analytics;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.enums.QueryType;
import com.dmwa.dpg3.DBLogger.fileoperations.FileUtility;
import com.dmwa.dpg3.DBLogger.models.DBLog;
import com.dmwa.dpg3.DBLogger.models.TableLog;
import com.dmwa.dpg3.QueryProcessing.enums.Path;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class OperationCounter {

    private static Map<String, DBLog> dbMap;

    static {
        try {
            dbMap = FileUtility.readFileFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logUpdateQueryTable() throws IOException {
        for (String dbName : dbMap.keySet()) {
            DBLog dbLog = dbMap.get(dbName);

            Map<String, TableLog> tableMap = dbLog.getTableMap();

            String filename= Path.ROOT+"/"+ "UpdateAnalytics.txt";
            FileWriter fw = new FileWriter(filename,true);
            for (String table : tableMap.keySet()) {
                if(tableMap.get(table).getQueryTypeCount().get(QueryType.UPDATE)!=null) {
                    int queryCount = tableMap.get(table).getQueryTypeCount().get(QueryType.UPDATE);
                    String updateLog = "Total " + queryCount + " Update operations are performed on " + table;
                    System.out.println(updateLog);
                    fw.write(updateLog);
                }
            }
            fw.write("\r\n");
            fw.close();
        }

    }
}
