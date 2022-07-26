package com.dmwa.dpg3.DBLogger;

import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.fileoperations.FileUtility;
import com.dmwa.dpg3.DBLogger.models.*;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DBLogger {

    private static Map<String, DBLog> dbMap = new HashMap<>();

    //Send me
    public static void logInfo(VMInstance vmInstance, String dbName, String tableName, QueryLog queryLog) throws IOException {
        DBLog db = getOrCreateDB(dbName.toLowerCase(Locale.ROOT), vmInstance);
        db.addOrUpdateActiveUser(queryLog.getUser().toLowerCase(Locale.ROOT));
        db.addOrUpdateTableInfo(tableName, queryLog);

        FileUtility.writeToDB(db);
    }

    /**
     * If the database entry is present inside map gets it otherwise creates a new entry inside the map.
     *
     * @param dbName
     * @return
     */
    private static DBLog getOrCreateDB(String dbName, VMInstance vmInstance) {
        if (!dbMap.containsKey(dbName)) {
            dbMap.put(dbName, new DBLog(dbName, vmInstance));
        }
        return dbMap.get(dbName);
    }

    public static void logEvent(Map<String, String> events) throws IOException {
        //Flexibility of writing the map to the file.

        String filename= "events.txt";
        FileWriter fw = new FileWriter(filename,true);
        for(String event:events.keySet()){
            String eventData="time :"+new Date()+","+"event: "+event+","+"text: "+events.get(event);

            fw.write("{Event {"+eventData+"}},");
        }

        fw.write("\r\n");
        fw.close();
    }

}
