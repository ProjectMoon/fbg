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
package edu.uara;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import edu.uara.config.Config;
import edu.uara.db.profiles.ProfileManager;

/**
 * This thread is registered as a shutdown hook so that when the program closes, settings are saved
 * and database connections are closed, all that kind of thing.
 * @author jeff
 */
public class CleanupHook implements Runnable {

    public void run() {
        System.out.println("Saving config.");
        Config.saveProperties();
        
        try {
            System.setOut(new PrintStream(FBG.origOut));
            System.setErr(new PrintStream(FBG.origErr));
            
            //a little bug fix to make sure we don't cause exceptions if the profile
            //was never set up
            Connection conn = null;
            if (ProfileManager.CURRENT_PROFILE != null && ProfileManager.CURRENT_PROFILE.isConnected())
                conn = ProfileManager.CURRENT_PROFILE.getConnection();
            
            if (conn != null && conn.isClosed() == false) {
                //Commit any lingering data and close everything.
                conn.commit();
                conn.close();
                System.out.println("Closing down lingering SQL connection.");
            }
        } 
        catch (SQLException ex) {
            System.err.println("Error trying to close the SQL connection.");
        } 
        catch (ClassNotFoundException ex) {
            System.err.println("Couldn't find the connection class to close. What?");
        }
        
    }

}
