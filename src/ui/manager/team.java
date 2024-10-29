package ui.manager;

import ui.admin.*;
import main.config;
import crud.teamCRUD;

import java.io.IOException;
import java.util.Scanner;

public class team extends config {
    Scanner sc = new Scanner(System.in);
    team_members tm = new team_members();
    
    boolean isSelected = false;
    
    public void teamInterface() throws IOException{
        teamCRUD t = new teamCRUD();
        
        do{
            System.out.println("================================================================================================================================================================");
            t.viewTeam();
            
            System.out.print("1. Add team"
                    + "\n2. Edit team"
                    + "\n3. Delete team"
                    + "\n4. View team info"
                    + "\n5. Filter by"
                    + "\n6. Back"
                    + "\nEnter selection: ");
            int choice = sc.nextInt();

            switch(choice){
                case 1:
                    t.addTeam();
                    break;
                case 2:
                    t.editTeam();
                    break;
                case 3:
                    t.deleteTeam();
                    break;
                case 4:
                    t.viewInfo();
                    break;
                case 5:
                    t.filterBy();
                    break;
                case 6:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid Selection.");
            }
        } while(!isSelected);
    }
}
