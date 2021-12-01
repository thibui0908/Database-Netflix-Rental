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
		System.out.println("[1] Make payment   [2] Exit");
		System.out.println();
		
		String response = scanner.nextLine().trim();
		
		if (response.equals("1")) {
			makePayment();
		} else if  (response.equals("2")) {
			return;
		}
		else if (response.length() > 1) {
			System.out.println("Please enter only the number option");
		} else {
			System.out.println("There seems to be an error. Please try again");
		}
	}

	public void makePayment() {
		System.out.println("Enter the amount you're paying: ");
		String amount = scanner.nextLine().trim();
		
		if (amount.isEmpty()) {
			System.out.println("Name must not be empty!");
			amount = scanner.nextLine().trim();
		}
		
		try {
			Statement statement = conn.createStatement();
			String query = "UPDATE Billing SET balance = balance - " + amount + " where uID = " + uID;
			statement.executeUpdate(query);
	
			System.out.println("You successfully made a payment!");		
			
			getBalance();
			
			insertPayment(amount);
			/*
			 * Insert Payment transaction 
			 */
		
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		}
	}
	
	public void insertPayment(String amount) {
		PreparedStatement statement = null;
		try {
			String query = "INSERT INTO Payment(uID, payment_date, payment_amount)"
					+ " values (?, CURRENT_TIMESTAMP,?)";
	
			statement = conn.prepareStatement(query, 
							 Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, uID);
			statement.setString(2, amount);

			statement.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		}
		
		return;
	}
	
	public void getBalance() {
		try {
			Statement statement = conn.createStatement();
			String query = "Select balance from Billing where uID = " + uID;
			statement.executeQuery(query);
	
			ResultSet result = statement.getResultSet();
			
			if (result.next()) {
				int balance = result.getInt("balance");
				System.out.println("Your current balance is: " + balance + " dollars");
			}
			
		
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: getBalance " + e.getMessage());
		}
		
		return;
	}
}
