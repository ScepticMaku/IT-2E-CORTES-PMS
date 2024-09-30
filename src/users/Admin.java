package users;

import main.*;
import authentication.*;
import java.util.Scanner;

public class Admin extends config {
    Scanner sc = new Scanner(System.in);
    
    int choice, select, selectp;
    boolean goBack = true;
            
    public void displayInterface(){
        System.out.print("\nSuccessfully Registered as an admin!");
        
        do{
            System.out.print("\n1. Projects\n"
                    + "2. Tasks\n"
                    + "3. Users\n"
                    + "4. Back\n"
                    + "Enter selection: ");
            select = sc.nextInt();
            
            switch(select){
                case 1:
                    while(goBack){
                        System.out.println("\nOptions:\n"
                            + "1. Add Project\n"
                            + "2. Edit Project\n"
                            + "3. View Projects\n"
                            + "4. Delete Project\n"
                            + "5. Back");
                        System.out.print("Enter selection: ");
                        selectp = sc.nextInt();

                        switch(selectp){
                            case 1:
                                addProject();
                                break;
                            case 3:
                                viewProject();
                                System.out.print("\n1. Projects Menu: "
                                        + "\n2. Main menu "
                                        + "\nEnter choice: ");
                                choice = sc.nextInt();
                                break;
                        }
                        
                        if(choice == 2 || selectp == 5){
                            goBack = false;
                        }
                    }
                    break;
            }
        } while(select != 4);
        
    }
}
