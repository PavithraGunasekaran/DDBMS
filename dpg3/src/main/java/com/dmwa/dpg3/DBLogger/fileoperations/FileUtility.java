package com.dmwa.dpg3.DBLogger.fileoperations;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.models.DBLog;
import com.dmwa.dpg3.DBLogger.models.TableLog;
import com.dmwa.dpg3.DBLogger.models.UserLog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtility {
    /**
     * Checks if file has a valid path and is a valid file.
     *
     * @param fileName
     * @return true if file path is valid and file exists.
     */
    public static boolean isFileExists(String fileName) {

        // Returns false if file is invalid.
        if (!isFileNameValid(fileName))
            return false;

        // Gives a file path.
        File filePath = new File(fileName.trim());

        // Returns false if file does not exist or file is not a valid file.
        if (!filePath.exists() || !filePath.isFile())
            return true;

        return true;
    }

    /**
     * Checks if file name is valid and has a valid extension.
     *
     * @param fileName
     * @return true if file name is not null, not empty and file extension is valid.
     */
    private static boolean isFileNameValid(String fileName) {

        // Returns false if file name is null or it's empty.
        if (fileName == null || fileName.trim().isEmpty())
            return false;

        return true;
    }

    /**
     * Writes the information to the given path.
     *
     * @param data
     * @param filePath
     * @return
     */
    public static boolean writeToFile(String data, String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
            writer.append(data);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONArray readFromFile(String filePath) throws IOException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(new FileReader(filePath));
            return jsonArray;
        } catch (Exception ex) {
            Map<String, String> map = new HashMap<>();
            map.put("Read From file exception", ex.getMessage());
            DBLogger.logEvent(map);
            return null;
        }
    }

    public static void writeToDB(DBLog dbLog) throws IOException {
        Map<String, DBLog> readFileDbMap =readFileFromDB();

        readFileDbMap.put(dbLog.getDbName(), dbLog);

        String print="";
        String brackets= String.valueOf('"');
        print+="[";
        for(String k:readFileDbMap.keySet()) {
            print+="{"+brackets+k+brackets+":"+readFileDbMap.get(k)+"},";
        }
        print=print.substring(0,print.length()-1);
        print+="]";
        writeToFile(print,"db.json");
    }

    public static Map<String, DBLog> readFileFromDB() throws IOException {
       Map<String, DBLog> readFileDbMap = new HashMap<>();
        var dbJsonArray = FileUtility.readFromFile("db.json");

        if(dbJsonArray!=null) {
            for (int index = 0; index < dbJsonArray.toArray().length; index++) {
                var jsonObject = (JSONObject) dbJsonArray.get(index);
                for (var item : jsonObject.keySet()) {
                    DBLog dbLog = new DBLog();
                    JSONObject js = (JSONObject) jsonObject.get(item);

                    String jsonStringUsers = (String) js.get("activeUsers").toString();
                    String jsonStringTables = (String) js.get("tableMap").toString();
                    ObjectMapper mapper = new ObjectMapper();
                    List<UserLog> activeUsers = mapper.readValue(jsonStringUsers, new TypeReference<List<UserLog>>() {
                    });
                    List<TableLog> tableMap = mapper.readValue(jsonStringTables, new TypeReference<List<TableLog>>() {
                    });
                    String dbNameJson = (String) js.get("dbName").toString();
                    String vmName = (String) js.get("vmInstance").toString();
                    VMInstance vmInstanceJson;
                    vmInstanceJson = vmName.equals("FIRST") ? VMInstance.FIRST : VMInstance.SECOND;
                    Map<String, UserLog> userLogMap = new HashMap<>();
                    for (UserLog log : activeUsers) {
                        UserLog userLog = new UserLog();
                        userLog.setUser(log.getUser());
                        userLog.setQueryCount(log.getQueryCount());
                        userLogMap.put(log.getUser(), userLog);
                    }

                    Map<String, TableLog> tableLogMap = new HashMap<>();
                    for (TableLog log : tableMap) {
                        TableLog tableLog = new TableLog();
                        tableLog.setTableName(log.getTableName());
                        tableLog.setQueries(log.getTableQueries());
                        tableLog.setQueryTypeCount(log.getQueryTypeCount());
                        tableLog.setNumberOfRecords(log.getNumberOfRecords());
                        tableLogMap.put(log.getTableName(), tableLog);
                    }

                    dbLog.setActiveUsers(userLogMap);
                    dbLog.setTableMap(tableLogMap);
                    dbLog.setDbName(dbNameJson);
                    dbLog.setVmInstance(vmInstanceJson);

                    readFileDbMap.put(dbNameJson, dbLog);
                }
            }
        }
        return readFileDbMap;
    }
}
