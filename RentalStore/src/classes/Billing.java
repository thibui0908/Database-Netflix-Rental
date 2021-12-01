package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Billing {
	
	private String uID;
	private String name;	
	
	Scanner scanner = new Scanner(System.in);
	String DB_URL = "jdbc:mysql://localhost:3306/disney";
	//  Database credentials
	String USERNAME = "root";
	String PASSWORD = "Monday2Tuesday3!";
	Connection conn = null;
	
	public Billing(String uID, String name) {
		this.uID = uID;
		this.name = name;
		
		try {
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		boolean billingExists = this.billingExists();
		if(billingExists == false) {
			this.createBilling();
		};
		billingPortal();
	} 
	
	/*
	 * Creates the user's billing account after inserting their address, with an initial balance of zero 
	 */
	public void createBilling() {
		try {
			System.out.println();
			System.out.println("It seems like your billing account has not been setup. Please enter your address to finish your billing setup.");
			
			String response = scanner.nextLine().trim();
			String address = response;
			Statement statement = conn.createStatement();
			String query = "INSERT into Billing values(" + uID + ",'" + name + "','" + address + "', 0)";
			
			statement.executeUpdate(query);
			
			System.out.println();
			System.out.print("Your billing account has been succesfully setup. ");
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 	
	}
	
	/*
	 * Handle user's billing requests after successfully
	 * creating their billing account
	 */
	public void billingPortal() {
		System.out.println();
		System.out.println("What would you like to do today?");
		System.out.println("[1] View Balance   [2] Add Balance   [3] View Address   [4] Make payment   [5] Return");
		System.out.println();
		
		String response = scanner.nextLine().trim();
		
		if (response.equals("1")) {
			getBalance();
		} else if (response.equals("2")) {
			addBalance();
		} else if (response.equals("3")) {
			getAddress();
		} else if (response.equals("4")) {
			//other functionality
			Payment pm = new Payment();
			pm.paymentPortal(uID);
		} else if (response.equals("5")) {
			return;
		}
		else if (response.length() > 1) {
			System.out.println("Please enter only the number option");
		} else {
			System.out.println("There seems to be an error. Please try again");
		}
		System.out.println();
		return;
	}
	

	/*
	 * Checks if the user has a current billing account
	 */
	public boolean billingExists() {
		boolean billingExists = false;
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT * from Billing where uID = " + uID + "";
			
			statement.executeQuery(query);
	
			ResultSet result = statement.getResultSet();
			
			if (result.next() == false) {
				billingExists = false;
			}
			else {
				billingExists = true;
			}		
			return billingExists;
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		}
		return billingExists; 		
	}
	
	/*
	 * Handles user's request to add to their current balance. 
	 */
	public void addBalance() {
		try {
			System.out.println();
			System.out.println("How much would you like to add to your billing balance?");
			
			int amount = scanner.nextInt();
			Statement statement = conn.createStatement();
			String query = "UPDATE Billing set balance = balance + " + amount + " where uID = " + uID + "";
			
			statement.executeUpdate(query);			
			System.out.println();
			System.out.print("Your balance has been successfully updated. ");
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 		
	}
	
	/*
	 * Gets the current billing balance of the current user
	 */
	public void getBalance() {
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT balance from Billing where uID = " + uID + "";
			
			statement.executeQuery(query);
	
			ResultSet result = statement.getResultSet();
			
			System.out.println();
			while (result.next()) {
				System.out.print("Your current balance is: ");
				int balance = result.getInt("balance");
				System.out.print(balance + " dollars");
			}	
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 		
	}
	
	/*
	 * Gets the billing address of the current user
	 */
	public void getAddress() {
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT address from Billing where uID = " + uID + "";
			
			statement.executeQuery(query);
	
			ResultSet result = statement.getResultSet();
			
			System.out.println();
			while(result.next()) {
				System.out.print("Your current billing address is: ");
				String address = result.getString("address");
				System.out.print(address);
			}	
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 		
	}
	
//	public void viewPayments() {
//		try {
//			Statement statement = conn.createStatement();
//			String query = "SELECT * from Payment where uID = " + uID + "";
//			
//			statement.executeQuery(query);
//	
//			ResultSet result = statement.getResultSet();
//			
//			System.out.println();
//			while(result.next()) {
//				String payment = result.getString(1);
//				System.out.println(payment);
//			}	
//		} catch (SQLException e) {
//			System.out.println("There seems to be an error. Please try again");
//			System.out.println("System message: " + e.getMessage());
//		} 		
//	}
//	
//	public void makePayment() {
//		
//	}
}
