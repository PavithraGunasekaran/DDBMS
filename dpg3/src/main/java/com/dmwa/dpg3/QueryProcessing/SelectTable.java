package com.dmwa.dpg3.QueryProcessing;

import com.dmwa.dpg3.QueryProcessing.enums.Path;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectTable {

    public static String getTableName(String query){
        query = query.trim();
        String[] selectString = query.trim().split("\\s+");
        ArrayList<String> col = new ArrayList<String>(Arrays.asList(selectString[1].split(",")));
        String table = selectString[3];
        return table;
    }
    public static void selectOperations(String query) throws FileNotFoundException, IOException {
        query = query.trim();
        String[] selectString = query.trim().split("\\s+");
        ArrayList<String> col = new ArrayList<String>(Arrays.asList(selectString[1].split(",")));
        String table = selectString[3];
        String clause;
        BufferedReader in = new BufferedReader(new FileReader(String.valueOf(Paths.get(Path.ROOT+"/"+Path.DBNAME+"/"+table + ".txt"))));
        String line;
        ArrayList<String> colsInTable = new ArrayList<String>();
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        int counter = 0;
        while ((line = in.readLine()) != null) {
            if (counter == 0) {
                colsInTable = new ArrayList<String>(Arrays.asList(line.split("\\s*\\|\\s*")));
            } else {
                ArrayList<String> lineData = new ArrayList<String>(Arrays.asList(line.split("\\s*\\|\\s*")));
                data.add(lineData);
            }

            counter++;
        }

        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();

        in.close();

        if (selectString.length > 4) {
            clause = selectString[5];
            String[] colAndValue = clause.split("\\s*=\\s*");
            String clauseCol = colAndValue[0];
            String clauseValue = colAndValue[1].replaceAll("'", "");

            int clauseIndex = -1;
            for (int i = 0; i < colsInTable.size(); i++) {
                String c = colsInTable.get(i);
                if (c.equals(clauseCol)) {
                    clauseIndex = i;
                    break;
                }
            }

            if (col.size() == 1) {
                String colValue = col.get(0);
                if (colValue.equals("*")) {
                    for (ArrayList<String> d : data) {
                        for (int j = 0; j < d.size(); j++) {
                            if (j == clauseIndex && d.get(j).equals(clauseValue)) {
                                results.add(d);
                            }
                        }
                    }

                    System.out.println(results);
                } else {
                    int selectedCol = -1;
                    for (int i = 0; i < colsInTable.size(); i++) {
                        String c = colsInTable.get(i);
                        if (c.equals(colValue)) {
                            selectedCol = i;
                            break;
                        }
                    }
                    System.out.println(selectedCol);

                    for (ArrayList<String> d : data) {
                        for (int i = 0; i < d.size(); i++) {
                            if (i == clauseIndex && d.get(i).equals(clauseValue)) {
                                ArrayList<String> r = new ArrayList<String>();
                                r.add(d.get(selectedCol));
                                results.add(r);
                            }
                        }
                    }
                    System.out.println(results);
                }
            } else {
                ArrayList<Integer> selectedColIndexes = new ArrayList<Integer>();

                for (String c : col) {
                    for (int j = 0; j < colsInTable.size(); j++) {
                        if (c.equals(colsInTable.get(j))) {
                            selectedColIndexes.add(j);
                        }
                    }
                }

                for (ArrayList<String> d : data) {
                    ArrayList<String> smallResult = new ArrayList<String>();
                    for (int i = 0; i < d.size(); i++) {
                        if (i == clauseIndex && d.get(i).equals(clauseValue)) {
                            for (int sci : selectedColIndexes) {
                                smallResult.add(d.get(sci));
                            }
                        }
                    }
                    if (smallResult.size() > 0) {
                        results.add(smallResult);
                    }
                }

                System.out.println(results);

            }

        } else {
            if (col.size() == 1) {
                String colValue = col.get(0);
                if (colValue.equals("*")) {
                    results = data;
                    System.out.println(results);
                } else {
                    int selectedCol = -1;
                    for (int i = 0; i < colsInTable.size(); i++) {
                        String c = colsInTable.get(i);
                        if (c.equals(colValue)) {
                            selectedCol = i;
                            break;
                        }
                    }

                    for (ArrayList<String> d : data) {
                        for (int i = 0; i < d.size(); i++) {
                            if (i == selectedCol) {
                                ArrayList<String> r = new ArrayList<String>();
                                r.add(d.get(i));
                                results.add(r);
                            }
                        }
                    }
                    System.out.println(results);
                }
            } else {
                ArrayList<Integer> selectedColIndexes = new ArrayList<Integer>();

                for (String c : col) {
                    for (int j = 0; j < colsInTable.size(); j++) {
                        if (c.equals(colsInTable.get(j))) {
                            selectedColIndexes.add(j);
                        }
                    }
                }

                for (ArrayList<String> d : data) {
                    ArrayList<String> smallResult = new ArrayList<String>();

                    for (int i = 0; i < d.size(); i++) {
                        if (selectedColIndexes.contains(i)) {
                            smallResult.add(d.get(i));
                        }
                    }
                    results.add(smallResult);
                }

                System.out.println(results);
            }
        }
    }
}

