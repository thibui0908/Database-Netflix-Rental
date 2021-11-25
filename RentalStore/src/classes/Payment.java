package classes;
import java.util.Scanner;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Payment {
	private String uID;
	
	Scanner scanner = new Scanner(System.in);
	String DB_URL = "jdbc:mysql://localhost:3306/disney";
	
	//  Database credentials
	String USERNAME = "root";
	String PASSWORD = "Monday2Tuesday3!";
	Connection conn = null;
	
	public void paymentPortal(String uID) {
		this.uID = uID;
		
		try {
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("You're in the payment portal:");
		System.out.println("[1] Check balance   [2] Make payment   [3] Exit");
		System.out.println();
		
		String response = scanner.nextLine().trim();
		
		if (response.equals("1")) {
			checkBalance();
		} else if (response.equals("2")) {
			makePayment();
		} else if  (response.equals("3")) {
			return;
		}
		else if (response.length() > 1) {
			System.out.println("Please enter only the number option");
		} else {
			System.out.println("There seems to be an error. Please try again");
		}
	}
	
	public void checkBalance() {
		
	}
	
	public void makePayment() {
		
	}
}
