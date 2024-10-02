package authentication;

import main.config;
import java.util.*;
import users.*;
import java.sql.*;
import password.securePassword;

public class Login extends config {
    Scanner sc = new Scanner(System.in);
    Admin admin = new Admin();
    securePassword passw = new securePassword();
    
    String user;
    String pass;
    
    public void loginCredentials(){
        
            System.out.println("\nLogin:");
            System.out.print("Enter username: ");
            user = sc.next();

            System.out.print("Enter password: ");
            pass = sc.next();
            
            locateUser(user, pass);
    }
    
    public void locateUser(String username, String password){
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT username, password_hash, role FROM user");
            ResultSet result = state.executeQuery();
            
            if(passw.passwordHashing(password).equals(result.getString("password_hash")) && username.equals(result.getString("username"))){
                if(result.getString("role").equals("admin")){
                    
                    
                    admin.displayInterface();
                }
            } else{
                System.out.println("Invalid Credentials.\n");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void setUser(String newUser){
        this.user = newUser;
    }
    
    public void setPass(String getPass){
        
    }
    
    public String getUser(){
        return this.user;
    }
    
    public String getPass(){
        return this.pass;
    }
}
