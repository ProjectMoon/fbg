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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * The class that actually holds defined constants. Also see the constants editor
 * frame.
 * @author jeff
 */
public class TableEditorConstants implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> constants;
	private boolean setShown = false;
	
	
	public TableEditorConstants() {
		constants = new HashMap<String, String>();
	}
	
	
	public String getConstant(String key) {
            String keyName = key;
            if((key.startsWith("!") || key.endsWith("!"))) {
                keyName = key.substring(1);
                keyName = keyName.substring(0, keyName.length() - 1).trim();
            }

            if(constants.get(keyName) == null)
                    return key;
            
            return constants.get(keyName);
	}
	
	public Set<String> getKeys() {
		return constants.keySet();
	}
	
	public String[] getKeyNames() {
		String[] s = new String[constants.keySet().size()];
		constants.keySet().toArray(s);
                for (int c = 0; c < s.length; c++) {
                    s[c] = "!" + s[c] + "!";
                }
		return s;
	}
	
	public void clearConstants() {
		constants.clear();
	}
	
	public void setConstant(String key, String value) {
            key = key.trim();
            constants.put(key, value);
	}
	
	public boolean isShown() {
		return this.setShown;
	}
	
	public void setShown(boolean shown) {
		this.setShown = shown;
	}
	
	public String convertString(String s) {
		String value = s;
		int index = 0;
		while(index < value.length() && value.indexOf("!", index)!=-1) {
			if(value.indexOf("!", index)>0&&value.charAt(value.indexOf("!", index)-1)=='\\') {
				index = value.indexOf("!", index)+1;
				continue;
			}
			int startIndex = value.indexOf("!", index);
			int endIndex = value.indexOf("!", startIndex+1);
                        if (endIndex != -1) {
                            String temp = this.getConstant(value.substring(startIndex, endIndex+1));
                            value = value.substring(0, startIndex) + temp + value.substring(endIndex+1);
                            index = endIndex+1;
                        }
                        else {
                            index++;
                        }
		}
		value = value.replaceAll("\\\\", "");
		return value;
	}
	
    public static void main(String args[]) {
        TableEditorConstants cons = new TableEditorConstants();
        cons.setConstant("hello", "99");
        System.out.println(cons.convertString("select from !hello!"));
        System.out.println(cons.convertString("select \\!hello\\! from !helldo!"));
        
    }

}
