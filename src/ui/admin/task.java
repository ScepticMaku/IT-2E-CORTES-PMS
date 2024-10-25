package ui.admin;

import main.config;
import crud.taskCRUD;

import java.io.IOException;
import java.util.Scanner;

public class task extends config{
    Scanner sc = new Scanner(System.in);
    
    taskCRUD tsk = new taskCRUD();
    
    public void taskListInterface() throws IOException{
        boolean isSelected = false;
        
        do{
            System.out.println("================================================================================================================================================================");
            tsk.viewTask();
            
            System.out.print("1. Add Task"
                    + "\n2. Edit Task"
                    + "\n3. Delete Task"
                    + "\n4. View Task Info"
                    + "\n5. Filter By"
                    + "\n6. Back"
                    + "\nEnter selection: ");
            int choice = sc.nextInt();

            switch(choice){
                case 1:
                    tsk.addTask();
                    break;
                case 2:
                    tsk.editTask();
                    break;
                case 3:
                    tsk.deleteTask();
                    break;
                case 4:
                    tsk.viewInfo();
                    break;
                case 5:
                    tsk.filterBy();
                    break;
                case 6:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
}
