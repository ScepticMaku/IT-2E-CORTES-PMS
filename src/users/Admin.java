package users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ui.*;
import java.util.Scanner;
import main.config;
import authentication.Register;

public class Admin extends config {
    Scanner sc = new Scanner(System.in);
    project proj = new project();
    Register reg = new Register();
    
    boolean isSelected = false;
    int userID;
            
    public void displayInterface(int uid, String user_role, String first_name){
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        do{
            System.out.print("\n1. Projects\n"
                    + "2. Tasks\n"
                    + "3. Users\n"
                    + "4. Logout\n"
                    + "Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    proj.projectInterface(uid);
                    break;
                case 3:
                    userInterface();
                    break;
                case 4:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    private void userInterface(){
        do{
            viewUserList();
        
            System.out.print("\n1. Register a user"
                    + "\n2. Edit user"
                    + "\n3. Delete user"
                    + "\n4. Back"
                    + "\nEnter selection: ");
            int userSelect = sc.nextInt();

            switch(userSelect){
                case 1:
                    reg.registerCredentials();
                    break;
                case 2:
                    System.out.print("\nEnter ID: ");
                    userID = sc.nextInt();

                    System.out.println("--------------------------------------------------------------------------------");
                    searchID(userID);
                    break;
                case 3:
                    System.out.print("\nEnter ID: ");
                    userID = sc.nextInt();

                    System.out.println("--------------------------------------------------------------------------------");
                    searchID(userID);
                    break;
                case 4:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection");
            }
        } while(!isSelected);
    }
    
    public void viewUserList(){
        String Query = "SELECT * FROM user";
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
}
