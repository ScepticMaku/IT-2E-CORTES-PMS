package ui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import authentication.Register;
import java.io.IOException;
import main.config;

public class users extends config {
    Scanner sc = new Scanner(System.in);
    Register reg = new Register();
    
    int userID;
    boolean isSelected = false;
    String Query;
    
    public void userInterface() throws IOException{
        do{
            System.out.println("================================================================================================================================================================");
            System.out.println("List of users: ");
            viewUserList();
        
            System.out.print("1. Register a user"
                    + "\n2. Edit user"
                    + "\n3. Delete user"
                    + "\n4. View full info"
                    + "\n5. Back"
                    + "\nEnter selection: ");
            int userSelect = sc.nextInt();

            switch(userSelect){
                case 1:
                    reg.registerCredentials();
                    break;
                case 2:
                    editUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    viewFullInfo();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection");
            }
        } while(!isSelected);
    }
    
    public void viewUserList(){
        Query = "SELECT * FROM user";
        String[] Header = {"ID", "Name", "Role"};
        String[] Column = {"user_id", "first_name", "Role"};
        try{
            PreparedStatement findRow = connectDB().prepareStatement(Query);
            ResultSet checkRow = findRow.executeQuery();
            
            if(!checkRow.next()){
                System.out.println("Table empty.");
            } else{
                viewRecords(Query, Header, Column);
            }
            checkRow.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void viewFullInfo() throws IOException{
        System.out.print("\nEnter ID: ");
        userID = sc.nextInt();

        System.out.println("--------------------------------------------------------------------------------");
        searchID(userID);
        
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT * FROM user WHERE user_id = ?");
            state.setInt(1, userID);
            
            ResultSet result = state.executeQuery();
            System.out.println("Full Information: "
                    + "\nFirst Name:"+result.getString("first_name")
                    + "\nMiddle Name:"+result.getString("middle_name")
                    + "\nLast Name:"+result.getString("last_name")
                    + "\nUsername: "+result.getString("username")
                    + "\nPassword: "+result.getString("password")
                    + "\nRole: "+result.getString("role"));
            System.out.println("--------------------------------------------------------------------------------");
            System.out.print("Press any key to continue...");
            System.in.read();
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void searchID(int id){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT * FROM user WHERE user_id = ?");
            
            search.setInt(1,id);
            ResultSet result = search.executeQuery();
            
            String[] selectedName = result.getString("first_name").split(" ");
            
            System.out.println("Selected user: "+selectedName[0]);
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void editUser(){
            System.out.print("\nEnter ID you want to edit: ");
            userID = sc.nextInt();

            System.out.println("--------------------------------------------------------------------------------");
            searchID(userID);

            System.out.print("Select what you want to edit:"
                    + "\n1. Password"
                    + "\n2. Role"
                    + "\n3. First Name"
                    + "\n4. Middle Name"
                    + "\n5. Last Name"
                    + "\n6. Back"
                    + "\nEnter selection: ");
            int select = sc.nextInt();

            switch(select){
                case 1:
                    System.out.print("\nEnter new password: ");
                    String newPass = sc.next();
                    
                    Query = "UPDATE user SET password = ? WHERE user_id = ?";
                    updateRecord(Query, newPass, userID);
                    break;
                case 2:
                    System.out.print("\nEnter new Role [admin/team member/project manager]: ");
                    sc.nextLine();
                    String newRole = sc.nextLine();
                    
                    Query = "UPDATE user SET role = ? WHERE user_id = ?";
                    updateRecord(Query, newRole, userID);
                    break;
                case 3:
                    System.out.print("\nEnter new first name: ");
                    sc.nextLine();
                    String newFname = sc.nextLine();
                    
                    Query = "UPDATE user SET first_name = ? WHERE user_id = ?";
                    updateRecord(Query, newFname, userID);
                    break;
                case 4:
                    System.out.print("\nEnter new middle name: ");
                    sc.nextLine();
                    String newMname = sc.nextLine();
                    
                    Query = "UPDATE user SET middle_name = ? WHERE user_id = ?";
                    updateRecord(Query, newMname, userID);
                    break;
                case 5:
                    System.out.print("\nEnter new last name: ");
                    sc.nextLine();
                    String newLname = sc.nextLine();
                    
                    Query = "UPDATE user SET middle_name = ? WHERE user_id = ?";
                    updateRecord(Query, newLname, userID);
                    break;
                case 6:
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
    }
    
    private void deleteUser(){
        System.out.print("\nEnter ID: ");
        userID = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchID(userID);
        
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            Query = "DELETE FROM user WHERE user_id = ?";
            deleteRecord(Query, userID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
        
}
