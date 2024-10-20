package authentication;

import java.io.IOException;
import main.config;
import java.util.*;
import users.*;
import java.sql.*;

public class Login extends config {
    Scanner sc = new Scanner(System.in);
    Admin admin = new Admin();
    Member member = new Member();
    
    int id;
    String user, pass, role, fname;
    String locatedUser, locatedPass, locatedRole;
    boolean userDetected = false;
    
    public void loginCredentials() throws IOException{
        
            System.out.println("\nLogin:");
            System.out.print("Enter username: ");
            user = sc.next();

            System.out.print("Enter password: ");
            pass = sc.next();
            locateUser(user, pass);
    }
    
    public void locateUser(String username, String password) throws IOException{
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT user_id, first_name, username, password, role FROM user");
            ResultSet result = state.executeQuery();
            
            while(result.next()){
                locatedUser = result.getString("username");
                locatedPass = result.getString("password");
                locatedRole = result.getString("role");
                fname = result.getString("first_name");
                id = result.getInt("user_id");
                
                if(username.equals(locatedUser) && password.equals(locatedPass)){
                    userDetected = true;
                    break;
                }
            }
            result.close();
            
            if(userDetected){
                if(locatedRole.equals("admin")){
                    
                    role = "admin";
                    admin.displayInterface(id, role, fname);
                } else if(locatedRole.equals("member")){
                    role = "Team Member";
                    member.displayInterface(id, role, fname);
                }
            } else{
                System.out.println("\nInvalid Credentials.\n");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
