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
package edu.uara.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uara.FBGFlow;
import edu.uara.gui.main.MainFactbookFrame;
import edu.uara.tableeditor.TableObject;

/**
 * A simple window for entering information about creating a new table. When Ok is
 * pressed, the new table is created and added to the project.
 * @author dustin
 */
public class NewTableWizard extends JDialog {
    private TableObject createdTable;
    private JTextField tableName;
    private JTextField rows;
    private JTextField columns;
    private MainFactbookFrame f;

    public NewTableWizard(MainFactbookFrame f) {
        super(f, true);
        super.setTitle("New Table");
        JPanel panel = new JPanel();
        this.f = f;
        JLabel tableLabel = new JLabel("Table Name: ");
        JLabel rowLabel = new JLabel("Row: ");
        JLabel columnLabel = new JLabel("Columns:");

        tableName = new JTextField();
        rows = new JTextField();
        columns = new JTextField();

        JButton ok = new JButton("Ok");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                addTable();
            }
        });

        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exitFrame();
            }
        });

        panel.setLayout(new GridLayout(0, 2));
        panel.add(tableLabel);
        panel.add(tableName);
        panel.add(rowLabel);
        panel.add(rows);
        panel.add(columnLabel);
        panel.add(columns);
        panel.add(ok);
        panel.add(cancel);
        this.setSize(400, 400);

        getContentPane().add(panel);
        this.setVisible(true);
    }

    public void addTable() {
        if (!isNum(rows.getText())) {
            JOptionPane.showMessageDialog(this, "Row Must Be Number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isNum(columns.getText())) {
            JOptionPane.showMessageDialog(this, "Columns Must Be Number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tableName.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "You Must Enter A Table Name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (FBGFlow.getOpenProject().getTable(tableName.getText()) != null) {
            JOptionPane.showMessageDialog(this, "Table Name Already Used In Project!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(Integer.parseInt(rows.getText())>100) {
        	JOptionPane.showMessageDialog(this, "Row input is too large!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(Integer.parseInt(columns.getText())>100) {
        	JOptionPane.showMessageDialog(this, "Column input is too large!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (FBGFlow.getOpenProject() != null) {
            createdTable = new TableObject(tableName.getText(), Integer.parseInt(rows.getText()), Integer.parseInt(columns.getText()));
            FBGFlow.getOpenProject().putTable(createdTable);
            f.updateProjectTree();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No Open Project Found!", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    public void exitFrame() {
        this.dispose();
    }
    
    public TableObject getCreatedTable() {
        return createdTable;
    }

    private boolean isNum(String val) {
        try {
            int i = Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
