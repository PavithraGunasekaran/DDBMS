package com.dmwa.dpg3.QueryProcessing;

import java.io.File;

public class UseDatabase {
    public static String dbName="";
    public static Boolean useDatabase(String databaseName)
    {
        File directory;
        directory = new File(databaseName).getAbsoluteFile();

        boolean result = false;
        if (directory.mkdirs() || directory.exists())
        {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }
        return result;
    }
    public static String getDbName(){

        return dbName;
    }

    public static void setDbName(String query){
        String[] splitUseQuery = query.split("\\s");
        dbName = splitUseQuery[1];
    }
}
