package com.dmwa.dpg3.ReverseEngineering;

import com.dmwa.dpg3.Export.ExportStructureValue;
import com.dmwa.dpg3.QueryProcessing.enums.Path;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pavithra Gunasekaran
 */
public class ReverseEngineering {
    public static Scanner scanner = new Scanner(System.in);
    public static void reverseEngineering(String dbName) throws IOException {
        String file=String.valueOf(Paths.get(Path.ROOT+"/"+ dbName+"/reverseEngineering.txt"));
        File myObj = new File(file);
        myObj.createNewFile();
        FileWriter erdFile=new FileWriter(file);
        ExportStructureValue exportStructureValue = new ExportStructureValue();
        BufferedReader br = new BufferedReader(new FileReader(String.valueOf(Paths.get(Path.ROOT+"/metaData.txt"))));
        //br.readLine();
        String splitTableValues = br.lines().collect(Collectors.joining("\n"));
        String[] values = splitTableValues.split("\n\n");
        int tableCount = values.length;
        int counter =0;
        for(String value:values){

            String[] listValues = value.split("\n");
            int stringLength = listValues.length;
            String foreignKeyString = listValues[stringLength-1].toLowerCase();
            //System.out.println(foreignKeyString);
            String dbFromMeta = listValues[0];
            String tableFromMeta = listValues[0].split("\\.")[1];

            if(listValues[0].contains(dbName.toLowerCase()) && foreignKeyString.contains("foreign")){
                if(counter+1==tableCount) {
                    StringBuffer sb = new StringBuffer(value);
                    sb.replace(value.lastIndexOf("\n"), value.lastIndexOf("\n") + 1, "");
                    value = sb.toString();
                }
                String[] listForeignKeyString = foreignKeyString.split(":");
                String referenceTable = listForeignKeyString[1].split("\\(")[0];
                System.out.println(foreignKeyString);
                System.out.println("Choose a table to create a relationship with "+listValues[0].split("\\.")[1]+" with "+referenceTable);
                System.out.println("Follow the format : ");
                System.out.println("Acceptable values - 1:N,1:1,M:N,N:1");

                String cardinality = scanner.next();

                erdFile.write(value+"\nCardinality : "+cardinality);
                erdFile.write("\n\n");
            }
            counter++;

        }
        erdFile.close();
    }
//    public static void main(String args[]) throws IOException {
//        reverseEngineering("database1");
//    }
}
