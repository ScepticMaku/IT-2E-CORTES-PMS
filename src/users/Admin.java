package users;

import main.config;
import ui.admin.project;
import ui.admin.task;
import ui.admin.users;
import ui.admin.team;
import ui.admin.team_members;

import java.util.Scanner;
import java.io.IOException;

public class Admin extends config {
    Scanner sc = new Scanner(System.in);
    
    team_members tmm = new team_members();
    project proj = new project();
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
                    + "4. Team Members\n"
                    + "5. Users\n"
                    + "6. Logout\n"
                    + "0. Exit\n"
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
                    tmm.memberInterface();
                    break;
                case 5:
                    u.userInterface();
                    break;
                case 6:
                    isSelected = true;
                    break;
                case 0:
                    exitMessage();
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
}
