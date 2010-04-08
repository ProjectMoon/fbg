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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uara.tableeditor.TableObject;

/**
 * Frame for entering prefix/postfix formatting information in table cells.
 * @author dustin
 */
public class PrePostFrame extends JFrame {
	private int row;
	private int column;
	private TableObject to;
	
	private JTextField pre;
	private JTextField post;
	
	public PrePostFrame(int row, int column, TableObject to) {
		this.row= row;
		this.column = column;
		this.to = to;
		
		String pref = "";
		String postf = "";
		if(to.getPreFix(row, column)!=null)
			pref = to.getPreFix(row, column);
		if(to.getPostFix(row, column)!=null)
			postf = to.getPostFix(row, column);
		
		JPanel p1 = new JPanel();
		JLabel l1 = new JLabel("Prefix:");
		pre = new JTextField(pref);
		p1.setLayout(new GridLayout(0,2));
		p1.add(l1);
		p1.add(pre);
		
		JPanel p2 = new JPanel();
		JLabel l2 = new JLabel("Postfix:");
		post = new JTextField(postf);
		p2.setLayout(new GridLayout(0,2));
		p2.add(l2);
		p2.add(post);
		
		JButton ok = new JButton("Ok");
		JButton cancel = new JButton("Cancel");
		
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		
		this.setLayout(new GridLayout(0, 2));
		this.getContentPane().add(p1);
		this.getContentPane().add(p2);
		this.getContentPane().add(ok);
		this.getContentPane().add(cancel);
		this.pack();
		this.setVisible(true);
	}
	
	private void ok() {
		to.setPreFix(pre.getText(), row, column);
		to.setPostFix(post.getText(), row, column);
		this.dispose();
	}
	
	private void cancel() {
		this.dispose();
	}

}
