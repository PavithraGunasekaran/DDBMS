package com.dmwa.dpg3;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.analytics.OperationCounter;
import com.dmwa.dpg3.DBLogger.analytics.QueryCounter;
import com.dmwa.dpg3.DBLogger.enums.QueryType;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.DBLogger.models.DBLog;
import com.dmwa.dpg3.DBLogger.models.QueryLog;
import com.dmwa.dpg3.Export.ExportStructureValue;
import com.dmwa.dpg3.QueryProcessing.CreateTable;
import com.dmwa.dpg3.QueryProcessing.InsertTable;
import com.dmwa.dpg3.QueryProcessing.QueryExecution;
import com.dmwa.dpg3.ReverseEngineering.ReverseEngineering;
import com.dmwa.dpg3.UIAndLogin.UserLogin;
import com.dmwa.dpg3.UIAndLogin.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

/*public class Program {
    public static void main(String[] args) throws IOException {
//        CreateTable c=new CreateTable();
//        c.createTable(args[0]);
//        InsertTable i=new InsertTable();
//        System.out.println(args[1]);
//        i.insertTable(args[1]);
//
//        ExportStructureValue e = new ExportStructureValue();
//        DBLog d = new DBLog("database1",VMInstance.FIRST);
//        e.getExportStructureValue("database1",d.getVmInstance());
//        DBLogger.logInfo(VMInstance.FIRST, "CSCI5408", "student", new QueryLog(LocalDateTime.now().toString(), "60", QueryType.INSERT, "Inserting data", 1, "udit"));
//        DBLogger.logInfo(VMInstance.FIRST, "CSCI5408", "Employee", new QueryLog(LocalDateTime.now().toString(), "60", QueryType.INSERT, "Inserting data", 1, "udit"));
//        QueryCounter.logQueryCountDB();
//          QueryCounter.logQueryCountDB();

//        UserLogin.registerUser(new User("user","passs2","ques","ans"));
//        UserLogin.isValidUser("user","s2","ques","ans");

//        UserLogin.registerUser(new User("neel","neel","ques","ans"));

        //UserLogin.isValidUser("neel","neel","ques","ans2");
    }
}*/

public class Program {
    public static void main(String[] args) throws IOException {



    }
}
