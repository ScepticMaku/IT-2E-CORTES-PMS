package authentication;

import main.config;
import java.util.*;
import java.sql.*;

public class Register extends config{
    
    String confirmPass;
    
    public void registerCredentials(){
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter first name: ");
        String fname = sc.nextLine();
        
        System.out.print("Enter middle name (type N/A if none): ");
        String mname = sc.next();
        
        System.out.print("Enter last name: ");
        sc.nextLine();
        String lname = sc.nextLine();
        
        System.out.print("Enter username: ");
        String username = sc.next();
        
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
            try{
            PreparedStatement state = connectDB().prepareStatement("INSERT INTO user (first_name, middle_name, last_name, username, password_hash, role) VALUES (?, ?, ?, ?, ?, 'team tember')");
            
            state.setString(1, fname);
            state.setString(2, mname);
            state.setString(3, lname);
            state.setString(4, username);
            state.setString(5, confirmPass);
            state.executeUpdate();
            
            System.out.println("Successfully Registered.");
            
            } catch(SQLException e){
                System.out.println("Error: "+e.getMessage());
            }
        } else{
            System.out.println("Registration Cancelled.");
        }
    }
}
