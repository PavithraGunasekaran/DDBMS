package com.dmwa.dpg3.QueryProcessing;

import com.dmwa.dpg3.QueryProcessing.enums.Path;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UpdateTable {
    public static String getTableName(String query){
        query = query.trim();
        String[] selectString = query.trim().split("\\s+");
        ArrayList<String> col = new ArrayList<String>(Arrays.asList(selectString[1].split(",")));
        String table = selectString[1];
        return table;
    }
    public static void updateOperations(String query) throws IOException {
        query = query.trim();
        String[] selectString = query.trim().split("\\s+");
        String table = selectString[1];
        String setters = selectString[3];
        ArrayList<String> settersList = new ArrayList<String>(Arrays.asList(setters.split(",")));
        HashMap<Integer, String> settersMap = new HashMap<Integer, String>();

        String clause = selectString[5];
        String[] colAndValue = clause.split("\\s*=\\s*");
        String clauseCol = colAndValue[0];
        String clauseValue = colAndValue[1].replaceAll("'", "");

        BufferedReader in = new BufferedReader(new FileReader(String.valueOf(Paths.get(Path.ROOT+"/"+Path.DBNAME+"/"+table + ".txt"))));
        String line;
        ArrayList<String> colsInTable = new ArrayList<String>();
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        int counter = 0;

        while ((line = in.readLine()) != null) {
            if (counter == 0) {
                colsInTable = new ArrayList<String>(Arrays.asList(line.split("\\s*\\|\\s*")));
                data.add(colsInTable);
            } else {
                ArrayList<String> lineData = new ArrayList<String>(Arrays.asList(line.split("\\s*\\|\\s*")));
                data.add(lineData);
            }

            counter++;
        }

        for (String sl : settersList) {
            String[] svalue = sl.split("=");
            String col = svalue[0];
            int colIndex = -1;

            for (int i = 0; i < colsInTable.size(); i++) {
                if (col.equalsIgnoreCase(colsInTable.get(i))) {
                    colIndex = i;
                }
            }

            String val = svalue[1].replaceAll("'", "");

            settersMap.put(colIndex, val);

        }

        int clauseIndex = -1;
        for (int i = 0; i < colsInTable.size(); i++) {
            String c = colsInTable.get(i);
            if (c.equalsIgnoreCase(clauseCol)) {
                clauseIndex = i;
                break;
            }
        }

        for (ArrayList<String> s : data) {
            for (int i = 0; i < s.size(); i++) {
                if (i == clauseIndex && s.get(i).equals(clauseValue)) {
                    for (int k : settersMap.keySet()) {
                        s.set(k, settersMap.get(k));
                    }
                }
            }
        }

//        FileWriter fw = new FileWriter(table + ".txt");
//        BufferedWriter bw = new BufferedWriter(fw);
//        bw.write(data.toString());

//        System.out.println(data.toString());

        FileWriter writeToTable=new FileWriter(String.valueOf(Paths.get(Path.ROOT+"/"+Path.DBNAME+"/"+table + ".txt")));

        for(ArrayList<String> s:data){
            String value=s.toString()
                    .replace(",","|");
            writeToTable.write(value.substring(1,value.length()-1));
            writeToTable.write("\n");
        }

        writeToTable.close();
        // insert into file code

//        bw.close();
    }
}

