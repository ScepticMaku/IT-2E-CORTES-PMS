package main;

import authentication.Login;
import authentication.Register;
import authentication.Change_Password;

import java.util.Scanner;
import java.io.IOException;

public class main {
    
    public static void main(String[] args) throws IOException{

        Scanner sc = new Scanner(System.in);
        
        Change_Password change = new Change_Password();
        Register register = new Register();
        Login login = new Login();
        config conf = new config();
        
        do{
            System.out.print("================================================================================================================================================================\n"
                    + "Project Management System\n"
                    + "1. Login\n"
                    + "2. Register\n"
                    + "3. Reset Password\n"
                    + "0. Exit\n"
                    + "Enter choice: ");
            int choice = sc.nextInt();
            
            System.out.println("================================================================================================================================================================");
            switch(choice){
                case 1:
                    login.loginCredentials();
                    break;
                case 2:
                    System.out.println("Register: ");
                    register.registerCredentials();
                    break;
                case 3:
                    System.out.println("Change Password: ");
                    change.changeCredentials();
                    break;
                case 0:
                    conf.exitMessage();
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        } while(true);
    }
}
