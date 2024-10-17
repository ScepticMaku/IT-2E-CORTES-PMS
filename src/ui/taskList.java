package ui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.config;

public class taskList extends config{
    boolean isSelected = false;
    
    public void taskListInterface(){
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
                    viewTaskList(pid, sqlQuery);
                }
                checkTable.close();
            } catch(SQLException e){
                System.out.println("Error: "+e.getMessage());
            }
            
            System.out.print("\n1. Add Task"
                    + "\n2. Edit Task"
                    + "\n3. Delete Task"
                    + "\n4. Back"
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
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error: Invalid selection.");
            }
        } while(isSelected);
    }
    
    public void viewTaskList(int id, String taskQuery){
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
}
