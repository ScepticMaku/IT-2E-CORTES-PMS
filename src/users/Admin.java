package users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    taskList tl = new taskList();
    
    boolean isSelected = false;
            
    public void displayInterface(int uid, String user_role, String first_name) throws IOException {
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        do{
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
                case 2:
                    tl.taskListInterface(uid);
                    break;
                case 3:
                    u.userInterface();
                    break;
                case 4:
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
