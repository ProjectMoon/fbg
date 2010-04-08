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
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A custom cell renderer so each individual cell can be formatted instead of the entire table at once
 * @author Paul Halvorsen
 */
public  class CustomTableCellRenderer extends DefaultTableCellRenderer implements Serializable {

    public static final long serialVersionUID = 1;
    private Color bgColor[][];
    private Color fgColor[][];
    private Integer horizontalAlign[][];
    private Integer verticalAlign[][];
    private Font fontFace[][];
//    private Border cellBorder[][];
    private CustomBorderGenerator cellBorderGen[][];
    
    /**
     * Constructor to set up the cell renderer
     * @param numRows: the number of rows in the table
     * @param numColumns: the number of columns in the table
     */
    public CustomTableCellRenderer(int numRows, int numColumns) {
        super();
        bgColor = new Color[numRows][numColumns];
        fgColor = new Color[numRows][numColumns];
        horizontalAlign = new Integer[numRows][numColumns];
        verticalAlign = new Integer[numRows][numColumns];
        fontFace = new Font[numRows][numColumns];
//        cellBorder = new Border[numRows][numColumns];
        cellBorderGen = new CustomBorderGenerator[numRows][numColumns];

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                bgColor[r][c] = Color.white;
                fgColor[r][c] = Color.black;
                horizontalAlign[r][c] = SwingConstants.LEFT;
                verticalAlign[r][c] = SwingConstants.TOP;
                fontFace[r][c] = this.getFont();
//                cellBorder[r][c] = BorderFactory.createLineBorder(Color.black);
                cellBorderGen[r][c] = new CustomBorderGenerator(1,1,1,1,Color.black);
            }
        }
    }

    /**
     * Copy constructor that creates a unique CustomTableCellRenderer with the same
     * settings as other.
     * @param other
     */
    public CustomTableCellRenderer(CustomTableCellRenderer other) {
        //Background color copy
        bgColor = new Color[other.bgColor.length][other.bgColor[0].length];
        for (int c = 0; c < other.bgColor.length; c++)
            System.arraycopy(other.bgColor[c], 0, bgColor[c], 0, other.bgColor[c].length);

        //Foreground color copy
        fgColor = new Color[other.fgColor.length][other.fgColor[0].length];
        for (int c = 0; c < other.fgColor.length; c++)
            System.arraycopy(other.fgColor[c], 0, fgColor[c], 0, other.fgColor[c].length);


        //Horizontal alignment copy
        horizontalAlign = new Integer[other.horizontalAlign.length][other.horizontalAlign[0].length];
        for (int c = 0; c < other.horizontalAlign.length; c++)
            System.arraycopy(other.horizontalAlign[c], 0, horizontalAlign[c], 0, other.horizontalAlign[c].length);

        //Vertical align copy
        verticalAlign = new Integer[other.verticalAlign.length][other.verticalAlign[0].length];
        for (int c = 0; c < other.verticalAlign.length; c++)
            System.arraycopy(other.verticalAlign[c], 0, verticalAlign[c], 0, other.verticalAlign[c].length);

        //Font face copy
        fontFace = new Font[other.fontFace.length][other.fontFace[0].length];
        for (int c = 0; c < other.fontFace.length; c++)
            System.arraycopy(other.fontFace[c], 0, fontFace[c], 0, other.fontFace[c].length);

        //Cell border copy
//        cellBorder = new Border[other.cellBorder.length][other.cellBorder[0].length];
//        for (int c = 0; c < other.cellBorder.length; c++)
//            System.arraycopy(other.cellBorder[c], 0, cellBorder[c], 0, other.cellBorder[c].length);
    }
    
    /**
     * Overriden function that is automattically called when table image is generated
     * @param table: the table to be painted
     * @param obj
     * @param isSelected: weather or not the particular cell is selected
     * @param hasFocus: does the cell have focus
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     * @return the individual cells of the table to be painted
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        String value = String.valueOf(table.getValueAt(row, column));
        if (value.compareTo("null") != 0) {
            this.setText(value);
        } else {
            this.setText(" ");
        }

        this.setFont(fontFace[row][column]);
        this.setHorizontalAlignment(horizontalAlign[row][column]);
        this.setVerticalAlignment(verticalAlign[row][column]);

        if (!isSelected) {
            this.setBackground(bgColor[row][column]);
            this.setForeground(fgColor[row][column]);
            //this.setBorder(cellBorder[row][column]);
            this.setBorder(cellBorderGen[row][column].generateBorder());
        } else {
            this.setForeground(fgColor[row][column]);
            this.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 255)));
            this.setBackground(new Color(150, 150, 255));
        }

        return this;
    }
    
    /**
     * Function used to auto width columns to fit their contents
     * @param table: the table being used
     * @param column: the column number of the column to be formatted
     */
    public void autoWidth(CustomJTable table, int column) {
        int maxWidth = 0;
        int tmpWidth = 0;
        for (int r = 0; r < table.getRowCount(); r++) {
            if (table.getHorizontalNumMerged(r, column) == 1) {
                FontMetrics fm = this.getFontMetrics(this.getFont());
                tmpWidth = fm.stringWidth(String.valueOf(table.getValueAt(r, column))) + fm.stringWidth("...");
                maxWidth = (tmpWidth > maxWidth) ? tmpWidth : maxWidth;
            }
        }
        table.getColumnModel().getColumn(column).setPreferredWidth(maxWidth);
    }

    /**
     * Sets the maximum width for a column
     * @param table: the table to be modified
     * @param column: the column to be modified
     * @param width: the maximum width for the column
     */
    public void setMaxColumnWidth(JTable table, int column, int width) {
        table.getColumnModel().getColumn(column).setMaxWidth(width);
    }

    /**
     * Gets the maximum column width
     * @param table: current working table
     * @param column: the column
     * @return the maximum width the given column in the given table can take on
     */
    public int getMaxColumnWidth(JTable table, int column) {
        return table.getColumnModel().getColumn(column).getMaxWidth();
    }

    /**
     * Sets the minumum width for a column
     * @param table: the table to be modified
     * @param column: the column to be modified
     * @param width: the minimum width for a column
     */
    public void setMinColumnWidth(JTable table, int column, int width) {
        table.getColumnModel().getColumn(column).setMinWidth(width);
    }

    /**
     * Gets the minimum width for a column
     * @param table: the current working table
     * @param column: the column
     * @return the minimum width the given column in the given table can take on
     */
    public int getMinColumnWidth(JTable table, int column) {
        return table.getColumnModel().getColumn(column).getMinWidth();
    }

    /**
     * Sets the absolute width of a column
     * @param table: the table to be modified
     * @param column: the column to be modified
     * @param width: the width of the column
     */
    public void setPreferredColumnWidth(JTable table, int column, int width) {
        table.getColumnModel().getColumn(column).setPreferredWidth(width);
    }

    /**
     * Gets the width of a column
     * @param table: the table
     * @param column: the column number
     * @return the width of the given column in the given table
     */
    public int getPreferedColumnWidth(JTable table, int column) {
        return table.getColumnModel().getColumn(column).getPreferredWidth();
    }

    /**
     * modifies the height of the row based on font size
     * @param table: the table being modified
     * @param row: the row number to modify
     */
    public void autoHeight(JTable table, int row) {
        int maxHeight = 0;
        int tmpHeight = 0;
        for (int c = 0; c < table.getColumnCount(); c++) {
            FontMetrics fm = this.getFontMetrics(this.getFont());
            //tmpHeight = fm.getMaxAscent() - fm.getMaxDescent();
            //tmpHeight = fm.getAscent() + fm.getHeight() - fm.getDescent();
            tmpHeight = fm.getHeight() - fm.getDescent();
            maxHeight = (tmpHeight > maxHeight) ? tmpHeight : maxHeight;
        }
        table.setRowHeight(row, maxHeight);
    }

    /**
     * Sets the height of the row
     * @param table: the table to be modified
     * @param row: the row to be modified
     * @param height: the height of the column
     */
    public void setRowHeight(JTable table, int row, int height) {
        table.setRowHeight(row, height);
    }

    /**
     * Gets the height of the row
     * @param table: the table
     * @param row: the row number
     * @return the height of the given row in the given table
     */
    public int getRowHeight(JTable table, int row) {
        return table.getRowHeight(row);
    }

    /**
     * sets the cell border properties
     * @param border: the border object that defines the look of the border
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     */
//    public void setCellBorder(Border border, int row, int column) {
//        cellBorder[row][column] = border;
//    }

    /**
     * get the border object for a particular cell
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     * @return the Border object that defines the look of the border for the cell at row, column
     */
//    public Border getCellBorder(int row, int column) {
//        return cellBorder[row][column];
//    }
    
    public void setCustomBorderGen(CustomBorderGenerator cbg, int row, int column) {
        cellBorderGen[row][column] = cbg;
    }
    
    public CustomBorderGenerator getCustomBorderGen(int row, int column) {
        return cellBorderGen[row][column];
    }

    /**
     * set the font for a particualr cell
     * @param face: the Font object to define the look of the text in the cell
     * @param row: row number of the cell
     * @param column: column number of the cell
     */
    public void setFontFace(Font face, int row, int column) {
        fontFace[row][column] = face;
    }

    /**
     * get the font of a particular cell
     * @param row: row number of the particual cell
     * @param column: column number of the particular cell
     * @return the Font object that defines the look of the text for a particular cell
     */
    public Font getFontFace(int row, int column) {
        return fontFace[row][column];
    }

    /**
     * sets the horizontal alignment of the text, along the X axis, using a swing constant
     * @param alignment: SwingConstant that defines the alignment of text along the X axis
     * @param row: row number of the cell
     * @param column: column number of the cell
     */
    public void setHorizontalAlignment(Integer alignment, int row, int column) {
        horizontalAlign[row][column] = alignment;
    }

    /**
     * gets the horizontal alignment, along the X axis, of the text in a specific cell
     * @param row: the row number for the cell
     * @param column: the column number for the cell
     * @return a SwingConstant complient integer for text alignment
     */
    public Integer getHorizontalAlignment(int row, int column) {
        return horizontalAlign[row][column];
    }

    /**
     * sets the vertical alignment, along the Y axis, of the text in a particular cell
     * carful using the vertical alignment, because it can cause some interesting effects
     * @param alignment: SwingConstant for vertical alignment along the Y axis
     * @param row: the row number for the cell
     * @param column: the column number for the cell
     */
    public void setVerticalAlignment(Integer alignment, int row, int column) {
        verticalAlign[row][column] = alignment;
    }

    /**
     * gets the vertical alignment, along the Y axis of the text in a particular cell
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     * @return SwingConstant compatible integer for the alignment of the text
     */
    public Integer getVerticalAlignment(int row, int column) {
        return verticalAlign[row][column];
    }

    /**
     * sets the background color of a particular cell
     * @param col: the color to be set
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     */
    public void setBackgroundColor(Color col, int row, int column) {
        bgColor[row][column] = col;
    }

    /**
     * gets the background color of a particular cell
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     * @return the color of the background of the cell
     */
    public Color getBackgroundColor(int row, int column) {
        return bgColor[row][column];
    }

    /**
     * sets the foreground color, color of the text, of a particular cell
     * @param col: the color to set as the foreground
     * @param row: the row number of the cell
     * @param column: the column number of the cell
     */
    public void setForegroundColor(Color col, int row, int column) {
        fgColor[row][column] = col;
    }

    /**
     * gets the foreground color, color of the text, of a particular cell
     * @param row: row number of the cell
     * @param column: column number of the cell
     * @return the color of the foreground (the text) of a particular cell
     */
    public Color getForegroundColor(int row, int column) {
        return fgColor[row][column];
    }
}
