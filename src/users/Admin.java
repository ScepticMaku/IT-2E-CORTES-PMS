package users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ui.*;
import java.util.Scanner;
import main.config;

public class Admin extends config {
    Scanner sc = new Scanner(System.in);
    project proj = new project();
            
    public void displayInterface(int uid, String user_role, String first_name){
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        while(true){
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
                    viewUserList();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        }
    }
    
    private void viewUserList(){
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
}
