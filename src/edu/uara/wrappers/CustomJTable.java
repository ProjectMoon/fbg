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
package edu.uara.wrappers;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Dimension;
import edu.uara.wrappers.multiselect.TableSelectionModel;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

/**
 * This class extends JTable
 * It is used to handle merging cells
 * @author Paul Halvorsen
 */
public class CustomJTable extends JTable implements Serializable {

    public static final long serialVersionUID = 1;
    protected TableSelectionModel tableSelectionModel;
    private VisibleCell visibleCell[][];
    private int selectedRow;

    /**
     * Constructor: sets up a new CustomJTable with the number of rows and columns
     * @param numRows: the number of rows contained in the table
     * @param numColumns: the number of columns contained in the table
     */
    public CustomJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        this.setUI(new CustomBasicTableUI());

        CustomCellEditor editor = new CustomCellEditor();
        for (int c = 0; c < numColumns; c++) {
            this.getColumnModel().getColumn(c).setCellEditor(editor);
        }

        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.setCellSelectionEnabled(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        visibleCell = new VisibleCell[numRows][numColumns];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                visibleCell[r][c] = new VisibleCell(r, c, 1, 1);
            }
        }
        createDefaultTableSelectionModel();
    }

    public CustomJTable(CustomJTable other) {
        super(other.getRowCount(), other.getColumnCount());
        this.setUI(new CustomBasicTableUI());
        
        //We need to completely copy the data, down to the last element in
        //each row, column
        Vector data = new Vector();
        Vector<String> columns = new Vector<String>();
        DefaultTableModel model = (DefaultTableModel) other.getModel();
        Vector origData = model.getDataVector();
        for (int c = 0; c < origData.size(); c++) {
            Vector v = (Vector) origData.get(c);
            Vector v2 = new Vector();
            for (int x = 0; x < v.size(); x++) {
                v2.add(v.get(x));
            }
            data.add(v2);
        }


        for (int c = 0; c < model.getColumnCount(); c++) {
            columns.add(model.getColumnName(c));
        }
        
        DefaultTableModel newModel = (DefaultTableModel) this.getModel();
        newModel.setDataVector(data, columns);
        this.setModel(newModel);
        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.setCellSelectionEnabled(other.getCellSelectionEnabled());
        this.setAutoResizeMode(other.getAutoResizeMode());

        visibleCell = new VisibleCell[other.visibleCell.length][other.visibleCell[0].length];
        for (int c = 0; c < other.visibleCell.length; c++) {
            System.arraycopy(other.visibleCell[c], 0, visibleCell[c], 0, other.visibleCell[c].length);
        }

        CustomCellEditor editor = new CustomCellEditor();
        for (int c = 0; c < other.getColumnCount(); c++) {
            this.getColumnModel().getColumn(c).setCellEditor(editor);
        }
        
        //createDefaultTableSelectionModel();
    }

    /**
     * Overridden from JTable
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     * @param includeSpacing: weather or not to include the spacing between cells
     * @return the Rectangle object containing the dimensions of the specified cell
     */
    @Override
    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
        if (row < 0 || row >= this.getRowCount() || column < 0 || column >= this.getColumnCount()) {
            return super.getCellRect(row, column, includeSpacing);
        }

        int visColumn = getVisibleCell(row, column).columnNum;
        int visRow = getVisibleCell(row, column).rowNum;
        Rectangle rect = super.getCellRect(visRow, visColumn, includeSpacing);

        if (getHorizontalNumMerged(visRow, visColumn) != 1) {
            for (int x = 1; x < getHorizontalNumMerged(visRow, visColumn); x++) {
                rect.width += this.getColumnModel().getColumn(visColumn + x).getWidth();
            }
        }
        if (getVerticalNumMerged(visRow, visColumn) != 1) {
            for (int x = 1; x < getVerticalNumMerged(visRow, visColumn); x++) {
                rect.height += this.getRowHeight(visRow + x);
            }
        }
        return rect;
    }

    /**
     * Overridden from JTable
     * @param p: the pixel point to find out what column it is in
     * @return the visible column number at the point
     */
    @Override
    public int columnAtPoint(Point p) {
        int column = super.columnAtPoint(p);
        int row = super.rowAtPoint(p);
        if (column < 0 || column >= this.getColumnCount() || row < 0 || row >= this.getRowCount()) {
            return -1;
        }
        return getVisibleCell(row, column).columnNum;
    }

    /**
     * Overridden from JTable
     * @param p: the pixel point to find out what row it is in
     * @return the visible row number at the point
     */
    @Override
    public int rowAtPoint(Point p) {
        int column = super.columnAtPoint(p);
        int row = super.rowAtPoint(p);
        if (row < 0 || row >= this.getRowCount() || column < 0 || column >= this.getColumnCount()) {
            return -1;
        }
        return getVisibleCell(row, column).rowNum;
    }

    /**
     * Locates the visible cells column at any particular row or column
     * @param row: the row number for the cell
     * @param column: the column number for the cell
     * @return the visible column number for the cell
     */
    public VisibleCell getVisibleCell(int row, int column) {
        return visibleCell[row][column];
    }

    /**
     * This sets which cell to merge and how many columns it should span
     * @param row: the row number of the cell to be merged
     * @param column: the column number of the cell to be merged
     * @param numToSpan: the number of columns to span (including the one it is currently in)
     * @return 0 if successfull, 1 if the column number is too large or small, 2 if the span goes outside the columns, 3 if the row number is too small or large, 4 if the column has already been merged into another cell
     */
    public Integer mergeHorizontal(int row, int column, int numToSpan) {
        if (0 > column && column >= this.getColumnCount() - 1) {
            return 1;
        } else if (column + numToSpan > this.getColumnCount()) {
            return 2;
        } else if (0 > row && row >= this.getRowCount()) {
            return 3;
        } else if (getVisibleCell(row, column).columnNum != column) {
            return 4;
        }

        for (int i = 0; i < numToSpan; i++) {
            for (int x = 0; x < visibleCell[row][column].numVerSpan; x++) {
                visibleCell[row + x][column + i].columnNum = column;
            }
        }
        visibleCell[row][column].numHorSpan = numToSpan;
        return 0;
    }

    /**
     * unmerges columns
     * @param row: the row number the cell that was merged is in
     * @param column: the column number the cell taht was merged is in
     * @return 0 if the unmerger was successfull, other if not
     */
    public Integer unmergeHorizontal(int row, int column) {
        int numVertMerged = getVerticalNumMerged(row, column);
        int numHorMerged = getHorizontalNumMerged(row, column);

        for (int r = row; r < numVertMerged + row; r++) {
            for (int c = column; c < numHorMerged + column; c++) {
                visibleCell[r][c].columnNum = c;
                if (c != column) {
                    visibleCell[r][c].rowNum = r;
                    visibleCell[r][c].numVerSpan = 1;
                }
            }
            visibleCell[r][column].numHorSpan = 1;
        }

        return 0;
    }

    /**
     * Merge cells vertically
     * @param row: The row number of the first cell
     * @param column: The column number of the first cell
     * @param numToSpan: The number of cells to merge, including this one
     * @return 0 if successfull, 1 if the column number is too large or small, 2 if the span goes outside the rows, 3 if the row number is too small or large, 4 if the row has already been merged into another cell
     */
    public Integer mergeVertical(int row, int column, int numToSpan) {
        if (0 > column && column >= this.getColumnCount() - 1) {
            return 1;
        } else if (row + numToSpan > this.getColumnCount()) {
            return 2;
        } else if (0 > row && row >= this.getRowCount()) {
            return 3;
        } else if (getVisibleCell(row, column).rowNum != row) {
            return 4;
        }

        for (int i = 0; i < numToSpan; i++) {
            for (int x = 0; x < visibleCell[row][column].numHorSpan; x++) {
                visibleCell[row + i][column + x].rowNum = row;
            }
        }
        visibleCell[row][column].numVerSpan = numToSpan;
        return 0;
    }

    /**
     * unmerge rows
     * @param row: the row number of the first cell
     * @param column: the column number of the first cell
     * @return 0 if successfull, other number if it is not
     */
    public Integer unmergeVertical(int row, int column) {
        int numVertMerged = getVerticalNumMerged(row, column);
        int numHorMerged = getHorizontalNumMerged(row, column);

        for (int c = column; c < numHorMerged + column; c++) {
            for (int r = row; r < numVertMerged + row; r++) {
                visibleCell[r][c].rowNum = r;
                if (r != row) {
                    visibleCell[r][c].columnNum = c;
                    visibleCell[r][c].numHorSpan = 1;
                }
            }
            visibleCell[row][c].numVerSpan = 1;
        }
        return 0;
    }

    /**
     * get the number horizontally spanned by a particular cell
     * @param rowNum: row number of the cell
     * @param columnNum: column number of the cell
     * @return the number of columns that the particular cell spans
     */
    public Integer getHorizontalNumMerged(int rowNum, int columnNum) {
        return visibleCell[rowNum][columnNum].numHorSpan;
    }

    /**
     * get the number vertically spaned by a particular cell
     * @param rowNum: row number of the cell
     * @param columnNum: column number of the cell
     * @return the number of rows that the particular cell spans
     */
    public Integer getVerticalNumMerged(int rowNum, int columnNum) {
        return visibleCell[rowNum][columnNum].numVerSpan;
    }

    /**
     * For getting the 2d array of visible cells
     * @return The visible cells
     */
    public VisibleCell[][] getVisibleCell() {
        return this.visibleCell;
    }

    /**
     * For setting the 2d array of visible cells
     * @param vis The visible cells
     */
    public void setVisibleCell(VisibleCell vis[][]) {
        this.visibleCell = vis;
    }

    /**
     * Returns the explicitly-set selected row of this custom JTable. Used to
     * facilitate row size changing.
     * @return The explicitly-set selected row.
     */
    public int getExplicitSelectedRow() {
        return selectedRow;
    }

    /**
     * Sets the explicitly selected row of this custom JTable. This manual behavior
     * is useful for dealing with row resizing so the table doesn't change the 
     * row that the user is resizing.
     * @param row
     */
    public void setExplicitSelectedRow(int row) {
        selectedRow = row;
    }

    /**
     * check if the cell is merged
     * @param row
     * @param col
     * @return true if the cell is merged
     */
    public boolean checkMergedCell(int row, int col) {
        VisibleCell c = getVisibleCell(row, col);
        if (c.numHorSpan == 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Component prepareEditor(TableCellEditor e, int row, int col) {
        CustomCellEditor editor = (CustomCellEditor) e;
        JTextComponent comp = (JTextComponent) super.prepareEditor(e, row, col);

        //comp.setPreferredSize(new Dimension(this.getRowHeight(row), this.getColumnModel().getColumn(col).getWidth()));
        if (editor.shouldClearText()) {
            comp.setText(null);
        }
        return comp;
    }

    /**
     * refers to its TableSelectionModel.
     */
    @Override
    public boolean isCellSelected(int row, int column) {
        return tableSelectionModel.isSelected(row, convertColumnIndexToModel(column));
    }

    /**
     * Creates a default TableSelectionModel.
     */
    public void createDefaultTableSelectionModel() {
        TableSelectionModel tsm = new TableSelectionModel();
        setTableSelectionModel(tsm);
    }

    /**
     * same intention as setSelectionModel(ListSelectionModel newModel)
     */
    public void setTableSelectionModel(TableSelectionModel newModel) {
        //the TableSelectionModel shouldn't be null
        if (newModel == null) {
            throw new IllegalArgumentException("Cannot set a null TableSelectionModel");
        }

        //save the old Model
        TableSelectionModel oldModel = this.tableSelectionModel;
        //set the new Model
        this.tableSelectionModel = newModel;
        //The model needs to know how many columns are there
        newModel.setColumns(getColumnModel().getColumnCount());
        getModel().addTableModelListener(newModel);

        if (oldModel != null) {
            removePropertyChangeListener(oldModel);
        }
        addPropertyChangeListener(newModel);

        firePropertyChange("tableSelectionModel", oldModel, newModel);
    }

    /**
     * @return the current TableSelectionModel.
     */
    public TableSelectionModel getTableSelectionModel() {
        return tableSelectionModel;
    }
}
