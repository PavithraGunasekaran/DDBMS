package com.dmwa.dpg3.GCP;

import com.dmwa.dpg3.QueryProcessing.Utils.TableUtility;

import java.io.IOException;

public class ConnectionHelper {

    public VMConnection createConnection(String query) {
        VMConnection vmConnection = decideVMToMakeConnectionWith(query);
        return vmConnection;
    }

    private VMConnection decideVMToMakeConnectionWith(String query) {
        try {
            String tableName = TableUtility.getTableName(query);
            //Search inside the Map where the tableName is present.
            //Map map
            String tableVMAddress = "";
            //Get the current IP address. If both the address matches
            String currentIPAddress = "";
            boolean isCurrentConnection = tableVMAddress.equals(currentIPAddress);
            VMConnection vmConnection = new VMConnection(isCurrentConnection, tableVMAddress);
            return vmConnection;
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public static void ConnectWithGCP() {
        //1. Run java.jar
        //2. Give IP address for current VM and another VM.
        //3. Update the address of the VM's in a file.
        //4. Now, login the respective user.
        //5. Execute the query.
        //6. On the basis of query decide whether the current vm has the data or the other one.
        //7. Execute the query on the respective VM and update the metadata on both the VM's.
    }

}
