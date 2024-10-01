package main;

import java.sql.*;
import java.util.*;

public class config {
    Scanner sc = new Scanner(System.in);
    
    String name, desc;
    
    public Connection connectDB(){
        Connection connect = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:records.db");
        } catch(Exception e){
            System.out.println("Connection Failed: "+e.getMessage());
        }
        return connect;
    }
    
    public void addProject(){
        System.out.print("\nEnter project name: ");
        name = sc.nextLine();
        
        System.out.println("Enter description: ");
        desc = sc.nextLine();
        
        try{
            PreparedStatement state = connectDB().prepareStatement("INSERT INTO project (project_name, description, project_manager_id, status VALUES (?, ?, ?, 'Planned')");
            
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void updateProject(){
        
    }
    
    public void deleteProject(){
        
    }
    
    public void viewProject(){
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT * FROM project");
            ResultSet display = state.executeQuery();
            
            System.out.println("\nList of Projects: ");
            System.out.printf("%-20s %-20s %-20s %-20s %-20s\n","ID","Name","Description","Project Manager ID","Status");
            
            while(display.next()){
                int pid = display.getInt("project_id");
                String project = display.getString("project_name");
                String desc = display.getString("description");
                int pmid = display.getInt("project_manager_id");
                String stats = display.getString("status");
                
                System.out.printf("%-20d %-20s %-20s %-20d %-20s\n", pid, project, desc, pmid, stats);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
