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
package edu.uara.db.structure;

/**
 * The name of this class is somewhat misleading, as it actually stores data about the database
 * tables. It is used by the program initialization system to verify the table structure and
 * create new tables if necessary.
 * @author jeff
 */
import java.util.TreeSet;

public class TableData {
    private String tableName;
    private TreeSet<String> tableColumns;
    
    /**
     * Initialize this TableData object.
     */
    public TableData() {
        tableColumns = new TreeSet<String>(new TableNameComparator());
    }
    
    /**
     * Sets the name of the table.
     * @param name - The name of the table.
     */
    public void setName(String name) {
        tableName = name;
    }
    
    /**
     * Adds a string column to the table. At present these oclumns are translated
     * to length 7 character values in the database.
     * @param name - The name of the column.
     */
    public void addStringColumn(String name) {
        tableColumns.add("::s::" + name);
    }
    
    public void addLongStringColumn(String name) {
        tableColumns.add("::ls::" + name);
    }
    
    /**
     * Adds an integer column to the database.
     * @param name - The name of the column.
     */
    public void addIntColumn(String name) {
        tableColumns.add("::i::" + name);
    }
    
    /**
     * Adds a float column to the database.
     * @param name - The name of the column.
     */
    public void addFloatColumn(String name) {
        tableColumns.add("::f::" + name);
    }
    
    /**
     * Adds a date/time column to the database. This is rendered
     * as the standard datetime SQL data type.
     * @param name - The name of the column.
     */
    public void addDateColumn(String name) {
        tableColumns.add("::d::" + name);
    }
    
    /**
     * This method generates the query necessary for the creation of the table
     * in the database.
     * @return The SQL query for creating the table.
     */
    public String getStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE");
        sb.append(" " + tableName + " ");
        sb.append(" (");
        for (String col : tableColumns) {
            if (col.matches("::i::.+")) {
                String colName = col.substring(5);
                sb.append(colName + " int,");
            }
            else if (col.matches("::s::.+")) {
                String colName = col.substring(5);
                sb.append(colName + " char(7),");
            }
            else if(col.matches("::f::.+")) {
                String colName = col.substring(5);
                sb.append(colName + " float,");
            }
            else if(col.matches("::d::.+")) {
                String colName = col.substring(5);
                sb.append(colName + " datetime,");
            }
            else if (col.matches("::ls::.+")) {
                String colName = col.substring(6);
                sb.append(colName + " char(50),");
            }
        }
        
        //remove the last comma
        sb.deleteCharAt(sb.lastIndexOf(","));
        
        //add the closing )
        sb.append(")");
        return sb.toString();
    }
}
