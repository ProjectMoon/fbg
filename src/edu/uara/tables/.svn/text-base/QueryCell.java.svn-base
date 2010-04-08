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
package edu.uara.tables;

import java.util.ArrayList;

public class QueryCell {
	
	private ArrayList<String> tableList;
	private ArrayList<String> extraWhere;
	private String select;
	
	public QueryCell() {
		setTableList(new ArrayList<String>());
		setExtraWhere(new ArrayList<String>());
		setSelect("");
	}
	
	public void addTable(String tableName) {
		getTableList().add(tableName);
	}
	
	public void addWhere(String where) {
		getExtraWhere().add(where);
	}

	public void setTableList(ArrayList<String> tableList) {
		this.tableList = tableList;
	}

	public ArrayList<String> getTableList() {
		return tableList;
	}

	public void setExtraWhere(ArrayList<String> extraWhere) {
		this.extraWhere = extraWhere;
	}

	public ArrayList<String> getExtraWhere() {
		return extraWhere;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getSelect() {
		return select;
	}
	

}
