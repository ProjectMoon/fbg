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
package edu.uara.db.profiles;

/**
 * This class creates a new database profile by purposely generating a
 * multitude of SQL errors and recording their error code output.
 * It requires a valid URL, driver, username, and password for the database
 * or else it will be unable to generate the profile. The class does violate
 * programming practices by using try-catch to control program flow, but oh well.
 * @author jeff
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import edu.uara.exceptions.ProfileGenerationException;

public class ProfileCreator {
    private String driverString;
    private String dbUrl;
    private String username;
    private String password;
    
    /**
     * Creates a ProfileCreator with the specified information.
     * @param driverString
     * @param dbUrl
     * @param username
     * @param password
     */
    public ProfileCreator(String driverString, String dbUrl, String username, String password) {
        this.driverString = driverString;
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
    }
    
    /**
     * Creates several database profile "probes" that purposely generate errors
     * to record the vendor-specific error codes. This makes the program somewhat
     * database-agnostic.
     * @param profileName - The name of the profile to generate.
     * @return The completed profile.
     * @throws edu.uara.exceptions.ProfileGenerationException
     */
    public DatabaseProfile generateProfile(String profileName) throws ProfileGenerationException, SQLException, ClassNotFoundException {
        DatabaseProfile dbp = new DatabaseProfile(profileName);
        //try {
            Class.forName(driverString);
            DriverManager.setLoginTimeout(30);

            Connection conn = DriverManager.getConnection(dbUrl, username, password);

            System.out.println("Generating table already exists error code...");
            dbp.setErrorCode("TABLE_EXISTS", getTableExists(conn));
            System.out.println("Generating table doesn't exist error code...");
            dbp.setErrorCode("TABLE_NO_EXIST", getTableNoExist(conn));
            System.out.println("Generating column doesn't exist error code...");
            dbp.setErrorCode("COLUMN_NO_EXIST", getColumnNoExist(conn));
            dbp.setConnectionURL(dbUrl);
            dbp.setDriverString(driverString);
            dbp.setUsername(username);
            
            //Close our temporary conn
            conn.close();
            
            //sets isConnected() to true
            dbp.getConnection(password);
            return dbp;
        //}
        //catch(ClassNotFoundException e) { throw new ProfileGenerationException("Unable to create DB profile. Invalid driver string passed!"); }
        //catch(SQLException e) {
            //JOptionPane.showMessageDialog(new JFrame(), "Unable to create DB profile.\n\n" + e, "Profile Generation Error", JOptionPane.ERROR_MESSAGE);
            //return null;
            //throw new ProfileGenerationException("Unable to create DB profile. Invalid database URL, username, or password!");
        //}
    }

    /**
     * Purposefully tries to select data from a table that doesn't exist.
     * @param conn
     * @return The error code.
     */
    private int getTableNoExist(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("select * from dbprobevjvjewruvbdsjbsdkdfsbj");
        }
        catch (SQLException e) {
            return e.getErrorCode();
        }
        
        //no error code found. what kind of table names do we have here?
        return -1;
    }
    
    /**
     * Purposefully tries to create a table that already exists.
     * @param conn
     * @return The error code.
     */
    private int getTableExists(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE dbprofileprobe (probe char(50))");
            stmt.executeUpdate("CREATE TABLE dbprofileprobe (probe char(50))");
        }
        catch (SQLException e) {
            return e.getErrorCode();
        }
        
        //no error code was found somehow... return -1
        return -1;        
    }
    
    /**
     * Purposefully tries to get data from a column that doesn't exist.
     * @param conn
     * @return The error code.
     */
    private int getColumnNoExist(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("SELECT failcolumnxzvewfasdfo FROM dbprofileprobe");
        }
        catch(SQLException e) {
            return e.getErrorCode();
        }
        
        //what kind of column names are we using?
        return -1;
    }

}
