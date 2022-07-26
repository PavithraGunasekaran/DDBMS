package com.dmwa.dpg3.QueryProcessing;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.models.QueryLog;
import com.dmwa.dpg3.QueryProcessing.Utils.QueryType;
import com.dmwa.dpg3.QueryProcessing.enums.Path;
import com.dmwa.dpg3.TransactionProcessing.LockManager;
import com.dmwa.dpg3.TransactionProcessing.Transaction;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryExecution {
    public static void executeQuery(String query) throws IOException {
        String type=QueryType.getQueryType(query);

        if (type.equals("SELECT")) {
            SelectTable.selectOperations(query);
            String dbName = Path.DBNAME;
            String tableName = SelectTable.getTableName(query);
            DBLogger.logInfo(VMInstance.FIRST,dbName,tableName,new QueryLog(LocalDateTime.now().toString(), "60", com.dmwa.dpg3.DBLogger.enums.QueryType.SELECT, "Selecting table: "+tableName+"in database: "+dbName, 1, "pavithra"));

        } else if (type.equals("UPDATE")) {
            UpdateTable.updateOperations(query);
            String dbName = Path.DBNAME;
            String tableName = UpdateTable.getTableName(query);
            System.out.println("db : "+dbName+", table "+tableName);
            DBLogger.logInfo(VMInstance.FIRST,dbName,tableName,new QueryLog(LocalDateTime.now().toString(), "60", com.dmwa.dpg3.DBLogger.enums.QueryType.UPDATE, "Updated table: "+tableName+"in database: "+dbName, 1, "pavithra"));

        } else if (type.equals("CREATE")) {
            CreateTable.createTable(query);
            String dbName = CreateTable.getDatabase(query);
            String tableName = CreateTable.getTable(query);
            DBLogger.logInfo(VMInstance.FIRST,dbName,tableName,new QueryLog(LocalDateTime.now().toString(), "60", com.dmwa.dpg3.DBLogger.enums.QueryType.CREATE, "Created table: "+tableName+"in database: "+dbName, 1, "pavithra"));

        } else if (type.equals("INSERT")) {
            InsertTable.insertTable(query);
            String dbName = Path.DBNAME;
            String tableName = InsertTable.getTable(query);
            DBLogger.logInfo(VMInstance.FIRST,dbName,tableName,new QueryLog(LocalDateTime.now().toString(), "60", com.dmwa.dpg3.DBLogger.enums.QueryType.INSERT, "Inserting data to table: "+tableName+"in database: "+dbName, 1, "pavithra"));

        } else if (type.equals("DELETE")){
            try{
                Pattern pattern;
                pattern = Pattern.compile("(?<QueryType>Delete)[ ]*from[ ]*[\\\"]?(?<TableName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*[;]?[ ]*where[ ]*[\\\"]?(?<ColumnName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*=[ ]*(?<Value>[a-z0-9A-Z\\.\\'\\_\\ ]*);?",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(query);
                matcher.find();
                //matcher.group(1) is Query Type;
                //matcher.group(2) is Table Name;
                //matcher.group(3) is Column Name;
                //matcher.group(4) is Value that needs to be deleted;
                String dbName = Path.DBNAME;
                String tableName = matcher.group(2).trim();
                DeleteRow.deleteStatement(tableName,matcher.group(3), matcher.group(4));
                DBLogger.logInfo(VMInstance.FIRST,dbName,tableName,new QueryLog(LocalDateTime.now().toString(), "60", com.dmwa.dpg3.DBLogger.enums.QueryType.DELETE, "Deleting data from table: "+matcher.group(2)+"in database: "+dbName, 1, "pavithra"));
            }catch (Exception e)
            {
                System.out.println(e.toString());
            }
        } else if(type.equals("CREATEDB")){
            try{
                Pattern pattern;
                pattern = Pattern.compile("(?<QueryType>Create)[ ]*database[ ]*[\\\"]?(?<DatabaseName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*[;]?",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(query);
                matcher.find();
                //matcher.group(1) is Query Type;
                //matcher.group(2) is Database Name;
                CreateDatabase.createDatabaseStatement(matcher.group(2));
                DBLogger.logInfo(VMInstance.FIRST,matcher.group(2),"No Table Name",new QueryLog(LocalDateTime.now().toString(), "60", com.dmwa.dpg3.DBLogger.enums.QueryType.CREATEDB, "Creating Database: "+matcher.group(2), 1, "pavithra"));
            }catch (Exception e)
            {
                System.out.println(e.toString());
            }
        } else if (type.equals("USE")){
            Pattern pattern;
            pattern = Pattern.compile("(?<QueryType>use)[ ]*[\\\"]?(?<DatabaseName>[a-z0-9A-Z\\.\\'\\_\\ ]*)[\\\"]?[ ]*[;]?",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(query);
            matcher.find();
            try{
                //matcher.group(1) is Query Type;
                //matcher.group(2) is Database Name;
                UseDatabase.setDbName(query);
                System.out.println("Using Database: "+matcher.group(2));
                UseDatabase.useDatabase(matcher.group(2));
                DBLogger.logInfo(VMInstance.FIRST,matcher.group(2),"No Table Name",new QueryLog(LocalDateTime.now().toString(), "60", com.dmwa.dpg3.DBLogger.enums.QueryType.USE, "Using Database: "+matcher.group(2), 1, "pavithra"));
            }catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }else
        {
            System.out.println(type);
        }
    }


	public static void executeTransaction(LockManager lockManager, String line) throws IOException {

		String[] transactionValues = line.split(" ");
		String transactionName = transactionValues[transactionValues.length - 1];
		ArrayList<String> queries = new ArrayList<String>();
		String input = null;
		while (!"commit".equals(input)) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			input = br.readLine();
			if (!"commit".equals(input) && !"".equals(input)) {
				queries.add(input);
			}
		}

		String transactionText = "";
		for (int i = 0; i < queries.size(); i++) {
			String query = queries.get(i);

			transactionText += query + System.lineSeparator();

		}
		Transaction transaction = new Transaction(lockManager, transactionText, transactionName);
		transaction.run();

	}
}
