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

package edu.uara.wrappers.customcharts;

import edu.uara.tableeditor.CellReferenceObject;
import edu.uara.tableeditor.ITableObject;
import edu.uara.tableeditor.TableObject;
import edu.uara.wrappers.CustomJTable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.jfree.util.TableOrder;

/**
 * A class to represent a chart dataset
 * @author Tu Hoang
 */
public class CustomDatasetTable extends CustomJTable implements Serializable 
{
    public static final int serialVersionUID = 1;
    private CellReferenceObject[] selectedColumnLabels = null;
    private String[] selectedRowLabels = null;
        String [][] contents = null;
    private CellReferenceObject[][] cellReferences = null;
    private ArrayList<String> groupKey = new ArrayList<String>();
    private TableOrder tableOrder = TableOrder.BY_ROW; 
    private ArrayList<ColumnGroup> columnGroup = new ArrayList<ColumnGroup>();
    
    //Initialize customDataset table
    public CustomDatasetTable(CellReferenceObject [][] tableContents, String[] rowLabels,
                              CellReferenceObject[] columnLabels)
    {
        super(30, 30);
        
        this.selectedColumnLabels = columnLabels;
        this.selectedRowLabels = rowLabels;
        
        this.initializeDataset(tableContents);
        this.cellReferences = tableContents;
        
    }

    public void setTableOrder(TableOrder tableOrder) {
        this.tableOrder = tableOrder;
    }
    
    /**
     * insert a new column above the current column
     */
    protected void insertRow()
    {
        try
        {
            int row = this.getSelectedRow();
            //shift data
            for(int r = this.getRowCount()-1; r >= row ; r++)
            {
                for(int c = 0; c < this.getColumnCount(); c++)
                {
                    this.setValueAt(getValueAt(r,c), row+1, c);
                }
            }
            //clear contents of current row
            for(int c = 0; c < this.getColumnCount(); c++)
                this.setValueAt("", row, c);
        }
        catch(Exception ex)
        {
            System.out.println("Error inserting new row: " + 
                                ex.getMessage());
        }
    }
    /**
     * insert a new column to the left of current column (all data is shifted left)
     */
    protected void insertColumn()
    {
        try
        {
            int column = this.getSelectedRow();
            //shift data
            for(int c = this.getColumnCount()-1; c >= column; c++)
            {
                for(int r = 0; r < this.getRowCount(); r++)
                {
                    this.setValueAt(getValueAt(r, c), r, c+1);
                }
            }
            //clear contents of current column
            for(int r = 0; r < this.getRowCount(); r++)
                this.setValueAt("", r, column);
        }
        catch(Exception ex)
        {
            System.out.println("Error inserting new row: " + 
                                ex.getMessage());
        }
    }
    /**
     * Copy contents for dataset
     * @param tableContents: 2D array contain data for dataset
     */
    private void initializeDataset(CellReferenceObject [][] tableContents)
    {
        this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
	this.setCellSelectionEnabled(true);
        contents = new String[tableContents.length][tableContents[0].length];
        setupHeadings();
        try
        {
            for (int r = 0; r < tableContents.length; r++) 
            {            
                for (int c = 0; c < tableContents[r].length; c++) 
                {
                    this.setValueAt(tableContents[r][c],r+1, c+1);
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error setting table references to dataset: "+
                                ex.getMessage());
        }
        
    }
    
    private void setupHeadings()
    {
        try
        {
            for(int r = 1; r < this.selectedRowLabels.length+1; r++)
            {
                setValueAt(this.selectedRowLabels[r-1],r, 0);
            }
            for(int c = 1; c < this.selectedColumnLabels.length+1; c++)
            {
                setValueAt(this.selectedColumnLabels[c-1], 0, c );
            }
        }
        catch(Exception ex)
        {
            System.out.print("Error setting up datasource headings! " + ex.getMessage());
        }
    }
    /**
     * get latest values of the cell contents before returning them.
     */
    private void updateCellReferences()
    {
        int countRows;
                      
         //countRows row label
        for(countRows = 0; countRows < this.getRowCount(); countRows++)
        {
            if(getValueAt(countRows+1, 0) == null)
                break;
        }
        int countColumns;
                      
         //countRows row label
        for(countColumns = 0; countColumns < this.getColumnCount(); countColumns++)
        {
            if(getValueAt(0, countColumns+1) == null)
                break;
        }
        cellReferences = new CellReferenceObject[countRows][countColumns];
        for(int r = 0; r < countRows; r++)
        {
            for(int c = 0; c < countColumns; c++)
            {
                this.cellReferences[r][c] = 
                        (CellReferenceObject)getValueAt(r+1, c+1);
            }
        }
        
    }
    public CellReferenceObject[][] getCellReferences() 
    {
        this.updateCellReferences();
        return cellReferences;
    }
    private void updateRowLabels()
    {
        int count;
                      
         //countRows row label
        for(count = 0; count < this.getRowCount(); count++)
        {
            if(getValueAt(count+1, 0) == null)
                break;
        }
        selectedRowLabels = new String[count];
        for(int i = 1; i <= count; i++)
        {
            selectedRowLabels[i-1] = getValueAt(i, 0).toString();
        }
    }
    
    public String[] getRowLabels()
    {
        this.updateRowLabels();
        return this.selectedRowLabels;
    }
    public void updateColumnLabels()
    {
        int count;
                      
         //countRows row label
        for(count = 0; count < this.getColumnCount(); count++)
        {
            if(getValueAt(0, count+1) == null)
                break;
        }
        this.selectedColumnLabels = new CellReferenceObject[count];
        for(int i = 1; i <= count; i++)
        {
            try
            {
                this.selectedColumnLabels[i-1] = 
                        (CellReferenceObject)getValueAt(0, i);
            }
            catch(Exception ex)
            {
                System.out.println("Error getting column labels for dataset. "
                        +ex.getMessage());
            }
        }
    }
    /**
     * get column label references
     * @return an array of CellReferences
     */
    public CellReferenceObject[] getColumnLabels()
    {
        this.updateColumnLabels();
        return this.selectedColumnLabels;
    }
    /**
     * get column labels strings
     * @param t: table source
     * @return
     */
    public String[] getColumnLabels(ITableObject t)
    {
        this.updateColumnLabels();
        String[] values = new String[selectedColumnLabels.length];
        for(int i = 0; i < values.length; i++)
        {
            CellReferenceObject ref = selectedColumnLabels[i];
            if(tableOrder == TableOrder.BY_COLUMN)
            {
                String temp = t.getDataAtReference(ref);
                for(ColumnGroup g : this.columnGroup)
                {
                    for(String s : g.columnNames)
                        if(s.equals(temp))
                        {
                            temp = g.groupName + " " + temp;
                            break;
                        }
                }
                values[i] = temp;
            }
            else
                values[i] = t.getDataAtReference(ref);
        }
        return values;
    }
    
    
    public String[] getGroupKey() {
        String[] a = new String[groupKey.size()];
        return groupKey.toArray(a);
    }
    
    public void setGroupKey(ArrayList<String> groupKey) {
        this.groupKey = groupKey;
    }
    /**
     * add group key for dataset which is used to generate groupedStackedCategory
     * dataset (used in generateGroupMap)
     * @param key
     */
    public void addGroupKey(String key)
    {
        this.groupKey.add(key);
    }
    public void addColGroupKey(String key, String[] cols)
    {
        this.groupKey.add(key);
        this.columnGroup.add(new ColumnGroup(key, cols));
    }
    /**
     * remove key after row series is deleted
     */
    public void validateGroupkey()
    {   
        //countRows row label
        int count;
        for(count = 0; count < this.getRowCount(); count++)
        {
            if(getValueAt(count+1, 0) == null)
                break;
        }
        
        for(int i = 0; i < groupKey.size(); i++)
        {
            String key = groupKey.get(i);
            boolean removed = true;
            for(int r = 0; r < count; r++ )
            {
                String rowLabel;
                try
                {
                    rowLabel = getValueAt(r+1, 0).toString();
                    if(rowLabel.contains(key))
                    {
                        removed = false;
                    }
                }
                catch(Exception ex)
                {
                    
                }
                
            }
            if(removed)//no rows contains this group key so remove
                groupKey.remove(i);
        }
        
    }

    public TableOrder getTableOrder() {
        return tableOrder;
    }
 
    /**
     * return dataset view
     * @param t
     * @return
     */
    public CustomJTable getDataView(ITableObject t, final JPopupMenu m)
    {
        final CustomJTable tableView = new CustomJTable(this.getRowCount(), 
                                                  this.getColumnCount());
        for(int r = 0; r < getRowCount(); r++)
        {
            for(int c = 0; c < getColumnCount(); c++)
            {
                String content = "";
                Object ref = getValueAt(r,c);
                if(ref instanceof CellReferenceObject)
                {
                    if(tableOrder == TableOrder.BY_COLUMN && r == 0)
                    {
                        String temp = t.getDataAtReference((CellReferenceObject)ref);
                        for(ColumnGroup g : this.columnGroup)
                        {
                            for(String s : g.columnNames)
                                if(s.equals(temp))
                                {
                                    temp = g.groupName + " " + temp;
                                    break;
                                }
                        }
                        content = temp;
                    }
                    else
                        content = t.getDataAtReference((CellReferenceObject)ref);
                }
                else
                {
                    try
                    {
                        content = getValueAt(r,c).toString();
                    }
                    catch(Exception ex)//empty cell
                    {
                        content = "";
                    }
                }
                tableView.setValueAt(content, r, c);
            }
        }
        
//        tableView.addKeyListener(new KeyListener(){
//            @Override
//            public void keyPressed(KeyEvent e) 
//            {
//                Object s = e.getSource();
//                if(s instanceof CustomJTable)
//                {
//                    CustomJTable source = (CustomJTable)s;
//                    if(source.getSelectedColumn() == 0 
//                            && source.getSelectedRow() == 0)
//                        return;
//                    if(source.getSelectedColumn() > 0 
//                            && source.getSelectedRow() > 0)
//                        return;
//                    if(e.getKeyCode()==KeyEvent.VK_ENTER
//                            || e.getKeyCode()==KeyEvent.VK_TAB)
//                    {
//                            
//                        syncLabels(source);
//                    }
//                }
//                    
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                    // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//                    // TODO Auto-generated method stub
//
//            }
//        });
        
        tableView.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(final MouseEvent e)
            {
                if(e.getButton() == MouseEvent.BUTTON3)
                {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if(m != null)
                                m.show(tableView, e.getX(), e.getY());
                        }
                    });
                }
            }
        });
        
        return tableView;
    }
    /**
     * Sync labels when dataview table is edited (only sync labels) 
     */
     
    public void syncLabels(CustomJTable t)
    {
        int c = t.getSelectedColumn();
        int r = t.getSelectedRow();
        String backup = this.getValueAt(r, c).toString();
        try
        {
            c = t.getSelectedColumn();
            r = t.getSelectedRow();
            this.setValueAt(t.getValueAt(r, c), r, c);
        }
        catch(NullPointerException ex)
        {
            this.setValueAt(backup, r, c);
        }
        
    }
    
    
    /**
     * get 2D array of data set values
     * @param t: table source instance
     * @return 2D array of data
     */
    public double[][] getTableContentAsValue(ITableObject t)
    {
        try
        {
            this.updateCellReferences();
            int rows = this.cellReferences.length;
            int columns = cellReferences[0].length;
            double[][] values = new double[rows][columns];
            for(int r = 0; r < rows; r++)
            {
                for(int c = 0; c < columns; c++)
                {
                    try
                    {
                        String value = t.getDataAtReference(cellReferences[r][c]);
                        values[r][c] = (double)Double.parseDouble(value);
                    }
                    catch(Exception ex)
                    {
                        values[r][c] = 0.0;
                    }
                }
            }
        
            return values;
        }
        catch(Exception ex)
        {
            System.out.println("Table values error: "+ex.getMessage());
            return null;
        }
        
    }
    /**
     * transpose dataset--get data by column
     * row label becomes column label and likewise for column label
     * @param t
     * @return array of double values
     */
    public double[][] getTableContentAsValueTranspose(ITableObject t)
    {
        try
        {
            this.updateCellReferences();
            int rows = this.cellReferences.length;
            int columns = cellReferences[0].length;
            double[][] values = new double[columns][rows];
            for(int c = 0; c < columns; c++)
            {
                for(int r = 0; r < rows; r++)
                {
                    try
                    {
                        String value = t.getDataAtReference(cellReferences[r][c]);
                        values[c][r] = (double)Double.parseDouble(value);
                    }
                    catch(Exception ex)
                    {
                        values[r][c] = 0.0;
                    }
                }
            }
        
            return values;
        }
        catch(Exception ex)
        {
            System.out.println("DatasetTable values error: "+ex.getMessage());
            return null;
        }
        
    }
    /**
     * generate datasource for ChartGenerationFrame
     * @return a dataset (CustomDatasetTable)
     */
    public static CustomDatasetTable generateDataSource(TableObject tableObj)
    {
        try {
            CustomJTable t = tableObj.table;
            String[] rowLabels;
            CellReferenceObject[] columnLabels;
            int labelRow = 0;//index of the row that contains columns label
            int[] selectedColumns = t.getSelectedColumns();
            if (selectedColumns.length == 0) {
                throw new Exception("No data Cell selected!");
            }
            int[] selectedRows = t.getSelectedRows();
            CellReferenceObject[][] cellReferences =
                    new CellReferenceObject[selectedRows.length]
                                           [selectedColumns.length];
            
            rowLabels = new String[t.getSelectedRowCount()];
            columnLabels = new CellReferenceObject[t.getSelectedColumnCount()];
            
            for (int r = 0; r < selectedRows.length; r++) 
            {
                //get row labels
                if(!t.checkMergedCell(selectedRows[r], 0))
                    rowLabels[r] = t.getValueAt(selectedRows[r], 0).toString();
                else
                {
                    rowLabels[r] = String.format("{0}", r);
                }
                for (int c = 0; c < selectedColumns.length; c++) 
                {
                    try 
                    {
                                             
                        cellReferences[r][c] = new CellReferenceObject(
                                                    selectedRows[r], 
                                                    selectedColumns[c]);
                    } catch (Exception ex) {
                        
                    }
                    //get column labels, do only once
                    
                    if(c == 0 && r == 0)
                    {
                        //check for row labels
                        //basically search for the first row that doesn't have 
                        //merged cells
                        int k = 0;
                        do
                        {
                            if(t.checkMergedCell(labelRow, selectedColumns[k]))
                            {
                                k = 0;//reset column count
                                labelRow++;
                            }
                            else
                                k++;
                            
                        }while(k < selectedColumns.length);
                    }
                    if(r == 0)
                    { 
                        columnLabels[c]  = new CellReferenceObject(labelRow,
                                    selectedColumns[c]);
                                            
                    }
                }
            }
            
            CustomDatasetTable datasetView = new CustomDatasetTable(
                                cellReferences, rowLabels, columnLabels);
            return datasetView;
        } catch (Exception ex) {
            System.out.println("error generating dataset"+ex.getMessage());
            return null;
        }
    }
    
    public class ColumnGroup
    {
        public String groupName = null;
        public String[] columnNames = null;
        public ColumnGroup(String groupName, String[] columnNames)
        {
            this.groupName = groupName;
            this.columnNames = columnNames;
        }
        
    }
}
