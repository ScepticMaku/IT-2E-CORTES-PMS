package authentication;

import main.config;
import java.util.*;
import users.*;
import java.sql.*;
import password.securePassword;

public class Login extends config {
    Scanner sc = new Scanner(System.in);
    Admin admin = new Admin();
    securePassword pass = new securePassword();
    
    public void loginCredentials(){
        
            System.out.println("\nLogin:");
            System.out.print("Enter username: ");
            String user = sc.next();

            System.out.print("Enter password: ");
            String pass = sc.next();
            
            locateUser(user, pass);
    }
    
    public void locateUser(String username, String password){
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT username, password_hash, role FROM user");
            ResultSet result = state.executeQuery();
            
            if(pass.passwordHashing(password).equals(result.getString("password_hash")) && username.equals(result.getString("username"))){
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
}
