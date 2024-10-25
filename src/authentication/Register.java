package authentication;

import main.config;

import java.util.*;

public class Register extends config{
    
    String confirmPass;
    
    public void registerCredentials(){
        Scanner sc = new Scanner(System.in);
        System.out.println("================================================================================================================================================================");
        System.out.print("Enter first name: ");
        String fname = sc.nextLine();
        
        System.out.print("Enter middle name (type N/A if none): ");
        String mname = sc.next();
        
        System.out.print("Enter last name: ");
        sc.nextLine();
        String lname = sc.nextLine();
        
        System.out.print("Enter username: ");
        String username = sc.next();
        
        System.out.print("Enter email address: ");
        String email = sc.next();
        
        System.out.print("Enter password: ");
        String password = sc.next();
        
        System.out.print("Confirm password: ");
        confirmPass = sc.next();
        
        while(!confirmPass.equals(password)){
            System.out.print("Passwords don't match, try again: ");
            confirmPass = sc.next();
        }
        
        System.out.print("Confirm registration? [y/n]: ");
        String confirm = sc.next();
        
        if(confirm.equals("y")){
            String sql = "INSERT INTO user (first_name, middle_name, last_name, username, email, password, role) VALUES (?, ?, ?, ?, ?, 'member')";
            
            addRecord(sql,fname, mname, lname, username, email, confirmPass);
        } else{
            System.out.println("Registration Cancelled.");
        }
    }
}
