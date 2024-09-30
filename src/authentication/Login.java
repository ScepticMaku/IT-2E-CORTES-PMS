package authentication;

import main.config;
import java.util.*;
import users.*;

public class Login extends config {
    Scanner sc = new Scanner(System.in);
    Admin admin = new Admin();
    
    public void loginCredentials(){
        
            System.out.println("\nLogin:");
            System.out.print("Enter username: ");
            String user = sc.next();

            System.out.print("Enter password: ");
            String pass = sc.next();
            
            if(user.equals("admin") && pass.equals("1234")){
                admin.displayInterface();
            }
                
    }
}
