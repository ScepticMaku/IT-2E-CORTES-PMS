package users;

import main.config;
import crud.userCRUD;
import crud.taskCRUD;
import crud.teamMemberCRUD;

import java.io.IOException;
import java.util.Scanner;

public class Member extends config {
    Scanner sc = new Scanner(System.in);
    
    userCRUD u = new userCRUD();
    taskCRUD tsk = new taskCRUD();
    teamMemberCRUD tm = new teamMemberCRUD();
    
    String sql;
    
    public void displayInterface(int uid, String user_role, String first_name) throws IOException{
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        
        boolean isSelected = false;
        
        do{
            System.out.print("\n================================================================================================================================================================"
                    + "\nDate: "+date.toString()
                    + "\n\nMain Menu:"
                    + "\n1. Profile"
                    + "\n2. Tasks"
                    + "\n3. Teams"
                    + "\n4. Logout"
                    + "\n0. Exit"
                    + "\nEnter selection: ");
            int mainSelect = sc.nextInt();
            
            switch(mainSelect){
                case 1:
                    u.viewProfile(uid);
                    break;
                case 2:
                    System.out.println("\nAssigned tasks:");
                    sql = "SELECT t.task_id, t.task_name, t.due_date, tm.member_name, p.project_name, t.status FROM task t INNER JOIN team_member tm ON t.assigned_to = tm.team_member_id INNER JOIN project p ON t.project_id = p.project_id WHERE tm.member_name = ?";
                    tsk.viewTaskFiltered(first_name, sql);
                    pause();
                    break;
                case 3:
                    System.out.println("\nAssigned teams:");
                    System.out.println("--------------------------------------------------------------------------------");
                    sql = "SELECT t.team_id, t.team_name, tm.member_name FROM team_member tm INNER JOIN team t ON tm.team_id = t.team_id WHERE tm.member_name = ?";
                    tm.viewAssignedTeams(first_name, sql);
                    pause();
                    break;
                /*case 4:
                    sql = "SELECT t.task_id, t.task_name, t.due_date, tm.member_name, t.status FROM task t INNER JOIN team_member tm ON t.assigned_to = tm.team_member_id WHERE tm.member_name = ?";
                    trackDays(name[0], sql);
                    pause();
                    break;*/
                case 4:
                    isSelected = true;
                    break;
                case 0:
                    exitMessage();
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    /*private void trackDays(String name, String query){
        try{
            PreparedStatement state = connectDB().prepareStatement(query);
            state.setString(1, name);
            
            try (ResultSet checkRow = state.executeQuery()) {
                System.out.println("\nList of Tasks:");
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "ID", "Name", "Due Date", "Status", "Remaining days");
                while(checkRow.next()){
                    int t_id = checkRow.getInt("task_id");
                    String t_name = checkRow.getString("task_name");
                    String t_status = checkRow.getString("status");
                    String d_date = checkRow.getString("due_date");
                    
                    long remain = ChronoUnit.DAYS.between(date, LocalDate.parse(d_date));
                    
                    String remDays = String.valueOf(remain);
                    
                    if(Integer.parseInt(remDays) < 0){
                        remDays = "0";
                    }
                    
                    System.out.printf("%-20d %-20s %-20s %-20s %-20s\n", t_id, t_name, d_date, t_status, remDays);
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }*/
}
