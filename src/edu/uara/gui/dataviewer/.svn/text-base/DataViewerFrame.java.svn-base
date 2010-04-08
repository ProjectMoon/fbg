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
package edu.uara.gui.dataviewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import edu.uara.FBG;
import edu.uara.FBGFlow;
import edu.uara.config.Config;
import edu.uara.db.QueryExecuter;
import edu.uara.db.profiles.ProfileManager;
import edu.uara.gui.main.MainFactbookFrame;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;

/**
 * A simple frame that displays data from the database in table format. It goes
 * through several stages as it loads the data, represented by a progress bar.
 * @author jh44695
 */
public class DataViewerFrame extends JFrame implements PropertyChangeListener {
    private int fileID;
    private String tableName;
    private String year;
    private JProgressBar progressBar;
    private JLabel loadingLabel;
    private DataViewWorker worker;
    
    
    /**
     * Internal class that hadnles the actual downloading of data and updating
     * of the progress bar.
     */
    class DataViewWorker extends SwingWorker {
        //the progress of the task so far; divided into three chunks:
        //1. get file ID and columns. jumps to 33% upon completion
        //2. get data from server. jumps to 66% upon completion
        //3. process data. goes to 100%.
        private Integer progress = 0;
        private ArrayList<String> columns;
        
        @Override
        protected Object doInBackground() throws Exception {
            //set up the tables
            JTable table = null;
            try {
                this.setProgress(0);
                
                //chunk 1: 33%. get file ID and column names.
                loadingLabel.setText("Loading: Getting file information.");
                fileID = getFileID(tableName, year);
                this.setProgress(this.getProgress() + 11);
                
                columns = QueryExecuter.getColumnNames(tableName);
                this.setProgress(this.getProgress() + 11);
                
                columns.remove(0); //remove file ID from data displayed
                String[] tableColumns = columns.toArray(new String[0]);
                
                this.setProgress(this.getProgress() + 11);
                //end chunk 1
                
                //chunk 2: 57%. updated inside getTableData
                loadingLabel.setText("Loading: Downloading data.");
                Object[][] tableData = getTableData();
                table = new JTable(tableData, tableColumns);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                //end chunk 2
                
                //chunk 3: 10%. auto-width the columns to the size of the column name.
                loadingLabel.setText("Loading: Formatting table.");
                int chunkAmount = columns.size() / 10;
                int chunkCounter = 0;
                JTableHeader header = table.getTableHeader();
                TableCellRenderer defaultHeaderRenderer = null;

                if (header != null) {
                    defaultHeaderRenderer = header.getDefaultRenderer();
                }
                TableColumnModel cols = table.getColumnModel();
                TableModel data = table.getModel();
                int rowCount = data.getRowCount();

                for (int i = cols.getColumnCount() - 1; i >= 0; --i) {
                    TableColumn column = cols.getColumn(i);
                    int columnIndex = column.getModelIndex();
                    int width = -1;

                    TableCellRenderer h = column.getHeaderRenderer();

                    if (h == null) {
                        h = defaultHeaderRenderer;
                    }
                    if (h != null) // Not explicitly impossible
                    {
                        Component c = h.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, i);
                        width = c.getPreferredSize().width;
                    }
                    
                    for (int row = rowCount - 1; row >= 0; --row) {
                        DefaultTableCellRenderer r = (DefaultTableCellRenderer)table.getCellRenderer(row, i);
                        r.setVerticalAlignment(SwingConstants.TOP);
                        Component c = r.getTableCellRendererComponent(table, data.getValueAt(row, columnIndex),
                                false, false, row, i);
                        width = Math.max(width, c.getPreferredSize().width);
                    }

                    if (width >= 0) {
                        column.setPreferredWidth(width + 5);
                    }
                    
                    //update counter if necessary
                    if (chunkCounter >= chunkAmount) {
                        this.setProgress(this.getProgress() + 1);
                        chunkCounter = 0;
                    }
                    else chunkCounter++;
                }
            }
            catch (Exception e) {
                e.printStackTrace(FBG.origErr);
            }            
            
            return table;
        }

        private int getFileID(String tableName, String year) {
            try {
                Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
                PreparedStatement stmt = conn.prepareStatement("select * from Files where File_Name=? and File_Year=?");
                stmt.setString(1, tableName);
                stmt.setString(2, year);

                ResultSet rs = stmt.executeQuery();
                rs.next();
                int fileID = rs.getInt(3); //3 corresponds to File_ID
                rs.close();

                return fileID;
            }
            catch(Exception e) {
                e.printStackTrace(FBG.origErr);
            }

            return -1;
        }
        
        private Object[][] getTableData() {
            try {
                Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
                String sql = "select * from " + tableName + " where File_ID=?";
                PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                stmt.setInt(1, fileID);

                ResultSet rs = stmt.executeQuery();

                //get the total number of records, and the columns per row.
                rs.last();
                int totalRecords = rs.getRow();
                int columnsPerRow = columns.size();
                rs.first();
                
                //from above info, create the 2D array to return
                Object[][] retData = new Object[totalRecords][columnsPerRow];
                
                //chunkAmount is used to update the progress bar. we divide it by
                //the amount of % time we feel this task should take.
                //this updates the progress bar at a defined interval.
                int chunkAmount = totalRecords / 57;
                int chunkCounter = 0;
                int arrayPos = 0;
                
                while (rs.next()) {
                    for (int c = 0; c < columns.size(); c++)
                        retData[arrayPos][c] = rs.getObject(columns.get(c));

                    arrayPos++;
                    
                    //every time we finish a "chunk", we update the bar and reset
                    //the chunk counter
                    if (chunkCounter >= chunkAmount) {
                        this.setProgress(this.getProgress() + 1);
                        chunkCounter = 0;
                    }
                    else chunkCounter++;                    
                }
                
                return retData;
            }
            catch (SQLException e) {
                e.printStackTrace(FBG.origErr);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace(FBG.origErr);
            }

            return null;
        }
        
        @Override
        protected void done() {
            try {
                JTable table = (JTable)this.get();
                
                final int rowCount = table.getRowCount();
                ListModel rowHeaderModel = new AbstractListModel() {

                    public int getSize() {
                        return rowCount;
                    }

                    public Object getElementAt(int index) {
                        return index + 1;
                    }
                };
                
                JScrollPane scroller = new JScrollPane(table);
                JList rowHeader = new JList(rowHeaderModel);
                rowHeader.setFixedCellWidth(50);
                rowHeader.setFixedCellHeight(table.getRowHeight());
                rowHeader.setCellRenderer(new RowHeaderRenderer(table));
                rowHeader.setBackground(scroller.getBackground());    
                scroller.setRowHeaderView(rowHeader);
                
                DataViewerFrame.this.remove(progressBar);
                DataViewerFrame.this.remove(loadingLabel);
                DataViewerFrame.this.add(scroller);
                DataViewerFrame.super.setSize(450, 500);
                DataViewerFrame.super.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            catch (Exception e) { System.err.println("Error in done: " + e); }
        }
    }

    public DataViewerFrame(String tableName, String year) {
        super("Data Viewer: " + tableName + " (" + year + ")");
        
        super.setLayout(new BorderLayout());

        this.tableName = tableName;
        this.year = year;

        loadingLabel = new JLabel("Loading...");
        super.add(loadingLabel, BorderLayout.CENTER);
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        super.add(progressBar, BorderLayout.SOUTH);
        worker = new DataViewWorker();
        worker.addPropertyChangeListener(this);
        worker.execute();
        

        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.pack();
        super.setVisible(true);
    }



    public static void main(String[] args) {
        Config.load();
        FBGFlow.setupProfile();
        SwingUtilities.invokeLater(new Runnable() { 
            public void run() {
                new DataViewerFrame("AIS", "2009");
            }
        });
        
    }

    public void propertyChange(PropertyChangeEvent e) {
        //not sure how good this is... it works, but probably not the best way to do it
        try {
            progressBar.setValue(worker.getProgress());
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }
}

class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    RowHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}
