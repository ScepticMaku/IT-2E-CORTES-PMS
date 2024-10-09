package ui;

import java.sql.*;
import main.config;
import java.util.Scanner;

public class task extends config {
    Scanner sc = new Scanner(System.in);
    
    String Tname, desc, status, sql;
    int tid;
    
    int t_ID;
    String taskName, taskDescription, taskStatus;
    
    public void taskInterface(int pid){
        System.out.println("--------------------------------------------------------------------------------");
        
        String sqlQuery = "SELECT * FROM task WHERE project_id = ?";
        try{
            PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
            findRow.setInt(1, pid);
            ResultSet checkTable = findRow.executeQuery();
            
            if(!checkTable.next()){
                System.out.println("Task Empty.");
            } else{
                viewTaskList(pid, sqlQuery);
            }
            checkTable.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        
        System.out.print("\n1. Add Task"
                + "\n2. Select Task"
                + "\n3. Back"
                + "\nEnter selection: ");
        int select = sc.nextInt();
        
        switch(select){
            case 1:
                System.out.print("Enter task name: ");
                sc.nextLine();
                Tname = sc.nextLine();
                
                System.out.println("Enter task description: ");
                desc = sc.nextLine();
                
                tid = pid;
                status = "Not Started";
                sql = "INSERT INTO task (task_name, description, project_id, status) VALUES (?, ?, ?, ?)";
                
                addRecord(sql, Tname, desc, tid, status);
                break;
            case 2:
                selectTask();
                break;
            case 3:
                break;
            default:
                System.out.println("Error: Invalid selection.");
        }
    }
    
    private void selectTask(){
        System.out.print("\nEnter ID: ");
        int taskID = sc.nextInt();
        
        searchID(taskID);
        System.out.print("Choose what you want to do: "
                + "\n1. Edit Task"
                + "\n2. Delete Task"
                + "\n3. "
                + "\n4. Back"
                + "\nEnter selection: ");
        int taskSelection = sc.nextInt();
        
        switch(taskSelection){
            case 1:
                editTask(taskID);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                System.out.println("Error: Invalid selection.");
        }
    }
    
    private void viewTaskList(int id, String taskQuery){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(taskQuery);
            findRow.setInt(1, id);
            ResultSet checkRow = findRow.executeQuery();
            
            System.out.printf("%-20s %-20s %-20s %-20s\n", "ID", "Name", "Description", "Status");
            while(checkRow.next()){
                int t_id = checkRow.getInt("task_id");
                String t_name = checkRow.getString("task_name");
                String t_desc = checkRow.getString("description");
                String t_status = checkRow.getString("status");
                
                System.out.printf("%-20d %-20s %-20s %-20s\n", t_id, t_name, t_desc, t_status);
            }
            System.out.println("--------------------------------------------------------------------------------");
            checkRow.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void editTask(int t_id){
        System.out.print("\n1. Change task name"
                + "\n2. Change task description"
                + "\n3. Change status"
                + "\n4. Back"
                + "\nEnter selection: ");
        int selectEdit = sc.nextInt();
        
        switch(selectEdit){
            case 1:
                System.out.print("\nEnter new task name: ");
                sc.nextLine();
                String newName = sc.nextLine();
                
                sql = "UPDATE task SET task_name = ? WHERE task_id = ?";
                updateRecord(sql, newName, t_id);
                break;
            case 2:
                System.out.println("\nEnter new task description: ");
                sc.nextLine();
                String newDesc = sc.nextLine();
                
                sql = "UPDATE task SET description = ? WHERE task_id = ?";
                updateRecord(sql, newDesc, t_id);
                break;
            case 3:
                System.out.print("\nEnter new status[Not Started/In Progress/Completed");
                sc.nextLine();
                String newStatus = sc.nextLine();
                
                sql = "UPDATE task SET status = ? WHERE task_id = ?";
                updateRecord(sql, newStatus, t_id);
                break;
            case 4:
                break;
            default:
                System.out.println("Error: Invalid selection.");
        }
    }
    
    private void searchID(int id){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT * FROM task WHERE task_id = ?");
            
            search.setInt(1, id);
            ResultSet result = search.executeQuery();
            System.out.println("\nSelected task: "+result.getString("task_name"));
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
