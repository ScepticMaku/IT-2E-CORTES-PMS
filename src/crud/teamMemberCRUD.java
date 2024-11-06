package crud;

import main.config;
import main.validation;

import java.io.IOException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class teamMemberCRUD extends config {
    Scanner sc = new Scanner(System.in);
    
    validation validate = new validation();
    userCRUD u = new userCRUD();
    taskCRUD tsk = new taskCRUD();
    
    String sql;
   
    public void viewTeam(){
        String sqlQuery = "SELECT team_member_id, member_name, team_member.team_id, t.team_name "
                + "FROM team_member "
                + "INNER JOIN team t ON team_member.team_id = t.team_id";
        
        try{
            PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
            
            try (ResultSet checkTable = findRow.executeQuery()) {
                if(!checkTable.next()){
                    System.out.println("Member List Empty");
                } else{
                    System.out.println("List of Members: ");
                    viewMemberList(sqlQuery);
                }
            }
        } catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void addMember(){
        teamCRUD t = new teamCRUD();
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT first_name FROM user WHERE user_id = ?");
            
            System.out.println("\nTeam List: ");
            t.viewTeam();
            
            System.out.print("Enter team ID to assign: ");
            int teamID = validate.validateInt();
            
            while(getSingleValue("SELECT team_id FROM team WHERE team_id = ?", teamID) == 0){
                System.out.print("Error: ID doesn't exist, try again: ");
                teamID = validate.validateInt();
            }
            
            System.out.println("Member List:");
            u.viewUserFiltered("member", "SELECT * FROM user WHERE role = ?");
            
            System.out.print("Enter user ID: ");
            int userID = validate.validateInt();
            
            while(getSingleValue("SELECT user_id FROM user WHERE user_id = ? AND role = 'member'", userID) == 0){
                System.out.print("Error: ID doesn't exist, try again: ");
                userID = validate.validateInt();
            }
            
            while(getSingleValue("SELECT user_id FROM team_member WHERE team_id = ? AND user_id = ?", teamID, userID) != 0){
                System.out.print("Error: ID is already assigned to this team, try again: ");
                userID = validate.validateInt();
            }
            
            state.setInt(1, userID);
            
            u.searchUser(userID);
            System.out.print("Confirm add? [y/n]: ");
            String confirm = sc.nextLine();
            
            try(ResultSet result = state.executeQuery()){
                String[] name = result.getString("first_name").split(" ");
                result.close();
                
                if(validate.confirm(confirm)){
                    addRecord("INSERT INTO team_member (team_id, member_name, user_id) "
                            + "VALUES (?, ?, ?)", teamID, name[0], userID);
                } else{
                    System.out.println("Add Cancelled.");
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void replaceMember(){
        teamCRUD t = new teamCRUD();
        
        System.out.println("\nTeam List: ");
        t.viewTeam();

        System.out.print("Enter team ID to assign: ");
        int teamID = validate.validateInt();

        while(getSingleValue("SELECT team_id FROM team WHERE team_id = ?", teamID) == 0){
            System.out.print("Error: ID doesn't exist, try again: ");
            teamID = validate.validateInt();
        }
        
        String sqlQuery = "SELECT team_member_id, member_name, team_member.team_id, t.team_name "
                + "FROM team_member  "
                + "INNER JOIN team t ON team_member.team_id = t.team_id WHERE team_member.team_id = ?";
        
        System.out.println("List of Members:");
        viewTeamMemberFiltered(sqlQuery, teamID);
        
        System.out.print("Enter member ID to replace: ");
        int targetMemberID = validate.validateInt();
        
        while(getSingleValue("SELECT team_member_id FROM team_member WHERE team_member_id = ? AND team_id = ?", targetMemberID, teamID) == 0){
            System.out.print("Error: ID is not on the team, try again: ");
            targetMemberID = validate.validateInt();
        }
        
        searchMember(targetMemberID);
        
        System.out.println("\nMember List:");
        u.viewUserFiltered("member", "SELECT * FROM user WHERE role = ?");
        
        System.out.print("Enter user ID to insert: ");
        int userID = validate.validateInt();
        
        while(getSingleValue("SELECT user_id FROM user WHERE user_id = ? AND role = 'member'", userID) == 0){
            System.out.print("Error: ID doesn't exist, try again: ");
            userID = validate.validateInt();
        }

        while(getSingleValue("SELECT user_id FROM team_member WHERE team_id = ? AND user_id = ?", teamID, userID) != 0){
            System.out.print("Error: ID is already assigned to this team, try again: ");
            userID = validate.validateInt();
        }
        
        System.out.print("Confirm replace? [y/n]: ");
        String confirm = sc.nextLine();
        
        if(validate.confirm(confirm)){
            updateRecord("UPDATE team_member SET user_id = ? "
                    + "WHERE team_member_id = ? AND team_id = ?", userID, targetMemberID, teamID);
        } else{
            System.out.println("Replacement Cancelled.");
        }
    }
    
    public void removeMember(){
        teamCRUD t = new teamCRUD();
        
        System.out.println("\nTeam List: ");
        t.viewTeam();

        System.out.print("Enter team ID: ");
        int teamID = validate.validateInt();

        while(getSingleValue("SELECT team_id FROM team WHERE team_id = ?", teamID) == 0){
            System.out.print("Error: ID doesn't exist, try again: ");
            teamID = validate.validateInt();
        }
        
        String sqlQuery = "SELECT team_member_id, member_name, team_member.team_id, t.team_name "
                + "FROM team_member  "
                + "INNER JOIN team t ON team_member.team_id = t.team_id WHERE team_member.team_id = ?";
        
        System.out.println("List of Members:");
        viewTeamMemberFiltered(sqlQuery, teamID);
        
        System.out.print("\nEnter member ID: ");
        int memberID = validate.validateInt();
        
        while(getSingleValue("SELECT team_member_id FROM team_member WHERE team_member_id = ? AND team_id = ?", memberID, teamID) == 0){
            System.out.print("Error: ID is not on the team, try again: ");
            memberID = validate.validateInt();
        }
        
        searchMember(memberID);
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.nextLine();
        
        if(validate.confirm(confirm)){
            deleteRecord("DELETE FROM team_member "
                    + "WHERE team_member_id = ? AND team_id = ?", memberID, teamID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
    
    public void selectMember() throws IOException {
        System.out.print("\nEnter member ID: ");
        int memberID = validate.validateInt();
        
        while(getSingleValue("SELECT team_member_id FROM team_member WHERE team_member_id = ?", memberID) == 0){
            System.out.print("Error: ID doesn't exist, try again: ");
            memberID = validate.validateInt();
        }
        
        System.out.println("--------------------------------------------------------------------------------");
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT team_member_id, member_name, t.team_name "
                    + "FROM team_member "
                    + "INNER JOIN team t ON team_member.team_id = t.team_id "
                    + "WHERE team_member_id = ?");
            search.setInt(1, memberID);
            try(ResultSet result = search.executeQuery()){    
                
                System.out.println("Selected User:      | "+result.getString("member_name")
                        + "\nMember ID:          | "+result.getInt("team_member_id")
                        + "\n--------------------------------------------------------------------------------");
                viewLists(result.getString("member_name"));
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
            int filterSelect = validate.validateInt();

            switch(filterSelect){
                case 1:
                    System.out.print("Enter team ID: ");
                    int teamID = validate.validateInt();

                    System.out.println("\nTeam list filtered by team:");
                    sql = "SELECT team_member_id, member_name, team_member.team_id, t.team_name "
                            + "FROM team_member "
                            + "INNER JOIN team t ON team_member.team_id = t.team_id "
                            + "WHERE team_member.team_id = ?";
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
                    String memberName = checkRow.getString("member_name");
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
        String[] memberHeaders = {"Member ID", "Member", "Team ID", "Team"};
        String[] memberColumns = {"team_member_id", "member_name", "team_id", "team_name"};
        
        viewRecords(Query, memberHeaders, memberColumns);
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
            int viewSelect = validate.validateInt();
            
            switch(viewSelect){
                case 1:
                    System.out.println("\nAssigned tasks:");
                    
                    tsk.viewTaskFiltered(getMember, "SELECT task_id, task_name, due_date, status "
                            + "FROM task t "
                            + "INNER JOIN team_member tm ON t.assigned_to = tm.team_member_id "
                            + "WHERE tm.member_name = ?");
                    break;
                case 2:
                    System.out.println("\nAssigned teams:");
                    System.out.println("--------------------------------------------------------------------------------");
                    
                    viewAssignedTeams(getMember, "SELECT t.team_id, t.team_name, tm.member_name "
                            + "FROM team_member tm "
                            + "INNER JOIN team t ON tm.team_id = t.team_id "
                            + "WHERE tm.member_name = ?");
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
            PreparedStatement search = connectDB().prepareStatement("SELECT team_member_id, member_name, t.team_name "
                    + "FROM team_member "
                    + "INNER JOIN team t ON team_member.team_id = t.team_id "
                    + "WHERE team_member_id = ?");
            search.setInt(1, mid);
            
            try (ResultSet result = search.executeQuery()) {
                
                System.out.println("Selected member: "+result.getString("member_name"));
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
