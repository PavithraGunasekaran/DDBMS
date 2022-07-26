package com.dmwa.dpg3.DBLogger.analytics;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.fileoperations.FileUtility;
import com.dmwa.dpg3.DBLogger.models.DBLog;
import com.dmwa.dpg3.DBLogger.models.UserLog;
import com.dmwa.dpg3.QueryProcessing.enums.Path;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class QueryCounter {

    private static Map<String, DBLog> dbMap;

    static {
        try {
            dbMap = FileUtility.readFileFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logQueryCountDB() throws IOException {
        for(String dbName:dbMap.keySet()){
            DBLog dbLog=dbMap.get(dbName);
            VMInstance vmInstance=dbLog.getVmInstance();

            Map<String, UserLog> activeUsers=dbLog.getActiveUsers();

            String filename= Path.ROOT+"/"+ "QueryAnalytics.txt";
            FileWriter fw = new FileWriter(filename,true);
            for(String user:activeUsers.keySet()){
                String queryLog="User "+user+" submitted "+activeUsers.get(user).getQueryCount()+" queries for "+dbName+" running on Virtual Machine "+vmInstance;
                System.out.println(queryLog);
                fw.write(queryLog);
            }

            fw.write("\r\n");
            fw.close();
        }
    }
}
