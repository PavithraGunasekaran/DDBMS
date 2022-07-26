package com.dmwa.dpg3.QueryProcessing;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class DeleteRow {
    public static void deleteStatement(String table, String columnName, String value) throws IOException
    {
        value = value.replace("'","");
        columnName = columnName.replace("'","");

        BufferedReader in = new BufferedReader(new FileReader(String.valueOf(Paths.get(com.dmwa.dpg3.QueryProcessing.enums.Path.ROOT +"/"+ com.dmwa.dpg3.QueryProcessing.enums.Path.DBNAME+"/"+table+".txt"))));

        String line;

        //ColsInTable is storing the column names.
        ArrayList<String> colsInTable = new ArrayList<String>();
        //Data is storing the data row by row.
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

        DataForDeleteRow dataForDeleteRow = getData(in,colsInTable,data);
        colsInTable = dataForDeleteRow.colsInTable;
        data = dataForDeleteRow.data;

        //Finding the data and deleting it
        findData(columnName, value, colsInTable, data);

        //Updating the database file after deleting the data.
        updateTheData(table, data);

    }

    //deleting the row and updating the entire table to adjust the ID.
    public static ArrayList<ArrayList<String>> getUpdatedData(ArrayList<ArrayList<String>> data)
    {
        int cnt = 1;
        ArrayList<ArrayList<String>> newData = new ArrayList<ArrayList<String>>();
        for (int i=0; i< data.size() ; i++) {
            ArrayList<String> y = data.get(i);
            ArrayList<String> temp = new ArrayList<>();
            for(int j=0; j<y.size();j++)
            {
                temp.add(y.get(j));
            }
            newData.add(temp);
        }
        return newData;
    }
    public static void updateTheData(String table, ArrayList<ArrayList<String>> data) throws IOException {
        FileWriter fw = new FileWriter(String.valueOf(Paths.get(com.dmwa.dpg3.QueryProcessing.enums.Path.ROOT+"/"+ com.dmwa.dpg3.QueryProcessing.enums.Path.DBNAME+"/"+table + ".txt")));
        BufferedWriter bw = new BufferedWriter(fw);
        System.out.println("Here, you can find updated file:"+String.valueOf(Paths.get(com.dmwa.dpg3.QueryProcessing.enums.Path.ROOT+"/"+ com.dmwa.dpg3.QueryProcessing.enums.Path.DBNAME+"/"+table + ".txt")));
        ArrayList<ArrayList<String>> newData = getUpdatedData(data);
        for (int i=0; i< newData.size() ; i++) {
            ArrayList<String> y = newData.get(i);
            for(int j=0; j<y.size();j++)
            {
                bw.write(y.get(j));
                if(j<y.size()-1)
                {
                    bw.write(" | ");
                }
            }
            bw.write("\n");
        }
        bw.close();
        fw.close();
    }

    //Finding the data and deleting it
    public static ArrayList<ArrayList<String>> findData(String columnName, String value, ArrayList<String> colsInTable, ArrayList<ArrayList<String>> data)
    {
        int indexOfColumn = -1;//Finding the column of the specified where clause.
        for(int i = 0; i<colsInTable.size();i++)
        {
            if(columnName.toLowerCase().equalsIgnoreCase(colsInTable.get(i).toLowerCase()))
            {
                indexOfColumn = i;
            }
        }
        if(indexOfColumn == -1)
        {
            System.out.println("The specified column ["+columnName+"] is not present in database, please re-enter the correct query.");
        }else
        {
            int indexOfRow = -1;//Finding the row of the specified where clause.
            for(int i = 0; i<data.size();i++)
            {
                ArrayList<String> x = data.get(i);
                if(value.toLowerCase().equalsIgnoreCase(x.get(indexOfColumn).toLowerCase()))
                {
                    indexOfRow = i;
                    data.remove(indexOfRow);
                }
            }
            if(indexOfRow == -1)
            {
                System.out.println("The specified value ["+value+"] is not present int column ["+columnName+"] in database, please re-enter the correct query.");
            }
        }
        return data;
    }

    //Getting data from text file in array lists.
    public static DataForDeleteRow getData(BufferedReader in, ArrayList<String> colsInTable, ArrayList<ArrayList<String>> data) throws IOException {
        String line;
        DataForDeleteRow dataForDeleteRow = new DataForDeleteRow();
        //Counter to check if the line is first line or not. To support the colsInTable.
        Integer counter = 0;
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
        dataForDeleteRow.colsInTable = colsInTable;
        dataForDeleteRow.data = data;
        return dataForDeleteRow;
    }


}
