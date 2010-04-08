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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.uara.FBGConstants;

/**
 * This class stores two pieces of information in serialized fashion: A database name, and its
 * associated driver string. When a user desires to create a new database profile, the program
 * searchs for serialized profile templates in ~/FBG/profiles/templates. They will then be
 * populated into a dropdown menu to autofill information.
 * @author jeff
 */
public class ProfileTemplate implements Serializable {
    public static final long serialVersionUID = 1L;
    private String dbName;
    private String dbURLPrefix;
    private String driverString;
    private String suffix;
    
    public ProfileTemplate(String dbName, String driverString, String dbURLPrefix, String suffix) {
        this.dbName = dbName;
        this.driverString = driverString;
        this.dbURLPrefix = dbURLPrefix;
        this.suffix = suffix;
        System.out.println("Suffix: " + this.suffix);
    }
    
    public String getDatabaseName() {
        return dbName;
    }
    
    public String getDriverString() {
        return driverString;
    }
        
    public String getConnectionURLPrefix() {
        return dbURLPrefix;
    }
    
    public String getURLSuffix() {
        return suffix;
    }
    
    public boolean saveTemplate() {
        try {
            File profileDir = new File(FBGConstants.USER_FILE_PATH +
                    System.getProperty("file.separator") + "profiles" + System.getProperty("file.separator"));

            File dir = new File(FBGConstants.USER_FILE_PATH +
                    System.getProperty("file.separator") + "profiles" + System.getProperty("file.separator") +
                    "templates" + System.getProperty("file.separator"));

            profileDir.mkdir(); //make the top level profiles directory if it's not there.
            dir.mkdir(); //makes the DIR if it's not already there.
            
            // Create stream
            FileOutputStream fos = new FileOutputStream(FBGConstants.USER_FILE_PATH +
                    System.getProperty("file.separator") + "profiles" + System.getProperty("file.separator") +
                    "templates" + System.getProperty("file.separator") + dbName + ".fdt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            // Write objects
            oos.writeObject(this);
            oos.flush();
            oos.close();        
        }
        catch (Exception e) {
            System.err.print("Error creating profile template " + dbName + ".fdt\n");
            return false;
        }
        
        //Everything was good, so return true.
        return true;
    }
    
    public String toString() {
        return getDatabaseName();
    }
    
    public static void makeTemplates() {
        ProfileTemplate pt1 = new ProfileTemplate("SQL Server 2005", "net.sourceforge.jtds.jdbc.Driver",
                "jdbc:jtds:sqlserver://", "");
        System.out.println("Saving SQL Server 2005 template...");
        boolean saved = pt1.saveTemplate();
        if (saved)
            System.out.println("Done.");

        ProfileTemplate pt2 = new ProfileTemplate("SQL Server 2005 (Domain Support)", "net.sourceforge.jtds.jdbc.Driver",
                "jdbc:jtds:sqlserver://", ";domain=SALISBURY");

        System.out.println("Saving domain template...");
        saved = pt2.saveTemplate();
        
        if (saved)
            System.out.println("Done");
    }
}
