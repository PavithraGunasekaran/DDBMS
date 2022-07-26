package com.dmwa.dpg3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import com.dmwa.dpg3.QueryProcessing.QueryExecution;
import com.dmwa.dpg3.QueryProcessing.UseDatabase;
import com.dmwa.dpg3.TransactionProcessing.LockManager;
import com.dmwa.dpg3.utils.CreateRootFolder;


public class App {
	public static void main(String[] args) throws IOException {
		CreateRootFolder.createRootDirectory();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line="";
		line = br.readLine();
		LockManager lockManager = new LockManager();

		if (line.contains("begin") || line.contains("BEGIN")) {
			QueryExecution.executeTransaction(lockManager, line);
		} else {
			QueryExecution.executeQuery(line);
			do {
				line = br.readLine();
				if(!line.equals("end")) {
					QueryExecution.executeQuery(line);
				}
			}while(!line.equals("end"));
		}

//		QueryExecution.executeQuery("UPDATE data SET id=20 where name='neel'");

	}


}
