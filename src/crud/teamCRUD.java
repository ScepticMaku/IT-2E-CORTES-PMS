package crud;

import java.io.IOException;
import main.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class teamCRUD extends config {
    Scanner sc = new Scanner(System.in);
    
    teamMemberCRUD tm = new teamMemberCRUD();
    
    String sql;
    
    public void viewTeam(){
        String sqlQuery = "SELECT t.team_id, t.team_name, t.project_id, p.project_name FROM team t INNER JOIN project p ON t.project_id = p.project_id";
        
        try{
            PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
            ResultSet checkTable = findRow.executeQuery();

            if(!checkTable.next()){
                System.out.println("Team List Empty");
            } else{
                System.out.println("List of teams working on this project: ");
                viewTeamList(sqlQuery);
            }
            checkTable.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void addTeam(){
        System.out.print("\nEnter team name: ");
        String team_name = sc.nextLine();

        System.out.print("Enter project ID to assign: ");
        int pid = sc.nextInt();
        
        sql = "INSERT INTO team(team_name, project_id) VALUES (?, ?)";
        addRecord(sql, team_name, pid);
        
        sc.nextLine();
    }
    
    public void editTeam(){
        System.out.print("\nEnter ID: ");
        int teamID = sc.nextInt();
        
        getTeamInfo(teamID);
        
        System.out.print("1. Change Name"
                + "\n2. Change assigned project"
                + "\n3. Cancel"
                + "\nEnter selection: ");
        int editSelect = sc.nextInt();
        
        switch(editSelect){
            case 1:
                System.out.print("Enter new name: ");
                sc.nextLine();
                String newName = sc.nextLine();

                sql = "UPDATE team SET team_name = ? WHERE team_id = ?";
                updateRecord(sql, newName, teamID);
                break;
            case 2:
                System.out.print("Enter project ID to assign: ");
                int newID = sc.nextInt();
                
                sql = "UPDATE team SET project_id = ? WHERE team_id = ?";
                updateRecord(sql, newID, teamID);
                break;
            case 3:
                System.out.println("Update Cancelled.");
                break;
        }
    }
    
    public void deleteTeam(){
        System.out.print("\nEnter ID: ");
        int teamID = sc.nextInt();
        
        getTeamInfo(teamID);
        
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM team WHERE team_id = ?";
            deleteRecord(sql, teamID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
    
    public void viewInfo() throws IOException{
        System.out.print("Enter ID: ");
        int teamID = sc.nextInt();
        
        getTeamInfo(teamID);

        sql = "SELECT tm.team_member_id, u.first_name, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id WHERE tm.team_id = ?";
        System.out.println("\nTeam Members: ");
        tm.viewTeamMemberFiltered(sql, teamID);
        System.out.print("Press any key to continue...");
        System.in.read();
    }
    
    public void filterBy(){
        boolean isBack = false;
        
        do{
            System.out.print("\nFilter by:"
                    + "\n1. Project"
                    + "\n2. Back"
                    + "\nEnter selection: ");
            int filterSelect = sc.nextInt();

            switch(filterSelect){
                case 1:
                    System.out.print("Enter project ID: ");
                    int getProject = sc.nextInt();

                    System.out.println("\nTeam list filtered by: Project");
                    System.out.println("--------------------------------------------------------------------------------");
                    try{
                        PreparedStatement state = connectDB().prepareStatement("SELECT p.project_name FROM team t INNER JOIN project p ON t.project_id = p.project_id WHERE t.project_id = ?");

                        state.setInt(1, getProject);
                        
                        try (ResultSet result = state.executeQuery()) {
                            sql = "SELECT t.team_id, t.team_name, p.project_name FROM team t INNER JOIN project p ON t.project_id = p.project_id WHERE p.project_name = ?";
                            System.out.println(result.getString("project_name"));

                            viewTeamFiltered(result.getString("project_name"), sql);
                        }
                    } catch(SQLException e){
                        System.out.println("Error: "+e.getMessage());
                    }
                    break;
                case 2:
                    isBack = true;
                    break;
                default: System.out.println("Error: Invalid selection");
            }
        } while(!isBack);
    }
    
    public void viewTeamFiltered(String getColumn, String query){
        try{
            PreparedStatement filter = connectDB().prepareStatement(query);
            filter.setString(1, getColumn);
            
            try (ResultSet checkRow = filter.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s\n", "ID", "Name", "Project");
                while(checkRow.next()){
                    int tid = checkRow.getInt("team_id");
                    String tname = checkRow.getString("team_name");
                    String pname = checkRow.getString("project_name");
                    
                    System.out.printf("%-20d %-20s %-20s\n", tid, tname, pname);
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void viewTeamList(String Query){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(Query);
            
            try (ResultSet checkRow = findRow.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s %-20s\n", "Team ID", "Name", "Project ID", "Project");
                
                while(checkRow.next()){
                    int tid = checkRow.getInt("team_id");
                    String tname = checkRow.getString("team_name");
                    int pid = checkRow.getInt("project_id");
                    String pname = checkRow.getString("project_name");
                    
                    System.out.printf("%-20d %-20s %-20s %-20s\n", tid, tname, pid, pname);
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void getTeamInfo(int id){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT tm.team_id, tm.team_name, p.project_name FROM team tm INNER JOIN project p ON tm.project_id = p.project_id WHERE team_id = ?");
            
            search.setInt(1,id);
            
            try (ResultSet result = search.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------"
                        + "\nSelected team:     | "+result.getString("team_name")
                        + "\n-------------------+"
                        + "\nFrom project:      | "+result.getString("project_name")
                        + "\n--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
