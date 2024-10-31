package users;

import main.config;
import main.validation;
import ui.admin.project;
import ui.admin.task;
import ui.admin.users;
import crud.userCRUD;
import ui.admin.team;
import ui.admin.team_members;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Admin extends config {
    
    validation validate = new validation();
    team_members tmm = new team_members();
    project proj = new project();
    userCRUD u = new userCRUD();
    users usr = new users();
    task tl = new task();
    team tm = new team();
    
    boolean isSelected = false;
            
    public void displayInterface(int uid, String user_role, String first_name) throws IOException {
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        
        do{
            System.out.print("\n================================================================================================================================================================");
            System.out.print("\nDate: "+date.toString()
                    + "\n\nMain menu: \n"
                    + "1. Profile\n"
                    + "2. Projects\n"
                    + "3. Tasks\n"
                    + "4. Track Days\n"
                    + "5. Teams\n"
                    + "6. Team Members\n"
                    + "7. Users\n"
                    + "8. Logout\n"
                    + "0. Exit\n"
                    + "Enter selection: ");
            int select = validate.validateInt();
            
            switch(select){
                case 1:
                    u.viewUserInfo(uid);
                    break;
                case 2:
                    proj.projectInterface(uid, name[0]);
                    break;
                case 3:
                    tl.taskListInterface(uid);
                    break;
                case 4:
                    String sql = "SELECT t.task_id, t.task_name, t.due_date, tm.member_name, t.status FROM task t INNER JOIN team_member tm ON t.assigned_to = tm.team_member_id";
                    trackDays(sql);
                    pause();
                    break;
                case 5:
                    tm.teamInterface();
                    break;
                case 6:
                    tmm.memberInterface();
                    break;
                case 7:
                    usr.userInterface();
                    break;
                case 8:
                    isSelected = true;
                    break;
                case 0:
                    exitMessage();
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
    
    private void trackDays(String query){
        try{
            PreparedStatement state = connectDB().prepareStatement(query);
            
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
    }
}
