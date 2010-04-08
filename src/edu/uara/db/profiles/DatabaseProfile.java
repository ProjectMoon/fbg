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


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import edu.uara.FBG;
import edu.uara.FBGFlow;
import edu.uara.gui.AuthenticationDialog;
/**
 * This class accesses database information stored in files on the local computer.
 * It may/may not store sensitive info (i.e. passwords), but that will be determined
 * at a later date based on the user's requirements. This class's main purpose
 * is to serialize and deserialize information relating to vendor-specific error
 * codes.
 * @author jeff
 */
public class DatabaseProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<String, Integer> errorCodes;
    private String profileName;
    private String connUrl;
    private String driverString;
    private String username;
    private transient int loginAttempts = 0; //attempts at login--reset by explicit call to login()
    private transient Connection currentConn;
    private transient boolean connected = false;
    
    public static final int MAX_ATTEMPTS = 5;
  
    /**
     * Create a new database profile with the specified name.
     * @param name
     */
    public DatabaseProfile(String name) {
        profileName = name;
        errorCodes = new HashMap<String, Integer>();
    }
    
    /**
     * Gets a database vendor-specific error code.
     * @param ec - The type of error code to get.
     * @return The vendor-sepcific error code, if it exists. -1 otherwise.
     */
    public int getErrorCode(String ec) {
        Integer error = errorCodes.get(ec);
        if (error == null) return -1;
        else return error.intValue();
    }
    
    /**
     * Sets a vendor-specific error code "ec" with the specified "value".
     * @param ec
     * @param value
     */
    public void setErrorCode(String ec, int value) {
        errorCodes.put(ec, value);
    }
    
    /**
     * Sets the database connection URL.
     * @param url
     */
    public void setConnectionURL(String url) {
        connUrl = url;
    }
    
    /**
     * Gets the database connection URL.
     * @return The database URL
     */
    public String getConnectionURL() {
        return connUrl;
    }
    
    /**
     * Gets the name of this profile.
     * @return The profile name.
     */
    public String getProfileName() {
        return profileName;
    }
    
    /**
     * Sets the name of the JDBC driver to use.
     * @param driver - The name of the driver.
     */
    public void setDriverString(String driver) {
        driverString = driver;
    }
    
    /**
     * Sets the database usernamem, if one is required for logon.
     * @param un - The username.
     */
    public void setUsername(String un) {
        username = un;
    }
        
    /**
     * Gets the JDBC driver string.
     * @return The driver string.
     */
    public String getDriverString() {
        return driverString;
    }
    
    /**
     * Gets the database username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns if this profile is currently connected to a server.
     * @return True if the profile is connected to the server, false otherwise.
     */
    public boolean isConnected() {
        return connected;
    }
        
    /**
     * Creates an entirely new connection and returns it. The profile caches this connection
     * for later use; though this method may be called at will to create new connections. The
     * preferred method to get the database connection is getConnection().
     * @param password - The password to the database.
     * @return A new JDBC connection.
     */
    public Connection createNewConnection(String password) throws SQLException, ClassNotFoundException {
        Class.forName(driverString);
        DriverManager.setLoginTimeout(30);
        Connection conn = DriverManager.getConnection(connUrl, username, password);
        conn.setAutoCommit(false);
        currentConn = conn;
        connected = true;
        return conn;
    }
    
    /**
     * This is the preferred method for getting or creating a database connection
     * directly. This method automatically creates a database connection or returns
     * a cached connection, so there aren't too many open connections at the same time.
     * This method can be used to create a connection if you know the password to the
     * database server. If you do not, call the login() method first, followed by
     * the other form of this method that takes no parameters.
     * @param password - The password to the database.
     * @return The JDBC connection.
     * @throws java.sql.SQLException - If there was an error creating the connection.
     * @throws java.lang.ClassNotFoundException - If the JDBC driver could not be found.
     */
    public Connection getConnection(String password) throws SQLException, ClassNotFoundException {
        //if we already have an open connection, return it instead of making a new one
        if (currentConn != null) return currentConn;
        else {
            Connection conn = createNewConnection(password);
            currentConn = conn;
            return conn;
        }
    }

    /**
     * Returns the currently cached connection assuming there is one available.
     * This method assumes you are logged in to the database. If not, it throws
     * an exception stating that you need to be logged in.
     * @return The cached connection.
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (currentConn != null) return currentConn;
        else {
            throw new SQLException("You need to log in to the database with login() first!.");
        }
    }

    /**
     * Logs this profile into the server that it specifies. It creates a graphical
     * interface for logging in and gives the user 5 attempts to input the password
     * correctly. The login may also be canceled by the user, resulting in a false
     * return value.
     * @return true if they were successfully logged in, false if otherwise.
     */
    public boolean login() {
        boolean authed = false;
        loginAttempts = 0;
        
        while (!authed) {
            try {
                if (loginAttempts > MAX_ATTEMPTS) {
                    break;
                }
                
                AuthenticationDialog auth = new AuthenticationDialog(null, this.getUsername(), "", "Attempt " + (loginAttempts + 1) + "/" + MAX_ATTEMPTS);

                //if they do not specify a username (i.e. do not log in)
                //just say they're immediately unauthed.
                if (auth.getUsername() == null || auth.getUsername().equals("")) {
                    break;
                }
                this.setUsername(auth.getUsername());
                String pw = auth.getPassword();
                this.getConnection(pw); //won't throw any error if authenticates properly
                authed = true;
            } catch (SQLException e) {
                System.err.println("Error authenticating profile:\n" + e);
                authed = false;
                loginAttempts++;
            } catch (ClassNotFoundException e) {
                System.err.println("authenticate: Can't find the right driver string");
            }
        }

        if (authed) {
            System.out.println("Successfully authenticated profile " + this);
        }        
        if (!authed)
            connected = false;
        
        return authed;
    }

    /**
     * Returns a friendly display name for this profile, consisting of a user@friendlyURL()
     * format.
     * @return The friendly display name.
     */
    public String toString() {
        return getUsername() + "@" + getFriendlyURL();
    }

    /**
     * This method strips the jdbc:drivername:// off the first part of the
     * database JDBC URL. This makes it easier to use in various places in the
     * program where the user will not care about seeing the full JDBC URL but
     * rather just the URL they are connected to.
     * @return
     */
    public String getFriendlyURL() {
        String url = getConnectionURL();

        //remove everything before the // in the JDBC url so we just get
        //the host name and DB name.
        return url.substring(url.indexOf("//") + 2);
    }
}
