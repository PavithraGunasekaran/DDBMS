package com.dmwa.dpg3.UIAndLogin;

import com.dmwa.dpg3.DBLogger.DBLogger;
import com.dmwa.dpg3.DBLogger.analytics.OperationCounter;
import com.dmwa.dpg3.DBLogger.analytics.QueryCounter;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.Export.ExportStructureValue;
import com.dmwa.dpg3.QueryProcessing.QueryExecution;
import com.dmwa.dpg3.QueryProcessing.enums.Path;
import com.dmwa.dpg3.ReverseEngineering.ReverseEngineering;
import com.dmwa.dpg3.TransactionProcessing.LockManager;
import com.dmwa.dpg3.UIAndLogin.model.User;
import com.dmwa.dpg3.utils.CreateRootFolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserLogin {
    public static String user;
    public static void setUserForSession(String name){
        user = name;
    }
    public static String getUserForSession(){
     return user;
    }
    public static void registerUser(User user) throws IOException {
        if(userExists(user.getName())){
            System.out.println("User with same name already exits");

            return;
        }else {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String bCryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

            File tableFile = new File(String.valueOf(Paths.get(Path.ROOT+"/login.txt")));
            FileWriter writeToFile = new FileWriter(tableFile, true);

            writeToFile.write(user.getName() + " | " + bCryptedPassword + " | " +
                    user.getSecurityQuestion() + " | " + user.getSecurityAnswer());
            writeToFile.write("\n");

            System.out.println("User successfully registered");
            writeToFile.close();
        }
    }

    public static Boolean isValidUser(String name,String password,String securityQuestion,String securityAnswer) throws IOException{
        ArrayList<String> userDetails=fetchUserDetails(name,password,securityQuestion,securityAnswer);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean passwordIsValid = bCryptPasswordEncoder.matches(password, userDetails.get(1));
        boolean rightSecurityAnswer=userDetails.get(3).equals(securityAnswer);

        if(userDetails==null || !passwordIsValid || !rightSecurityAnswer) {
            File dir = new File(System.getProperty("user.dir"));

            dir.setExecutable(false, false);
            dir.setReadable(false, false);
            dir.setWritable(false, false);

            System.out.println("Wrong credentials");

            Map<String, String> events = new HashMap<>();
            events.put("User",name);
            events.put("Status","Unsuccessful");
            DBLogger.logEvent(events);

            return false;
        }else{
            System.out.println("User logged In");

            Map<String, String> events = new HashMap<>();
            events.put("User",name);
            events.put("Status","Successful");
            DBLogger.logEvent(events);

            return true;
        }
    }

    public static boolean userExists (String name) throws IOException{
        BufferedReader in = new BufferedReader(new FileReader(String.valueOf(Paths.get(Path.ROOT+"/"+"login.txt"))));
        String line;

        while ((line = in.readLine()) != null) {
            ArrayList<String> details = new ArrayList<String>(Arrays.asList(line.split("\\s*\\|\\s*")));

            if(details.get(0).equals(name)){
                return true;
            }
        }

        return false;
    }

    public static ArrayList<String> fetchUserDetails (String name,String password,String securityQuestion,String securityAnswer) throws IOException{
        BufferedReader in = new BufferedReader(new FileReader( Path.ROOT+"/"+"login.txt"));
        String line;

        while ((line = in.readLine()) != null) {
            ArrayList<String> details = new ArrayList<String>(Arrays.asList(line.split("\\s*\\|\\s*")));

           if(details.get(0).equals(name)){
               return details;
           }
        }

        return null;
    }

    public static void loginTry(int tryCount) throws IOException {
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Add your login details...");
        System.out.print("\n Enter Name:");
        String name = sc.readLine();
        System.out.print("\n Enter Password:");
        String password = sc.readLine();
        String securityQue = "What is your lucky number?";
        System.out.println(securityQue);
        System.out.print("\n Enter Answer:");
        String securityAns = sc.readLine();

        if(UserLogin.isValidUser(name,password,securityQue,securityAns))
        {
            //Call to DB module in which all the funcitonality is visible.
            setUserForSession(name);
            functionalities();
        }else {
            if(tryCount == 0);
            {
                System.out.println("You have exceeded the limit of try.");
                System.exit(0);
            }
            System.out.println("You now have "+tryCount+" tries to enter right credentials.");
            loginTry(tryCount--);
        }
    }

    public static void functionalities() throws IOException {
        while(true)
        {
            System.out.println("Select operation from the below list.");
            System.out.println("1) Query Implementation");
            System.out.println("2) Data modeling - Reverse Engineering");
            System.out.println("3) Export structure and values");
            System.out.println("4) Analytics");
            System.out.println("5) Quit");

            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
            int operation = Integer.parseInt(sc.readLine());

            if(operation == 1)
            {
                System.out.println("Here you can execute the SQL queries.");
//                System.out.print("\n Enter the query:");
//                String query = sc.readLine();
//                QueryExecution.executeQuery(query);
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

            }else if(operation == 2)
            {
                System.out.println("2) Data modeling - Reverse Engineering");

                System.out.print("\n Enter database name:");
                String dbName = sc.readLine();
                ReverseEngineering.reverseEngineering(dbName);
            }else if(operation == 3)
            {
                System.out.println("3) Export structure and values");

                System.out.print("\n Enter database name:");
                String dbName = sc.readLine();
                //TODO : Here, add function which can get us the VM Instance name for particular dbname.
                VMInstance vmInstance = VMInstance.FIRST;
                ExportStructureValue.getExportStructureValue(dbName,vmInstance);
            }else if(operation == 4)
            {
                System.out.println("4) Analytics");

                System.out.println("Enter your choice for analytics.");
                System.out.println("1) Update Analytics");
                System.out.println("2) Query Analytics");
                System.out.println("Enter: ");
                int analyticsChoice = Integer.parseInt(sc.readLine());
                if(analyticsChoice == 1)
                {
                    OperationCounter.logUpdateQueryTable();
                }else if(analyticsChoice == 2) {
                    QueryCounter.logQueryCountDB();
                }
            }else if(operation == 5)
            {
                System.out.println("Quiting the DPG3");
                System.exit(0);
            }
        }

    }
}
