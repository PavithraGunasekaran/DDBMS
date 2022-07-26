package com.dmwa.dpg3.QueryProcessing.Utils;

import com.dmwa.dpg3.QueryProcessing.CreateTable;
import com.dmwa.dpg3.QueryProcessing.InsertTable;

import java.io.IOException;

public class TableUtility {
    public static String getTableName(String query) throws IOException {
        String type=QueryType.getQueryType(query);
        InsertTable insertTable=new InsertTable();
        CreateTable createTable=new CreateTable();

        if (type.equals("SELECT")) {
            query = query.trim();
            String[] selectString = query.trim().split("\\s+");
            String table = selectString[3];
            return table;
        } else if (type.equals("UPDATE")) {
            query = query.trim();
            String[] selectString = query.trim().split("\\s+");
            String table = selectString[1];
            return table;
        } else if (type.equals("INSERT")) {
            return insertTable.getTable(query);
        }
        else if (type.equals("CREATE")) {
            return createTable.getTable(query);
        }

        return null;
    }
}
