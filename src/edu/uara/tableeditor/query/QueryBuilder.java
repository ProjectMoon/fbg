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
import java.sql.SQLException;
import java.util.TreeMap;

import edu.uara.FBG;
import edu.uara.db.QueryExecuter;
import edu.uara.tableeditor.TableEditorConstants;
import edu.uara.tableeditor.query.exception.NullColumnException;
import edu.uara.tableeditor.query.exception.NullTableException;

public class QueryBuilder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3241220324417812028L;

	public static enum SQLFunction { VALUE, AVG, COUNT, MAX, MIN, SUM }
	
	private SQLFunction queryFunction;
	private String table;
	private String column;
	private String equation;
	TreeMap<Integer, WhereArg> args;
	protected TableEditorConstants cons;
	public QueryBuilder(TableEditorConstants cons) { 
		this.cons = cons;
		args = new TreeMap<Integer, WhereArg>();
		queryFunction = SQLFunction.VALUE;
		table = null;
		column = null;
		equation = null;
	}

	public SQLFunction getQueryFunction() {
		return queryFunction;
	}

	public void setQueryFunction(SQLFunction queryFunction) {
		this.queryFunction = queryFunction;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
	}
	
	public void addWhereArg(Integer id, WhereArg arg) {
		args.put(id, arg);
	}
	public WhereArg getWhereArg(Integer id) {
		return args.get(id);
	}
	public void removeWhereArg(Integer id) {
		args.remove(id);
	}
	public TreeMap<Integer, WhereArg> getCriteraMap() {
		return this.args;
	}
	
	public void clearWheres() {
		this.args = new TreeMap<Integer, WhereArg>();
	}
	
	public String generateQuery() throws NullColumnException, NullTableException {
		if(table == null) 
			throw new NullTableException();
		if(column == null)
			throw new NullColumnException();
		
		StringBuilder query = new StringBuilder();
		query.append("select ");
		if(queryFunction != SQLFunction.VALUE) {
			query.append(queryFunction.toString());
			query.append("(");
			query.append(handleEq());
			query.append(")");
		}
		else {
			query.append(handleEq());
		}
		
		query.append(" from ");
		query.append(table);
		
		if(args.size()!=0) {
			query.append(" where ");
			for(Integer i : args.keySet()) {
				WhereArg arg = args.get(i);
				if(i!=args.firstKey()) {
					query.append(arg.getBooleanOperatorConnector());
					if((arg.getBooleanOperatorConnector().equals("and")) 
							&& args.keySet().size() > i+1 && args.get(i+1).getBooleanOperatorConnector().equals("or")) {
						query.append("(");
					}
				}
				else if(args.keySet().size() > i+1 && args.get(i+1).getBooleanOperatorConnector().equals("or")) {
					query.append("(");
				}
				query.append(" ");
				query.append(arg.getColumnName());
				query.append(" ");
				query.append(arg.getOperator());
				query.append(" ");
				String appendString = "";
				int dataType = -1;
				try {
					dataType = QueryExecuter.getColumnDataType(this.getTable(), arg.getColumnName());
				}
				catch(SQLException e) {
					e.printStackTrace(FBG.origErr);
				}
				catch(ClassNotFoundException e) {
					e.printStackTrace(FBG.origErr);
				}
				if(dataType==java.sql.Types.VARCHAR || dataType==java.sql.Types.CHAR 
						|| dataType==java.sql.Types.LONGNVARCHAR || dataType==java.sql.Types.LONGVARCHAR)
					appendString = "'";
				
				query.append(appendString);
				String value = arg.getValue();
				value = cons.convertString(value);
				query.append(value);
				query.append(appendString);
				query.append(" ");
				if(i!=args.firstKey()) {
					if(arg.getBooleanOperatorConnector().equals("or") && (args.keySet().size()==i+1 ||(args.keySet().size()> (i+1) && args.get(i+1).getBooleanOperatorConnector().equals("and")))) {
						query.append(")");
					}
				}
			}
		}
		return query.toString();
	}
	
	public String toString() {
		if(table == null) 
			throw new NullTableException();
		if(column == null)
			throw new NullColumnException();
		
		StringBuilder query = new StringBuilder();
		query.append("select ");
		if(queryFunction != SQLFunction.VALUE) {
			query.append(queryFunction.toString());
			query.append("(");
			query.append(handleEqr());
			query.append(")");
		}
		else {
			query.append(handleEqr());
		}
		
		query.append(" from ");
		query.append(table);
		
		if(args.size()!=0) {
			query.append(" where ");
			for(Integer i : args.keySet()) {
				WhereArg arg = args.get(i);
				if(i!=args.firstKey()) {
					query.append(arg.getBooleanOperatorConnector());
					if((arg.getBooleanOperatorConnector().equals("and")) 
							&& args.keySet().size() > i+1 && args.get(i+1).getBooleanOperatorConnector().equals("or")) {
						query.append("(");
					}
				}
				else if(args.keySet().size() > i+1 && args.get(i+1).getBooleanOperatorConnector().equals("or")) {
					query.append("(");
				}
				query.append(" ");
				query.append(arg.getColumnName());
				query.append(" ");
				query.append(arg.getOperator());
				query.append(" ");
				String appendString = "";
				int dataType = -1;
				try {
					dataType = QueryExecuter.getColumnDataType(this.getTable(), arg.getColumnName());
				}
				catch(SQLException e) {
					e.printStackTrace(FBG.origErr);
				}
				catch(ClassNotFoundException e) {
					e.printStackTrace(FBG.origErr);
				}
				if(dataType==java.sql.Types.VARCHAR || dataType==java.sql.Types.CHAR 
						|| dataType==java.sql.Types.LONGNVARCHAR || dataType==java.sql.Types.LONGVARCHAR|| dataType==java.sql.Types.DATE)
					appendString = "'";
				query.append(appendString);
				String value = arg.getValue();
				query.append(value);
				query.append(appendString);
				query.append(" ");
				if(i!=args.firstKey()) {
					if(arg.getBooleanOperatorConnector().equals("or") && (args.keySet().size()==i+1 ||(args.keySet().size()> (i+1) && args.get(i+1).getBooleanOperatorConnector().equals("and")))) {
						query.append(")");
					}
				}
			}
		}
		return query.toString();
	}
	
	private String handleEq() {
		if(equation==null || equation.equals(""))
			return column;
		String equation2 = equation;
		equation2 = cons.convertString(equation2);
		while(equation2.indexOf("#")!=-1) {
			int startIndex = equation2.indexOf("#");
			String temp = equation2.substring(0, startIndex) + column;
			if(startIndex!=equation.length()-1) {
				temp += equation2.substring(startIndex+1);
			}
			equation2 = temp;
		}
		return equation2;
	}
	
	private String handleEqr() {
		if(equation==null || equation.equals(""))
			return column;
		String equation2 = equation;
		while(equation2.indexOf("#")!=-1) {
			int startIndex = equation2.indexOf("#");
			String temp = equation2.substring(0, startIndex) + column;
			if(startIndex!=equation.length()-1) {
				temp += equation2.substring(startIndex+1);
			}
			equation2 = temp;
		}
		return equation2;
	}
	
	public static void main(String args[]) throws NullColumnException, NullTableException {
		/*QueryBuilder builder = new QueryBuilder();
		builder.setTable("TABLE_NAME");
		builder.setColumn("COLUMN_NAME");
		System.out.println(builder.generateQuery());
		
		builder = new QueryBuilder();
		builder.setQueryFunction(SQLFunction.AVG);
		builder.setTable("TABLE_NAME");
		builder.setColumn("COLUMN_NAME");
		System.out.println(builder.generateQuery());
		
		builder = new QueryBuilder();
		builder.setQueryFunction(SQLFunction.AVG);
		builder.setTable("TABLE_NAME");
		builder.setColumn("COLUMN_NAME");
		WhereArg arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_1");
		arg.setOperator("=");
		arg.setValue("4");
		builder.addWhereArg(0, arg);
		System.out.println(builder.generateQuery());
		
		builder = new QueryBuilder();
		builder.setQueryFunction(SQLFunction.AVG);
		builder.setTable("TABLE_NAME");
		builder.setColumn("COLUMN_NAME");
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_1");
		arg.setOperator("=");
		arg.setValue("4");
		builder.addWhereArg(0, arg);
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("and");
		arg.setColumnName("TEST_COLUMN_2");
		arg.setOperator("<");
		arg.setValue("6");
		builder.addWhereArg(1, arg);
		System.out.println(builder.generateQuery());
		
		builder = new QueryBuilder();
		builder.setQueryFunction(SQLFunction.AVG);
		builder.setEquation("!YEAR!-#");
		builder.setTable("TABLE_NAME");
		builder.setColumn("BIRTH_YEAR");
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_2");
		arg.setOperator("<");
		arg.setValue("6");
		builder.addWhereArg(1, arg);
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_1");
		arg.setOperator("=");
		arg.setValue("4");
		builder.addWhereArg(0, arg);
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("and");
		arg.setColumnName("TEST_COLUMN_2");
		arg.setOperator("<");
		arg.setValue("6");
		builder.addWhereArg(1, arg);
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_3");
		arg.setOperator("=");
		arg.setValue("6");
		builder.addWhereArg(2, arg);
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_4");
		arg.setOperator(">");
		arg.setValue("6");
		builder.addWhereArg(3, arg);
		builder = new QueryBuilder();
		builder.setQueryFunction(SQLFunction.AVG);
		builder.setEquation("!YEAR!-#");
		builder.setTable("TABLE_NAME");
		builder.setColumn("BIRTH_YEAR");
		arg.setBooleanOperatorConnector("and");
		arg.setColumnName("TEST_COLUMN_2");
		arg.setOperator("<");
		arg.setValue("6");
		builder.addWhereArg(0, arg);
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_3");
		arg.setOperator("=");
		arg.setValue("6");
		builder.addWhereArg(1, arg);
		arg = new WhereArg();
		arg.setBooleanOperatorConnector("or");
		arg.setColumnName("TEST_COLUMN_4");
		arg.setOperator(">");
		arg.setValue("6");
		builder.addWhereArg(2, arg);
		System.out.println(builder.generateQuery());*/
	}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.queryFunction != null ? this.queryFunction.hashCode() : 0);
        hash = 67 * hash + (this.table != null ? this.table.hashCode() : 0);
        hash = 67 * hash + (this.column != null ? this.column.hashCode() : 0);
        hash = 67 * hash + (this.equation != null ? this.equation.hashCode() : 0);
        hash = 67 * hash + (this.args != null ? this.args.hashCode() : 0);
        return hash;
    }

    @Override
        public boolean equals(Object other) {
            if ((other == null) || (other instanceof QueryBuilder == false))
                return true;
            
            int thisHash = hashCode();
            int otherHash = other.hashCode();
            
            return (thisHash == otherHash);
        }
}
