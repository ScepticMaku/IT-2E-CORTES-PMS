package ui.admin;

import main.validation;
import main.config;
import crud.userCRUD;
import authentication.Register;

import java.util.Scanner;
import java.io.IOException;

public class users extends config {
    Scanner sc = new Scanner(System.in);
    
    validation validate = new validation();
    Register reg = new Register();
    userCRUD u = new userCRUD();
    
    int userID;
    boolean isSelected = false;
    String Query;
    
    public void userInterface() throws IOException{
        do{
            System.out.println("================================================================================================================================================================");
            System.out.println("List of users: ");
            u.viewUser();
        
            System.out.print("1. Register a user"
                    + "\n2. Edit user"
                    + "\n3. Delete user"
                    + "\n4. View full info"
                    + "\n5. Back"
                    + "\nEnter selection: ");
            int userSelect = validate.validateInt();

            switch(userSelect){
                case 1:
                    reg.registerCredentials();
                    break;
                case 2:
                    u.editUser();
                    break;
                case 3:
                    u.deleteUser();
                    break;
                case 4:
                    System.out.print("\nEnter ID: ");
                    int uid = sc.nextInt();
                    
                    u.viewUserInfo(uid);
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection");
            }
        } while(!isSelected);
    }
        
}
