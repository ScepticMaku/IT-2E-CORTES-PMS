package ui;

import main.config;
import java.util.*;

public class project extends config {
    Scanner sc = new Scanner(System.in);
    
    boolean goBack = true;
    int select, choice;
    
    public void projectInterface(){
        while(goBack){
                        System.out.println("\nOptions:\n"
                            + "1. Add Project\n"
                            + "2. Edit Project\n"
                            + "3. View Projects\n"
                            + "4. Delete Project\n"
                            + "5. Back");
                        System.out.print("Enter selection: ");
                        select = sc.nextInt();

                        switch(select){
                            case 1:
                                addProject();
                                break;
                            case 3:
                                viewProject();
                                break;
                        }
                        
                        System.out.print("\n1. Projects Menu: "
                                + "\n2. Main menu "
                                + "\nEnter choice: ");
                        choice = sc.nextInt();
                        
                        if(choice == 2 || select == 5){
                            goBack = false;
                        }
                    }
    }
}
