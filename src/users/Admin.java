package users;

import ui.*;
import java.util.Scanner;

public class Admin {
    Scanner sc = new Scanner(System.in);
    project proj = new project();
            
    public void displayInterface(int uid, String user_role, String name){
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name.substring(0, 4));
        while(true){
            System.out.print("\n1. Projects\n"
                    + "2. Tasks\n"
                    + "3. Users\n"
                    + "4. Logout\n"
                    + "Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    proj.projectInterface(uid);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        }
    }
}
