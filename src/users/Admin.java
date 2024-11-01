package users;

import main.config;
import main.validation;
import ui.admin.project;
import ui.admin.task;
import ui.admin.users;
import crud.userCRUD;
import ui.admin.team;
import ui.admin.team_members;

import java.io.IOException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends config {
    Scanner sc = new Scanner(System.in);
    
    validation validate = new validation();
    team_members tmm = new team_members();
    project proj = new project();
    userCRUD u = new userCRUD();
    users usr = new users();
    task tl = new task();
    team tm = new team();
    
    boolean isSelected = false;
            
    public void displayInterface(int uid, String user_role, String first_name) throws IOException {
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        
        do{
            System.out.print("\n================================================================================================================================================================");
            System.out.print("\nDate: "+date.toString()
                    + "\n\nMain menu: \n"
                    + "1. Profile\n"
                    + "2. Projects\n"
                    + "3. Tasks\n"
                    + "4. Teams\n"
                    + "5. Team Members\n"
                    + "6. Users\n"
                    + "7. Logout\n"
                    + "0. Exit\n"
                    + "Enter selection: ");
            int select = validate.validateInt();
            
            switch(select){
                case 1:
                    boolean isBack = false;
                    
                    do{
                        u.viewUserInfo(uid);
                    
                        System.out.print("1. Edit Info\n"
                                + "2. Back\n"
                                + "Enter selection: ");
                        int editSelect = validate.validateInt();

                        switch(editSelect){
                            case 1:
                                editInfo(uid);
                                break;
                            case 2:
                                isBack = true;    
                                break;
                            default: System.out.println("Error: Invalid selection.");
                        }
                    } while(!isBack);
                    break;
                case 2:
                    proj.projectInterface(uid, name[0]);
                    break;
                case 3:
                    tl.taskListInterface(uid);
                    break;
                case 4:
                    tm.teamInterface();
                    break;
                case 5:
                    tmm.memberInterface();
                    break;
                case 6:
                    usr.userInterface();
                    break;
                case 7:
                    isSelected = true;
                    break;
                case 0:
                    exitMessage();
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    private void editInfo(int userID){
        String Query;
        
        System.out.print("\nSelect what you want to edit:"
                + "\n1. Password"
                + "\n2. Role"
                + "\n3. Email Address"
                + "\n4. First Name"
                + "\n5. Middle Name"
                + "\n6. Last Name"
                + "\n7. Back"
                + "\nEnter selection: ");
        int select = validate.validateInt();

        switch(select){
            case 1:
                System.out.print("\nEnter new password: ");
                String newPassword = sc.nextLine();

                while(validate.spaceValidate(newPassword)){
                    newPassword = sc.nextLine();
                }

                while(validatePassword(userID, newPassword)){
                    System.out.print("Error: Must not be an old password, try again: ");
                    newPassword = sc.nextLine();

                    while(validate.spaceValidate(newPassword)){
                        newPassword = sc.nextLine();
                    }
                }

                Query = "UPDATE user SET password = ? WHERE user_id = ?";
                updateRecord(Query, newPassword, userID);
                break;
            case 2:
                System.out.print("\nEnter new Role [admin/member/project manager]: ");
                String newRole = sc.nextLine();
                
                while(validate.spaceValidate(newRole)){
                    newRole = sc.nextLine();
                }
                
                while(!(validate.roleValidate(newRole))){
                    newRole = sc.nextLine();
                }

                Query = "UPDATE user SET role = ? WHERE user_id = ?";
                updateRecord(Query, newRole, userID);
                break;
            case 3:
                System.out.print("\nEnter new email address: ");
                String newEmail = sc.nextLine();
                
                while(validate.spaceValidate(newEmail)){
                    newEmail = sc.nextLine();
                }
                
                while(validate.emailValidate(newEmail)){
                    newEmail = sc.nextLine();
                }

                Query = "UPDATE user SET email = ? WHERE user_id = ?";
                updateRecord(Query, newEmail, userID);
                break;
            case 4:
                System.out.print("\nEnter new first name: ");
                sc.nextLine();
                String newFname = sc.nextLine();

                Query = "UPDATE user SET first_name = ? WHERE user_id = ?";
                updateRecord(Query, newFname, userID);
                break;
            case 5:
                System.out.print("\nEnter new middle name: ");
                sc.nextLine();
                String newMname = sc.nextLine();

                Query = "UPDATE user SET middle_name = ? WHERE user_id = ?";
                updateRecord(Query, newMname, userID);
                break;
            case 6:
                System.out.print("\nEnter new last name: ");
                sc.nextLine();
                String newLname = sc.nextLine();

                Query = "UPDATE user SET middle_name = ? WHERE user_id = ?";
                updateRecord(Query, newLname, userID);
                break;
            case 7:
                break;
            default: System.out.println("Error: Invalid selection.");
        }
    }
    
    private boolean validatePassword(int getID, String getPassword){
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT password FROM user WHERE user_id = ?");
            state.setInt(1, getID);
            ResultSet result = state.executeQuery();
            
            String pass = result.getString("password");
            result.close();
            
            if(getPassword.equals(pass))
            return true;
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        return false;
    }
}
