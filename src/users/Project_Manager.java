package users;

import main.config;
import crud.userCRUD;
import ui.manager.project;
import ui.manager.task;
import ui.manager.team;
import ui.manager.team_members;

import java.io.IOException;

import java.util.Scanner;

public class Project_Manager extends config {
    Scanner sc = new Scanner(System.in);
    
    userCRUD u = new userCRUD();
    project proj = new project();
    task tl = new task();
    team tm = new team();
    team_members tmm = new team_members();
    
    public void displayInterface(int uid, String user_role, String first_name) throws IOException {
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s! uid: %d", user_role, name[0], uid);
        
        boolean isSelected = false;
        
        do{
            System.out.print("\n================================================================================================================================================================");
            System.out.print("\nDate: "+date.toString()
                    + "\n\nMain menu: \n"
                    + "1. Profile \n"
                    + "2. Projects\n"
                    + "3. Tasks\n"
                    + "4. Teams\n"
                    + "5. Team Members\n"
                    + "6. Logout\n"
                    + "0. Exit\n"
                    + "Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    u.viewProfile(uid);
                    break;
                case 2:
                    proj.projectInterface(uid, name[0]);
                    break;
                case 3:
                    tl.taskListInterface(uid);
                    break;
                case 4:
                    tm.teamInterface();
                    break;
                case 5:
                    tmm.memberInterface();
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
