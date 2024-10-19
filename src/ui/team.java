package ui;

import main.config;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class team extends config {
    Scanner sc = new Scanner(System.in);
    team_members tm = new team_members();
    
    String team_name, sql, another;
    int project_id, choice, userID;
    boolean isSelected = false;
    
    public void teamInterface(){
        do{
            System.out.println("================================================================================================================================================================");
            String sqlQuery = "SELECT t.team_id, t.team_name, p.project_name FROM team t INNER JOIN project p ON t.project_id = p.project_id";;
            try{
                PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
                ResultSet checkTable = findRow.executeQuery();

                if(!checkTable.next()){
                    System.out.println("Team List Empty");
                } else{
                    System.out.println("List of teams working on this project: ");
                    System.out.println("--------------------------------------------------------------------------------");
                    viewTeamList(sqlQuery);
                }
                checkTable.close();
            } catch(SQLException e){
                System.out.println("Error: "+e.getMessage());
            }

            System.out.print("\n1. Add team"
                    + "\n2. Edit team"
                    + "\n3. Delete team"
                    + "\n4. Select team"
                    + "\n5. Back"
                    + "\nEnter selection: ");
            choice = sc.nextInt();

            switch(choice){
                case 1:
                    System.out.print("\nEnter team name: ");
                    sc.nextLine();
                    team_name = sc.nextLine();
                    
                    System.out.print("Enter project ID to assign: ");
                    int pid = sc.nextInt();
                    sql = "INSERT INTO team (team_name, project_id) VALUES (?, ?)";

                    addRecord(sql, team_name, pid);
                    break;
                case 2:
                    editTeam();
                    break;
                case 3:
                    deleteTeam();
                    break;
                case 4:
                    System.out.print("Enter ID: ");
                    int teamID = sc.nextInt();
                    tm.memberInterface(teamID);
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid Selection.");
            }
        } while(!isSelected);
    }
   
    private void editTeam(){
        System.out.print("\nEnter ID: ");
        int teamID = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchID(teamID);
        System.out.print("\n1. Change Name"
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
    
    private void deleteTeam(){
        System.out.print("\nEnter ID: ");
        int teamID = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchID(teamID);
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM team WHERE team_id = ?";
            deleteRecord(sql, teamID);
        }
    }
    
    private void viewTeamList(String Query){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(Query);
            ResultSet checkRow = findRow.executeQuery();
            
            System.out.printf("%-20s %-20s %-20s\n", "ID", "Name", "Project");
            while(checkRow.next()){
                int tid = checkRow.getInt("team_id");
                String tname = checkRow.getString("team_name");
                String pname = checkRow.getString("project_name");
                
                System.out.printf("%-20d %-20s %-20s\n", tid, tname, pname);
            }
            System.out.println("--------------------------------------------------------------------------------");
            checkRow.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void searchID(int id){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT * FROM team WHERE team_id = ?");
            
            search.setInt(1,id);
            ResultSet result = search.executeQuery();
            System.out.println("Selected team: "+result.getString("team_name"));
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
