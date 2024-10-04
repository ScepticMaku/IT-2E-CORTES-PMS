package users;

import ui.*;
import java.util.Scanner;

public class Admin {
    Scanner sc = new Scanner(System.in);
    project proj = new project();
    
    int select;
            
    public void displayInterface(int uid, String user_role, String name){
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!\n", user_role, name.substring(0, 4));
        do{
            System.out.print("\n1. Projects\n"
                    + "2. Tasks\n"
                    + "3. Users\n"
                    + "4. Back\n"
                    + "5. Logout\n"
                    + "Enter selection: ");
            select = sc.nextInt();
            
            switch(select){
                case 1:
                    proj.projectInterface(uid);
                    break;
                case 5:
                    System.exit(0);
            }
        } while(select != 4);
    }
}
