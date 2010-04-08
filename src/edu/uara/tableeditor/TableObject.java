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
package edu.uara.tableeditor;

import edu.uara.wrappers.CustomTableCellRenderer;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import edu.uara.FBGFlow;
import edu.uara.cutnpaste.QueryBuilderTuple;
import edu.uara.db.QueryExecuter;
import edu.uara.gui.Utilities;
import edu.uara.gui.tableeditor.calculator.EquationString;
import edu.uara.tableeditor.query.NonEditableQueryBuilder;
import edu.uara.tableeditor.query.QueryBuilder;
import edu.uara.tables.BlankTable;
import edu.uara.wrappers.CustomCellEditor;
import edu.uara.wrappers.CustomJTable;
import edu.uara.wrappers.VisibleCell;
import edu.uara.wrappers.multiselect.CustomTableSelectionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * This class represents an editable table and all of the figures that go along
 * with it. It is the main encapsulating class for table data and table formatting.
 * The class stores the data in two separate parts. It maintains an internal CustomJTable
 * for the actual displayed data, and several two-dimensional "backend" arrays for
 * storing un-executed queries and equations. The output of the queries and equations
 * are stored in the CustomJTable's TableModel.
 * @author paul
 */
public class TableObject implements Serializable, ITableObject {

    public static final long serialVersionUID = 1;
    public static final int MIN_ROW_HEIGHT = new JTable().getRowHeight();
    public transient CustomJTable table;
    private CustomTableCellRenderer cRenderer;
    private String name;
    private String footnote;
    private ArrayList<TableFigure> tableFigures = new ArrayList<TableFigure>();
    private String tableContents[][];
    private String cellFormat[][];
    private QueryBuilder queries[][];
    private String preFix[][];
    private String postFix[][];
    private transient Point lastSelectedCell;
    private transient DataCellListener cellListener = null;

    /**
     * Constructor number one
     * @param name The name of the table
     */
    public TableObject(String name) {
        this.name = name;
        lastSelectedCell = new Point(-1, -1);
    }

    /**
     * Cunstructor number two
     * @param name The name of the table
     * @param numOfRows The number of rows in the table
     * @param numOfColumns The number of columns in the table
     */
    public TableObject(String name, int numOfRows, int numOfColumns) {
        this.name = name;
        this.setUpTable(numOfRows, numOfColumns);
        lastSelectedCell = new Point(-1, -1);
    }

    /**
     * 3rd Constructor that takes customJTable as parameter
     * @param table: CustomJTable object
     */
    public TableObject(String name, CustomJTable cTable) {
        this.name = name;
        int numOfRows = cTable.getRowCount();
        int numOfColumns = cTable.getColumnCount();
        this.name = name;
        this.table = cTable;
        tableContents = new String[numOfRows][numOfColumns];
        cellFormat = new String[numOfRows][numOfColumns];
        queries = new QueryBuilder[numOfRows][numOfColumns];
        cRenderer = (CustomTableCellRenderer) table.getColumnModel().getColumn(0).getCellRenderer();
        preFix = new String[numOfRows][numOfColumns];
        postFix = new String[numOfRows][numOfColumns];

        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        lastSelectedCell = new Point(-1, -1);

        for (int r = 0; r < numOfRows; r++) {
            for (int c = 0; c < numOfColumns; c++) {
                if (cTable.getModel().getValueAt(r, c) != null) {
                    tableContents[r][c] = cTable.getModel().getValueAt(r, c).toString();
                }
                cellFormat[r][c] = null;
                queries[r][c] = null;
                preFix[r][c] = null;
                postFix[r][c] = null;
            }
        }
    }

    /**
     * Copy constructor that creates a unique copy of the TableObject "other."
     * @param other
     */
    public TableObject(TableObject other) {
        //Create unique copies of the renderer and table
        cRenderer = new CustomTableCellRenderer(other.cRenderer);
        table = new CustomJTable(other.table);

        //Copying this stuff over is easy.
        name = other.name;
        footnote = other.footnote;
        lastSelectedCell = new Point(other.lastSelectedCell);
        //TODO: This might need reworking... might not actually copy completely.
        tableFigures = new ArrayList<TableFigure>(other.tableFigures);

        //Copy all the backend arrays
        tableContents = new String[other.tableContents.length][other.tableContents[0].length];
        for (int c = 0; c < other.tableContents.length; c++) {
            System.arraycopy(other.tableContents[c], 0, tableContents[c], 0, other.tableContents[c].length);
        }
        cellFormat = new String[other.cellFormat.length][other.cellFormat[0].length];
        for (int c = 0; c < other.cellFormat.length; c++) {
            System.arraycopy(other.cellFormat[c], 0, cellFormat[c], 0, other.cellFormat[c].length);
        }
        queries = new QueryBuilder[other.queries.length][other.queries[0].length];
        for (int c = 0; c < other.queries.length; c++) {
            System.arraycopy(other.queries[c], 0, queries[c], 0, other.queries[c].length);
        }
        table.getTableSelectionModel().addTableSelectionListener(new CustomTableSelectionListener());
    }

    /**
     * Needs to be called if the first constructor is used. If a table already exists this will clear it out.
     * @param numRows The number of rows in the table
     * @param numColumns The number of columns in the table
     */
    public void setUpTable(int numOfRows, int numOfColumns) {
        BlankTable blTable = new BlankTable();
        table = blTable.gernerateBlankTable(numOfRows, numOfColumns);
        cRenderer = new CustomTableCellRenderer(numOfRows, numOfColumns);
        tableContents = new String[numOfRows][numOfColumns];
        cellFormat = new String[numOfRows][numOfColumns];
        queries = new QueryBuilder[numOfRows][numOfColumns];
        preFix = new String[numOfRows][numOfColumns];
        postFix = new String[numOfRows][numOfColumns];

        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        this.setCustomTableCellRenderer(cRenderer);

        for (int r = 0; r < numOfRows; r++) {
            for (int c = 0; c < numOfColumns; c++) {
                tableContents[r][c] = "";
                cellFormat[r][c] = null;
                queries[r][c] = null;
                postFix[r][c] = null;
                preFix[r][c] = null;
            }
        }

        table.getTableSelectionModel().addTableSelectionListener(new CustomTableSelectionListener());
    }

    /**
     * Custom serialization code for writing the TableObject. We discovered that
     * serializing the JTable directly was impossible, or at the least very hard
     * and esoteric. This method basically writes all data about the table without
     * serializing the JTable itself.
     * @param out
     * @throws java.io.IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        System.out.println("Saving table " + this);
        out.defaultWriteObject();

        //We now begin serializing data about the table
        //First, the actual data
        DefaultTableModel tm = (DefaultTableModel) table.getModel();
        out.writeObject(tm.getDataVector());
        out.writeObject(table.getVisibleCell());

        //Now serialize columns: names and widths.
        Vector<String> columns = new Vector<String>();
        Vector<Integer> columnWidths = new Vector<Integer>();
        for (int c = 0; c < tm.getColumnCount(); c++) {
            columns.add(tm.getColumnName(c));
            columnWidths.add(table.getColumnModel().getColumn(c).getPreferredWidth());
        }

        out.writeObject(columns);
        out.writeObject(columnWidths);

        //Finally serialize row heights
        Vector<Integer> rowHeights = new Vector<Integer>();
        for (int c = 0; c < table.getRowCount(); c++) {
            rowHeights.add(table.getRowHeight(c));
        }

        out.writeObject(rowHeights);
    }

    /**
     * Custom code for reading the state of this TableObject back in. Since we
     * do not serialize the JTable itself, we must construct it from scratch.
     * @param in
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.out.println("Loading table " + this);

        Vector data = (Vector) in.readObject();
        VisibleCell[][] cells = (VisibleCell[][]) in.readObject();
        Vector<String> columns = (Vector<String>) in.readObject();
        Vector<Integer> columnWidths = (Vector<Integer>) in.readObject();
        Vector<Integer> rowHeights = (Vector<Integer>) in.readObject();

        table = new CustomJTable(tableContents.length, tableContents[0].length);

        //Restore the data and column names and visible cells
        DefaultTableModel tm = (DefaultTableModel) table.getModel();
        tm.setDataVector(data, columns);
        table.setModel(tm);
        table.setVisibleCell(cells);

        //Restore the column widths
        for (int c = 0; c < columnWidths.size(); c++) {
            table.getColumnModel().getColumn(c).setPreferredWidth(columnWidths.get(c));
        }

        //Restore row heights
        for (int c = 0; c < rowHeights.size(); c++) {
            table.setRowHeight(c, rowHeights.get(c));
        }

        //Set cell editor since it seems to be getting lost somewhere
        CustomCellEditor editor = new CustomCellEditor();
        for (int c = 0; c < table.getColumnCount(); c++) {
            table.getColumnModel().getColumn(c).setCellEditor(editor);
        }

        table.setCellSelectionEnabled(true);
        setCustomTableCellRenderer(cRenderer);
        setTableContents(tableContents);
        lastSelectedCell = new Point(-1, -1);
    }

    /**
     * Fills a table with a 2D array of cell contents
     * @param contents The 2D array of the same size of the table
     * @return true if contents are the same size of the table, false if not
     */
    public boolean setTableContents(String contents[][]) {
        if (contents.length == table.getRowCount() && contents[0].length == table.getColumnCount()) {
            for (int r = 0; r < table.getRowCount(); r++) {
                for (int c = 0; c < table.getColumnCount(); c++) {
                    if (contents[r][c] != null) {
                        //this.updateView(contents[r][c], r, c);
                        this.addCellContents(contents[r][c], r, c);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * For pasting table contents formatted in the \t \n table copy format.
     * @param startRow The first row value of the selected cells
     * @param startCol The first column value of the selected cells
     * @param contents The contents to be placed
     */
    public void pasteTableContents(int startRow, int startCol, String contents) {
        String[] splitContents = contents.split("\n");

        for (int r = 0; r < splitContents.length; r++) {
            String[] rowContents = splitContents[r].split("\t");

            for (int c = 0; c < rowContents.length; c++) {
                if (rowContents[c] != null && (rowContents[c].equals("[null]") == false)) {
                    try {
                        String cellData = rowContents[c].trim();
                        addCellContents(cellData, r + startRow, c + startCol);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //Silently ignore anything that goes out of table bounds.
                    }
                }
            }
        }
    }
    
    public void pasteQueryBuilders(int startRow, int startCol, QueryBuilderTuple ... queries) {
        if (queries != null) {
            for (int c = 0; c < queries.length; c++) {
                try {
                    addQueryBuilder(queries[c].query, queries[c].row + startRow, queries[c].column + startCol);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    //Silently ignore anything that goes out of table bounds.
                }
            }
        }
    }

    /**
     * For getting the entirty of the table contents
     * @return
     */
    public String[][] getTableContents() {
        return this.tableContents;
    }

    /**
     * For setting the table's model
     * @param model The table model
     */
    public void setTableModel(TableModel model) {
        this.table.setModel(model);
    }

    /**
     * For getting the table's model
     * @return The table's model
     */
    public TableModel getTableModel() {
        return this.table.getModel();
    }

    /**
     * For retrieving the CustomTableCellRenderer
     * @return The current CustomTableCellRenderer
     */
    public CustomTableCellRenderer getCustomTableCellRenderer() {
        return cRenderer;
    }

    /**
     * For Setting the CustomTableCellRenderer
     * @param render The CustomTableCellRenderer to set
     */
    public void setCustomTableCellRenderer(CustomTableCellRenderer render) {
        cRenderer = render;
        table.setDefaultRenderer(Object.class, cRenderer);
    }

    /**
     * Gets a copy of the table for use in outputting tables to images
     * @return A copy of the table
     */
    @Override
    public JTable getTable() {
        BlankTable blTable = new BlankTable();
        CustomJTable pTable = blTable.gernerateBlankTable(table.getRowCount(), table.getColumnCount());
        pTable.setColumnModel(table.getColumnModel());
        pTable.setDefaultRenderer(Object.class, cRenderer);
        for (int r = 0; r < table.getRowCount(); r++) {
            for (int c = 0; c < table.getColumnCount(); c++) {
                pTable.setRowHeight(r, table.getRowHeight());
                if (tableContents[r][c] == null) {
                    pTable.getModel().setValueAt("", r, c);
                } else if (tableContents[r][c].startsWith(":=")) {
                    pTable.getModel().setValueAt(formatCell(this.executeQueryCell(this.queries[r][c].generateQuery()), r, c), r, c);
                } else if (tableContents[r][c].startsWith("=")) {
                    //pTable.getModel().setValueAt(formatCell, r, c);
                    pTable.getModel().setValueAt(formatCell(FBGFlow.getOpenProject().getTableEditorConstants().convertString(this.executeEquationCell(this.tableContents[r][c])), r, c), r, c);
                } else {
                    pTable.getModel().setValueAt(formatCell(FBGFlow.getOpenProject().getTableEditorConstants().convertString(this.tableContents[r][c]), r, c), r, c);
                }

            }
        }
        pTable.setVisibleCell(table.getVisibleCell());
        return pTable;
    }

    /**
     * For adding prefixes and postfixes to the cell
     * @param value The value inside the cell
     * @param row The row number of the cell
     * @param column The column number of the cell
     * @return The contents of the cell with pre and post fix components
     */
    private String formatCell(String value, int row, int column) {
        String tPreFix = null;
        String tPostFix = null;
        Formatter form = new Formatter();

        if (preFix[row][column] == null) {
            tPreFix = "";
        } else {
            tPreFix = preFix[row][column];
        }
        if (postFix[row][column] == null) {
            tPostFix = "";
        } else {
            tPostFix = postFix[row][column];
        }
        if (value == null) {
            return form.format("%s%s", tPreFix, tPostFix).toString();
        }

        try {
            double dValue = Double.parseDouble(value);
            int iValue = (int) dValue;
            if (dValue == iValue) {
                return form.format("%s%d%s", tPreFix, iValue, tPostFix).toString();
            }

            return form.format("%s%.2f%s", tPreFix, dValue, tPostFix).toString();
        } catch (Exception e) {
            return form.format("%s%s%s", tPreFix, value, tPostFix).toString();
        }
    }

    /**
     * For adding a QueryBuilder object to one of the cells in the table
     * @param bldr The QueryBuilder to be added (or replaced)
     * @param rowNum The row of the cell
     * @param columnNum The column of the cell
     */
    public void addQueryBuilder(QueryBuilder bldr, int rowNum, int columnNum) {
        queries[rowNum][columnNum] = bldr;
        String query = ":=" + bldr.toString();
        addCellContents(query, rowNum, columnNum);
    }

    /**
     * For retrieving a QueryBuilder that has already been added
     * @param rowNum The row number of the cell
     * @param columnNum The column number of the cell
     * @return The QueryBuilder in the requested cell. Null if there is none
     */
    public QueryBuilder getQueryBuilder(int rowNum, int columnNum) {
        return queries[rowNum][columnNum];
    }

    /**
     * For adding an equation from the equation GUI
     * @param eqString the equation string object
     * @param rowNum the row number for where the table is going
     * @param columnNum the column number for where the table is going
     */
    public void addEquation(EquationString eqString, int rowNum, int columnNum) {
        String eq = "=" + eqString.getEquation();
        addCellContents(eq, rowNum, columnNum);
    }

    /**
     * For adding cell contents. := is for a query, = is for an equation, plain text for anything else
     * @param contents What will be added to the cell
     * @param rowNum The cell's row number
     * @param columnNum The cell's column number
     */
    public void addCellContents(String contents, int rowNum, int columnNum) {
        tableContents[rowNum][columnNum] = contents;
        this.table.getModel().setValueAt(contents, rowNum, columnNum);
    }
    
    /**
     * Updates a particular cell
     * @param contents The contents that will be placed into the cell
     * @param rowNum The row number of the cell
     * @param columnNum The column number of the cell
     */
    public void updateView(String contents, int rowNum, int columnNum) {
        if (contents.startsWith(":=")) {
            if (this.getQueryBuilder(rowNum, columnNum) == null) {
                this.queries[rowNum][columnNum] = new NonEditableQueryBuilder(contents, FBGFlow.getOpenProject().getTableEditorConstants());
            }
            if (this.getQueryBuilder(rowNum, columnNum) instanceof NonEditableQueryBuilder) {
                ((NonEditableQueryBuilder) this.getQueryBuilder(rowNum, columnNum)).setQueryString(contents);
            }
            String toAdd = "";
            if (!this.getQueryBuilder(rowNum, columnNum).toString().startsWith(":=")) {
                toAdd = ":=";
            }
            tableContents[rowNum][columnNum] = toAdd + this.getQueryBuilder(rowNum, columnNum).toString();
            addQueryToCell(this.getQueryBuilder(rowNum, columnNum).generateQuery(), rowNum, columnNum);
        } else {
            this.queries[rowNum][columnNum] = null;
            if (contents.startsWith("=")) {
                tableContents[rowNum][columnNum] = contents;
                addEquationToCell(FBGFlow.getOpenProject().getTableEditorConstants().convertString(contents), rowNum, columnNum);
            } else {
                tableContents[rowNum][columnNum] = contents;
                table.getModel().setValueAt(this.formatCell(FBGFlow.getOpenProject().getTableEditorConstants().convertString(contents), rowNum, columnNum), rowNum, columnNum);
            }
        }

        int numOfColumns = table.getColumnCount();
        int numOfRows = table.getRowCount();
        String tmpStr = null;
        String tmpStrTwo = null;
        boolean modified = true;

        while (modified) {
            modified = false;
            for (int r = 0; r < numOfRows; r++) {
                for (int c = 0; c < numOfColumns; c++) {
                    tmpStr = this.tableContents[r][c];
                    if (tmpStr != null) {
                        if (tmpStr.length() > 0) {
                            if (tmpStr.charAt(0) == '=') {
                                tmpStrTwo = table.getModel().getValueAt(r, c).toString();
                                this.addEquationToCell(tmpStr, r, c);
                                if (!tmpStrTwo.equals(table.getModel().getValueAt(r, c).toString())) {
                                    modified = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        //Calling cell listener
        if (this.cellListener != null) {
            cellListener.updateDataCellContents();
        }
    }

    /**
     * For adding and executing a query to a cell
     * @param query The query to be added and used
     * @param rowNum The cell's row number
     * @param columnNum The cell's column number
     */
    private void addQueryToCell(String query, int rowNum, int columnNum) {
        String result = executeQueryCell(query);

        try {
            Double dResult = Double.parseDouble(result);
            placeCellContents(dResult, rowNum, columnNum);
        } catch (NumberFormatException e) {
            table.getModel().setValueAt(this.formatCell(result, rowNum, columnNum), rowNum, columnNum);
        }

    }

    /**
     * For executing a query
     * @param query The query to be exicuted
     * @return The string that is produced by the executed query
     */
    private String executeQueryCell(String query) {
        query = query.replaceFirst(":=", "");
        String result = null;

        try {
            result = QueryExecuter.executeQuery(query);
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
            result = "#SQL!";
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Could not locate database driver!");
            result = "#SQL!";
        }
        return result;
    }

    /**
     * For adding and executing an equation in a cell
     * @param equation The equation to be added and exicuted
     * @param rowNum The cell's row number
     * @param columnNum The cell's column number
     */
    private String addEquationToCell(String equation, int rowNum, int columnNum) {
        equation = FBGFlow.getOpenProject().getTableEditorConstants().convertString(equation);
        String result = executeEquationCell(equation);

        try {
            Double dResult = Double.parseDouble(result);
            placeCellContents(dResult, rowNum, columnNum);
        } catch (NumberFormatException e) {
            table.getModel().setValueAt(this.formatCell(result, rowNum, columnNum), rowNum, columnNum);
        }
        return result;

    }

    /**
     * For executing an equation
     * @param equation The equation to be executed
     * @return the string value of the executed equation
     */
    private String executeEquationCell(String equation) {
        equation = equation.replaceFirst("=", "");
        String eq = equation.replace(" ", "");
        eq = eq.toUpperCase();
        String tmpString = null;
        int indexOne = 0, indexTwo = 0;
        Double tmpValue = null;

        while ((indexOne = eq.indexOf("N")) != -1) {
            if (indexOne == 0) {
                indexTwo = eq.indexOf(")");
                tmpString = eq.substring(indexOne, indexTwo + 1);
                tmpValue = this.getValueFromEquCell(tmpString);
            } else {
                if (eq.charAt(indexOne - 1) == '(') {
                    indexOne -= 4;
                    //indexTwo = eq.indexOf(")", eq.indexOf(")", eq.indexOf(")") + 1) + 1);
                    indexTwo = eq.indexOf("(", indexOne);
                    int balance = 1;
                    while (balance != 0) {
                        indexTwo++;
                        if (indexTwo >= eq.length()) {
                            return "#EQ!";
                        }

                        if (eq.charAt(indexTwo) == '(') {
                            balance++;
                        } else if (eq.charAt(indexTwo) == ')') {
                            balance--;
                        }
                    }

                    if (eq.substring(indexOne, indexOne + 3).compareTo("SUM") == 0) {
                        tmpValue = this.doSum(eq.substring(indexOne, indexTwo + 1));
                    } else if (eq.substring(indexOne, indexOne + 3).compareTo("AVG") == 0) {
                        tmpValue = this.doAvg(eq.substring(indexOne, indexTwo + 1));
                    } else {

                        return "#EQ!";
                    }
                } else {
                    indexTwo = eq.indexOf(")", indexOne);
                    tmpString = eq.substring(indexOne, indexTwo + 1);
                    tmpValue = this.getValueFromEquCell(tmpString);
                }
            }

            if (tmpValue == null) {

                return "#EQ!";
            }

            tmpString = eq.substring(0, indexOne) + tmpValue.toString() + eq.substring(indexTwo + 1);
            eq = tmpString;
        }
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        try {
            Object finalVal = jsEngine.eval(eq);
            return finalVal.toString();
        } catch (ScriptException e) {
            return "#EQ!";
        }
    }

    /**
     * Performs the average function for the equation
     * @param function The formatted average function from an equaiton
     * @return The average of the selected cells
     */
    private Double doAvg(String function) {
        Double value = 0.0;
        int cellCount = 0;

        String func = function.substring(function.indexOf("(") + 1);

        String[] params = func.split(";");

        if (func.indexOf(";") == -1) {
            params = new String[1];
            params[0] = func;
        }

        try {
            for (int x = 0; x < params.length; x++) {
                String param = params[x].trim();
                boolean isRange = false;
                if (param.indexOf(":") != -1) {
                    isRange = true;
                }
                if (isRange) {
                    //Range of cells
                    String[] range = param.split(":");
                    String first = range[0].trim();
                    String last = range[1].trim();

                    int[] fCell = this.getCellFromEquation(first);
                    int[] lCell = this.getCellFromEquation(last);

                    Double tmpValue = null;
                    for (int r = fCell[0]; r <= lCell[0]; r++) {
                        for (int c = fCell[1]; c <= lCell[1]; c++) {
                            tmpValue = this.getTableCellValue(r, c);
                            if (tmpValue == null) {
                                return null;
                            }
                            value += tmpValue;
                            cellCount++;
                        }
                    }
                } else {
                    //Single cell
                    int[] cell = this.getCellFromEquation(param);
                    value += this.getTableCellValue(cell[0], cell[1]);
                    cellCount++;
                }
            }
        } catch (NullPointerException e) {
            return null;
        }

        value = value / cellCount;
        return value;
    }

    /**
     * Perform the summation function for the equations
     * @param function the formated summation string for the equation
     * @return the value of the summation
     */
    private Double doSum(String function) {
        Double value = 0.0;

        String func = function.substring(function.indexOf("(") + 1);

        String[] params = func.split(";");

        if (func.indexOf(";") == -1) {
            params = new String[1];
            params[0] = func;
        }
        try {
            for (int x = 0; x < params.length; x++) {
                String param = params[x].trim();
                boolean isRange = false;
                if (param.indexOf(":") != -1) {
                    isRange = true;
                }
                if (isRange) {
                    //Range of cells
                    String[] range = param.split(":");
                    String first = range[0].trim();
                    String last = range[1].trim();

                    int[] fCell = this.getCellFromEquation(first);
                    int[] lCell = this.getCellFromEquation(last);

                    Double tmpValue = null;
                    for (int r = fCell[0]; r <= lCell[0]; r++) {
                        for (int c = fCell[1]; c <= lCell[1]; c++) {
                            tmpValue = this.getTableCellValue(r, c);
                            if (tmpValue == null) {
                                return null;
                            }
                            value += tmpValue;
                        }
                    }
                } else {
                    //Single cell
                    int[] cell = this.getCellFromEquation(param);
                    value += this.getTableCellValue(cell[0], cell[1]);
                }
            }
        } catch (NullPointerException e) {
            return null;
        }

        return value;
    }

    /**
     * Gets the value from a cell using the equation format of N(r,c)
     * @param cell The cell in equation format of N(r,c)
     * @return The value in the cell
     */
    private Double getValueFromEquCell(String cell) {
        int cellNums[] = getCellFromEquation(cell);
        return this.getTableCellValue(cellNums[0], cellNums[1]);
    }

    /**
     * For retrieving a cell from a string formated as N(r,c)
     * @param value The string value for the cell in the format N(r,c)
     * @return an array containing the row and cell numbers
     */
    private int[] getCellFromEquation(String value) {
        int cell[] = new int[2];
        String values[] = new String[2];
        String tmpValue = null;
        int comma = 0;

        tmpValue = value.replace("N(", "");
        tmpValue = tmpValue.replace(")", "");
        comma = tmpValue.indexOf(",");

        values[0] = tmpValue.substring(0, comma);
        values[1] = tmpValue.substring(comma + 1);

        for (int i = 0; i < values.length; i++) {
            try {
                cell[i] = Integer.parseInt(values[i]) - 1;
            } catch (NumberFormatException e) {
                char charVals[] = values[i].toCharArray();
                int val = 0;
                for (int rc = 0; rc < charVals.length; rc++) {
                    int powVal = charVals.length - (rc + 1);
                    if (charVals[rc] >= 'a' && charVals[rc] <= 'z') {
                        val += (charVals[rc] - 'a' + powVal) * (int) Math.pow(26, powVal);
                    } else if (charVals[rc] >= 'A' && charVals[rc] <= 'Z') {
                        val += (charVals[rc] - 'A' + powVal) * (int) Math.pow(26, powVal);
                    }
                }
                cell[i] = val;
            }
        }
        return cell;
    }

    /**
     * For retrieving the value (number) in the cell
     * @param row The row of the cell
     * @param column The column of the cell
     * @return The number contained in the cell or NULL if the cell does not contain a number
     */
    public Double getTableCellValue(int row, int column) {
        Double value = null;
        Object tmpValue = null;
        tmpValue = table.getModel().getValueAt(row, column);
        if (tmpValue == null) {
            return null;
        }
        try {
            value = Double.parseDouble(tmpValue.toString());
            return value;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * For retrieving the string from a table cell
     * @param row The row of the cell
     * @param column The column of the cell
     * @return The string contained in the cell
     */
    public String getTableCellString(int row, int column) {
        return table.getModel().getValueAt(row, column).toString();
    }

    /**
     * For adding a format to a cell
     * @param format A format string to be used to format the cell's contents
     * @param rowNum The cell's row number
     * @param columnNum The cell's column number
     */
    public void addFormatToCell(String format, int rowNum, int columnNum) {
    }

    /**
     * Refreshes each individual cells, used when things are modified
     * @param row The row number of the cell
     * @param column The column number of the cell
     */
    public void refreshCellModel(int row, int column) {
        this.updateView(this.tableContents[row][column], row, column);
    }

    /**
     * To refresh the table model, used when things are modified
     */
    public void refreshTableModel() {
        for (int r = 0; r < this.tableContents.length; r++) {
            for (int c = 0; c < this.tableContents[r].length; c++) {
                refreshCellModel(r, c);
            }
        }
    }

    /**
     * For placing the final value into a cell
     * @param contents The contents to be added
     * @param rowNum The cell's row number
     * @param columnNum The cell's column number
     */
    private void placeCellContents(String contents, int rowNum, int columnNum) {
        String value = null;
        if (cellFormat[rowNum][columnNum] != null) {
            value = String.format(this.cellFormat[rowNum][columnNum], contents);
        } else {
            value = contents;
        }
        table.getModel().setValueAt(value, rowNum, columnNum);
    }

    /**
     * For placing the final value into a cell
     * @param contents The contents to be added
     * @param rowNum The cell's row number
     * @param columnNum The cell's column number
     */
    private void placeCellContents(Double contents, int rowNum, int columnNum) {
        String value = this.formatCell(contents.toString(), rowNum, columnNum);
        table.getModel().setValueAt(value, rowNum, columnNum);
    }

    /**
     * For getting the selected cells in a table
     * @return A 2-D array containing the row and column coordinates for each cell
     */
    public int[][] getSelectedCells() {
        int selected[][] = null;
        int rowStart = table.getSelectedRow();
        int rowEnd = table.getSelectionModel().getMaxSelectionIndex();
        int columnStart = table.getSelectedColumn();
        int columnEnd = table.getColumnModel().getSelectionModel().getMaxSelectionIndex();
        int index = 0;
        int numSelected = 0;

        if (rowStart < 0 || columnStart < 0) {
            return null;
        }
        if (rowStart < 0 || columnStart < 0) {
            return null;
        }
        for (int r = rowStart; r <= rowEnd; r++) {
            for (int c = columnStart; c <= columnEnd; c++) {
                if (table.isCellSelected(r, c)) {
                    numSelected++;
                }
            }
        }

        if (numSelected < 1) {
            return null;        //Do we have anything to return?
        }
        if (numSelected == 0) {
            return null;        //Yes! Continue!
        }
        selected = new int[numSelected][2];

        for (int r = rowStart; r <= rowEnd; r++) {
            for (int c = columnStart; c <= columnEnd; c++) {
                if (table.isCellSelected(r, c)) {
                    selected[index][0] = r;
                    selected[index][1] = c;
                    //selectedCell[0] = r;
                    //selectedCell[1] = c;
                    //selected[index++] = selectedCell;
                    index++;
                }
            }
        }

        return selected;
    }
    
    /**
     * Getse an array of selected query builders.
     * @return An array of selected query builders, or null if there are none.
     */
    public QueryBuilderTuple[] getSelectedQueryBuilders() {
        int[][] cells = getSelectedCells();
        int rowOffset = cells[0][0];
        int colOffset = cells[0][1];
        
        LinkedList<QueryBuilderTuple> list = new LinkedList<QueryBuilderTuple>();
        
        for (int c = 0; c < cells.length; c++) {
            int row = cells[c][0];
            int col = cells[c][1];
            String value = getTableContents(row, col);
            
            //Is it a query? If so, test to see if it has a querybuilder
            //and then add it to our list.
            if (value.startsWith(":=")) {
                QueryBuilder qb = getQueryBuilder(row, col);
                if (qb != null) {
                    QueryBuilderTuple qbt = new QueryBuilderTuple();
                    qbt.row = row - rowOffset;
                    qbt.column = col - colOffset;
                    qbt.query = qb;
                    list.add(qbt);
                }
            }
        }
        
        if (list.size() > 0) {
            return list.toArray(new QueryBuilderTuple[0]);
        }
        else {
            return null;
        }
    }

    /**
     * Gets the contents of the cell (aka, the stuff behind the cell, not what is displayed)
     * @param row The row of the cell
     * @param column The column of the cell
     * @return The background infomation stored in the cell
     */
    public String getTableContents(int row, int column) {
        return this.tableContents[row][column];
    }

    public void setPreFix(String sPreFix, int row, int column) {
        preFix[row][column] = sPreFix;
    //this.refreshCellModel(row, column);
    }

    public String getPreFix(int row, int column) {
        return preFix[row][column];
    }

    public void setPostFix(String sPostFix, int row, int column) {
        postFix[row][column] = sPostFix;
    //this.refreshCellModel(row, column);
    }

    public String getPostFix(int row, int column) {
        return postFix[row][column];
    }

    /**
     * Adds a figure to this TableObject.
     * @param figure
     */
    @Override
    public void addFigure(TableFigure figure) {
        tableFigures.add(figure);
    }

    /**
     * Sets the footnote for this particular table.
     * @param note
     */
    public void setFootnote(String note) {
        footnote = note;
    }

    /**
     * get data from a cell with provided cellReference
     * @param ref: reference to data cell (row, column)
     * @return: value at the referenced cell, if null return emtry string
     */
    @Override
    public String getDataAtReference(CellReferenceObject ref) {
        try {
            int row = ref.getRow();
            int col = ref.getColumn();
            String value = table.getValueAt(row, col).toString();
            
            String prefix = this.getPreFix(row, col);
            String postfix = this.getPostFix(row, col);
            
            try
            {
                value = value.replaceAll(Matcher.quoteReplacement(prefix), "");
                value.trim();
            }
            catch(Exception ex)
            {
                value.trim();
            }
            try
            {
                value = value.replaceAll(Matcher.quoteReplacement(postfix), "");
                value.trim();
            }
            catch(Exception ex)
            {
                value.trim();
            }
            
            return value;
        } catch (Exception ex)//empty cell
        {
            return "";
        }
    }

    /**
     * parse title for the chart
     * @return the title string on the first row which is merged
     */
    @Override
    public String getTitle() {
        String n = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.checkMergedCell(0, i)) {
                n = table.getValueAt(0, i).toString();
                break;
            }
        }
        if(n != null)
            n = n + " " + this.getCurrentYearString();
        return n;
    }

    /**
     * return current year string
     * @return
     */
    private String getCurrentYearString()
    {
        String[] keys = FBGFlow.getOpenProject().getTableEditorConstants().
                        getKeyNames();
        String keyName = "";
        for(String k : keys)
        {
            String keyTrimmed = k.toLowerCase();
            if(keyTrimmed.contains("current year"))
                keyName = k;
            else if(keyTrimmed.contains("currentyear"))
                keyName = k;
            else if(keyTrimmed.contains("thisyear"))
                keyName = k;
            else if(keyTrimmed.contains("this year"))
                keyName = k;
        }
        if(keyName != null)
            return FBGFlow.getOpenProject().
                getTableEditorConstants().
                convertString(keyName);
        else
            return "";
    }
    /**
     * Gets the name of the table.
     * @return The name of this table.
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Gets the footnote.
     * @return
     */
    public String getFootnote() {
        return footnote;
    }
    
    /**
     * Add data cell listener
     */
    @Override
    public void addDataCellListener(DataCellListener d) {
        cellListener = d;
    }

    /**
     * Returns the name of this table.
     * @return The name of this table.
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Returns all TableFigures in array format.
     * @return
     */
    @Override
    public TableFigure[] getFigures() {
        return (TableFigure[]) (tableFigures.toArray(new TableFigure[0]));
    }

    public void setStuff(TableObject to) {
        System.out.println("restoring table: " + to);

        cRenderer = to.cRenderer;
        table = to.table;

        name = to.name;
        footnote = to.footnote;
        tableFigures = to.tableFigures;

        //Copy all the backend arrays
        tableContents = to.tableContents;
        cellFormat = to.cellFormat;
        queries = to.queries;
    }

    /**
     * remove tableFigure object from collection
     * @param f
     */
    public void removeFigure(TableFigure f) {
        this.tableFigures.remove(f);
    }

    public void setLastSelectedCell(int row, int column) {
        this.lastSelectedCell.setLocation(row, column);
    }

    public Point getLastSelectedCell() {
        return this.lastSelectedCell;
    }

    /**
     * Gets selected cells for copying. This does not
     * allow selection of multiple "sections" of the table. That is, everything
     * must be adjacent to one another. What is selected must be a box. The cells
     * are returned as a series of string values. Rows are separated by newline
     * characters and individual cells in a row are separated by tabs.
     * @return A string containing the selected cells with the structure described.
     */
    public String getSelectedCopyableCells() {
        //The string of selected cell data to return.
        String selected = "";

        //First used for the actual end of the table.
        //Later set to the end of our selection area.
        int rowEnd = table.getRowCount() - 1;
        int columnEnd = table.getColumnCount() - 1;

        //Find row and column start by parsing.
        //More sophisticated because of the way you can select discontiguous
        //cells.
        //The -1 will allow us to catch ourselves below should we not
        //find anything
        int rowStart = -1;
        int columnStart = -1;

        outer:
        for (int r = 0; r <= rowEnd; r++) {
            for (int c = 0; c <= columnEnd; c++) {
                if (table.isCellSelected(r, c)) {
                    rowStart = r;
                    columnStart = c;
                    break outer;
                }
            }
        }

        //If they're less than 1, nothing was selected.
        if (rowStart < 0 || columnStart < 0) {
            return null;
        }

        //Parse backwards until we find the first cell that's selected
        //This is the end of our selection area. Note the use of labels. Yay!
        //Since we're expecting a box we can set both row and column end here.
        outer:
        for (int r = rowEnd; r >= rowStart; r--) {
            for (int c = columnEnd; c >= columnStart; c--) {
                if (table.isCellSelected(r, c)) {
                    rowEnd = r;
                    columnEnd = c;
                    break outer;
                }
            }
        }

        //Actually create the string array.
        for (int r = rowStart; r <= rowEnd; r++) {
            for (int c = columnStart; c <= columnEnd; c++) {
                if (table.isCellSelected(r, c)) {
                    String contents = tableContents[r][c];
                    if (contents == null) {
                        //Leet hax. This space is necessary
                        //So when pasting we can clear out the
                        //box to paste into. It gets trimmed
                        //there.
                        selected += " \t";
                    } else {
                        contents = contents.trim();
                        if (contents.equals("")) //Leet hax. This space is necessary
                        //So when pasting we can clear out the
                        //box to paste into. It gets trimmed
                        //there.
                        {
                            selected += " \t";
                        } else {
                            selected += contents + "\t";
                        }
                    }
                } else {
                    Utilities.displayError(null, "Illegal Operation", "You cannot copy multiple sections.");
                    return null;
                }
            }
            selected += "\n";
        }
        return selected;
    }

    @Override
    public boolean equals(Object other) {
        if ((other == null) || (other instanceof TableObject == false)) {
            return false;
        }
        int thisHash = hashCode();
        int otherHash = other.hashCode();
        return (thisHash == otherHash);
    }

    @Override
    public int hashCode() {
        int code = 0;

        for (int r = 0; r < tableContents.length; r++) {
            for (int c = 0; c < tableContents[0].length; c++) {
                if (tableContents[r][c] != null) {
                    code += tableContents[r][c].hashCode();
                }
            }
        }

        return code;
    }
}
