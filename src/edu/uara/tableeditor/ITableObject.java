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
package edu.uara.tableeditor;

import javax.swing.JTable;

/**
 *
 * @author Tu Hoang
 */
public interface ITableObject 
{
    /**
     * Adds a figure to this TableObject.
     * @param figure
     */
     void addFigure(TableFigure figure);
     /**
      * get table name
      * @return: name of table
      */
     String getName();
     /**
      * add data cell listener for data sync
      * @param d: object that implements DataCellListener interface
      */
     void addDataCellListener(DataCellListener d);
     /**
      * get data from source table with provided cell reference
      * @param ref: CellReferenceObject to obtain data
      * @return: string value of the referenced cell
      */
     String getDataAtReference(CellReferenceObject ref);
     /**
      * get table that contains data for display
      * @return: JTable
      */
     JTable getTable();
     /**
      * get chart title from table
      * @return
      */
     String getTitle();
     /**
      * get the list of figures this table contains
      * @return
      */
     TableFigure[] getFigures();
}
