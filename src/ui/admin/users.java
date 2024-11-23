package ui.admin;

import main.validation;
import main.config;
import crud.userCRUD;
import authentication.Register;

import java.io.IOException;

public class users extends config {
    
    validation validate = new validation();
    Register reg = new Register();
    userCRUD u = new userCRUD();
    
    boolean isSelected = false;
    
    public void userInterface() throws IOException{
        do{
            System.out.println("================================================================================================================================================================");
            System.out.println(""
                        + "╒═══════════════════════════════════════════════════════════════════════════════╕\n"
                        + "│ List of users                                                                 │\n"
                        + "└───────────────────────────────────────────────────────────────────────────────┘");
            u.viewUser();
            
            System.out.print(""
                    + "╒═══════════════════════════════════════════════════════════════════════════════╕\n"
                    + "│[1]| Register a user                                                           │\n"
                    + "│[2]| Edit user                                                                 │\n"
                    + "│[3]| Delete user                                                               │\n"
                    + "│[4]| View user info                                                            │\n"
                    + "│[5]| Back                                                                      │\n"
                    + "│[0]| Exit                                                                      │\n"
                    + "└───────────────────────────────────────────────────────────────────────────────┘\n"
                    + "| Enter selection: ");
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
                    System.out.print("| Enter ID: ");
                    int uid = validate.validateInt();
                    
                    while(getSingleValue("SELECT user_id FROM user WHERE user_id = ?", uid) == 0){
                        System.out.print("| Error: ID doesn't exist, try again: ");
                        uid = validate.validateInt();
                    }
                    
                    u.viewUserInfo(uid);
                    pause();
                    break;
                case 5:
                    isSelected = true;
                    break;
                case 0:
                    exitMessage();
                    break;
                default:
                    System.out.println("Error: Invalid selection");
            }
        } while(!isSelected);
    }
        
}
