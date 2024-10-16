package ui;

import java.io.IOException;
import java.sql.*;
import main.config;
import java.util.Scanner;

public class projectTask extends config {
    Scanner sc = new Scanner(System.in);
    
    String Tname, desc, status, sql;
    int tid, choice, selectEdit, userID;
    
    public void taskInterface(int pid) throws IOException{
        boolean isSelected = false;
        do{
            System.out.println("--------------------------------------------------------------------------------");
            String sqlQuery = "SELECT t.task_id, t.task_name, t.description, u.first_name, t.status FROM task t INNER JOIN user u ON t.assigned_to = u.user_id WHERE project_id = ?";
            try{
                PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
                findRow.setInt(1, pid);
                ResultSet checkTable = findRow.executeQuery();

                if(!checkTable.next()){
                    System.out.println("Task Empty.");
                } else{
                    System.out.println("List of tasks in this project: ");
                    System.out.println("--------------------------------------------------------------------------------");
                    viewTaskList(pid, sqlQuery);
                }
                checkTable.close();
            } catch(SQLException e){
                System.out.println("Error: "+e.getMessage());
            }

            System.out.print("\n1. Add Task"
                    + "\n2. Edit Task"
                    + "\n3. Delete Task"
                    + "\n4. View Task Info"
                    + "\n5. Back"
                    + "\nEnter selection: ");
            choice = sc.nextInt();

            switch(choice){
                case 1:
                    System.out.print("Enter task name: ");
                    sc.nextLine();
                    Tname = sc.nextLine();

                    System.out.print("Enter task description: ");
                    desc = sc.nextLine();
                    
                    System.out.print("Enter user ID to assign: ");
                    userID = sc.nextInt();

                    tid = pid;
                    status = "Not Started";
                    sql = "INSERT INTO task (task_name, description, assigned_to, project_id, status) VALUES (?, ?, ?, ?, ?)";

                    addRecord(sql, Tname, desc, userID, tid, status);
                    break;
                case 2:
                    editTask();
                    break;
                case 3:
                    deleteTask();
                    break;
                case 4:
                    viewInfo();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    public void viewInfo() throws IOException{
        System.out.print("\nEnter ID: ");
        int taskID = sc.nextInt();

        System.out.println("--------------------------------------------------------------------------------");
        searchID(taskID);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.print("Press any key to continue...");
        System.in.read();
    }
    
    public void viewTaskList(int id, String taskQuery){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(taskQuery);
            findRow.setInt(1, id);
            ResultSet checkRow = findRow.executeQuery();
            
            System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "ID", "Name", "Description", "Assigned to", "Status");
            while(checkRow.next()){
                int t_id = checkRow.getInt("task_id");
                String t_name = checkRow.getString("task_name");
                String t_desc = checkRow.getString("description");
                String u_name = checkRow.getString("first_name");
                String t_status = checkRow.getString("status");
                
                System.out.printf("%-20d %-20s %-20s %-20s %-20s\n", t_id, t_name, t_desc, u_name, t_status);
            }
            System.out.println("--------------------------------------------------------------------------------");
            checkRow.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void editTask(){
        boolean isSelected = false;
        System.out.print("\nEnter ID: ");
        int taskID = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchID(taskID);
        
        do{
            System.out.print("\n1. Change task name"
                    + "\n2. Change task description"
                    + "\n3. Change status"
                    + "\n4. Back"
                    + "\nEnter selection: ");
            selectEdit = sc.nextInt();

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
                    System.out.print("\nEnter new status[Not Started/In Progress/Completed");
                    sc.nextLine();
                    String newStatus = sc.nextLine();

                    sql = "UPDATE task SET status = ? WHERE task_id = ?";
                    updateRecord(sql, newStatus, taskID);
                    break;
                case 4:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    public void deleteTask(){
        System.out.print("\nEnter ID: ");
        int taskID = sc.nextInt();
        
        System.out.println("--------------------------------------------------------------------------------");
        searchID(taskID);
        System.out.print("Confirm delelte? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            sql = "DELETE FROM task WHERE task_id = ?";
            deleteRecord(sql, taskID);
        }
    }
    
    private void searchID(int id){
        try{
            PreparedStatement search = connectDB().prepareStatement("SELECT task_name, t.description, u.first_name, p.project_name, t.status FROM task t INNER JOIN user u ON assigned_to = u.user_id INNER JOIN project p ON t.project_id = p.project_id WHERE task_id = ?;");
            
            search.setInt(1, id);
            ResultSet result = search.executeQuery();
            System.out.println("Selected task: "+result.getString("task_name")
                    + "\nDescription: "+result.getString("description")
                    + "\nAssigned to: "+result.getString("first_name")
                    + "\nFrom project: "+result.getString("project_name")
                    + "\nStatus: "+result.getString("status"));
            result.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
