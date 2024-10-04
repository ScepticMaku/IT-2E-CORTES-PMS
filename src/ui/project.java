package ui;

import main.config;
import java.util.*;
import java.sql.*;


public class project extends config {
    Scanner sc = new Scanner(System.in);
    
    String name, desc, sql, status, confirm;
    boolean goBack = true;
    int select, choice, id, selectEdit;
    
    public void projectInterface(int uid){
        while(goBack){
                        System.out.println("\nOptions:\n"
                            + "1. Add Project\n"
                            + "2. Edit Project\n"
                            + "3. View Projects\n"
                            + "4. Delete Project\n"
                            + "5. Back");
                        System.out.print("Enter selection: ");
                        select = sc.nextInt();

                        switch(select){
                            case 1:
                                System.out.print("\nEnter project name: ");
                                sc.nextLine();
                                name = sc.nextLine();

                                System.out.println("Enter description: ");
                                desc = sc.nextLine();
                                
                                id = uid;
                                status = "Planned";
                                
                                sql = "INSERT INTO project (project_name, description, project_manager_id, status) VALUES (?, ?, ?, ?)";
                                addRecord(sql, name, desc, id, status);
                                break;
                            case 2:
                                System.out.println();
                                viewProject();
                                System.out.print("Enter ID you want to edit: ");
                                id = sc.nextInt();
                                
                                searchID(id);
                                System.out.print("1. Change project name\n"
                                        + "2. Change project description\n"
                                        + "3. Change status\n"
                                        + "Enter selection: ");
                                selectEdit = sc.nextInt();
                                
                                switch(selectEdit){
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                        System.out.print("\nEnter new status[Planned/In-Progress/Completed]: ");
                                        break;
                                }
                                break;
                            case 3:
                                viewProject();
                                break;
                            case 4:
                                System.out.println();
                                viewProject();
                                System.out.print("Enter ID you want to delete: ");
                                id = sc.nextInt();
                                
                                searchID(id);
                                System.out.print("Confirm Delete? [y/n]: ");
                                confirm = sc.next();
                                
                                if(confirm.equals("y")){
                                    sql = "DELETE FROM project WHERE project_id = ?";
                                    String updateID = "UPDATE project SET project_id = ? WHERE project_id = ?";
                                    String fetchID = "SELECT project_id FROM project ORDER BY project_id";
                                    String updateSequence = "UPDATE sqlite_sequence SET seq = (SELECT MAX(project_id) FROM project) WHERE name = \"project\";";
                                    
                                    deleteRecord(sql, fetchID, updateID, updateSequence, id, "project_id");
                                }
                                break;
                        }
                        
                        System.out.print("\n1. Projects Menu: "
                                + "\n2. Main menu "
                                + "\nEnter choice: ");
                        choice = sc.nextInt();
                        
                        if(choice == 2 || select == 5){
                            goBack = false;
                        }
                    }
    }
    
    private void viewProject(){
        String projectQuery = "SELECT * FROM project";
        String[] projectHeaders = {"ID", "Name", "Description", "Status"};
        String[] projectColumns = {"project_id", "project_name", "description", "status"};

        viewRecords(projectQuery, projectHeaders, projectColumns);
    }
    
    private void searchID(int pid){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT * FROM project WHERE project_id = ?");
            
            search.setInt(1, pid);
            ResultSet result = search.executeQuery();
            System.out.println("Selected project: "+result.getString("project_name"));
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
