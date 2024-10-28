package crud;

import main.config;


import java.io.IOException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class teamMemberCRUD extends config {
    Scanner sc = new Scanner(System.in);
    
    userCRUD u = new userCRUD();
    taskCRUD tsk = new taskCRUD();
    
    String sql;
   
    public void viewTeam(){
        String sqlQuery = "SELECT tm.team_member_id, tm.member_name, tm.team_id, t.team_name FROM team_member tm INNER JOIN team t ON tm.team_id = t.team_id";
        
        try{
            PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
            
            try (ResultSet checkTable = findRow.executeQuery()) {
                if(!checkTable.next()){
                    System.out.println("Member List Empty");
                } else{
                    System.out.println("List of Members: ");
                    System.out.println("--------------------------------------------------------------------------------");
                    viewMemberList(sqlQuery);
                }
            }
        } catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void addMember(){
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT first_name FROM user WHERE user_id = ?");
            
            System.out.print("\nEnter user ID: ");
            int userID = sc.nextInt();
            
            state.setInt(1, userID);

            System.out.print("Enter team ID to assign: ");
            int teamID = sc.nextInt();

            u.searchUser(userID);
            System.out.print("Confirm add? [y/n]: ");
            String confirm = sc.next();
            
            try(ResultSet result = state.executeQuery()){
                String[] name = result.getString("first_name").split(" ");
                result.close();
                
                if(confirm.equals("y")){
                    
                    sql = "INSERT INTO team_member (team_id, member_name, user_id) VALUES (?, ?, ?)";
                    addRecord(sql, teamID, name[0], userID);
                } else{
                    System.out.println("Add Cancelled.");
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void replaceMember(){
        String sqlQuery = "SELECT tm.team_member_id, u.first_name, tm.team_id, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id";
        System.out.println("");
        viewMemberList(sqlQuery);
        
        System.out.print("Enter member ID to replace: ");
        int targetMemberID = sc.nextInt();
        searchMember(targetMemberID);
        
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
    
    public void removeMember(){
        System.out.print("\nEnter ID: ");
        int memberID = sc.nextInt();
        
        System.out.print("Enter team ID: ");
        int teamID = sc.nextInt();
        
        searchMember(memberID);
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM team_member WHERE team_member_id = ? AND team_id = ?";
            deleteRecord(sql, memberID, teamID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
    
    public void selectMember() throws IOException {
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
                    
                    System.out.println("Selected User:      | "+setName[0]
                            + "\nMember ID:          | "+result.getInt("team_member_id")
                            + "\n--------------------------------------------------------------------------------");
                    viewLists(getName);
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void filterBy(){
        boolean isBack = false;
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
                    sql = "SELECT tm.team_member_id, u.first_name, tm.team_id, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id WHERE tm.team_id = ?";
                    viewTeamMemberFiltered(sql, teamID);    
                    break;
                case 2:
                    isBack = true;
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isBack);
    }
    
    public void viewTeamMemberFiltered(String Query, int getTeamID){
        try{
            PreparedStatement filter = connectDB().prepareStatement(Query);
            filter.setInt(1, getTeamID);
            
            try (ResultSet checkRow = filter.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s %-20s\n", "ID", "Member", "Team ID", "Team");
                
                while(checkRow.next()){
                    int memberID = checkRow.getInt("team_member_id");
                    String memberName = checkRow.getString("first_name");
                    int teamID = checkRow.getInt("team_id");
                    String teamName = checkRow.getString("team_name");
                    
                    String[] name = memberName.split(" ");
                    
                    System.out.printf("%-20d %-20s %-20d %-20s\n", memberID, name[0], teamID, teamName);
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
            
            try (ResultSet checkRow = findRow.executeQuery()) {
                System.out.printf("%-20s %-20s %-20s %-20s\n", "Member ID", "Member", "Team ID", "Team");
                
                while(checkRow.next()){
                    int memberID = checkRow.getInt("team_member_id");
                    String memberName = checkRow.getString("member_name");
                    int teamID = checkRow.getInt("team_id");
                    String teamName = checkRow.getString("team_name");
                    
                    String[] name = memberName.split(" ");
                    
                    System.out.printf("%-20d %-20s %-20d %-20s\n", memberID, name[0], teamID, teamName);
                }
                System.out.println("--------------------------------------------------------------------------------");
                checkRow.close();
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void viewAssignedTeams(String member, String getQuery){
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
                    sql = "SELECT t.task_id, t.task_name, t.due_date, tm.member_name, p.project_name, t.status FROM task t INNER JOIN team_member tm ON t.assigned_to = tm.team_member_id INNER JOIN project p ON t.project_id = p.project_id WHERE tm.member_name = ?";
                    tsk.viewTaskFiltered(getMember, sql);
                    break;
                case 2:
                    System.out.println("\nAssigned teams");
                    System.out.println("--------------------------------------------------------------------------------");
                    sql = "SELECT t.team_id, t.team_name, tm.member_name FROM team_member tm INNER JOIN team t ON tm.team_id = t.team_id WHERE tm.member_name = ?";
                    viewAssignedTeams(getMember, sql);
                    break;
                case 3:
                    isBack = true;
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isBack);
    }
    
    public void searchMember(int mid){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT tm.team_member_id, u.first_name, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id WHERE team_member_id = ?");
            search.setInt(1, mid);
            
            try (ResultSet result = search.executeQuery()) {
                String[] setName = result.getString("first_name").split(" ");
                
                System.out.println("Selected User: "+setName[0]);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
