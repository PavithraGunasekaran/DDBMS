package com.dmwa.dpg3.QueryProcessing;

import com.dmwa.dpg3.QueryProcessing.enums.Path;

import java.io.File;

public class CreateDatabase {
    public static void createDatabaseStatement(String databaseName)
    {
        File dbDirectory = new File(Path.ROOT+"/"+databaseName);
        if (!dbDirectory.exists()){
            dbDirectory.mkdirs();
        }
    }
}
