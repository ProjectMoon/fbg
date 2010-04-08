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

package edu.uara.tables;

import edu.uara.wrappers.CustomJTable;
import edu.uara.wrappers.CustomTableCellRenderer;

/**
 * Used for createing a blank table.
 * This was needed so that Borders from the CustomTableCellRenderer would work.
 * I don't know why it was needed, but it was.
 * @author paul
 */
public class BlankTable {
    
    /**
     * Constructor
     */
    public BlankTable() {}
    
    /**
     * Creates a blank table
     * @param numOfRows The number of rows in the table
     * @param numOfColumns The number of columns in the table
     * @return A CustomJTable with a blank CustomTableCellRenerer as the default renderer
     */
    public CustomJTable gernerateBlankTable(int numOfRows, int numOfColumns) {
        CustomJTable table = new CustomJTable(numOfRows, numOfColumns);
        
        table = formatTable(table, numOfRows, numOfColumns);
        
        return table;
    }
    
    /**
     * Makes the DefaultCellRenderer for table a CustomTableCellRenderer
     * @param table The table the CustomTableCellRenderer will be used on
     * @param numOfRows The number of rows in the table
     * @param numOfColumns The number of columns in the table
     * @return A table containing a blank CustomTableCellRenderer as the default renderer
     */
    private CustomJTable formatTable(CustomJTable table, int numOfRows, int numOfColumns) {
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer(numOfRows, numOfColumns);
        table.setShowGrid(false);        
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        return table;
    }

}
