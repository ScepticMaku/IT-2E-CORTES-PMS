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
            
            System.out.print("1. Add Task"
                    + "\n2. Edit Task"
                    + "\n3. Delete Task"
                    + "\n4. Assign a member"
                    + "\n5. View Task Info"
                    + "\n6. Filter By"
                    + "\n7. Back"
                    + "\nEnter selection: ");
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
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
}
