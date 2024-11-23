package ui.manager;

import main.validation;
import main.config;
import crud.teamMemberCRUD;

import java.io.IOException;

public class team_members extends config{
    
    validation validate = new validation();
    teamMemberCRUD tm = new teamMemberCRUD();
    
    boolean isSelected = false;
    int choice;
    
    public void memberInterface() throws IOException{
       do{
            System.out.println("================================================================================================================================================================");
            tm.viewTeam();
            
            System.out.print(""
                    + "╒═══════════════════════════════════════════════════════════════════════════════╕\n"
                    + "│[1]| Add member                                                                │\n"
                    + "│[2]| Replace member                                                            │\n"
                    + "│[3]| Remove member                                                             │\n"
                    + "│[4]| View member info                                                          │\n"
                    + "│[5]| Filter by                                                                 │\n"
                    + "│[6]| Back                                                                      │\n"
                    + "│[0]| Exit                                                                      │\n"
                    + "└───────────────────────────────────────────────────────────────────────────────┘\n"
                    + "| Enter selection: ");
            choice = validate.validateInt();
            
            switch(choice){
                case 1:
                    tm.addMember();
                    break;
                case 2:
                    tm.replaceMember();
                    break;
                case 3:
                    tm.removeMember();
                    break;
                case 4:
                    tm.selectMember();
                    break;
                case 5:
                    tm.filterBy();
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
