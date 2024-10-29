package crud;

import main.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Scanner;

public class userCRUD extends config {
    Scanner sc = new Scanner(System.in);
    
    String Query;
    
    public void viewUser(){
        Query = "SELECT * FROM user";
        String[] Header = {"ID", "Name", "Role"};
        String[] Column = {"user_id", "first_name", "role"};
        
        try{
            PreparedStatement findRow = connectDB().prepareStatement(Query);
            
            try (ResultSet checkRow = findRow.executeQuery()) {
                if(!checkRow.next()){
                    System.out.println("Table empty.");
                } else{
                    viewRecords(Query, Header, Column);
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void deleteUser(){
        System.out.print("\nEnter ID: ");
        int userID = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchUser(userID);
        
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            Query = "DELETE FROM user WHERE user_id = ?";
            deleteRecord(Query, userID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
    
    public void viewUserInfo(int userID) throws IOException{
        System.out.println("--------------------------------------------------------------------------------");
        searchUser(userID);
        
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT * FROM user WHERE user_id = ?");
            state.setInt(1, userID);
            
            try (ResultSet result = state.executeQuery()) {
                System.out.println("\nFull Information: "
                        + "\n--------------------------------------------------------------------------------"
                        + "\nUser ID:           | "+result.getInt("user_id")
                        + "\nFirst Name:        | "+result.getString("first_name")
                        + "\nMiddle Name:       | "+result.getString("middle_name")
                        + "\nLast Name:         | "+result.getString("last_name")
                        + "\nEmail Address:     | "+result.getString("email")
                        + "\nUsername:          | "+result.getString("username")
                        + "\nPassword:          | "+result.getString("password")
                        + "\nRole:              | "+result.getString("role"));
                System.out.println("--------------------------------------------------------------------------------");
                System.out.print("Press any key to continue...");
                System.in.read();
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void editUser(){
            System.out.print("\nEnter ID you want to edit: ");
            int userID = sc.nextInt();

            System.out.println("--------------------------------------------------------------------------------");
            searchUser(userID);

            System.out.print("Select what you want to edit:"
                    + "\n1. Password"
                    + "\n2. Role"
                    + "\n3. Email Address"
                    + "\n4. First Name"
                    + "\n5. Middle Name"
                    + "\n6. Last Name"
                    + "\n7. Back"
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
                    System.out.print("\nEnter new Role [admin/member/project manager]: ");
                    sc.nextLine();
                    String newRole = sc.nextLine();
                    
                    Query = "UPDATE user SET role = ? WHERE user_id = ?";
                    updateRecord(Query, newRole, userID);
                    break;
                case 3:
                    System.out.print("\nEnter new email address: ");
                    String newEmail = sc.next();
                    
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
    
    public void searchUser(int id){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT * FROM user WHERE user_id = ?");
            
            search.setInt(1,id);
            try (ResultSet result = search.executeQuery()) {
                String[] selectedName = result.getString("first_name").split(" ");
                
                System.out.println("Selected user: "+selectedName[0]);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}