package com.dmwa.dpg3.QueryProcessing;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.enums.QueryType;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.models.QueryLog;
import com.dmwa.dpg3.QueryProcessing.enums.Path;
import com.dmwa.dpg3.QueryProcessing.models.Table;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Pavithra Gunasekaran
 */
public class InsertTable {
   static Table table=new Table();

    public static boolean isDbExists(String databaseName){
        File dbDirectory = new File(Path.ROOT+"/"+ Path.DBNAME);
        boolean isDBExists=dbDirectory.exists();
        return  isDBExists;
    }
    public static boolean isTableExists(String databaseName,String tableName){
        File tableFile = new File(Path.ROOT+"/"+ Path.DBNAME+"/"+tableName+".txt");
        boolean isTableExists=tableFile.exists();
        return  isTableExists;
    }
    public static String getDatabase(String query){
        String databaseTableNameRegex="(?i)into [a-zA-Z0-9]*\\.[a-zA-Z0-9]*";
        Pattern databaseTableNamePattern=Pattern.compile(databaseTableNameRegex);
        Matcher databaseTableNameMatcher = databaseTableNamePattern.matcher(query);

        if(databaseTableNameMatcher.find()) {
            String databaseTableNameMatcherSplit[]=databaseTableNameMatcher.group().split("\\.");
            //System.out.println("inside db"+databaseTableNameMatcherSplit[0]);
            table.setDatabaseName(databaseTableNameMatcherSplit[0].split("\\s")[1]);

        }
        return table.getDatabaseName();
    }
    public static String getTable(String query){
        String tableNameRegex="[a-zA-Z0-9]*\\s?\\(";
        Pattern tableNamePattern=Pattern.compile(tableNameRegex);
        Matcher tableNameMatcher = tableNamePattern.matcher(query);
        if(tableNameMatcher.find()) {
            String tableNameMatcherSplit[]=tableNameMatcher.group().split("\\(");
            //System.out.println(tableNameMatcherSplit[0]);
            table.setTableName(tableNameMatcherSplit[0].replace(".", ""));

        }
        //System.out.println(tableName);
        return table.getTableName();
    }

    public static String getColumns(String query){
        String columnsRegex="(.*?)\\s?\\((.*?)\\)";
        Pattern columnsPattern=Pattern.compile(columnsRegex);
        Matcher columnsMatcher = columnsPattern.matcher(query);
        String columns="";
        if(columnsMatcher.find()) {
            //System.out.println("Columns : "+columnsMatcher.group(2));
             columns = columnsMatcher.group(2);
        }
        return columns;
    }
    public static List<String> getValues(String query){
        String valuesRegex="(?i)VALUES\\s?(\\((.*?)\\),?)+";
        Pattern valuesPattern=Pattern.compile(valuesRegex);
        Matcher valuesMatcher = valuesPattern.matcher(query);
        List<String>  values = new ArrayList<String>();
        if(valuesMatcher.find()){
            //System.out.println("values "+columnsMatcher.group());
            String [] valuesList=valuesMatcher.group().split(",\\(");

            for(String valuesIterator : valuesList){
                values.add(valuesIterator.replace("(","").replace(")","").replaceAll("(?i)values",""));
            }
            //System.out.println(values);
        }

        return values;
    }

    public static void insertData(List<String> values,String tableName, String dbName) throws IOException {
        File tableFile = new File(Path.ROOT+"/"+ Path.DBNAME+"/"+tableName+".txt");
        FileWriter writeToTable=new FileWriter(tableFile,true);
        for(int i=0;i<values.size();i++){

            String value=values.get(i).replace(" ","").replace(",","|");
            writeToTable.write(value);
            writeToTable.write("\n");
            //System.out.println("Inserted values : "+values.get(i));
        }
        writeToTable.close();

    }
    public static void insertTable(String query) throws IOException {
        Pattern p = Pattern.compile("(?i)(INSERT INTO) (\\S+).*\\((.*?)\\).*(VALUES).*(\\((.*?)\\),?)(.*\\;?)");
        Matcher m = p.matcher(query);
        boolean b = m.matches();
        table.setTableName(getTable(query));
        table.setDatabaseName(Path.DBNAME);
        int columnCount=0;
//        System.out.println("database : "+table.getDatabaseName()+" tableName : "+table.getTableName());
        String columns = getColumns(query);
        List<String> values = getValues(query);
//        System.out.println("Columns : "+columns);
//        System.out.println("Values : "+values);
        boolean isDbExists = isDbExists(table.getDatabaseName());
        if(isDbExists){
           if(isTableExists(Path.DBNAME,table.getTableName())){
//               System.out.println("Table "+table.getTableName()+" exists");
               insertData(values,table.getTableName(),Path.DBNAME);

           }
           else{
               throw  new RuntimeException("Invalid table : "+table.getTableName());
           }
        }
        else{
            throw new RuntimeException("Invalid database : "+table.getDatabaseName());
        }

        }
}

