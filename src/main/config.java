package main;

import java.sql.*;
import password.securePassword;

public class config {
    securePassword passw = new securePassword();
    
    int id;
    
    public Connection connectDB(){
        Connection connect = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:records.db");
        } catch(Exception e){
            System.out.println("Connection Failed: "+e.getMessage());
        }
        return connect;
    }
    
    public void addRecord(String sql, Object... values) {
    try {
        PreparedStatement pstmt = connectDB().prepareStatement(sql); 

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
    
    public void updateProject(){
        
    }
    
    public void deleteProject(){
        
    }
    
    public void viewRecords (String sqlQuery, String[] columnHeaders, String[] columnNames){
        
        // Check that columnHeaders and columnNames arrays are the same length
        if(columnHeaders.length != columnNames.length){
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }
        
        try{
            PreparedStatement state = connectDB().prepareStatement(sqlQuery);
            ResultSet result = state.executeQuery();
            
            // Print the headers dynamically
            StringBuilder headerLine = new StringBuilder();
            System.out.println("\nList of Projects: ");
            headerLine.append("--------------------------------------------------------------------------------\n");
            for(String header : columnHeaders){
                headerLine.append(String.format("%-20s ",header)); // Adjust format as needed
            }
            headerLine.append("\n");    
            
            System.out.println(headerLine.toString());
                    
            // Print the rows dynamically based on the provided column names
            while(result.next()){
                StringBuilder row = new StringBuilder("| ");
                for(String colName : columnNames){
                    String value = result.getString(colName);
                    row.append(String.format("%-20s ", value != null ? value : "")); //Adjust formatting
                }
                System.out.println(row.toString());
            }
            System.out.println("--------------------------------------------------------------------------------");
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public int getUID(String username, String password){
        try{
            PreparedStatement state = connectDB().prepareStatement("SELECT user_id, username, password_hash, role FROM user");
            ResultSet result = state.executeQuery();
            
            if(passw.passwordHashing(password).equals(result.getString("password_hash")) && username.equals(result.getString("username"))){
                id = result.getInt("user_id");
            } else{
                System.out.println("UID not found.\n");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        
        return id;
    }
}
