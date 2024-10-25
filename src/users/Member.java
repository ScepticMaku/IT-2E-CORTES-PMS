package users;

import java.util.Scanner;

public class Member {
    Scanner sc = new Scanner(System.in);
    
    public void displayInterface(int uid, String user_role, String first_name){
        String[] name = first_name.split(" ");
        System.out.printf("\nSuccessfully Logged in as: %s, welcome %s!", user_role, name[0]);
        
        boolean isSelected = false;
        
        do{
            System.out.print("\n================================================================================================================================================================"
                    + "\nMain Menu: "
                    + "\n0. Logout"
                    + "\nEnter selection: ");
            int mainSelect = sc.nextInt();
            
            switch(mainSelect){
                case 0:
                    isSelected = true;
                    break;
                default: System.out.println("Error: Invalid selection.");
            }
        } while(!isSelected);
    }
}
