package ui.manager;

import main.validation;
import main.config;
import crud.taskCRUD;

import java.io.IOException;

public class task extends config{
    
    validation validate = new validation();
    taskCRUD tsk = new taskCRUD();
    
    public void taskListInterface(int uid) throws IOException{
        boolean isSelected = false;
        
        do{
            System.out.println("================================================================================================================================================================");
            tsk.viewTask();
            
            System.out.print(""
                    + "╒═══════════════════════════════════════════════════════════════════════════════╕\n"
                    + "│[1]| Add task                                                                  │\n"
                    + "│[2]| Edit task                                                                 │\n"
                    + "│[3]| Delete task                                                               │\n"
                    + "│[4]| Assign member                                                             │\n"
                    + "│[5]| View task info                                                            │\n"
                    + "│[6]| Filter by                                                                 │\n"
                    + "│[7]| Back                                                                      │\n"
                    + "│[0]| Exit                                                                      │\n"
                    + "└───────────────────────────────────────────────────────────────────────────────┘\n"
                    + "| Enter selection: ");
            int choice = validate.validateInt();

            switch(choice){
                case 1:
                    tsk.addTask(uid);
                    break;
                case 2:
                    tsk.editTask();
                    break;
                case 3:
                    tsk.deleteTask();
                    break;
                case 4:
                    tsk.assignMember();
                    break;
                case 5:
                    tsk.viewInfo();
                    break;
                case 6:
                    tsk.filterBy();
                    break;
                case 7:
                    isSelected = true;
                    break;
                case 0:
                    exitMessage();
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
}
