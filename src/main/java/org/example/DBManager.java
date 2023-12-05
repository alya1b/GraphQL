package org.example;

import org.example.component.Column;
import org.example.component.Database;
import org.example.component.Row;
import org.example.component.Table;
import org.example.component.column.*;

public class DBManager {

  private static DBManager instance;

  private DBManager() {
  }

  public static DBManager getInstance() {
    if (instance == null) {
      instance = new DBManager();
      database = new Database("DB");
    }
    return instance;
  }



  public static Database database;

  public static void populateTable() {
    Table table = new Table("Table1");
    table.addColumn(new TypeInteger("column1"));
    table.addColumn(new TypeCharInvl("column2"));
    table.addColumn(new TypeStringInvl("column3"));
    Row row1 = new Row();
    row1.values.add("10");
    row1.values.add("a, b");
    row1.values.add("some text");
    table.addRow(row1);
    Row row2 = new Row();
    row2.values.add("20");
    row2.values.add("c, d");
    row2.values.add("some text 2");
    table.addRow(row2);
    database.addTable(table);

    Table table2 = new Table("Table2");
    table2.addColumn(new TypeInteger("column1"));
    table2.addColumn(new TypeCharInvl("column2"));
    table2.addColumn(new TypeStringInvl("column3"));
    Row row12 = new Row();
    row12.values.add("100");
    row12.values.add("1, 2");
    row12.values.add("dif text");
    table2.addRow(row12);
    Row row22 = new Row();
    row22.values.add("15");
    row22.values.add("3, 4");
    row22.values.add("dif text 2");
    table2.addRow(row22);
    database.addTable(table2);

  }

  public void deleteRow(int tableIndex, int rowIndex){

    if (rowIndex != -1) {
      database.tables.get(tableIndex).deleteRow(rowIndex);
    }
  }

  public static Boolean addColumn(int tableIndex, String columnName, TypeColumn typeColumn) {
    if (columnName != null && !columnName.isEmpty()) {
      if (tableIndex != -1) {

        switch (typeColumn) {
          case INT -> {
            Column columnInt = new TypeInteger(columnName);
            database.tables.get(tableIndex).addColumn(columnInt);
          }
          case REAL -> {
            Column columnReal = new TypeReal(columnName);
            database.tables.get(tableIndex).addColumn(columnReal);
          }
          case STRING -> {
            Column columnStr = new TypeString(columnName);
            database.tables.get(tableIndex).addColumn(columnStr);
          }
          case CHAR -> {
            Column columnChar = new TypeChar(columnName);
            database.tables.get(tableIndex).addColumn(columnChar);
          }
          case CHARINVL -> {
            Column typeHTML = new TypeCharInvl(columnName);
            database.tables.get(tableIndex).addColumn(typeHTML);
          }
          case STRINGINVL -> {
            Column typeStringInvl = new TypeStringInvl(columnName);
            database.tables.get(tableIndex).addColumn(typeStringInvl);
          }
        }
        for (Row row : database.tables.get(tableIndex).rows) {
          row.values.add("");
        }
        return true;
      }
      else {
        return false;
      }
    }
    else {
      return false;
    }
  }

  public static void addTable(String name){
    if (name != null && !name.isEmpty()) {
      Table table = new Table(name);
      database.addTable(table);
    }
  }

  public static void addRow(int tableIndex, Row row){

    if (tableIndex != -1) {
      database.tables.get(tableIndex).addRow(row);
      for (int i = row.values.size(); i < database.tables.get(tableIndex).columns.size(); i++){
        row.values.add("");
      }
    }
  }

  public static Table mergeTables(String tableName1, String tableName2) {
    // Find tables by their names
    Table table1 = null;
    Table table2 = null;
    for (Table table : database.tables) {
      if (table.name.equals(tableName1)) {
        table1 = table;
      } else if (table.name.equals(tableName2)) {
        table2 = table;
      }
    }

    if (table1 == null || table2 == null) {
      // Return null if either table is not found
      return null;
    }

    // Check if tables have the same number of columns
    if (table1.columns.size() != table2.columns.size()) {
      // Return null if columns count doesn't match
      return null;
    }

    // Check if columns match by name and type
    for (int i = 0; i < table1.columns.size(); i++) {
      Column column1 = table1.columns.get(i);
      Column column2 = table2.columns.get(i);
      if (!column1.name.equals(column2.name) ||
              !column1.getType().equals(column2.getType())) {
        // Return null if any column doesn't match
        return null;
      }
    }

    // Create a new merged table
    Table mergedTable = new Table("MergedTable");

    // Copy columns from table1 to mergedTable
    for (Column column : table1.columns) {
      mergedTable.addColumn(column);
    }

    // Merge rows by stacking them
    mergedTable.rows.addAll(table1.rows);
    mergedTable.rows.addAll(table2.rows);
    database.tables.add(mergedTable);
    return mergedTable;
  }

}
