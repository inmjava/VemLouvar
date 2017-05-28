package org.hibernate.dialect;

public class SQLiteDialect extends Dialect{
   public SQLiteDialect()
   {
      registerColumnType(java.sql.Types.BINARY, "BLOB");
      registerColumnType(java.sql.Types.TINYINT, "INTEGER(1)");
      registerColumnType(java.sql.Types.SMALLINT, "INTEGER(2)");
      registerColumnType(java.sql.Types.INTEGER, "INTEGER(4)");
      registerColumnType(java.sql.Types.BIGINT, "INTEGER(8)");
      registerColumnType(java.sql.Types.BIT, "INTEGER(1)");
      registerColumnType(java.sql.Types.BOOLEAN, "INTEGER(1)");
      registerColumnType(java.sql.Types.DECIMAL, "REAL");
      registerColumnType(java.sql.Types.DOUBLE, "REAL");
      registerColumnType(java.sql.Types.FLOAT, "REAL");
      registerColumnType(java.sql.Types.CHAR, "TEXT(1)");
      registerColumnType(java.sql.Types.VARCHAR, "TEXT($l)");
      registerColumnType(java.sql.Types.CLOB, "TEXT");
      registerColumnType(java.sql.Types.BLOB, "BLOB");
      registerColumnType(java.sql.Types.DATE, "DATETIME");
      registerColumnType(java.sql.Types.TIME, "DATETIME");
      registerColumnType(java.sql.Types.NULL, "NULL");
   }
   @Override
   public String getIdentitySelectString()
   {
      return "SELECT last_insert_rowid()";
   }
   @Override
   public String appendIdentitySelectToInsert(String insertSql)
   {
      return insertSql + "; " + getIdentitySelectString();
   }
   @Override
   public  boolean hasAlterTable()
   {
       return false;
   }
   @Override
   public boolean dropConstraints()
   {
      return false;
   }
   @Override
   public String getForUpdateString()
   {
      return "";
   }
   @Override
   public boolean supportsSubselectAsInPredicateLHS()
   {
   // TODO: try it, at nhibernate's original code says:
        // SQLite actually does support subselects, but gives syntax errors
   // in tests. Need to investigate this.
      return false;
   }
}