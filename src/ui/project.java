package ui;

import main.config;
import java.util.*;


public class project extends config {
    Scanner sc = new Scanner(System.in);
    
    String name, desc, sql, status;
    boolean goBack = true;
    int select, choice, id;
    
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
                            case 3:
                                String projectQuery = "SELECT * FROM project";
                                String[] projectHeaders = {"ID", "Name", "Description", "Status"};
                                String[] projectColumns = {"project_id", "project_name", "description", "status"};
                                
                                viewRecords(projectQuery, projectHeaders, projectColumns);
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
}
