package users;

import main.config;
import ui.manager.project;
import ui.manager.task;
import ui.manager.team;
import ui.manager.team_members;

import java.io.IOException;

import java.util.Scanner;

public class Project_Manager extends config {
    Scanner sc = new Scanner(System.in);
    
    project proj = new project();
    task tl = new task();
    team tm = new team();
    team_members tmm = new team_members();
    
    public void displayInterface(int uid, String user_role, String first_name) throws IOException {
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        
        boolean isSelected = false;
        
        do{
            System.out.print("\n================================================================================================================================================================");
            System.out.print("\nDate: "+date.toString()
                    + "\n\nMain menu: \n"
                    + "1. Projects\n"
                    + "2. Tasks\n"
                    + "3. Teams\n"
                    + "4. Team Members\n"
                    + "5. Logout\n"
                    + "0. Exit\n"
                    + "Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    proj.projectInterface(uid, name[0]);
                    break;
                case 2:
                    tl.taskListInterface(uid);
                    break;
                case 3:
                    tm.teamInterface();
                    break;
                case 4:
                    tmm.memberInterface();
                    break;
                case 5:
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
