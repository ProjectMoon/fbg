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
package edu.uara.cutnpaste;

import edu.uara.tableeditor.query.QueryBuilder;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Stores a selection of table data. This Transferable object supports both String
 * and QueryBuilder data flavors. It supports the QueryBuilder flavor so that when
 * you copy and paste it copies the actual query builder with it, not just the query.
 * @author jh44695
 */
public class TableSelection implements Transferable {
    public static final DataFlavor queryFlavor = new DataFlavor(QueryBuilder.class, "Query Builder");

    private QueryBuilderTuple[] queries;
    private String tableSelection;

    public TableSelection(String contents, QueryBuilder ... queries) {
        
    }

    public TableSelection(String contents) {

    }
    
    public TableSelection(String contents, QueryBuilderTuple ... queries) {
        tableSelection = contents;
        this.queries = queries;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.stringFlavor, queryFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor df) {
        if ((df.equals(DataFlavor.stringFlavor) || df.equals(queryFlavor)))
            return true;
        else
            return false;
    }

    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        if (!(df.equals(DataFlavor.stringFlavor)) && !(df.equals(queryFlavor)))
            throw new UnsupportedFlavorException(df);
        
        else {
            if (df.equals(DataFlavor.stringFlavor))
                return tableSelection;
            else if (df.equals(queryFlavor))
                return queries;
        }
        
        return null;
    }

}
