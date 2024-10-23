package main;

import authentication.Login;
import authentication.Register;
import authentication.Change_Password;

import java.util.Scanner;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        Login login = new Login();
        Register register = new Register();
        Change_Password change = new Change_Password();
        
        while(true){
            System.out.print("================================================================================================================================================================");
            System.out.println("Project Management System");
            System.out.print("1. Login\n"
                    + "2. Register\n"
                    + "3. Reset Password\n"
                    + "4. Close\n"
                    + "Enter choice: ");
            int choice = sc.nextInt();
            
            System.out.print("================================================================================================================================================================");
            switch(choice){
                case 1:
                    login.loginCredentials();
                    break;
                case 2:
                    System.out.println("\nRegister: ");
                    register.registerCredentials();
                    break;
                case 3:
                    System.out.println("\nChange Password: ");
                    change.changeCredentials();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nInvalid selection.");
                    System.exit(0);
            }
        }
    }
}
