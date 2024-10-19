package users;

import ui.*;
import java.util.Scanner;
import main.config;
import authentication.Register;
import java.io.IOException;

public class Admin extends config {
    Scanner sc = new Scanner(System.in);
    project proj = new project();
    Register reg = new Register();
    users u = new users();
    task tl = new task();
    team tm = new team();
    
    boolean isSelected = false;
            
    public void displayInterface(int uid, String user_role, String first_name) throws IOException {
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        do{
            System.out.print("\n================================================================================================================================================================");
            System.out.print("\nMain menu: \n"
                    + "1. Projects\n"
                    + "2. Tasks\n"
                    + "3. Teams\n"
                    + "4. Users\n"
                    + "5. Logout\n"
                    + "Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    proj.projectInterface(uid);
                    break;
                case 2:
                    tl.taskListInterface();
                    break;
                case 3:
                    tm.teamInterface();
                    break;
                case 4:
                    u.userInterface();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    private void viewTasks(){
    }
}
