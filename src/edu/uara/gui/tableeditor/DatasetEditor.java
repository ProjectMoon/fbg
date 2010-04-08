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

package edu.uara.gui.tableeditor;

import edu.uara.tableeditor.CellReferenceObject;
import edu.uara.tableeditor.ITableObject;
import edu.uara.tableeditor.TableObject;
import edu.uara.wrappers.CustomJTable;
import edu.uara.wrappers.customcharts.CustomDatasetTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author  Tu
 */
public class DatasetEditor extends javax.swing.JFrame 
{
    
    
    private JTable t = null;
    private CustomDatasetTable dataset = null;
    private CustomJTable td;
    private IDataRefreshListener refreshNotifyObj = null;
    private int[] selectedRows = null, selectedColumns = null;
    private String[] rowLabels = null;
    
    private CellReferenceObject[][] cellReferences = null;
    private int dsSelectedColumn, dsSelectedRow;
    
    
    /** Creates new form DatasetEditor */
    public DatasetEditor(ITableObject ts,
                         CustomDatasetTable ds,
                         String tableName,
                         IDataRefreshListener r) 
    {
        super("Dataset Editor");
        
        initComponents();
        t = ts.getTable();
        dataset = ds;
        this.lbl_table.setText("Table: "+tableName);
        JScrollPane scrollPane1 = new JScrollPane(t);
        //add mouse listener for copy from tablesource
         //create copy event 
        ActionListener copyListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy();
            }
        };
        KeyStroke copyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, 
                                            ActionEvent.CTRL_MASK, false);
        t.registerKeyboardAction(
                                copyListener, "Copy",
                                copyStroke,
                                JComponent.WHEN_FOCUSED);
                
        
        this.refreshNotifyObj = r;
        
        this.jSplitPane1.setTopComponent(scrollPane1);
        ////Handles copy and paste key bindings
//        ActionListener copyListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                copy();
//            }
//        };
//        KeyStroke copyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C,
//                                            ActionEvent.CTRL_MASK, false);
//        t.registerKeyboardAction(copyListener, "Copy", copyStroke,
//                                JComponent.WHEN_FOCUSED);
        setupDatasetTable();
        
    }
    private JPopupMenu createPopupMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem copy = new JMenuItem("Copy  Ctrl+C");
        JMenuItem paste = new JMenuItem("Paste  Ctrl+v");
        JMenuItem delete = new JMenuItem("Delete    Del");     
        
        menu.add(copy);
        menu.add(paste);
        menu.add(delete);
        
        copy.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                datasetCopy();
            }
        });
        paste.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                paste();
            }
        });
        delete.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteCells();
            }
        });
        return menu;
    }
    private void updateStatus(String status)
    {
        this.txt_status.setText("Status: " + status);
    }
    private void datasetCopy()
    {
        this.selectedColumns = this.td.getSelectedColumns();
        this.selectedRows = this.td.getSelectedRows();
        try
        {
            if (selectedColumns.length == 0) {
                throw new Exception("No data Cell selected!");
            }
            cellReferences =
                    new CellReferenceObject[selectedRows.length]
                                           [selectedColumns.length];
            
            rowLabels = new String[td.getSelectedRowCount()];
            
            
            for(int r = 0; r < selectedRows.length; r++)
            {
                for(int c = 0; c < selectedColumns.length; c++)
                {
                    try 
                    {
                        //if copying the row labels only store string
                        if(selectedColumns[c] != 0)
                        {
                            
                            cellReferences[r][c] = 
                                    new CellReferenceObject((CellReferenceObject)
                                    dataset.getValueAt(selectedRows[r],selectedColumns[c]));
                        }
                        else
                        {
                            rowLabels[r] = dataset.getValueAt(selectedRows[r],
                                                    selectedColumns[c]).toString();
                        }
                    } catch (Exception ex) {
                        
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error copying data: " + ex.getMessage());
        }
        
        updateStatus("Data has been copied..");
    }
    private void copy()
    {
        this.selectedColumns = this.t.getSelectedColumns();
        this.selectedRows = this.t.getSelectedRows();
        
        try
        {
            if (selectedColumns.length == 0) {
                throw new Exception("No data Cell selected!");
            }
            cellReferences =
                    new CellReferenceObject[selectedRows.length]
                                           [selectedColumns.length];
            
            rowLabels = new String[t.getSelectedRowCount()];
            
            
            for(int r = 0; r < selectedRows.length; r++)
            {
                for(int c = 0; c < selectedColumns.length; c++)
                {
                    try 
                    {
                        //if copying the row labels only store string
                        if(selectedColumns[c] != 0)
                        {
                            cellReferences[r][c] = new CellReferenceObject(
                                                    selectedRows[r], 
                                                    selectedColumns[c]);
                        }
                        else
                        {
                            rowLabels[r] = t.getValueAt(selectedRows[r],
                                                    selectedColumns[c]).toString();
                        }
                    } catch (Exception ex) {
                        
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error copying data: " + ex.getMessage());
        }
        
        updateStatus("Data has been copied..");
    }
    private void paste()
    {
        try
        {
            int targetColumnIndex = this.dsSelectedColumn;
            int targetRowIndex = this.dsSelectedRow;

            for(int r = 0; r < this.selectedRows.length; r++)
            {
                //copy row labels if it doesnt exist

                for(int c = 0; c < this.selectedColumns.length; c++)
                {
                    if(selectedColumns[c] == 0)
                    {
                        dataset.setValueAt(this.rowLabels[r],
                                           r+targetRowIndex,
                                           c+targetColumnIndex);

                    }
                    else
                    {
                        this.dataset.setValueAt(this.cellReferences[r][c],
                                            r+targetRowIndex,
                                            c+targetColumnIndex);

                    }

                }
            }

            this.setupDatasetTable();//refresh view
            updateStatus("Data has been pasted..");
        } 
        catch(Exception ex)
        {
            updateStatus("No data was copied from the source table.." 
                    + ex.getMessage());
        }
    }
    private void deleteCells()
    {
        try
        {
            int[] columns = td.getSelectedColumns();
            int[] rows = td.getSelectedRows();

            for(int r = 0; r < rows.length; r++)
            {
                for(int c = 0; c < columns.length; c++)
                {
                    dataset.setValueAt(null, rows[r] ,columns[c]);
                }
            }
            dataset.validateGroupkey();
            setupDatasetTable();//refresh dataview
            updateStatus("Cell(s) deleted");
        }
        catch(Exception ex)
        {
            updateStatus("No cells were selected.." 
                    + ex.getMessage());
        }
        
    }
    private void setupDatasetTable()
    {
        
        ITableObject newSourceTable = new TableObject("", (CustomJTable)t);
        td = dataset.getDataView(newSourceTable,null);
        JScrollPane scrollPane2 = new JScrollPane(td);
        
        td.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) 
            {
                if(e.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    //handle delete
                    deleteCells();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        
        });
        //set up pop up menu and wire event and target click for paste handler
        final JPopupMenu popupMenu = this.createPopupMenu();
        td.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(final MouseEvent e) 
            {
                //handle selection for chart customization
                if(e.getButton() == MouseEvent.BUTTON1)
                {
                    getTargetCellCoord();
                }
                else if(e.getButton() == MouseEvent.BUTTON3)
                {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if(popupMenu != null)
                                popupMenu.show(td, e.getX(), e.getY());
                        }
                    });
                }
                
            }
        });
        ActionListener copyListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datasetCopy();
            }
        };
        ActionListener pasteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        };
        //create paste event handler
        KeyStroke pasteStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, 
                                            ActionEvent.CTRL_MASK, false);    
        
                                            
        td.registerKeyboardAction(
                                pasteListener, "Paste",
                                pasteStroke,
                                JComponent.WHEN_FOCUSED);
       //create copy event 
       KeyStroke copyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, 
                                            ActionEvent.CTRL_MASK, false);
       td.registerKeyboardAction(
                                copyListener, "Copy",
                                copyStroke,
                                JComponent.WHEN_FOCUSED);
        
        
        this.jSplitPane1.setBottomComponent(scrollPane2);
        this.jSplitPane1.setDividerLocation(0.6);
        this.validate();
        this.repaint();
    }
    private void getTargetCellCoord()
    {
        dsSelectedRow = td.getSelectedRow();
        dsSelectedColumn = td.getSelectedColumn();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        txt_status = new javax.swing.JLabel();
        cmd_close = new javax.swing.JButton();
        lbl_table = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        txt_status.setText("Status");

        cmd_close.setText("Done");
        cmd_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_closeActionPerformed(evt);
            }
        });

        lbl_table.setText("Table View");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txt_status, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmd_close))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_table)
                        .addContainerGap(705, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_table)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmd_close)
                    .addComponent(txt_status))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void cmd_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_closeActionPerformed
// TODO add your handling code here:
    this.setVisible(false);
    if(this.refreshNotifyObj != null)
        this.refreshNotifyObj.refreshDataset();
    
}//GEN-LAST:event_cmd_closeActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new DatasetEditor(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmd_close;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbl_table;
    private javax.swing.JLabel txt_status;
    // End of variables declaration//GEN-END:variables

}
