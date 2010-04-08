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
package edu.uara.project;

import edu.uara.gui.Utilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.uara.tableeditor.TableEditorConstants;
import edu.uara.tableeditor.TableFigure;
import edu.uara.tableeditor.TableObject;
import java.io.*;

/**
 * This class represents a "project." This contains information about an individual
 * factbook: the listing of tables (and all the figures related to that table). 
 * The class is non-instantiatable; only being created by calling the static 
 * loadFactbook method.
 * @author jeff
 */
public class FactbookProject implements Serializable {
    public static final int serialVersionUID = 1;
        
    private String factbookName;
    private String description;
    private TableEditorConstants cons;
    private HashMap<String, TableObject> tables;
    

    /**
     * Creates a new factbook project, initializing the tables map to an empty
     * map.
     */
    public FactbookProject() {
        tables = new HashMap<String, TableObject>();
        cons = new TableEditorConstants();
    }

    /**
     * Generates a DefaultTreeModel from this project's elements. At the root of this
     * tree model is the project's name (stored internally as a string). The next
     * level of elements comprise the tables (stored internally as TableObjects).
     * Each TableObject then has the charts attached to it.
     * @return The generated tree model.
     */
    public DefaultTreeModel generateTree() {
        DefaultTreeModel tree;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.factbookName);
        
        //For each table:
        for (TableObject t : tables.values()) {
            System.out.println("Adding table " + t);
            //Add to root
            DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(t);
            root.add(tableNode);
            
            //For each figure
            for (TableFigure f : t.getFigures()) {
                //Add to table node
                DefaultMutableTreeNode figureNode = new DefaultMutableTreeNode(f);
                tableNode.add(figureNode);
            }
        }
            
        tree = new DefaultTreeModel(root);
        
        return tree;
    }

    /**
     * Returns the name of this factbook.
     * @return The name of this factbook.
     */
    public String getName() {
        return factbookName;
    }

    /**
     * Sets the name of this factbook.
     * @param name
     */
    public void setName(String name) {
        factbookName = name;
    }

    /**
     * Sets the description of this factbook. Generally it is a longer form of
     * the name, possibly containing information about the authors, the year, and
     * more.
     * @param desc
     */
    public void setDescription(String desc) {
        description = desc;
    }

    /**
     * Returns the description.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Serializes this factbook project into the specified File.
     * @param f
     */
    public void save(File f) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(this);
        }
        catch (IOException e) {
            Utilities.displayError(null, "Error!", "Error saving project file: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Serializes this factbook project into the specified file pointed to by <code>savePath.</code>
     * @param savePath
     */
    public void save(String savePath) {
        try {

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savePath));
            oos.writeObject(this);
        }
        catch (IOException e) {
            Utilities.displayError(null, "Error!", "Error saving project file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Static helper method for loading a factbook project via deserialization.
     * @param filename The file to load, pointed to by a string.
     * @return The completely loaded factbook project.
     * @throws java.io.IOException If there was an error loading the factbook.
     */
    public static FactbookProject loadFactbook(String filename) throws IOException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            FactbookProject p = (FactbookProject)ois.readObject();
            return p;
        }
        catch (ClassNotFoundException e) {
            System.err.println("Couldn't find the class to load!");
            return null;
        }
        
    }

    /**
     * Returns the specified TableObject based on the name given. TableObjects
     * are stored internally as a HashMap with String keys.
     * @param name
     * @return The TableObject, if it exists. Otherwise, null is returned.
     */
    public TableObject getTable(String name) {
        return tables.get(name);
    }

    /**
     * Returns all of the TableObjects in this FactbookProject, encapsulated inside
     * a Collection.
     * @return The Collection of tables.
     */
    public Collection getTables() {
        return tables.values();
    }

    /**
     * Returns all TableObjects in this FactbookProject, encapsulated inside an
     * ArrayList.
     * @return The list of tables.
     */
    public ArrayList<TableObject> getTableObjects() {
    	ArrayList<TableObject> list = new ArrayList<TableObject>();
    	for(String s : tables.keySet()) {
    		list.add(tables.get(s));
    	}
    	return list;
    }

    /**
     * Adds a new TableObject to the project. This method will add the table using
     * the table's name as a key.
     * @param t
     */
    public void putTable(TableObject t) {
        tables.put(t.getName(), t);
    }

    /**
     * Adds a new TableObject to the project. This method will add the table using
     * the specified key.
     * @param name
     * @param t
     */
    public void putTable(String name, TableObject t) {
        tables.put(name, t);
    }

    /**
     * Removes the specified TableObject from the HashMap of stored tables.
     * @param t
     */
    public void removeTable(TableObject t) {
        tables.remove(t.getName());
    }
    
    public TableEditorConstants getTableEditorConstants() {
    	return this.cons;
    }
    public void setTableEditorConstants(TableEditorConstants cons) {
    	this.cons = cons;
    }
}
