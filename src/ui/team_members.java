package ui;

import main.config;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
import users.Admin;

public class team_members extends config{
    Scanner sc = new Scanner(System.in);
    
    boolean isSelected = false;
    int choice;
    String sql;
    
    public void memberInterface(int tid){
       do{
            System.out.println("--------------------------------------------------------------------------------");
            String sqlQuery = "SELECT member_id, user.first_name, team.team_name FROM members INNER JOIN team ON members.team_id INNER JOIN user ON members.user_id WHERE team.team_id = ?";
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
                    break;
                case 3:
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
            
            System.out.printf("%-20s %-20s %-20s\n", "ID", "Member", "Team");
            while(checkRow.next()){
                int memberID = checkRow.getInt("member_id");
                String memberName = checkRow.getString("first_name");
                String teamName = checkRow.getString("team_name");
                
                String[] name = memberName.split(" ");
                
                System.out.printf("%-20d %-20s %-20s\n", memberID, name[0], teamName);
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
            sql = "INSERT INTO members (team_id, user_id) VALUES (?, ?)";
            addRecord(sql, id, memberID);
        } else{
            System.out.println("Cancelled.");
        }
    }
    
    private void replaceMember(){
        
    }
    
    private void removeMember(){
        
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
