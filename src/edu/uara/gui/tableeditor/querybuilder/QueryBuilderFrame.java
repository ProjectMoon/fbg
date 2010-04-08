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
package edu.uara.gui.tableeditor.querybuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uara.FBGFlow;
import edu.uara.config.Config;
import edu.uara.db.QueryExecuter;
import edu.uara.tableeditor.TableEditorConstants;
import edu.uara.tableeditor.TableObject;
import edu.uara.tableeditor.query.NonEditableQueryBuilder;
import edu.uara.tableeditor.query.QueryBuilder;
import edu.uara.tableeditor.query.QueryBuilder.SQLFunction;
import edu.uara.tableeditor.query.exception.NullColumnException;
import edu.uara.tableeditor.query.exception.NullTableException;

public class QueryBuilderFrame extends JFrame {
	private javax.swing.JButton addCritera;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JComboBox columnChooser;
    private javax.swing.JLabel columnChooserLabel;
    private javax.swing.JPanel columnChooserPanel;
    private javax.swing.JButton done;
    private javax.swing.JTextField equationBox;
    private javax.swing.JLabel equationLabel;
    private javax.swing.JPanel equationPanel;
    private javax.swing.JComboBox functionChooser;
    private javax.swing.JLabel functionChooserLabel;
    private javax.swing.JPanel northEquationPanel;
    private javax.swing.JPanel southEquationPanel;
    private javax.swing.JComboBox tableChooser;
    private javax.swing.JLabel tableChooserLabel;
    private javax.swing.JPanel tableChooserPanel;
    private javax.swing.JPanel topChooserPanel;
	
	private QueryBuilder builder;
	private CriteriaDefinerFrame criteraFrame = null;
	private int currentTable;
	
	private int row;
	private int column;
	private TableObject tableData;
	private TableEditorConstants cons;
	
	public QueryBuilderFrame(TableObject tableData, int row, int column, TableEditorConstants cons) {
		super("UARA Factbook - Query Builder");
		this.cons = cons;
		this.tableData = tableData;
		this.row = row;
		this.column = column;
		
		
		topChooserPanel = new javax.swing.JPanel();
        tableChooserPanel = new javax.swing.JPanel();
        tableChooserLabel = new javax.swing.JLabel();
        tableChooser = new javax.swing.JComboBox();
        columnChooserPanel = new javax.swing.JPanel();
        columnChooserLabel = new javax.swing.JLabel();
        columnChooser = new javax.swing.JComboBox();
        equationPanel = new javax.swing.JPanel();
        southEquationPanel = new javax.swing.JPanel();
        equationLabel = new javax.swing.JLabel();
        equationBox = new javax.swing.JTextField();
        northEquationPanel = new javax.swing.JPanel();
        functionChooserLabel = new javax.swing.JLabel();
        functionChooser = new javax.swing.JComboBox();
        buttonPanel = new javax.swing.JPanel();
        addCritera = new javax.swing.JButton();
        done = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        topChooserPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Value Select"));
        topChooserPanel.setLayout(new java.awt.BorderLayout(5, 5));

        tableChooserPanel.setLayout(new java.awt.BorderLayout(5, 5));

        tableChooserLabel.setText("Choose Table: ");
        tableChooserPanel.add(tableChooserLabel, java.awt.BorderLayout.WEST);

        tableChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AIS", "DIS", "EDS", "EIS", "POP" }));
        tableChooser.setSelectedIndex(-1);
        tableChooserPanel.add(tableChooser, java.awt.BorderLayout.EAST);

        topChooserPanel.add(tableChooserPanel, java.awt.BorderLayout.WEST);

        columnChooserPanel.setLayout(new java.awt.BorderLayout(5, 5));

        columnChooserLabel.setText("Choose Column:");
        columnChooserPanel.add(columnChooserLabel, java.awt.BorderLayout.WEST);

        columnChooserPanel.add(columnChooser, java.awt.BorderLayout.CENTER);

        topChooserPanel.add(columnChooserPanel, java.awt.BorderLayout.EAST);

        getContentPane().add(topChooserPanel, java.awt.BorderLayout.NORTH);

        equationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Equation"));
        equationPanel.setLayout(new java.awt.BorderLayout(5, 5));

        southEquationPanel.setLayout(new java.awt.BorderLayout(5, 5));

        equationLabel.setText("Enter Equation:");
        southEquationPanel.add(equationLabel, java.awt.BorderLayout.WEST);

        equationBox.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        equationBox.setPreferredSize(new java.awt.Dimension(300, 20));
        southEquationPanel.add(equationBox, java.awt.BorderLayout.CENTER);

        equationPanel.add(southEquationPanel, java.awt.BorderLayout.SOUTH);

        northEquationPanel.setLayout(new java.awt.BorderLayout(5, 5));

        functionChooserLabel.setText("Choose Function:");
        functionChooserLabel.setPreferredSize(new java.awt.Dimension(108, 15));
        northEquationPanel.add(functionChooserLabel, java.awt.BorderLayout.LINE_START);
        
        functionChooser.setModel(new javax.swing.DefaultComboBoxModel(QueryBuilder.SQLFunction.values()));
        northEquationPanel.add(functionChooser, java.awt.BorderLayout.CENTER);

        equationPanel.add(northEquationPanel, java.awt.BorderLayout.NORTH);

        getContentPane().add(equationPanel, java.awt.BorderLayout.WEST);

        buttonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        addCritera.setText("Add Critera");
        buttonPanel.add(addCritera, java.awt.BorderLayout.WEST);
        functionChooser.setModel(new DefaultComboBoxModel(QueryBuilder.SQLFunction.values()
		));
        done.setText("Done");
        buttonPanel.add(done, java.awt.BorderLayout.EAST);
        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        
        
        builder = tableData.getQueryBuilder(row, column);
		if(builder==null)
			builder = new QueryBuilder(cons);
		else if(builder instanceof NonEditableQueryBuilder) {
			JOptionPane.showMessageDialog(null, "Query has been manually modified, can not edit using Query Builder!", "Can not edit query", JOptionPane.ERROR_MESSAGE);
			this.dispose();
			return;
		}
		else {
			tableChooser.setSelectedItem(builder.getTable());
			handleTableSelect(null, false);
			columnChooser.setSelectedItem(builder.getColumn());
			functionChooser.setSelectedItem(builder.getQueryFunction());
			equationBox.setText(builder.getEquation());
		}
       
        done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitWizard(e);				
			}
		});
        
		tableChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleTableSelect(e, true);				
			}
		});
		
		addCritera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddCritera(e);				
			}
		});
		
        pack();
        this.setVisible(true);
	}
	
	//ActionListener for the table combo box select
	private void handleTableSelect(ActionEvent e, boolean check) {
		if(builder.getCriteraMap().keySet().size()!=0) {
			if(check) {
				int choice = JOptionPane.showConfirmDialog(this, "Continuing to change table will clear all critera, continue?", "Warning", JOptionPane.YES_NO_OPTION);
				if(choice==JOptionPane.NO_OPTION) {
					tableChooser.setSelectedIndex(currentTable);
					return;
				}
				else {
					builder.clearWheres();
					columnChooser.removeAll();
					if(criteraFrame!=null) {
						criteraFrame.dispose();
						criteraFrame = null;
					}
				}
			}	
		}
		String tableName = (String) tableChooser.getSelectedItem();
		ArrayList<String> columnNames = null;
		try {
			columnNames = QueryExecuter.getColumnNames(tableName);
		} catch (SQLException e1) {
			columnNames = new ArrayList<String>();
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			columnNames = new ArrayList<String>();
			e1.printStackTrace();
		}
		columnChooser.removeAll();
		for(String s : columnNames) {
			columnChooser.addItem(s);
		}
		columnChooser.setSelectedIndex(0);
		currentTable = tableChooser.getSelectedIndex(); 
		pack();
	}
	
	private void handleAddCritera(ActionEvent e) {
		if(okQuery()) {
			if(criteraFrame==null||!criteraFrame.isDisplayable()) {
				String[] columns = new String[columnChooser.getItemCount()];
				for(int i = 0; i < columns.length; ++i) {
					columns[i] = columnChooser.getItemAt(i).toString();
				}
				criteraFrame = new CriteriaDefinerFrame(columns, builder);
				criteraFrame.setVisible(true);
			}
		}
	}
	
	private void exitWizard(ActionEvent e) {
		if(okQuery()) {
			tableData.addQueryBuilder(builder, row, column);
			this.dispose();
		}
	}
	
	
	
	private boolean okQuery() {
		builder.setTable((String) tableChooser.getSelectedItem());
		builder.setColumn((String) columnChooser.getSelectedItem());
		builder.setEquation(equationBox.getText());
		builder.setQueryFunction(SQLFunction.valueOf(functionChooser.getSelectedItem().toString()));
		try {
			builder.generateQuery();
		} catch (NullTableException e) {
			JOptionPane.showMessageDialog(this, "A Table Must Be Selected", "Table Selection Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		} catch (NullColumnException e) {
			JOptionPane.showMessageDialog(this, "A Column Must Be Selected", "Column Selection Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	private static class TableObjectTester extends TableObject { 

		public TableObjectTester(String name) {
			super(name, 10, 10);
		}
		
		public void addQueryBuilder(QueryBuilder bldr, int rowNum, int columnNum) {
			JOptionPane.showMessageDialog(null, bldr.toString());
	    }
		
	}

}
