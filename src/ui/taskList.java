package ui;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.config;
import java.util.Scanner;

public class taskList extends config{
    Scanner sc = new Scanner(System.in);
    projectTask pj = new projectTask();
    
    boolean isSelected = false;
    
    public void taskListInterface() throws IOException{
        do{
            System.out.println("--------------------------------------------------------------------------------");
            String sqlQuery = "SELECT t.task_id, t.task_name, t.description, u.first_name, t.status FROM task t INNER JOIN user u ON t.assigned_to = u.user_id";
            try{
                PreparedStatement findRow = connectDB().prepareStatement(sqlQuery);
                ResultSet checkTable = findRow.executeQuery();

                if(!checkTable.next()){
                    System.out.println("Task Empty.");
                } else{
                    System.out.println("List of Tasks: ");
                    System.out.println("--------------------------------------------------------------------------------");
                    viewTaskList(sqlQuery);
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
            int choice = sc.nextInt();

            switch(choice){
                case 1:
                    addTask();
                    break;
                case 2:
                    pj.editTask();
                    break;
                case 3:
                    pj.deleteTask();
                    break;
                case 4:
                    pj.viewInfo();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    public void viewTaskList(String taskQuery){
        try{
            PreparedStatement findRow = connectDB().prepareStatement(taskQuery);
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
    
    private void addTask(){
        System.out.println("--------------------------------------------------------------------------------");
        System.out.print("Enter task name: ");
                    sc.nextLine();
                    String Tname = sc.nextLine();

                    System.out.print("Enter task description: ");
                    String desc = sc.nextLine();
                    
                    System.out.print("Enter user ID to assign: ");
                    int userID = sc.nextInt();
                    
                    System.out.print("Enter project ID to assign: ");
                    int pid = sc.nextInt();

                    int tid = pid;
                    String status = "Not Started";
                    String sql = "INSERT INTO task (task_name, description, assigned_to, project_id, status) VALUES (?, ?, ?, ?, ?)";

                    addRecord(sql, Tname, desc, userID, tid, status);
    }
}
