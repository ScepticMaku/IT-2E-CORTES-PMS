package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class validation extends config {
    
    public boolean confirm(String input){
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }
    
    public boolean emailValidate(String getEmail){
        String[] domain = {"@gmail.com", "@yahoo.com", "@outlook.com"};
        
        for(String i : domain){
            if(getEmail.equals(i)){
                System.out.print("Error: Email must not be the domain name only, try again: ");
                return true;
            }
            else if(getEmail.contains(i)){
                try{
                    PreparedStatement findEmail = connectDB().prepareStatement("SELECT email FROM user");
                    ResultSet result = findEmail.executeQuery();
                    
                    while(result.next()){
                        String email = result.getString("email");
                        
                        if(getEmail.equals(email)){
                            System.out.print("Error: Email is already registered, try again: ");
                            return true;
                        }
                    }
                    return false;
                } catch(SQLException e){
                    System.out.println("Error: "+e.getMessage());
                }
            }
        }
        System.out.print("Error: Email must have supported domain name (@gmail.com, @yahoo.com, @outlook.com), try again: ");
        return true;
    }
    
    public boolean userValidate(String getUser){
        try{
            PreparedStatement findUser = connectDB().prepareStatement("SELECT username FROM user");
            ResultSet result = findUser.executeQuery();
            
            while(result.next()){
                String user = result.getString("username");
                
                if(getUser.equalsIgnoreCase(user)){
                    System.out.print("Error: Must not have duplicated usernames, try again: ");
                    return true;
                }
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        return false;
    }
    
    public boolean spaceValidate(String getString){
        if(getString.contains(" ")){
            System.out.print("Error: Must not have a space, try again: ");
            return true;
        }
        return false;
    }
}
