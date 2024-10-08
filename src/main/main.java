package main;

import java.util.Scanner;
import authentication.*;

public class main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Login login = new Login();
        Register register = new Register();
        
        while(true){
            System.out.println("Project Management System");
            System.out.print("1. Login\n"
                    + "2. Register\n"
                    + "3. Close\n"
                    + "Enter choice: ");
            int choice = sc.nextInt();
            
            System.out.print("--------------------------------------------------------------------------------");
            switch(choice){
                case 1:
                    login.loginCredentials();
                    break;
                case 2:
                    register.registerCredentials();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nInvalid selection.");
                    System.exit(0);
            }
        }
    }
}
