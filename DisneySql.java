package db;
import java.sql.*;

class DisneySql{
	
	
	// JDBC driver name and database URL
	   static final String DB_URL = "jdbc:mysql://localhost:3306/disney";
	   //  Database credentials
	   static final String USERNAME = "root";
	   static final String PASSWORD = "walk1234";
	   private static Connection conn = null;
	   private static Statement statement = null;
	
	public static void main(String args[]) {
		
		try {
			
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = conn.createStatement();
			
			String query1 = "SELECT  AVG(age)  AS avg_age\n"
					+ "\n"
					+ "FROM User\n"
					+ "";
			System.out.print("Find the average of users age: ");
		
			ResultSet rs = statement.executeQuery(query1);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNum = rsmd.getColumnCount();
			
			while(rs.next()) {
				for (int i = 1; i <= columnsNum; i++) {

					if (i > 1)
						System.out.print(", ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
					
				}
				System.out.println();
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}