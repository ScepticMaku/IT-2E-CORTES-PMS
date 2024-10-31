package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class validation extends config {
    Scanner sc = new Scanner(System.in);
    
    public int validateInt() {
        int getNum;
        
        while(true) {
            try {
                    getNum = sc.nextInt();
                    break;
            } catch(InputMismatchException e) {
                System.out.print("Invalid Input: Must only be a number, try again: ");
                sc.next();
            }
        }
        return getNum;
    }
    
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
    
    public boolean dateValidate(String getDate){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            LocalDate.parse(getDate, format);
            return true;
        } catch(DateTimeParseException e){
            System.out.print("Error: Date is incorrect, try again: ");
        }
        return false;
    }
}
