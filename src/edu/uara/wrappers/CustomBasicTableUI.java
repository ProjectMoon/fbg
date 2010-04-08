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

import edu.uara.wrappers.multiselect.TableSelectionModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * This is an overriden class of the BasicTableUI
 * This class allows for merged cells to be painted properly
 * @author Paul Halvorsen
 */
public class CustomBasicTableUI extends BasicTableUI implements Serializable {

    public static final long serialVersionUID = 1;
    private int prevRow = -1;
    private int prevColumn = -1;

    /**
     * This is an overridden function from BasicTableUI to paint each cell in the table.
     * @param g
     * @param comp
     */
    @Override
    public void paint(Graphics g, JComponent comp) {
        Rectangle rec = g.getClipBounds();
        int firstRow = ((CustomJTable) table).rowAtPoint(new Point(0, rec.y));
        int lastRow = ((CustomJTable) table).rowAtPoint(new Point(0, rec.y + rec.height));
        // -1 is a flag that the ending point is outside the table
        if (lastRow < 0) {
            lastRow = table.getRowCount() - 1;
        }
        for (int r = 0; r < table.getRowCount(); r++) {
            for (int c = 0; c < table.getColumnCount(); c++) {
                if (((CustomJTable) table).getVisibleCell(r, c).columnNum == c &&
                        ((CustomJTable) table).getVisibleCell(r, c).rowNum == r) {
                    paintCell(r, c, g, table.getCellRect(r, c, true));
                }
            }
        }
    }

    /**
     * Paints a specific row of the table. This takes into account merged cells
     * @param row: the row number to be painted
     * @param g: graphics object from the ovridden paint function
     */
    private void paintRow(int row, Graphics g) {
        Rectangle r = g.getClipBounds();
        for (int i = 0; i < table.getColumnCount(); i++) {
            Rectangle r1 = table.getCellRect(row, i, true);
            if (r1.intersects(r)) // at least a part is visible
            {
                int sk = ((CustomJTable) table).getVisibleCell(row, i).columnNum;
                paintCell(row, sk, g, r1);
                // increment the column counter
                int span = ((CustomJTable) table).getHorizontalNumMerged(row, sk);
                if (span != 1) {
                    i += span;
                }
            }
        }
    }

    /**
     * This paints the individual cell, expanding the cell where necissary for merged cells
     * @param row: the row number for the cell being painted
     * @param column: the vidible column number for the cell being painted
     * @param g: graphics object from paint function
     * @param area: the total area of the cell
     */
    private void paintCell(int row, int column, Graphics g, Rectangle area) {
        int verticalMargin = table.getRowMargin();
        int horizontalMargin = table.getColumnModel().getColumnMargin();

        Color c = g.getColor();
        g.setColor(table.getGridColor());
        /*g.setColor(Color.BLACK);
        if (table.getShowHorizontalLines() && table.getShowVerticalLines()) {
        g.drawLine(area.x, area.y, area.x + area.width, area.y);
        g.drawLine(area.x, area.y + area.height - 1, area.x + area.width, area.y + area.height - 1);
        //g.drawRect(area.x, area.y, area.width - 1, 1);
        //g.drawRect(area.x, area.y + area.height - 1, area.width-1, 1);
        //g.drawRect(area.x, area.y, area.width - 1, area.height - 1);
        }*/
        g.setColor(c);

        area.setBounds(area.x + horizontalMargin / 2,
                area.y + verticalMargin / 2,
                area.width - horizontalMargin,
                area.height - verticalMargin);

        if (table.isEditing() && table.getEditingRow() == row &&
                table.getEditingColumn() == column) {
            Component component = table.getEditorComponent();
            component.setBounds(area);
            component.validate();
        } else {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component component = table.prepareRenderer(renderer, row, column);
            if (component.getParent() == null) {
                rendererPane.add(component);
            }
            rendererPane.paintComponent(g, component, table, area.x, area.y,
                    area.width, area.height, true);
            g.setColor(Color.BLACK);
            if (table.getShowHorizontalLines() && table.getShowVerticalLines()) {
                g.drawRect(area.x, area.y, area.width - 1, area.height - 1);
            }
        }
    }

    /**
     * Creates a new CustomBaseicTableUI
     * @param c the JComponent
     * @return new CustomBasicTableUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new CustomBasicTableUI();
    }

    /**
     * Overrides to create a new AnySelectionMouseInputHandler
     * @return new AnySelectionMouseInputHandler
     */
    @Override
    protected MouseInputListener createMouseInputListener() {
        return new AnySelectionMouseInputHandler();
    }

    /**
     * to get access to the table from the inner class MyMouseInputHandler
     */
    protected CustomJTable getTable() {
        return (CustomJTable) table;
    }

    /**
     * Updates the table selection model
     * @param row the row number of the currently selected cell
     * @param column the column number of the currently selected cell
     * @param ctrlDown if ctrl is being held down
     * @param shiftDown if shift is being held down
     * @param drag if the mouse is being dragged
     */
    protected void updateTableSelectionModel(int row, int column, boolean ctrlDown, boolean shiftDown, boolean drag) {
        CustomJTable t = getTable();
        column = t.convertColumnIndexToModel(column);
        TableSelectionModel tsm = t.getTableSelectionModel();

        if (ctrlDown && !shiftDown && !drag) {
            ctrlClick(row, column, tsm);
        } else if (!ctrlDown && shiftDown && !drag) {
            shiftClick(row, column, tsm);
        } else if (!ctrlDown && !shiftDown && !drag) {
            regClick(row, column, tsm);
        } else {
            dragClick(row, column, tsm);
        }
        t.repaint();
    }

    /**
     * Called to update the TableSelectionModel when the mouse is dragged
     * @param row the row number of the currently selected cell
     * @param column the column number of the currenlty selected cell
     * @param tsm the TableSelectionModel to be updated
     */
    private void dragClick(int row, int column, TableSelectionModel tsm) {
        tsm.clearSelection();
        shiftClick(row, column, tsm);
    }

    /**
     * Called to update the TableSelectionModel when no modifyer is held
     * @param row the row number of the clicked cell
     * @param column the column number of the clicked cell
     * @param tsm the TableSelectionModel to be updated
     */
    private void regClick(int row, int column, TableSelectionModel tsm) {
        tsm.clearSelection();
        tsm.setSelection(row, column);
        this.prevColumn = column;
        this.prevRow = row;
    }

    /**
     * Called to update the TableSelectionModel when ctrl is held
     * @param row the row number of the clicked cell
     * @param column the column number of the clicked cell
     * @param tsm the TableSelectionModel to be updated
     */
    private void ctrlClick(int row, int column, TableSelectionModel tsm) {
        if (tsm.isSelected(row, column)) {
            tsm.removeSelection(row, column);
        } else {
            tsm.addSelection(row, column);
        }
        this.prevColumn = column;
        this.prevRow = row;
    }

    /**
     * Called to update the TableSelectionModel when shift is held
     * @param row the row number of the selected cell
     * @param column the column number of the selected cell
     * @param tsm the TableSelectionModel that needs to be updated
     */
    private void shiftClick(int row, int column, TableSelectionModel tsm) {
        int numOfCells = (Math.abs(this.prevColumn - column) + 1) * (Math.abs(this.prevRow - row) + 1);
        int r = row, c = column;

        while (numOfCells > 0) {
            tsm.addSelection(r, c);
            if (r < this.prevRow) {
                r++;
            } else if (r > this.prevRow) {
                r--;
            } else if (r == this.prevRow) {
                if (c < this.prevColumn) {
                    r = row;
                    c++;
                } else if (c > this.prevColumn) {
                    r = row;
                    c--;
                }
            }
            numOfCells--;
        }
    }

    /**
     * Creates a new key listener using CustomKeyInputListener
     * @return a new CustomKeyInputListener
     */
    @Override
    protected KeyListener createKeyListener() {
        return new CustomKeyInputListener();
    }

    /**
     * This class handles navigation of the table via keyboard.
     */
    public class CustomKeyInputListener extends KeyHandler {

        /**
         * Called when key is pressed
         * @param e keyevent object
         */
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
        }

        /**
         * Called when a key is released, needed for using arrow and enter navigation
         * @param e keyevent object
         */
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);

            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP ||
                    e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT ||
                    e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {

                int column = getTable().getSelectedColumn();
                int row = getTable().getSelectedRow();

                TableCellEditor tce = getTable().getCellEditor();
                if ((tce == null) || (tce.shouldSelectCell(e))) {
                    getTable().requestFocus();
                    updateTableSelectionModel(row, column, e.isControlDown(), e.isShiftDown(), false);
                }
            }
        }
    }

    /**
     * Almost the same implementation as its super class.
     * Except updating the TableSelectionModel rather than the
     * default ListSelectionModel.
     */
    public class AnySelectionMouseInputHandler extends MouseInputHandler {

        /**
         * Called when the mouse is dragged
         * @param e a mouse event object
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            super.mouseDragged(e);

            Point p = e.getPoint();
            int row = getTable().rowAtPoint(p);
            int column = getTable().columnAtPoint(p);

            if ((column == -1) || (row == -1)) {
                return;
            }

            TableCellEditor tce = getTable().getCellEditor();
            if ((tce == null) || (tce.shouldSelectCell(e))) {
                getTable().requestFocus();
                updateTableSelectionModel(row, column, false, false, true);
                if (e.isShiftDown()) {
                    getTable().repaint();
                }
            }
        }

        /**
         * Called when the mouse is pressed
         * @param e a mouse event object
         */
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            Point p = e.getPoint();
            int row = getTable().rowAtPoint(p);
            int column = getTable().columnAtPoint(p);
            if ((column == -1) || (row == -1)) {
                return;
            }
            TableCellEditor tce = getTable().getCellEditor();
            if ((tce == null) || (tce.shouldSelectCell(e))) {
                getTable().requestFocus();
                updateTableSelectionModel(row, column, e.isControlDown(), e.isShiftDown(), false);
                if (e.isShiftDown()) {
                    getTable().repaint();
                }
            }
        }
    }
}
