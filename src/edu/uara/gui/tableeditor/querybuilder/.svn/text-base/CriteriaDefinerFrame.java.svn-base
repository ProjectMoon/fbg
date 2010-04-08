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

import edu.uara.gui.Utilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uara.tableeditor.query.QueryBuilder;
import edu.uara.tableeditor.query.WhereArg;
import java.awt.event.FocusListener;

public class CriteriaDefinerFrame extends JFrame {

    private javax.swing.JButton addCritera;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel eastPanel;
    private javax.swing.JScrollPane mainScrollPanel;
    private JPanel childPanel;
    private JPanel childPanel2;
    private javax.swing.JButton removeCriteriaButton;
    private javax.swing.JButton doneButton;
    private ArrayList<CriteriaPanel> panelList;
    private String[] columnNames;
    private QueryBuilder builder;
    private int position;

    private CriteriaDefinerFrame(String[] columnNames) {
        super("CriteraDefinerFrame");

        panelList = new ArrayList<CriteriaPanel>();
        this.columnNames = columnNames;

        childPanel = new JPanel();
        childPanel2 = new JPanel();
        eastPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        addCritera = new javax.swing.JButton();
        removeCriteriaButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();
        mainScrollPanel = new javax.swing.JScrollPane(childPanel);

        setMinimumSize(new java.awt.Dimension(650, 400));
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        eastPanel.setLayout(new java.awt.BorderLayout(10, 10));

        controlPanel.setLayout(new java.awt.BorderLayout(10, 5));

        addCritera.setText("Add Critera");
        addCritera.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        addCritera.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        controlPanel.add(addCritera, java.awt.BorderLayout.NORTH);

        removeCriteriaButton.setText("Remove Critera #1");
        removeCriteriaButton.setEnabled(false);
        controlPanel.add(removeCriteriaButton, java.awt.BorderLayout.CENTER);

        doneButton.setText("Done");
        controlPanel.add(doneButton, java.awt.BorderLayout.SOUTH);

        eastPanel.add(controlPanel, java.awt.BorderLayout.NORTH);

        getContentPane().add(eastPanel, java.awt.BorderLayout.EAST);
        getContentPane().add(mainScrollPanel, java.awt.BorderLayout.CENTER);

        childPanel.setSize(mainScrollPanel.getWidth(), mainScrollPanel.getHeight());
        childPanel.setLayout(new BorderLayout(5, 5));
        childPanel.add(childPanel2, BorderLayout.NORTH);
        childPanel2.setLayout(new GridLayout(0, 1, 5, 5));


        addCritera.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addCritera(e);
            }
        });

        removeCriteriaButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeCritera(e);
            }
        });

        doneButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doneCritera(e);
            }
        });

        pack();
    }

    public CriteriaDefinerFrame(String[] columnNames, QueryBuilder builder) {
        this(columnNames);
        TreeMap<Integer, WhereArg> map = builder.getCriteraMap();
        this.builder = builder;
        for (Integer i : map.keySet()) {
            CriteriaPanel p = new CriteriaPanel(!panelList.isEmpty(), columnNames);
            p.booleanOperator.setSelectedItem(map.get(i).getBooleanOperatorConnector());
            p.columnChooser.setSelectedItem(map.get(i).getColumnName());
            p.logicalOperator.setSelectedItem(map.get(i).getOperator());
            p.value.setText(map.get(i).getValue());
            p.setPreferredSize(new Dimension(childPanel.getWidth(), 20));
            p.setSize(childPanel.getWidth(), 20);
            childPanel2.add(p);
            mainScrollPanel.validate();
            mainScrollPanel.repaint();
            panelList.add(p);
        }
        
        if (panelList.size() > 0)
            removeCriteriaButton.setEnabled(true);
    }

    private void addCritera(ActionEvent e) {
        final CriteriaPanel p = new CriteriaPanel(!panelList.isEmpty(), columnNames);
        panelList.add(p);
        p.setPreferredSize(new Dimension(childPanel.getWidth(), 20));
        p.setSize(childPanel.getWidth(), 20);
             
        childPanel2.add(p);
        mainScrollPanel.validate();
        mainScrollPanel.repaint();
        removeCriteriaButton.setEnabled(true);        
    }

    private void removeCritera(ActionEvent e) {
        if (!panelList.isEmpty()) {
            //CriteriaPanel p = panelList.remove(panelList.size() - 1);
            if (panelList.size() > 1 && position == 0) {
                Utilities.displayError(null, "Cannot Remove", "You can't remove the root clause before removing all the others!");
                return;
            }
            else {
                CriteriaPanel p = panelList.remove(position);
                childPanel2.remove(p);
                childPanel2.validate();
                mainScrollPanel.validate();
                position--;
                if (position < 0) position = 0;
                removeCriteriaButton.setText("Remove Criteria #" + (position + 1));
                if (panelList.size() == 0)
                    removeCriteriaButton.setEnabled(false);
            }
        }
    }

    private void doneCritera(ActionEvent e) {
        builder.clearWheres();
        int i = 0;
        for (CriteriaPanel p : panelList) {
            WhereArg arg = new WhereArg();
            arg.setBooleanOperatorConnector(p.booleanOperator.getSelectedItem().toString());
            arg.setColumnName(p.columnChooser.getSelectedItem().toString());
            arg.setOperator(p.logicalOperator.getSelectedItem().toString());
            arg.setValue(p.value.getText());
            builder.addWhereArg(i, arg);
            ++i;
        }
        this.dispose();
    }

    private class CriteriaPanel extends JPanel {

        public JComboBox booleanOperator;
        public JComboBox columnChooser;
        public JComboBox logicalOperator;
        public JTextField value;

        public CriteriaPanel(boolean booleanOperator, String[] columnNames) {
            super();

            this.booleanOperator = new JComboBox();
            this.columnChooser = new JComboBox();
            this.logicalOperator = new JComboBox();
            this.value = new JTextField();

            this.columnChooser.setModel(new DefaultComboBoxModel(columnNames));
            this.booleanOperator.setModel(new DefaultComboBoxModel(new String[]{
                        "and", "or"
                    }));
            this.logicalOperator.setModel(new DefaultComboBoxModel(new String[]{
                        "=", "!=", ">", "<", "<=", ">=", "like", "not like"
                    }));

            ArrayList<Component> componetList = new ArrayList<Component>();
            int cols = 3;
            
            
            FocusListener focusListener = new FocusListener() {
                public void focusGained(FocusEvent fe) {
                    int criteriaPos = 0;
                    for (int c = 0; c < panelList.size(); c++) {
                        CriteriaDefinerFrame.CriteriaPanel testPanel = panelList.get(c);
                        if (testPanel.equals(CriteriaPanel.this)) {
                            criteriaPos = c;
                            break;
                        }
                    }

                    CriteriaDefinerFrame.this.position = criteriaPos;
                    CriteriaDefinerFrame.this.removeCriteriaButton.setText("Remove Criteria #" + (position + 1));
                }

                public void focusLost(FocusEvent fe) {

                }
            };
            
            columnChooser.addFocusListener(focusListener);
            logicalOperator.addFocusListener(focusListener);
            value.addFocusListener(focusListener);
            
            if (booleanOperator) {
                cols = 4;
                this.booleanOperator.addFocusListener(focusListener);
                componetList.add(this.booleanOperator);
            }
            componetList.add(this.columnChooser);
            componetList.add(this.logicalOperator);
            componetList.add(this.value);
            

            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            for (Component c : componetList) {
                this.add(c);
            }

        }
    }
}
