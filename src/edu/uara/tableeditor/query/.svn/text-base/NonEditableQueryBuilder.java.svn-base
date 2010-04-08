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
package edu.uara.tableeditor.query;

import java.io.Serializable;
import java.util.TreeMap;

import edu.uara.tableeditor.TableEditorConstants;
import edu.uara.tableeditor.query.exception.NullColumnException;
import edu.uara.tableeditor.query.exception.NullTableException;

public class NonEditableQueryBuilder extends QueryBuilder implements Serializable {
	private String queryString;
	
	public NonEditableQueryBuilder(String queryString, TableEditorConstants cons) {
		super(cons);
		this.queryString = queryString;
	}
	public NonEditableQueryBuilder(TableEditorConstants cons) {
		super(cons);
		this.queryString = "";
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	@Override
	public String generateQuery() throws NullColumnException, NullTableException {
		return cons.convertString(queryString);
	}
	@Override
	public String toString() {
		return queryString;
	}

	@Override
	public SQLFunction getQueryFunction() {
		return null;
	}
	@Override
	public void setQueryFunction(SQLFunction queryFunction) {
	}
	@Override
	public String getTable() {
		return null;
	}
	@Override
	public void setTable(String table) {
	}
	@Override
	public String getColumn() {
		return null;
	}
	@Override
	public void setColumn(String column) {
	}
	@Override
	public String getEquation() {
		return null;
	}
	@Override
	public void setEquation(String equation) {
	}
	@Override
	public void addWhereArg(Integer id, WhereArg arg) {
	}
	@Override
	public WhereArg getWhereArg(Integer id) {
		return null;
	}
	@Override
	public void removeWhereArg(Integer id) {
	}
	@Override
	public TreeMap<Integer, WhereArg> getCriteraMap() {
		return null;
	}
	@Override
	public void clearWheres() {}

}
