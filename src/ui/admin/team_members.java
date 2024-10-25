package ui.admin;

import java.io.IOException;
import main.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class team_members extends config{
    Scanner sc = new Scanner(System.in);
    task ts = new task();
    users us = new users();
    
    boolean isSelected = false;
    int choice;
    String sql;
    
    public void memberInterface() throws IOException{
        boolean isBack = false;
       do{
            System.out.println("================================================================================================================================================================");
            String sqlQuery = "SELECT tm.team_member_id, u.first_name, tm.team_id, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id";
            try{
                PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
                ResultSet checkTable = findRow.executeQuery();
                
                if(!checkTable.next()){
                    System.out.println("Member List Empty");
                } else{
                    System.out.println("List of Members: ");
                    System.out.println("--------------------------------------------------------------------------------");
                    viewMemberList(sqlQuery);
                }
                checkTable.close();
            } catch (SQLException e){
                System.out.println("Error: "+e.getMessage());
            }
            
           System.out.print("\n1. Add member"
                    + "\n2. Replace member"
                    + "\n3. Remove member"
                    + "\n4. Select member"
                    + "\n5. Filter by"
                    + "\n6. Back"
                    + "\nEnter selection: ");
            choice = sc.nextInt();
            
            switch(choice){
                case 1:
                    addMember();
                    break;
                case 2:
                    replaceMember();
                    break;
                case 3:
                    removeMember();
                    break;
                case 4:
                    selectMember();
                    break;
                case 5:
                    do{
                        System.out.print("\nFilter by:"
                                + "\n1. Team"
                                + "\n2. Back"
                                + "\nEnter selection: ");
                        int filterSelect = sc.nextInt();
                        
                        switch(filterSelect){
                            case 1:
                                System.out.print("Enter team ID: ");
                                int teamID = sc.nextInt();

                                System.out.println("\nTeam list filtered by team:");
                                System.out.println("--------------------------------------------------------------------------------");
                                sql = "SELECT tm.team_member_id, u.first_name, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id WHERE tm.team_id = ?";
//                                viewFilteredList(sql, teamID);    
                                break;
                            case 2:
                                isBack = true;
                                break;
                            default: System.out.println("Error: Invalid selection.");
                        }
                    } while(!isBack);
                    break;
                case 6:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    private void selectMember() throws IOException {
        System.out.print("\nEnter member ID: ");
        int memberID = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT tm.team_member_id, u.first_name, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id WHERE tm.team_member_id = ?");
            
            search.setInt(1, memberID);
            try(ResultSet result = search.executeQuery()){
                if(result.getString("first_name") == null){
                    System.out.println("Member not found.");
                } else{
                    String getName = result.getString("first_name");
                    String[] setName = getName.split(" ");
                    
                    System.out.println("Selected User: "+setName[0]);
                    viewLists(getName);
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void viewLists(String getMember){
        boolean isBack = false;
        do{
            System.out.print("\n1. View assigned tasks"
                    + "\n2. View assigned teams"
                    + "\n3. Back"
                    + "\nEnter selection: ");
            int viewSelect = sc.nextInt();
            
            switch(viewSelect){
                case 1:
                    System.out.println("\nAssigned tasks");
                    System.out.println("--------------------------------------------------------------------------------");
                    sql = "SELECT t.task_id, t.task_name, t.due_date, u.first_name, p.project_name, t.status FROM task t INNER JOIN user u ON t.assigned_to = u.user_id INNER JOIN project p ON t.project_id = p.project_id WHERE u.first_name = ?";
//                    ts.viewFilteredList(getMember, sql);
                    break;
                case 2:
                    System.out.println("\nAssigned teams");
                    System.out.println("--------------------------------------------------------------------------------");
                    sql = "SELECT t.team_id, t.team_name, u.first_name FROM team_member tm INNER JOIN team t ON tm.team_id = t.team_id INNER JOIN user u ON tm.user_id = u.user_id WHERE u.first_name = ?";
                    viewAssignedTeams(getMember, sql);
                    break;
                case 3:
                    isBack = true;
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isBack);
    }
    
    private void viewAssignedTeams(String member, String getQuery){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(getQuery);
            findRow.setString(1, member);
            
            try(ResultSet getRow = findRow.executeQuery()){
                System.out.printf("%-5s %-20s\n", "ID", "Team");
                while(getRow.next()){
                    int t_id = getRow.getInt("team_id");
                    String t_name = getRow.getString("team_name");

                    System.out.printf("%-5d %-20s\n", t_id, t_name);
                }
                System.out.println("--------------------------------------------------------------------------------");
            } 
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void viewMemberList(String Query){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(Query);
            ResultSet checkRow = findRow.executeQuery();
            
            System.out.printf("%-20s %-20s %-20s %-20s\n", "Member ID", "Member", "Team ID", "Team");
            while(checkRow.next()){
                int memberID = checkRow.getInt("team_member_id");
                String memberName = checkRow.getString("first_name");
                int teamID = checkRow.getInt("team_id");
                String teamName = checkRow.getString("team_name");
                
                String[] name = memberName.split(" ");
                
                System.out.printf("%-20d %-20s %-20d %-20s\n", memberID, name[0], teamID, teamName);
            }
            System.out.println("--------------------------------------------------------------------------------");
            checkRow.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void addMember(){
        System.out.print("\nEnter user ID: ");
        int userID = sc.nextInt();
        
        System.out.print("Enter team ID to assign: ");
        int teamID = sc.nextInt();
        
        us.searchID(userID);
        System.out.print("Confirm add? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "INSERT INTO team_member (team_id, user_id) VALUES (?, ?)";
            addRecord(sql,teamID, userID);
        } else{
            System.out.println("Add Cancelled.");
        }
    }
    
    private void replaceMember(){
        String sqlQuery = "SELECT tm.team_member_id, u.first_name, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id";
        System.out.println("");
        viewMemberList(sqlQuery);
        
        System.out.print("Enter member ID to replace: ");
        int targetMemberID = sc.nextInt();
        searchID(targetMemberID);
        
        System.out.print("Enter user ID to insert: ");
        int memberID = sc.nextInt();
        
        System.out.print("Enter team ID to assign: ");
        int teamID = sc.nextInt();
        
        System.out.print("Confirm replace? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "UPDATE team_member SET user_id = ? WHERE team_member_id = ? AND team_id = ?";
            updateRecord(sql, memberID, targetMemberID, teamID);
        } else{
            System.out.println("Replacement Cancelled.");
        }
    }
    
    private void removeMember(){
        System.out.print("\nEnter ID: ");
        int memberID = sc.nextInt();
        
        System.out.print("Enter team ID: ");
        int teamID = sc.nextInt();
        
        searchID(memberID);
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM team_member WHERE team_member_id = ? AND team_id = ?";
            deleteRecord(sql, memberID, teamID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void searchID(int mid){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT tm.team_member_id, u.first_name, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id WHERE u.user_id = ?");
            
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
}
