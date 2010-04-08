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
package edu.uara.gui.main;

import edu.uara.tableeditor.TableObject;
import java.util.Stack;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author jh44695
 */
public class UndoBuffer {
    private Stack<TableObject> undoTables = new Stack<TableObject>();
    private Stack<TableObject> redoTables = new Stack<TableObject>();

    public TableObject undo() throws CannotUndoException {
        if (undoTables.isEmpty()) throw new CannotUndoException();
        TableObject to = undoTables.pop();
        redoTables.push(to);
        return to;
    }

    public TableObject redo() throws CannotRedoException {
        if (redoTables.isEmpty()) throw new CannotRedoException();
        TableObject to = redoTables.pop();
        undoTables.push(to);
        return to;
    }

    public boolean canRedo() {
        return (!redoTables.isEmpty());
    }

    public boolean canUndo() {
        return (!undoTables.isEmpty());
    }

    public void pushTable(TableObject to) {
        boolean allowPush = true;
        if (!undoTables.isEmpty()) {
            TableObject other = undoTables.peek();
            if (to.equals(other) == true)
                allowPush = false;
        }
            
        if (allowPush) {
            System.out.println("pushing...");
            undoTables.push(new TableObject(to));
            redoTables.clear();
        }
    }
}
