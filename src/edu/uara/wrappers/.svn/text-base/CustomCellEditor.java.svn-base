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
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * A small custom class that extends the capabilities of the default listener.
 * This is used in conjunction with the overriden prepareEditor method in CustomJTable
 * to decide whether we should clear text when we begin editing or not.
 * Text is cleared when the user just starts typing, but not on a double click.
 * @author jeff
 */
public class CustomCellEditor extends DefaultCellEditor {
    private boolean clearText;
    
    public CustomCellEditor() {
        super(new JTextField());
    }
    
    @Override
    public boolean isCellEditable(EventObject e) {
        boolean editable = super.isCellEditable(e);
        
        if (editable) {
            if (e instanceof java.awt.event.KeyEvent) {
                clearText = true;
            }
            else {
                clearText = false;
            }
        }
        
        return editable;
    }

    @Override
    public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
        JTextComponent comp = (JTextComponent)super.getTableCellEditorComponent(arg0, arg1, arg2, arg3, arg4);
        comp.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 255)));
        return comp;
    }
    
    public boolean shouldClearText() {
        return clearText;
    }
}
