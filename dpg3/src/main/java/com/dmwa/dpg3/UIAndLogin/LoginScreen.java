package com.dmwa.dpg3.UIAndLogin;

import com.dmwa.dpg3.DBLogger.analytics.OperationCounter;
import com.dmwa.dpg3.DBLogger.analytics.QueryCounter;
import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import com.dmwa.dpg3.Export.ExportStructureValue;
import com.dmwa.dpg3.QueryProcessing.QueryExecution;
import com.dmwa.dpg3.ReverseEngineering.ReverseEngineering;
import com.dmwa.dpg3.UIAndLogin.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.dmwa.dpg3.UIAndLogin.UserLogin.loginTry;

/**
 * @author Pavithra Gunasekaran
 */
public class LoginScreen {

    public static void main(String args[]) throws IOException {
        System.out.println("Hello There Welcome to DPG3, please choose from the below options.");
        System.out.println("1) Login");
        System.out.println("2) Sign-up");

        System.out.println("Enter your choice:");
        BufferedReader sc = new BufferedReader (new InputStreamReader(System.in));
        int choice = Integer.parseInt(sc.readLine());

        if(choice == 1)
        {
            loginTry(3);
        }else if(choice == 2)
        {
            User user = new User();
            System.out.print("Registering your information...");
            System.out.print("\n Enter Name:");
            String name = sc.readLine();
            System.out.print("\n Enter Password:");
            String password = sc.readLine();
            System.out.println("Enter Security Question:");
            String securityQue = "What is your lucky number?";
            System.out.println(securityQue);
            System.out.print("\n Enter Answer:");
            String securityAns = sc.readLine();

            user.setName(name);
            user.setPassword(password);
            user.setSecurityQuestion(securityQue);
            user.setSecurityAnswer(securityAns);

            UserLogin.registerUser(user);
            System.out.println("Please login with your credentials to access DPG3");
            loginTry(3);
        }
    }


}
