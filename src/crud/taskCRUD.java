package crud;

import main.config;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class taskCRUD extends config {
    Scanner sc = new Scanner(System.in);
    
    String sql;
    
    public void viewTask(){
        sql = "SELECT t.task_id, t.task_name, t.due_date, p.manager_name, tm.member_name, p.project_name, t.status FROM task t JOIN team_member tm ON t.assigned_to = tm.team_member_id JOIN project p ON t.project_id = p.project_id";
        
        try{
            PreparedStatement findRow = connectDB().prepareStatement(sql);
            
            try (ResultSet checkTable = findRow.executeQuery()) {
                if(!checkTable.next()){
                    System.out.println("Task Empty.");
                } else{
                    System.out.println("List of Tasks: ");
                    viewTaskList(sql);
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void addTask(int id){
        System.out.println("--------------------------------------------------------------------------------");
        System.out.print("Enter task name: ");
        String Tname = sc.nextLine();

        System.out.print("Enter task description: ");
        String desc = sc.nextLine();

        System.out.print("Enter due date [FORMAT: YYYY-MM-DD]: ");
        String dueDate = sc.next();

        System.out.print("Enter project ID to assign: ");
        int pid = sc.nextInt();
        
        sql = "INSERT INTO task (task_name, description, date_created, due_date, created_by, assigned_to, project_id, status) VALUES (?, ?, ?, ?, ?, 0, ?, 'Not Started')";

        addRecord(sql, Tname, desc, date.toString(), dueDate, id, pid);
    }
    
    public void editTask(){
        boolean Selected = false;
        
        System.out.print("\nEnter ID: ");
        int taskID = sc.nextInt();
        
        getTaskInfo(taskID);
        
        do{
            System.out.print("1. Change task name"
                    + "\n2. Change task description"
                    + "\n3. Change due date"
                    + "\n4. Change status"
                    + "\n5. Remove asignee"
                    + "\n6. Back"
                    + "\nEnter selection: ");
            int selectEdit = sc.nextInt();
            
            switch(selectEdit){
                case 1:
                    System.out.print("\nEnter new task name: ");
                    sc.nextLine();
                    String newName = sc.nextLine();

                    sql = "UPDATE task SET task_name = ? WHERE task_id = ?";
                    updateRecord(sql, newName, taskID);
                    break;
                case 2:
                    System.out.println("\nEnter new task description: ");
                    sc.nextLine();
                    String newDesc = sc.nextLine();

                    sql = "UPDATE task SET description = ? WHERE task_id = ?";
                    updateRecord(sql, newDesc, taskID);
                    break;
                case 3:
                    System.out.print("\nEnter new due date [FORMAT: YYYY-MM-DD]: ");
                    String newDate = sc.next();
                    
                    sql = "UPDATE task SET due_date = ? WHERE task_id = ?";
                    updateRecord(sql, newDate, taskID);
                    break;
                case 4:
                    System.out.print("\nEnter new status [Not Started/In Progress/Completed]: ");
                    sc.nextLine();
                    String newStatus = sc.nextLine();

                    sql = "UPDATE task SET status = ? WHERE task_id = ?";
                    updateRecord(sql, newStatus, taskID);
                    break;
                case 5:
                    System.out.print("Confirm remove? [y/n]: ");
                    String confirm = sc.next();
                    
                    if(confirm.equalsIgnoreCase("y")){
                        sql = "UPDATE task SET assigned_to = 0 WHERE task_id = ?";
                        updateRecord(sql, taskID);
                    } else{
                        System.out.println("Cancelled removal.");
                    }
                    break;
                case 6:
                    Selected = true;
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!Selected);
    }
    
    public void deleteTask(){
        System.out.print("\nEnter ID: ");
        int taskID = sc.nextInt();
        
        getTaskInfo(taskID);
        
        System.out.print("Confirm delete? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM task WHERE task_id = ?";
            deleteRecord(sql, taskID);
        } else{
            System.out.println("Deletion cancelled.");
        }
    }
    
    public void assignMember(){
        teamCRUD t = new teamCRUD();
        teamMemberCRUD tm = new teamMemberCRUD();
         
        System.out.println("");
        t.viewTeam();
        
        System.out.print("Enter team ID: ");
        int tid = sc.nextInt();
        
        System.out.println("\nTeam members: ");
        sql = "SELECT tm.team_member_id, u.first_name, tm.team_id, t.team_name FROM team_member tm INNER JOIN user u ON tm.user_id = u.user_id INNER JOIN team t ON tm.team_id = t.team_id WHERE tm.team_id = ?";
        tm.viewTeamMemberFiltered(sql, tid);  
        
        System.out.print("Enter member ID: ");
        int mid = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        tm.searchMember(mid);
        
        sql = "SELECT t.task_id, t.task_name, t.due_date, p.manager_name, tm.member_name, p.project_name, t.status FROM task t JOIN team_member tm ON t.assigned_to = tm.team_member_id JOIN project p ON t.project_id = p.project_id WHERE tm.member_name = ?";
        viewTaskFiltered("None", sql);
        
        System.out.print("Enter task ID: ");
        int task = sc.nextInt();
        
        getTaskInfo(task);
        
        System.out.print("Confirm assign member? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equalsIgnoreCase("y")){
            sql = "UPDATE task SET assigned_to = ? WHERE task_id = ?";
            updateRecord(sql, mid, task);
        } else{
            System.out.print("Assign cancelled.");
        }
    }
    
    public void filterBy(){
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

                    System.out.println("Task list filtered by: Date");
                    sql = "SELECT t.task_id, t.task_name, t.due_date, u.first_name, p.project_name, t.status FROM task t INNER JOIN user u ON t.assigned_to = u.user_id INNER JOIN project p ON t.project_id = p.project_id WHERE t.due_date = ?";
                    viewTaskFiltered(getDate, sql);
                    break;
                case 2:
                    System.out.print("Enter status [Not Started/In Progress/Completed]: ");
                    sc.nextLine();
                    String getStatus = sc.nextLine();

                    System.out.println("Task list fileted by: Status");
                    sql = "SELECT t.task_id, t.task_name, t.due_date, u.first_name, p.project_name, t.status FROM task t INNER JOIN user u ON t.assigned_to = u.user_id INNER JOIN project p ON t.project_id = p.project_id WHERE t.status = ?";
                    viewTaskFiltered(getStatus, sql);
                    break;
                case 3:
                    isBack = true;
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isBack);
    }
    
    private void viewTaskList(String query){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(query);
            
            try (ResultSet checkRow = findRow.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "ID", "Name", "Due Date", "Created by", "Assigned to", "Project", "Status");
                
                while(checkRow.next()){
                    
                    int t_id = checkRow.getInt("task_id");
                    String t_name = checkRow.getString("task_name");
                    String d_date = checkRow.getString("due_date");
                    String p_creator = checkRow.getString("manager_name");
                    String tm_name = checkRow.getString("member_name");
                    String p_project = checkRow.getString("project_name");
                    String t_status = checkRow.getString("status");
                    
                    System.out.printf("%-20d %-20s %-20s %-20s %-20s %-20s %-20s\n", t_id, t_name, d_date, p_creator, tm_name, p_project, t_status);
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void viewTaskFiltered(String getColumn, String query){
        try{
            PreparedStatement filter = connectDB().prepareStatement(query);
            filter.setString(1, getColumn);
            
            try (ResultSet checkRow = filter.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s\n", "ID", "Name", "Due Date", "Assigned to", "Project", "Status");
                while(checkRow.next()){
                    String[] name = checkRow.getString("member_name").split(" ");
                    int t_id = checkRow.getInt("task_id");
                    String t_name = checkRow.getString("task_name");
                    String d_date = checkRow.getString("due_date");
                    String u_name = name[0];
                    String p_name = checkRow.getString("project_name");
                    String t_status = checkRow.getString("status");
                    
                    System.out.printf("%-20d %-20s %-20s %-20s %-20s %-20s\n", t_id, t_name, d_date, u_name, p_name, t_status);
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void viewInfo() throws IOException{
        System.out.print("\nEnter ID: ");
        int taskID = sc.nextInt();
        
        getTaskInfo(taskID);
        System.out.print("Press any key to continue...");
        System.in.read();
    }
    
    private void getTaskInfo(int id){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT t.task_id, t.task_name, t.description, t.date_created, t.due_date, p.manager_name, tm.member_name, p.project_name, t.status FROM task t JOIN team_member tm ON t.assigned_to = tm.team_member_id JOIN project p ON t.project_id = p.project_id WHERE t.task_id = ?;");
            search.setInt(1, id);
            
            try (ResultSet result = search.executeQuery()) {
                String[] name = result.getString("member_name").split(" ");
                System.out.println("--------------------------------------------------------------------------------"
                        + "\nSelected task:     | "+result.getString("task_name")
                        + "\n-------------------+"
                        + "\nTask ID:           | "+result.getInt("task_id")
                        + "\nDescription:       | "+result.getString("description")
                        + "\nDate created:      | "+result.getString("date_created")
                        + "\nDue date:          | "+result.getString("due_date")
                        + "\nCreated By:        | "+result.getString("manager_name")
                        + "\nAssigned to:       | "+name[0]
                        + "\nFrom project:      | "+result.getString("project_name")
                        + "\nStatus:            | "+result.getString("status")
                        + "\n--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
