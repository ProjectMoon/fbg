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
package edu.uara.parser.integrated;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uara.FBG;
import edu.uara.db.profiles.ProfileManager;

/**
 *
 * @author jh44695
 */
public class BatchUploader {
    private String tableName;
    private Connection conn;
    private PreparedStatement stmt;
    private int batchCount;
    private int uniqueID;
    private int year;
    public static final int BATCH_MAX = 100;
    private boolean illegal;

    public BatchUploader(String tableName, int year) {
        this.tableName = tableName;
        this.year = year;
        init();
    }

    private void init() {
        try {
            conn = ProfileManager.CURRENT_PROFILE.getConnection();
            uniqueID = generateUniqueID();
            
            if (fileOnServer())
                illegal = true;
            else
                initServerFile();


            batchCount = 0;
        }
        catch (SQLException e) {
            System.err.print("Upload error: " + e);
            e.printStackTrace(FBG.origErr);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace(FBG.origErr);
        }
    }
    
    private boolean fileOnServer() {
        //the very first thing we must check is if this file/year are already present in the files table
        //if so we must generate an error
        try {
            System.out.println("Checking server-side status of file.");
            Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from Files where File_Name=? and File_Year=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, tableName);
            stmt.setString(2, new Integer(year).toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.last() == true) {
                JOptionPane.showMessageDialog(new JFrame(), "You cannot upload this file because it's already on the server!", "Upload Error", JOptionPane.ERROR_MESSAGE);
                return true;
            }
        } catch (Exception e) {
            System.err.print("Upload error: " + e);
            e.printStackTrace(FBG.origErr);
        }
        
        return false;
    }

    private void initServerFile() {
        try {
            Connection conn = ProfileManager.CURRENT_PROFILE.getConnection();
            PreparedStatement fileStmt = conn.prepareStatement("insert into Files values (?, ?, ?, ?)");
            fileStmt.setString(1, tableName);
            fileStmt.setString(2, new Integer(year).toString());
            fileStmt.setInt(3, uniqueID);
            fileStmt.setString(4, "UNFINISHED");
            fileStmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            System.err.print("Upload error: " + e);
            e.printStackTrace(FBG.origErr);
        }
    }

    public boolean put(List<RawData> record) {
        if (illegal) {
            System.err.println("You cannot upload files already on the server!");
            return false;
        }
        try {
            //init our prepared statement if we have
            //not already
            if (stmt == null)
                initStmt(record);

            //now bind all of the parameters and add to the batch
            //first is file ID
            stmt.setInt(1, uniqueID);

            for (int c = 2; c < record.size() + 2; c++) {
                RawData rd = (RawData) record.get(c - 2);
                //System.out.println(c + ": " + rd);
                Object o = rd.getData();

                //determine what type the data is, and send the proper type
                //to the prepared statement.
                if (rd instanceof IntegerRawData) {
                    if (o == null) {
                        stmt.setNull(c, Types.INTEGER);
                    } else {
                        Integer i = (Integer) o;
                        stmt.setInt(c, i.intValue());
                    }
                } else if (rd instanceof StringRawData) {
                    if (o == null) {
                        stmt.setNull(c, Types.VARCHAR);
                    } else {
                        String s = (String) o;
                        stmt.setString(c, s);
                    }
                } else if (rd instanceof DoubleRawData) {
                    if (o == null) {
                        stmt.setNull(c, Types.DOUBLE);
                    } else {
                        Double d = (Double) o;
                        stmt.setDouble(c, d);
                    }
                } else if (rd instanceof DateRawData) {
                    if (o == null) {
                        stmt.setNull(c, Types.DATE);
                    } else {
                        java.sql.Date d = (java.sql.Date) o;
                        stmt.setDate(c, d);
                        stmt.setDate(c, d);
                    }
                }
            }

            //add the statement to the batch.
            stmt.addBatch();
            batchCount++;

            //now if we have reached a certain amount of items in the batch,
            //commit for victory!
            if (batchCount == BATCH_MAX)
                updateBatch();

        } //end try
        catch (SQLException ex) {
            ex.printStackTrace(FBG.origErr);
            //Logger.getLogger(BatchUploader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private void updateBatch() {
        try {
            stmt.executeBatch();
            conn.commit();
            stmt.clearBatch();
            batchCount = 0;
        }
        catch (SQLException ex) {
            ex.printStackTrace(FBG.origErr);
            //Logger.getLogger(BatchUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void finishBatch() {
        if (illegal) {
            FBG.origErr.println("You cannot upload files already on the server!");
            return;
        }

        updateBatch();

        //update the Files table to reflect that the file is fully uploaded
        try {
            Statement finishedStmt = conn.createStatement();
            finishedStmt.executeUpdate("update Files set File_Status='FINISHED' where File_ID=" + uniqueID);
            conn.commit();
        }
        catch (SQLException e) {
            e.printStackTrace(FBG.origErr);
        }
        
    }

    private int generateUniqueID() {
        Random rand = new Random(System.currentTimeMillis());
        int uniqueID = rand.nextInt(Integer.MAX_VALUE);

        //see if this ID is in the database
        try {
            Connection con = ProfileManager.CURRENT_PROFILE.getConnection();
            Statement ustmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ustmt.executeQuery("select * from Files where File_ID=" + uniqueID);

            //if there is 1+ records, we need to continue to generate IDs until we find a unique one.
            //chances are we won't need to execute this code, but you never know...
            if (rs.last() == true) {
                boolean done = false;
                while (!done) {
                    uniqueID = new Random().nextInt(Integer.MAX_VALUE);

                    //see if this ID is in the database
                    con = ProfileManager.CURRENT_PROFILE.getConnection();
                    ustmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = ustmt.executeQuery("select * from Files where File_ID=" + uniqueID);
                    done = rs.last();
                }
            }
        }
        catch (Exception e) {
            System.err.print("Upload error: " + e);
            e.printStackTrace(FBG.origErr);
        }

        return uniqueID;
    }

    private void initStmt(List<RawData> data) {
        String sql = toSQL(data);
        
        try {
            stmt = conn.prepareStatement(sql);
        }
        catch (SQLException e) {
            e.printStackTrace(FBG.origErr);
        }
    }

    private String toSQL(List<RawData> data) {
        //first construct a prepared statement based on the length
        //of a given record in the list. all sub list sizes should be the same
        //so we only concentrate on the first record to construct our statement.
        StringBuilder sb = new StringBuilder("insert into " + tableName + " values (");
        for (int c = 0; c < data.size() + 1; c++) {
            sb.append("?,");
        }

        //remove the last comma and put in the closing )
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");

        //finally, get our sql string
        String sql = sb.toString();

        return sql;
    }
}
