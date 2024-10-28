package crud;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import main.config;

public class projectCRUD extends config {
    Scanner sc = new Scanner(System.in);
    
    String sql;
    
    public void viewProject(){
        System.out.println("List of Projects: ");
        
        String projectQuery = "SELECT * FROM project";
        String[] projectHeaders = {"ID", "Name", "Due Date", "Status"};
        String[] projectColumns = {"project_id", "project_name", "due_date", "status"};
        
        try{
            PreparedStatement findRow = connectDB().prepareStatement(projectQuery);
            
            try (ResultSet checkRow = findRow.executeQuery()) {
                if(!checkRow.next()){
                    System.out.println("Table empty.");
                }
                else{
                    viewRecords(projectQuery, projectHeaders, projectColumns);
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void addProject(int uid, String mname){
        System.out.println("--------------------------------------------------------------------------------");
        System.out.print("Enter project name: ");
        String name = sc.nextLine();

        System.out.println("Enter description: ");
        String desc = sc.nextLine();

        System.out.print("Enter due date [FORMAT: YYYY-MM-DD]: ");
        String dueDate = sc.next();
        
        sql = "INSERT INTO project (project_name, description, date_created, due_date, project_manager_id, manager_name, status) VALUES (?, ?, ?, ?, ?, ?, 'Planned')";
        addRecord(sql, name, desc, date.toString(), dueDate, uid, mname);
        sc.nextLine();
    }
    
    public void editProject(){
        boolean isSelected = false;
        
        System.out.print("Enter project ID: ");
        int id = sc.nextInt();
        
        getProjectInfo(id);
        do{
            System.out.print("Choose what you want to do:"
                    + "\n1. Change project name\n"
                    + "2. Change project description\n"
                    + "3. Change due date: \n"
                    + "4. Change status\n"
                    + "5. Back\n"
                    + "Enter selection: ");
            int selectEdit = sc.nextInt();
            
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
                    System.out.print("\nEnter new due date [FORMAT: YYYY-MM-DD]: ");
                    String newDate = sc.next();

                    sql = "UPDATE project SET due_date = ? WHERE project_id = ?";
                    updateRecord(sql, newDate, id);
                    break;
                case 4:
                System.out.print("\nEnter new status[Planned/In-Progress/Completed]: ");
                sc.nextLine();
                String newStatus = sc.nextLine();

                sql = "UPDATE project SET status = ? WHERE project_id = ?";
                updateRecord(sql, newStatus, id);
                break;
                case 5:
                    isSelected = true;
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
            System.out.println("");
        } while(!isSelected);
    }
    
    public void deleteProject(){
        System.out.print("Enter project ID: ");
        int id = sc.nextInt();
        
        getProjectInfo(id);
        System.out.print("Confirm Delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM project WHERE project_id = ?";
            deleteRecord(sql, id);
        } else{
            System.out.println("Deletion Cancelled.");
        }
    }
    
    public void FilterBy() throws IOException{
        boolean isBack = false;
        do{
            System.out.print("\nFilter by: "
                    + "\n1. Due date"
                    + "\n2. Status"
                    + "\n3. Back"
                    + "\nEnter selection: ");
            int filterSelect = sc.nextInt();

            switch(filterSelect){
                case 1:
                    System.out.print("Enter due date [YYYY-MM-DD]: ");
                    String getDate = sc.next();

                    System.out.println("\nTask list fileted by: Date");
                    sql = "SELECT * FROM project WHERE due_date = ?";
                    viewProjectFiltered(getDate,sql);
                    break;
                case 2:
                    System.out.print("Enter status [Planned/Completed/In-Progress]: ");
                    String getStatus = sc.next();

                    System.out.println("\nTask list fileted by: Status");
                    sql = "SELECT * FROM project WHERE status = ?";
                    viewProjectFiltered(getStatus, sql);
                    break;
                case 3:
                    isBack = true;
                    break;
                default: System.out.println("Error: Invalid Selection.");
            }
        } while(!isBack);
    }
    
    public void viewProjectInfo() throws IOException{
        System.out.print("Enter project ID: ");
        int id = sc.nextInt();
        
        getProjectInfo(id);
        viewList(id);
    }
    
    private void getProjectInfo(int id){
        try{
            PreparedStatement findRow = connectDB().prepareStatement("SELECT p.project_id, p.project_name, p.description, p.date_created, p.due_date, p.manager_name, p.status FROM project p INNER JOIN user u ON project_manager_id = u.user_id WHERE project_id = ?;");
            findRow.setInt(1, id);
            
            try (ResultSet result = findRow.executeQuery()) {
                
                System.out.println("--------------------------------------------------------------------------------"
                        + "\nSelected project:  | "+result.getString("project_name")
                        + "\n-------------------+ "
                        + "\nProject ID:        | "+result.getInt("project_id")
                        + "\nDescription:       | "+result.getString("description")
                        + "\nDate Created:      | "+result.getString("date_created")
                        + "\nDue Date:          | "+result.getString("due_date")
                        + "\nProject Manager:   | "+result.getString("manager_name")
                        + "\nStatus:            | "+result.getString("status")
                        + "\n--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void viewList(int pid) throws IOException{
        taskCRUD tsk = new taskCRUD();
        teamCRUD tm = new teamCRUD();
        
        boolean isSelected = false;
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT p.project_name, p.description, p.date_created, p.due_date, u.first_name, p.status FROM project p INNER JOIN user u ON project_manager_id = u.user_id WHERE project_id = ?;");
            search.setInt(1, pid);
            
            try(ResultSet result = search.executeQuery()){
                do{
                    System.out.print("1. Show team list"
                            + "\n2. Show task list"
                            + "\n3. Back"
                            + "\nEnter selection: ");
                    int showSelect = sc.nextInt();

                    switch(showSelect){
                        case 1:
                            System.out.println("\nTeam list:");
                            sql = "SELECT t.team_id, t.team_name, p.project_name FROM team t INNER JOIN project p ON t.project_id = p.project_id WHERE p.project_name = ?";
                            tm.viewTeamFiltered(result.getString("project_name"), sql);
                            break;
                        case 2:
                            System.out.println("\nTask list:");
                            sql = "SELECT t.task_id, t.task_name, t.due_date, u.first_name, p.project_name, t.status FROM task t INNER JOIN user u ON t.assigned_to = u.user_id INNER JOIN project p ON t.project_id = p.project_id WHERE p.project_name = ?";
                            tsk.viewTaskFiltered(result.getString("project_name"), sql);
                            break;
                        case 3:
                            isSelected = true;
                            break;
                        default: System.out.println("Error: Invalid selection");
                    }
                } while(!isSelected);
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        System.out.println("\nTeam list: ");
    }
    
    private void viewProjectFiltered (String getColumn, String query) throws IOException{
        try{
            PreparedStatement filter = connectDB().prepareStatement(query);

            filter.setString(1, getColumn);
            try (ResultSet checkRow = filter.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s %-20s\n", "ID", "Name", "Due Date", "Status");
                
                while(checkRow.next()){
                    int pid = checkRow.getInt("project_id");
                    String pname = checkRow.getString("project_name");
                    String pdue = checkRow.getString("due_date");
                    String pstats = checkRow.getString("status");
                    
                    System.out.printf("%-20d %-20s %-20s %-20s\n", pid, pname, pdue, pstats);
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
