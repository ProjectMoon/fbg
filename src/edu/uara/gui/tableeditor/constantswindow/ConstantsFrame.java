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
package edu.uara.gui.tableeditor.constantswindow;

import edu.uara.gui.Utilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import edu.uara.tableeditor.TableEditorConstants;
import edu.uara.tableeditor.TableObject;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A frame that allows addition (and removal of) constants. Constants are defined
 * with any type of value (text or number) and can be used almost anywhere in a
 * factbook project, from queries to equations.
 * @author jeff
 */
public class ConstantsFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ConstantsPanel> constantList;
	private TableEditorConstants constants;
	private JPanel childPanel;
	private JScrollPane pane;
	private ArrayList<TableObject> to;
	public ConstantsFrame(TableEditorConstants tec, ArrayList<TableObject> to) { 
		super("Project Constants");
		this.to = to;
		this.constants = tec;
		childPanel = new JPanel();
		JPanel childPane2 = new JPanel();
		pane = new JScrollPane(childPane2);
		setMinimumSize(new java.awt.Dimension(650, 400));
		
		constantList = new ArrayList<ConstantsPanel>();
		
		JPanel controlPanel = new JPanel();
		JButton add = new JButton("Add");
		JButton done = new JButton("Done");
		JButton remove = new JButton("Remove");
		controlPanel.setLayout(new BorderLayout(5, 5));
		controlPanel.add(add, BorderLayout.NORTH);
		controlPanel.add(remove, BorderLayout.CENTER);
		controlPanel.add(done, BorderLayout.SOUTH);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout());
		eastPanel.add(controlPanel, BorderLayout.NORTH);
		
		
		childPane2.setLayout(new BorderLayout());
		childPanel.setLayout(new GridLayout(0,1,5,5));
		
		childPane2.add(childPanel, BorderLayout.NORTH);
		
		this.setLayout(new BorderLayout());
		
		this.getContentPane().add(pane, BorderLayout.CENTER);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewConstant("", "");
			}
		});
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeConstant();
			}
		});
		
		for(String s : tec.getKeys()) {
                    addNewConstant(s, tec.getConstant(s));
		}
                
                this.addWindowListener(new WindowAdapter() { 
                    public void windowClosing(WindowEvent e) {
                        done();
                    }
                });
		this.pack();
		this.setVisible(true);
	}
	
	private void addNewConstant(String key, String value) {
		ConstantsPanel p = new ConstantsPanel(key, value);
		p.setPreferredSize(new Dimension(childPanel.getWidth(), 20));
		synchronized (this) {
			childPanel.add(p);
			constantList.add(p);
			pane.validate();
			pane.repaint();
		}
	}
	private void removeConstant() {
		synchronized (this) {
			if(constantList.size()>0) {
				childPanel.remove(constantList.remove(constantList.size()-1));
				pane.validate();
				pane.repaint();
			}
		}
	}
	
	private void done() {
		synchronized (this) {
			this.constants.clearConstants();
			for(ConstantsPanel p : constantList) {
                            if (p.key.getText().contains("!")) {
                                Utilities.displayError(this, "Invalid Key Format", "Constant names cannot contain !s");
                                return;
                            }
                            else {
                                this.constants.setConstant(p.key.getText(), p.value.getText());
                            }
			}
			this.dispose();
			for(TableObject t : to)
				t.refreshTableModel();
			synchronized (this.constants) {
				this.constants.setShown(false);
			}
		}
	}
	
	private class ConstantsPanel extends JPanel {
		public JTextField key;
		public JTextField value;
		
		public ConstantsPanel(String key, String value) {
			super();
			this.key = new JTextField(key);
			this.value = new JTextField(value);
			this.setLayout(new GridLayout(1,2));
			this.add(this.key);
			this.add(this.value);
		}
	}
}
