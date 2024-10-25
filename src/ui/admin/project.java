package ui.admin;

import main.config;
import crud.projectCRUD;

import java.io.IOException;
import java.util.Scanner;

public class project extends config {
    public void projectInterface(int uid) throws IOException{
        Scanner sc = new Scanner(System.in);
        
        projectCRUD proj = new projectCRUD();
        
        boolean pSelected = false;
        
        do{
            System.out.println("================================================================================================================================================================");
            proj.viewProject();
            
            System.out.print("1. Add Project"
                    + "\n2. Edit Project"
                    + "\n3. Delete Project"
                    + "\n4. View Info"
                    + "\n5. Filter by"
                    + "\n6. Back"
                    + "\n0. Exit"
                    + "\nEnter choice: ");
            int choice = sc.nextInt();
            
            switch(choice){
                case 1:
                    proj.addProject(sc, uid);
                    break;
                case 2:
                    proj.editProject(sc);
                    break;
                case 3:
                    proj.deleteProject(sc);
                    break;
                case 4:
                    proj.viewProjectInfo(sc);
                    break;
                case 5:
                    proj.FilterBy(sc);
                    break;
                case 6:
                    pSelected = true;
                    break;
                case 0:
                    exitMessage();
                    break;
                default: System.out.println("Error: Invalid Selection.");
            }
        } while(!pSelected);
    }
}
