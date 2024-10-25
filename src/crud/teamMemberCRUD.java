package crud;

import main.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class teamMemberCRUD extends config {
    public void viewTeamMemberFiltered(String Query, int getTeamID){
        try{
            PreparedStatement filter = connectDB().prepareStatement(Query);
            filter.setInt(1, getTeamID);
            
            try (ResultSet checkRow = filter.executeQuery()) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("%-20s %-20s %-20s\n", "ID", "Member", "Team");
                while(checkRow.next()){
                    int memberID = checkRow.getInt("team_member_id");
                    String memberName = checkRow.getString("first_name");
                    String teamName = checkRow.getString("team_name");
                    
                    String[] name = memberName.split(" ");
                    
                    System.out.printf("%-20d %-20s %-20s\n", memberID, name[0], teamName);
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
