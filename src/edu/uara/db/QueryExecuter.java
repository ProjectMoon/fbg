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
package edu.uara.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.uara.db.profiles.ProfileManager;

/**
 * 
 * @author Dustin Yourstone
 * Made This because its a pain to keep doing this in the bean shell scripts
 *
 */
public class QueryExecuter {
    /**
     * Executes a query given an array string of variable names, plus a prepared statement query.
     * @param vars
     * @param query
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static String executeQuery(String[] vars, String query) throws SQLException, ClassNotFoundException {
        Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
	PreparedStatement stmt = conn.prepareStatement(query);
        int i = 0;
        for(String var : vars) {
            stmt.setString(i, var);
        }
	ResultSet set = stmt.executeQuery();
	set.next();
	return set.getString(1);
    }
    
    /**
     * Executes a query with no variables.
     * @param query
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static String executeQuery(String query) throws SQLException, ClassNotFoundException {
        Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet set = stmt.executeQuery();
        set.next();
        return set.getString(1);
    }
    
    public static ArrayList<String> getColumnNames(String tableName) throws SQLException, ClassNotFoundException {
    	Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
    	DatabaseMetaData meta = conn.getMetaData();
    	ResultSet rsColumns = meta.getColumns(null, null, tableName, null);
    	ArrayList<String> columnList = new ArrayList<String>();
    	while(rsColumns.next()) {
    		columnList.add(rsColumns.getString("COLUMN_NAME"));
    	}
    	return columnList;
    }
    
    public static int getColumnDataType(String tableName, String columnName) throws SQLException, ClassNotFoundException {
    	Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
    	DatabaseMetaData meta = conn.getMetaData();
    	ResultSet rsColumns = meta.getColumns(null, null, tableName, columnName);
    	rsColumns.next();
    	return rsColumns.getInt("DATA_TYPE");
    }
}
