package ui;

import main.config;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class team_members extends config{
    Scanner sc = new Scanner(System.in);
    
    boolean isSelected = false;
    int choice;
    String sql;
    
    public void memberInterface(int tid){
       do{
            System.out.println("--------------------------------------------------------------------------------");
            String sqlQuery = "SELECT u.user_id, u.first_name FROM team_member tm INNER JOIN User u ON tm.user_id = u.user_id WHERE tm.team_id = ?;";
            try{
                PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
                findRow.setInt(1, tid);
                ResultSet checkTable = findRow.executeQuery();
                
                if(!checkTable.next()){
                    System.out.println("Member List Empty");
                } else{
                    System.out.println("List of Members: ");
                    System.out.println("--------------------------------------------------------------------------------");
                    viewMemberList(tid, sqlQuery);
                }
                checkTable.close();
            } catch (SQLException e){
                System.out.println("Error: "+e.getMessage());
            }
            
           System.out.print("\n1. Add member"
                    + "\n2. Replace member"
                    + "\n3. Remove member"
                    + "\n4. Back"
                    + "\nEnter selection: ");
            choice = sc.nextInt();
            
            switch(choice){
                case 1:
                    addMember(tid);
                    break;
                case 2:
                    replaceMember(tid);
                    break;
                case 3:
                    removeMember(tid);
                    break;
                case 4:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    private void viewMemberList(int id, String Query){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(Query);
            findRow.setInt(1, id);
            ResultSet checkRow = findRow.executeQuery();
            
            
            System.out.printf("%-20s %-20s\n", "ID", "Member");
            while(checkRow.next()){
                int memberID = checkRow.getInt("user_id");
            String memberName = checkRow.getString("first_name");
                
                String[] name = memberName.split(" ");
                
                System.out.printf("%-20d %-20s\n", memberID, name[0]);
            }
            System.out.println("--------------------------------------------------------------------------------");
            checkRow.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void addMember(int id){
        viewUserList();
        System.out.print("\nEnter ID: ");
        int memberID = sc.nextInt();
        
        searchID(memberID);
        System.out.print("Confirm add? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "INSERT INTO team_member (team_id, user_id) VALUES (?, ?)";
            addRecord(sql, id, memberID);
        } else{
            System.out.println("Add Cancelled.");
        }
    }
    
    private void replaceMember(int id){
        String sqlQuery = "SELECT u.user_id, u.first_name FROM team_member tm INNER JOIN User u ON tm.user_id = u.user_id WHERE tm.team_id = ?;";
        viewMemberList(id, sqlQuery);
        
        System.out.print("\nEnter target ID: ");
        int memberID = sc.nextInt();
        
        searchID(memberID);
        viewUserList();
        System.out.print("Enter ID: ");
        int replacedMemberID = sc.nextInt();
        System.out.print("Confirm replace? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "UPDATE team_member SET user_id = ? WHERE team_member_id = ?";
            updateRecord(sql, replacedMemberID, memberID);
        } else{
            System.out.println("Replacement Cancelled.");
        }
    }
    
    private void removeMember(int id){
        String sqlQuery = "SELECT u.user_id, u.first_name FROM team_member tm INNER JOIN User u ON tm.user_id = u.user_id WHERE tm.team_id = ?;";
        viewMemberList(id, sqlQuery);
        
        System.out.print("\nEnter ID: ");
        int memberID = sc.nextInt();
        
        searchID(memberID);
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM team_member WHERE user_id = ?";
            deleteRecord(sql, memberID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void searchID(int mid){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT * FROM user WHERE user_id = ?");
            
            search.setInt(1, mid);
            ResultSet result = search.executeQuery();
            
            String getName = result.getString("first_name");
            String[] setName = getName.split(" ");
            
            System.out.println("Selected User: "+setName[0]);
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
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
