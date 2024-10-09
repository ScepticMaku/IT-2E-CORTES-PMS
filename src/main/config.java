package main;

import java.sql.*;

public class config {
    
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
        Connection conn = this.connectDB(); // Use the connectDB method
         PreparedStatement pstmt = conn.prepareStatement(sql); 

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
    
    //-----------------------------------------------
    // UPDATE METHOD
    //-----------------------------------------------

    public void updateRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB(); // Use the connectDB method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }
    
    // Add this method in the config class
    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
    
    /*public void deleteRecord(String sqlQuery, String getID, String getUpdate, String sequenceQuery, int pid, String columnName){
        try{
            PreparedStatement delete = connectDB().prepareStatement(sqlQuery);
            PreparedStatement update = connectDB().prepareStatement(getUpdate);
            PreparedStatement fetch = connectDB().prepareStatement(getID);
            PreparedStatement sequence = connectDB().prepareStatement(sequenceQuery);
            
            // Deletes the row from the selected id
            delete.setInt(1, pid);
            delete.executeUpdate();
            
            // Sorts the table after deletion
            int newID = 1;
            
            ResultSet getMax = fetch.executeQuery();
            while(getMax.next()){
                int currID = getMax.getInt(columnName);
                getMax.close();
                
                if(currID != newID){
                    update.setInt(1, newID);
                    update.setInt(2, currID); 
                    update.executeUpdate();
                }
                newID++;
            }

            // Updates the auto increment sequence (from the primary key)
            sequence.executeUpdate();
            
            System.out.println("Deletion successful.");
        } catch (SQLException e){
            System.out.print("Error: "+e.getMessage());
        }
    }*/
    
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
            System.out.println("List of Projects: ");
            headerLine.append("--------------------------------------------------------------------------------\n");
            for(String header : columnHeaders){
                headerLine.append(String.format("%-20s ",header)); // Adjust format as needed
            }
            headerLine.append("\n");    
            
            System.out.print(headerLine.toString());
                    
            // Print the rows dynamically based on the provided column names
            while(result.next()){
                StringBuilder row = new StringBuilder();
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
}
