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
import java.io.OutputStream;

import javax.swing.JTextArea;
/**
 * A simple class that sends output to the tail-end of a JTextArea. Useful for redirecting
 * System.out to a graphical "console."
 * @author jeff
 */
public class TextAreaOutputStream extends OutputStream {
    private JTextArea textArea;
    
    public TextAreaOutputStream(JTextArea area) {
        textArea = area;
    }
    
    public void write(int i) {
        textArea.append(Integer.toString(i));
    }
    
    public void write(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < b.length; c++) {
            sb.append((char)b[c]);
        }
        textArea.append(sb.toString());
    }
    
    public void write(byte[] b, int off, int len) {
        StringBuilder sb = new StringBuilder();
        for (int c = off; c < off + len; c++) {
            sb.append((char)b[c]);
        }
        textArea.append(sb.toString());
    }
    
    
    
}
