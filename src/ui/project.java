package ui;

import java.io.IOException;
import main.config;
import java.util.*;
import java.sql.*;



public class project extends config {
    Scanner sc = new Scanner(System.in);
    team tm = new team();
    
    String name, desc, sql, status, confirm, dueDate;
    int  choice, id, selectEdit, projectID;
    boolean isSelected = false;
    
    public void projectInterface(int uid) throws IOException{
        do{
            System.out.println("================================================================================================================================================================");
            System.out.println("List of Projects: ");
            viewProjectList();
            
            System.out.print("\n1. Add Project"
                    + "\n2. Edit Project"
                    + "\n3. Delete Project"
                    + "\n4. View Info"
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
                    
                    System.out.print("Enter due date [FORMAT: YYYY-MM-DD]: ");
                    dueDate = sc.next();
                    
                    status = "Planned";
                    sql = "INSERT INTO project (project_name, description, date_created, due_date, project_manager_id, status) VALUES (?, ?, ?, ?, ?, ?)";
                    addRecord(sql, name, desc, date.toString(), dueDate, uid, status);
                    break;
                case 2:
                    editProject();
                    break;
                case 3:
                    deleteProject();
                    break;
                case 4:
                    viewProjectInfo();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid Selection.");
            }
        } while(!isSelected);
    }
    
    private void viewProjectInfo() throws IOException{
        System.out.print("Enter ID: ");
        projectID = sc.nextInt();

        System.out.println("--------------------------------------------------------------------------------");
        searchID(projectID);
        System.out.print("Press any key to continue...");
        System.in.read();
    }
    
    public void viewProjectList(){
        String projectQuery = "SELECT * FROM project";
        String[] projectHeaders = {"ID", "Name", "Due Date", "Status"};
        String[] projectColumns = {"project_id", "description", "due_date", "status"};
        
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
            PreparedStatement search = connectDB().prepareStatement("SELECT p.project_name, p.description, p.date_created, p.due_date, u.first_name, p.status FROM project p INNER JOIN user u ON project_manager_id = u.user_id WHERE project_id = ?;");
            
            search.setInt(1, pid);
            ResultSet result = search.executeQuery();
            
            String project = result.getString("project_name");
            System.out.println("Selected project: "+project
                        + "\nDescription: "+result.getString("description")
                        + "\nDate Created: "+result.getString("date_created")
                        + "\nDue Date: "+result.getString("due_date")
                        + "\nProject Manager: "+result.getString("first_name")
                        + "\nStatus: "+result.getString("status"));
            
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
                + "3. Change due date: \n"
                + "4. Change status\n"
                + "5. Back\n"
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
                System.out.print("\nEnter new due date [FORMAT: YYYY-MM-DD]: ");
                String newDate = sc.next();
                
                sql = "UPDATE project SET due_date = ? WHERE project_id = ?";
                updateRecord(sql, newDate, id);
                break;
            case 5:
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
