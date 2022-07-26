package com.dmwa.dpg3.QueryProcessing;

import com.dmwa.dpg3.QueryProcessing.enums.Path;
import com.dmwa.dpg3.QueryProcessing.models.Table;
import com.dmwa.dpg3.Session.User;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pavithra Gunasekaran
 */
public class CreateTable {

    public static String META_DATA_FILE="/resources/metadata.txt";
    static Table table=new Table();
    //static String tableName="";
    //static String databaseName="";
    static String[] queryParameters=new String[100];
    public static FileWriter metaDataFile, tableDataFile;
    static List<String> columns=new ArrayList<String>();
    public CreateTable() throws IOException {
    }

    public static boolean isDbDirectoryExists(String databaseName){
        File dbDirectory = new File(String.valueOf(Paths.get(Path.ROOT+"/"+ Path.DBNAME)));
        return dbDirectory.exists();
    }
    public static boolean isTableExists(String databaseName, String tableName){
        File tableFile = new File(String.valueOf(Paths.get(Path.ROOT+"/"+ Path.DBNAME+"/"+tableName+".txt")));
        //System.out.println("file path : "+ Paths.get(Path.ROOT + "/" + Path.DBNAME + "/" + tableName + ".txt"));
        return tableFile.exists();
    }
    public static String getTable(String query){
        String tableNameRegex="\\.?[a-zA-Z0-9]*\\(";
        Pattern tableNamePattern=Pattern.compile(tableNameRegex);
        Matcher tableNameMatcher = tableNamePattern.matcher(query);
        if(tableNameMatcher.find()) {
            String tableNameMatcherSplit[]=tableNameMatcher.group().split("\\(");
            table.setTableName(tableNameMatcherSplit[0].replace(".", ""));

        }
        //System.out.println(tableName);
        return table.getTableName();
    }

    public static String getDatabase(String query){
        String databaseTableNameRegex="table [a-zA-Z0-9]*\\.[a-zA-Z0-9]*";
        Pattern databaseTableNamePattern=Pattern.compile(databaseTableNameRegex);
        Matcher databaseTableNameMatcher = databaseTableNamePattern.matcher(query);

        if(databaseTableNameMatcher.find()) {
            String databaseTableNameMatcherSplit[]=databaseTableNameMatcher.group().split("\\.");
            table.setDatabaseName(databaseTableNameMatcherSplit[0].replace("table ", ""));

        }
        return table.getDatabaseName();
    }


    public static FileWriter writeTableInfoToMetaData(String tableName, String databaseName) throws IOException {
        //create a file for the table
        String file= String.valueOf(Paths.get(Path.ROOT+"/"+"metaData.txt"));
       // File myObj = new File(file);
        //myObj.createNewFile();
        metaDataFile=new FileWriter(file,true);
        System.out.println("db : "+Path.DBNAME);
//        metaDataFile.write(User.User+"\n");
        metaDataFile.write(Path.DBNAME+"."+tableName.replace(" ",""));
        metaDataFile.write("\n");
        return metaDataFile;

    }

    public static void writeTableStructureToFile(List<String> columns, String tableFile) throws IOException {
        tableDataFile=new FileWriter(tableFile,true);
        System.out.println("columns size : "+columns.size());
        for(int i=0;i<columns.size();i++){
            tableDataFile.write(columns.get(i)+"|");
        }
        tableDataFile.write("\n");


    }

    public static void createTableFile(String tableName, String databaseName) throws IOException {
        String file=Path.ROOT+"/"+ Path.DBNAME+"/"+tableName+".txt";
        //System.out.println("File path : "+file);
        File myObj = new File(file);
        myObj.createNewFile();
        writeTableStructureToFile(table.getColumns(),file);

    }

    public static void getAndWriteTableMetaDataToFile(String query) throws IOException {
        System.out.println("Columns before : "+table.getColumns());
        //table.setColumns(Collections.<String> emptyList());
        System.out.println("Columns after setting null : "+table.getColumns());
        String columnNameRegex = ".?[a-zA-Z0-9]*\\([\\s\\S]*";
        //Extract columns
        Pattern columnNamePattern = Pattern.compile(columnNameRegex);
        Matcher columnNameMatcher = columnNamePattern.matcher(query);
        int columnCounter=0;
        if (columnNameMatcher.find()) {
            String columnNameMatcherSplit[] = columnNameMatcher.group().split(table.getTableName());
            String columnsSplit[] = columnNameMatcherSplit[1].split(",");
            int queryParameterIndex = 0;
            for (String columnsSplitElements : columnsSplit) {
                columnsSplitElements = columnsSplitElements.toUpperCase();
                String columnsStartRegex = "^\\([\\S]*";
                String columnsEndRegex = "[\\S]*\\)\\)";
                String columnsPrimaryKeyRegex = "(?i)(^primary\\skey)";
                String columnsForeignKeyRegex = "(?i)(^foreign\\skey)";

                Pattern columnsStartPattern = Pattern.compile(columnsStartRegex);
                Matcher columnsStartMatcher = columnsStartPattern.matcher(columnsSplitElements);
                Pattern columnsEndPattern = Pattern.compile(columnsEndRegex);
                Matcher columnsEndMatcher = columnsEndPattern.matcher(columnsSplitElements);

                Pattern columnsPrimaryKeyPattern = Pattern.compile(columnsPrimaryKeyRegex);
                Matcher columnsPrimaryKeyMatcher = columnsPrimaryKeyPattern.matcher(columnsSplitElements);

                Pattern columnsForeignKeyPattern = Pattern.compile(columnsForeignKeyRegex);
                Matcher columnsForeignKeyMatcher = columnsForeignKeyPattern.matcher(columnsSplitElements);
                if (columnsStartMatcher.find()) {
                    columnsSplitElements = columnsSplitElements.replace("(", "");

                }

                if (columnsEndMatcher.find()) {
                    columnsSplitElements = columnsSplitElements.replace("))", ")");
                }
                if (columnsPrimaryKeyMatcher.find()) {
                    columnsSplitElements = columnsSplitElements.replace("(", "-");
                    columnsSplitElements = columnsSplitElements.replace(")", "");
                }
                if (columnsForeignKeyMatcher.find()) {
                    columnsSplitElements = columnsSplitElements.replaceFirst("\\(", "-");
                    columnsSplitElements = columnsSplitElements.replaceFirst("\\)", "");
                    columnsSplitElements = columnsSplitElements.replace(" REFERENCES ", ":");

                }

                queryParameters[queryParameterIndex] = columnsSplitElements;
                if(columnsSplitElements.toLowerCase().contains("primary") ||
                        columnsSplitElements.toLowerCase().contains("foreign")){

                }
                else{
                    columns.add(columnsSplitElements.split(" ")[0]);

                    table.setColumns(columns);
                }
                System.out.println("columns : "+columns);
                metaDataFile.write(columnsSplitElements + "\n");

            }
            metaDataFile.write("\n");

            //metaDataFile.write("\n");
        }

        //table.setColumns(Collections.<String> emptyList());
    }
//    private static void indexOfLastMatch(Matcher matcher, String input) {
//        //Matcher matcher = pattern.matcher(input);
//        for (int i = input.length(); i > 0; --i) {
//            Matcher region = matcher.region(0, i);
//            if (region.hitEnd()) {
//                System.out.println(i);
//            }
//        }
//
//
//    }

    public static void createTable(String query) throws IOException {

//        Pattern p = Pattern.compile("(?i)^create\\stable\\s[a-zA-Z0-9]*\\.[a-zA-A0-9]*" +
//                "\\(" +
//                "(?i)([a-zA-Z0-9]*\\s[a-zA-Z0-9]*(\\([0-9]*\\))?(\\sNOT\\sNULL)?,?)+" +
//                "(?i)(,PRIMARY\\sKEY\\([a-zA-Z0-9]*\\))?" +
//                "(?i)(,FOREIGN\\sKEY\\([a-zA-Z0-9]*\\)\\sREFERENCES\\s[a-zA-Z0-9]*\\([a-zA-Z0-9]*\\))?" +
//                "\\)");

        Pattern p = Pattern.compile("(?i)^create\\stable\\s([a-zA-Z0-9]*)?\\.?[a-zA-A0-9]*" +
                "\\(" +
                "(?i)([a-zA-Z0-9]*\\s[a-zA-Z0-9]*(\\([0-9]*\\))?(\\sNOT\\sNULL)?,?)+" +
                "(?i)(,PRIMARY\\sKEY\\([a-zA-Z0-9]*\\))?" +
                "(?i)(,foreign key\\([a-zA-Z0-9]*\\)\\sreferences\\s[a-zA-Z0-9]*\\([a-zA-Z0-9]*\\))?"+
                "\\)");




        //validate if it is a create table command

        Matcher m = p.matcher(query);
        boolean b = m.matches();
        if (m.matches()) {
            //System.out.println(b);
            table.setTableName(getTable(query));
            table.setDatabaseName(Path.DBNAME);
            metaDataFile = writeTableInfoToMetaData(table.getTableName(),Path.DBNAME);
            System.out.println("IS table exists : "+isTableExists(Path.DBNAME,table.getTableName()));
            if(!isTableExists(Path.DBNAME,table.getTableName())){
                getAndWriteTableMetaDataToFile(query);
                createTableFile(table.getTableName(),Path.DBNAME);
                columns.clear();
                metaDataFile.close();
                tableDataFile.close();
                //table.setColumns(Collections.<String> emptyList());
            }
            else{
                System.out.println("Table already exists");
            }
            }
        else {
            //indexOfLastMatch(m,query);
            throw new RuntimeException("Create command has a syntax error");
        }
    }
}
