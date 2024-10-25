package authentication;

import main.config;

import java.util.Scanner;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Change_Password extends config {
    Scanner sc = new Scanner(System.in);
    
    boolean isFound = false;
    
    public void changeCredentials(){
        do{
            System.out.print("\nEnter email: ");
            String email = sc.next();

            findEmail(email);
        } while(!isFound);
        
    }
    
    private void findEmail(String getEmail){
        try{
            PreparedStatement findEmail = connectDB().prepareStatement("SELECT email, first_name FROM user WHERE email = ?");
            findEmail.setString(1, getEmail);
            
            try(ResultSet checkEmail = findEmail.executeQuery()){
                if(getEmail.equals(checkEmail.getString("email"))){
                    
                    System.out.println("Username found: "+checkEmail.getString("username"));
                    checkEmail.close();
                    
                    setPassword(getEmail);
                    
                    isFound = true;
                } else{
                    System.out.println("Email not found, try again.");
                    isFound = false;
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    private void setPassword(String userEmail){
        boolean isMatched = false;
        do{
            System.out.print("\nEnter new password: ");
            String newPassword = sc.next();

            System.out.print("Confirm password: ");
            String confirmPass = sc.next();

            if(!confirmPass.equals(newPassword)){
                System.out.print("Passwords don't match, try again: ");
            } else{
                System.out.print("Confirm credentials? [y/n]: ");
                String confirm = sc.next();

                if(confirm.equalsIgnoreCase("y")){
                    String updatePass = "UPDATE user SET password = ? WHERE email = ?";
                    updateRecord(updatePass, confirmPass, userEmail);
                    break;
                } else{
                    System.out.println("Change password cancelled.");
                    break;
                }
            }
        } while(!isMatched);
    }
}
