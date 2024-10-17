package ui;

import main.config;
import java.util.*;
import java.sql.*;


public class project extends config {
    Scanner sc = new Scanner(System.in);
    projectTask ts = new projectTask();
    team tm = new team();
    
    String name, desc, sql, status, confirm;
    int  choice, id, selectEdit, projectID;
    boolean isSelected = false;
    
    public void projectInterface(int uid){
        do{
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("List of Projects: ");
            viewProjectList();
            
            System.out.print("\n1. Add Project"
                    + "\n2. Edit Project"
                    + "\n3. Delete Project"
                    + "\n4. Select Project"
                    + "\n5. Back "
                    + "\nEnter choice: ");
            choice = sc.nextInt();
            
            switch(choice){
                case 1:
                    System.out.print("\nEnter project name: ");
                    sc.nextLine();
                    name = sc.nextLine();

                    System.out.println("Enter description: ");
                    desc = sc.nextLine();

                    status = "Planned";
                    sql = "INSERT INTO project (project_name, description, project_manager_id, status) VALUES (?, ?, ?, ?)";
                    addRecord(sql, name, desc, uid, status);
                    break;
                case 2:
                    editProject();
                    break;
                case 3:
                    deleteProject();
                    break;
                case 4:
                    System.out.print("Enter ID: ");
                    projectID = sc.nextInt();

                    System.out.println("--------------------------------------------------------------------------------");
                    searchID(projectID);
                    System.out.print("Choose what you want to do: "
                            + "\n1. Manage Tasks"
                            + "\n2. Manage Teams"
                            + "\n3. Back"
                            + "\nEnter selection: ");
                    int select = sc.nextInt();
                    
                    switch(select){
                        case 1:
                            ts.taskInterface(projectID);
                            break;
                        case 2:
                            tm.teamInterface(projectID);
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Error: Invalid selection.");
                    }
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid Selection.");
            }
        } while(!isSelected);
    }
    
    public void viewProjectList(){
        String projectQuery = "SELECT * FROM project";
        String[] projectHeaders = {"ID", "Name", "Description", "Status"};
        String[] projectColumns = {"project_id", "project_name", "description", "status"};
        
        try{
            PreparedStatement findRow = connectDB().prepareStatement(projectQuery);
            ResultSet checkRow = findRow.executeQuery();
            
            if(!checkRow.next()){
                System.out.println("Table empty.");
            } else{
                viewRecords(projectQuery, projectHeaders, projectColumns);
            }
            checkRow.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void searchID(int pid){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT * FROM project WHERE project_id = ?");
            
            search.setInt(1, pid);
            ResultSet result = search.executeQuery();
            
            String project = result.getString("project_name");
            System.out.println("Selected project: "+project);
            
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void editProject(){
        System.out.print("Enter ID: ");
        id = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchID(id);
        System.out.print("\n1. Change project name\n"
                + "2. Change project description\n"
                + "3. Change status\n"
                + "4. Back\n"
                + "Enter selection: ");
        selectEdit = sc.nextInt();

        switch(selectEdit){
            case 1:
                sql = "UPDATE project SET project_name = ? WHERE project_id = ?";
                
                System.out.print("\nEnter new project name: ");
                sc.nextLine();
                String newName = sc.nextLine();
                updateRecord(sql, newName, id);
                break;
            case 2:
                System.out.print("\nEnter new project description: ");
                sc.nextLine();
                String newDesc = sc.nextLine();

                sql = "UPDATE project SET description = ? WHERE project_id = ?";
                updateRecord(sql, newDesc, id);
                break;
            case 3:
                System.out.print("\nEnter new status[Planned/In-Progress/Completed]: ");
                sc.nextLine();
                String newStatus = sc.nextLine();

                sql = "UPDATE project SET status = ? WHERE project_id = ?";
                updateRecord(sql, newStatus, id);
                break;
            case 4:
                break;
            default:
                System.out.println("Error: Invalid selection.");
        }
    }
    
    private void deleteProject(){
        System.out.print("Enter ID: ");
        id = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchID(id);
        System.out.print("Confirm Delete? [y/n]: ");
        confirm = sc.next();

        if(confirm.equals("y")){
            sql = "DELETE FROM project WHERE project_id = ?";
            deleteRecord(sql, id);
        } else{
            System.out.println("Deletion Cancelled.");
        }
    }
}
