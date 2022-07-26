package com.dmwa.dpg3.QueryProcessing.Utils;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.models.QueryLog;
import com.dmwa.dpg3.QueryProcessing.CreateDatabase;
import com.dmwa.dpg3.QueryProcessing.DeleteRow;
import com.dmwa.dpg3.QueryProcessing.InsertTable;
import com.dmwa.dpg3.QueryProcessing.UseDatabase;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryType {
    public static String getQueryType(String query){
        String selectRegex = "(SELECT|select)\\s+[^\\s]+\\s+(FROM|from)\\s+[^\\s]+(\\s*(((WHERE|where)\\s+(\\w|\\d)+\\s*(=|>|<|(!=))\\s*(((')[^\\s]*('))|(\\d)+))|\\s))";
        String updateRegex = "(UPDATE|update)\\s+?[^\\s]+\\s+(SET|set)\\s+([^\\s]+\\s*=\\s*(((')[^\\s]*('))|(\\d)+),*\\s*)+\\s+(WHERE|where)\\s+(\\w|\\d)+\\s*(=|>|<|(!=))\\s*(((')[^\\s]*('))|(\\d)+)";
        //String createTable = "(CREATE|create)\\s+(TABLE|table)\\s+\\w+\\s*\\((\\s*[^\\s]+\\s+(char\\s*\\(\\d+\\)|text|boolean|int|varchar\\s*\\(\\d+\\)),|\\s*[^\\s]+\\s+(char\\s*\\(\\d+\\)|text|boolean|int|varchar\\s*\\(\\d+\\)))+\\s+\\)";
        String createTable = "(?i)^create\\stable\\s([a-zA-Z0-9]*)?\\.?[a-zA-A0-9]*" +
                "\\(" +
                "(?i)([a-zA-Z0-9]*\\s[a-zA-Z0-9]*(\\([0-9]*\\))?(\\sNOT\\sNULL)?,?)+" +
                "(?i)(,PRIMARY\\sKEY\\([a-zA-Z0-9]*\\))?" +
                "(?i)(,foreign key\\([a-zA-Z0-9]*\\)\\sreferences\\s[a-zA-Z0-9]*\\([a-zA-Z0-9]*\\))?"+
                "\\)";
        String insertTable ="(?i)(INSERT INTO) (\\S+).*\\((.*?)\\).*(VALUES).*(\\((.*?)\\),?)(.*\\;?)";
        String createDatabase = "(?i)^create\\sdatabase\\s[a-zA-Z0-9]*";
        String ss = (query + " ").replaceAll(selectRegex, "");
        String ss2 = query.trim().replaceAll(selectRegex, "");
        String us = query.trim().replaceAll(updateRegex, "");
        String ct = query.trim().replaceAll(createTable, "");
        String it = query.trim().replaceAll(insertTable,"");
        String cb = query.trim().replaceAll(createDatabase,"");

        if (ss.equals("") | ss2.equals("")) {
            return com.dmwa.dpg3.DBLogger.enums.QueryType.SELECT.toString();
        } else if (us.equals("")) {
            return com.dmwa.dpg3.DBLogger.enums.QueryType.UPDATE.toString();
        } else if (ct.equals("")) {
            return com.dmwa.dpg3.DBLogger.enums.QueryType.CREATE.toString();
        } else if(it.equals("")){
            return com.dmwa.dpg3.DBLogger.enums.QueryType.INSERT.toString();
        }else if(cb.equals("")){
            return com.dmwa.dpg3.DBLogger.enums.QueryType.CREATEDB.toString();
        }
        else {
            return fetchFromQuery(query);
        }

    }

    public static String fetchFromQuery( String sql)
    {
        Pattern pattern;

        pattern = Pattern.compile("(?<QueryType>Delete)[ ]*from[ ]*[\\\"]?(?<TableName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*[;]?[ ]*where[ ]*[\\\"]?(?<ColumnName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*=[ ]*(?<Value>[a-z0-9A-Z\\.\\'\\_\\ ]*);?",Pattern.CASE_INSENSITIVE);
        if(!pattern.matcher(sql).find())
        {
            pattern = Pattern.compile("(?<QueryType>Create)[ ]*database[ ]*[\\\"]?(?<DatabaseName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*[;]?",Pattern.CASE_INSENSITIVE);
        }
        if(!pattern.matcher(sql).find())
        {
            pattern = Pattern.compile("(?<QueryType>use)[ ]*[\\\"]?(?<DatabaseName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*[;]?",Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(sql);

        if(matcher.find())
        {
            if(matcher.group(1).toLowerCase().equals("Delete".toLowerCase()))
            {
                return com.dmwa.dpg3.DBLogger.enums.QueryType.DELETE.toString();
            }else if(matcher.group(1).toLowerCase().equals("Create".toLowerCase()))
            {
                return com.dmwa.dpg3.DBLogger.enums.QueryType.CREATEDB.toString();
            }else if(matcher.group(1).toLowerCase().equals("Use".toLowerCase()))
            {
                return com.dmwa.dpg3.DBLogger.enums.QueryType.USE.toString();
            }else
            {
                return "Your query contains syntax error, please re-enter your query.";
            }
        }else {
            return "Your query contains syntax error, please re-enter your query.";
        }

    }
}
