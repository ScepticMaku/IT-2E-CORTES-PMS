package ui.manager;

import main.validation;
import main.config;
import crud.teamCRUD;

import java.io.IOException;

public class team extends config {
    
    validation validate = new validation();
    
    boolean isSelected = false;
    
    public void teamInterface() throws IOException{
        teamCRUD t = new teamCRUD();
        
        do{
            System.out.println("================================================================================================================================================================");
            System.out.println(""
                    + "╒═══════════════════════════════════════════════════════════════════════════════╕\n"
                    + "│ Team list                                                                     │\n"
                    + "└───────────────────────────────────────────────────────────────────────────────┘");
            t.viewTeam();
            
            System.out.print(""
                    + "╒═══════════════════════════════════════════════════════════════════════════════╕\n"
                    + "│[1]| Add team                                                                  │\n"
                    + "│[2]| Edit team                                                                 │\n"
                    + "│[3]| Delete team                                                               │\n"
                    + "│[4]| View team info                                                            │\n"
                    + "│[5]| Filter by                                                                 │\n"
                    + "│[6]| Back                                                                      │\n"
                    + "│[0]| Exit                                                                      │\n"
                    + "└───────────────────────────────────────────────────────────────────────────────┘\n"
                    + "| Enter selection: ");
            int choice = validate.validateInt();

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
                case 0:
                    exitMessage();
                    break;
                default:
                    System.out.println("Error: Invalid Selection.");
            }
        } while(!isSelected);
    }
}
