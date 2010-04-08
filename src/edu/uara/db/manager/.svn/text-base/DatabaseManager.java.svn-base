/*
* This file is part of the Factbook Generator.
* 
* The Factbook Generator is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* The Factbook Generator is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with The Factbook Generator.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2008, 2009 Bradley Brown, Dustin Yourstone, Jeffrey Hair, Paul Halvorsen, Tu Hoang
*/
package edu.uara.db.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.uara.FBGFlow;
import edu.uara.config.Config;
import edu.uara.db.profiles.ProfileManager;

/**
 * This class provides a programatic interface for managing the database server.
 * This management includes dropping tables, querying the server for file information, etc.
 * @author jeff
 */
public class DatabaseManager {
    private static DatabaseManager dbm;
    
    private DatabaseManager() {
        
    }
    
    public String[] getUploadedFiles() {
        String[] s = { "d" };
        return s;
    }
    
    public boolean dropAllTables() {
        try {
            Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
            System.out.println("Beginning full clear of the database!!");
            
            //AIS
            System.out.println("Dropping AIS table.");
            PreparedStatement stmt = conn.prepareStatement("drop table AIS");
            stmt.executeUpdate();
            
            //DIS
            System.out.println("Dropping DIS table.");
            stmt = conn.prepareStatement("drop table DIS");
            stmt.executeUpdate();
                       
            //EIS
            System.out.println("Dropping EIS table.");
            stmt = conn.prepareStatement("drop table EIS");
            stmt.executeUpdate();
            
            //EDS
            System.out.println("Dropping EDS table.");
            stmt = conn.prepareStatement("drop table EDS");
            stmt.executeUpdate();
            
            //POP
            System.out.println("Dropping POP table.");
            stmt = conn.prepareStatement("drop table POP");
            stmt.executeUpdate();
            
            //Clear files table
            System.out.println("Clearing files table.");
            stmt = conn.prepareStatement("truncate table Files");
            stmt.executeUpdate();            
            
            System.out.println("Committing database clear.");
            conn.commit();
            
            System.out.println("Done.");
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    public boolean deleteServerFile(String tableName, String year) {
        try {
            Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
            System.out.println("Beginning deletion of " + tableName + "(" + year + ")");
            
            //first we need to get the unique ID of the file.
            PreparedStatement stmt = conn.prepareStatement("select * from Files where File_Name=? and File_Year=?");
            stmt.setString(1, tableName);
            stmt.setString(2, year);
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int fileID = rs.getInt(3); //3 corresponds to File_ID
            
            System.out.println("Retrieved ID " + fileID + ", commencing with delete.");
            
            //now we execute a delete with this ID.
            stmt = conn.prepareStatement("delete from " + tableName + " where File_ID=?");
            stmt.setInt(1, fileID);
            stmt.addBatch();
            stmt.executeUpdate();
            
            System.out.println("Removed data from table.");
            
            stmt = conn.prepareStatement("delete from Files where File_ID=?");
            stmt.setInt(1, fileID);
            stmt.executeUpdate();
            
            System.out.println("Removed entry from Files table.");
            
            conn.commit();
            System.out.println("Delete committed and finished.");

        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
        return true;
    }
    
    public static DatabaseManager getInstance() {
        if (dbm == null) dbm = new DatabaseManager();
        return dbm;
    }
    
    public static void main(String[] args) {
        Config.load();
        FBGFlow.setupProfile();
        DatabaseManager dbm = DatabaseManager.getInstance();
        dbm.dropAllTables();
        System.out.println("Tables should be dropped");
    }
}
