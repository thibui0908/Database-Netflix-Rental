package classes;
import java.util.Scanner;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Member {
	
	private String uID;
	private String name;	
	private boolean logout = false;
	
	Scanner scanner = new Scanner(System.in);
	String DB_URL = "jdbc:mysql://localhost:3306/disney";
	//  Database credentials
	String USERNAME = "root";
	String PASSWORD = "Monday2Tuesday3!";
	Connection conn = null;
	
	/*
	 * Login portal
	 */
	public void memberlogin() {
		try {
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Hi there! Select an option below:");
		System.out.println("[1] Sign in   [2] Sign up   [3] Exit");
		System.out.println();
		
		String response = scanner.nextLine().trim();
		
		if (response.equals("1")) {
			signin();
		} else if (response.equals("2")) {
			signup();
		} else if  (response.equals("3")) {
			return;
		}
		else if (response.length() > 1) {
			System.out.println("Please enter only the number option");
		} else {
			System.out.println("There seems to be an error. Please try again");
		}

	}
	
	/*
	 * Handle user sign in 
	 * Take in user's ID and password
	 * Redirect to user's portal if succeed
	 */
	public void signin() {
		System.out.println();
		System.out.println("Enter your member ID: ");
		String uID = scanner.nextLine().trim();
		
		if (uID.length() != 4) {
			System.out.println("Invalid user ID, please try again");
			uID = scanner.nextLine().trim();
		}
		
		System.out.println();
		System.out.println("Enter your password");
		String password = scanner.nextLine().trim();
		
		if (password.isEmpty()) {
			System.out.println("Password cannot be empty, try again!");
			password = scanner.nextLine().trim();
		}
		
		try {
			CallableStatement statement = conn.prepareCall("{CALL userLogin(?,?)}");
			
			statement.setInt(1, Integer.parseInt(uID));
			statement.setString(2, password);

			boolean result = statement.execute();
			
			if (result) {
				ResultSet rs = statement.getResultSet();
				rs.next();
				System.out.println("You have successfully signed in!");
				System.out.println("Good to see you again, " + rs.getString(1) + "!\n");
				this.name = rs.getString(1);
				this.uID = uID;
				this.logout = false;
				userPortal();
			} else {
				
				System.out.println("User cannot be found. Please try again");
			}
			
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 
		
		memberlogin();
	}
	
	/*
	 * Handle user's requests after successful log in
	 * For each activity we redirect to a different method
	 */
	public void userPortal() {
		System.out.println();
		System.out.println("Welcome back! What would you like to do today?");
		System.out.println("[1] Search   [2] Browse   [3] Rental   [4] Billing   [5] Log out");
		System.out.println();
		
		String response = scanner.nextLine().trim();
		
		if (response.equals("1")) {
			search();
			
		} else if (response.equals("2")) {
			browse();
		} else if (response.equals("3")) {
			
			/*
			 * Check current rentals for that user only
			 */
			
		} else if (response.equals("4")) {
			Billing billing = new Billing(uID, name);			
		} else if (response.equals("5")) {
			logout = true;
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
	
	public void browse() {
		System.out.println();
		System.out.println("How would you like to browse?");
		System.out.println();
		System.out.println("[1] Director [2] Rating   [3] Year");
		
		String response = scanner.nextLine().trim();
		
		String director, rating, releaseYear = "";
		
		if (response.equals("1")) {
			System.out.println();
			System.out.println("Enter a director's name: ");
			director = scanner.nextLine().trim();
			if (director.isEmpty()) {
				System.out.println("Name must not be empty!");
				director = scanner.nextLine().trim();
			}
			
		} else if  (response.equals("2")) {
			System.out.println();
			System.out.println("Enter a rating type: ");
			rating = scanner.nextLine().trim();
			if (rating.isEmpty()) {
				System.out.println("Rating must not be empty!");
				rating = scanner.nextLine().trim();
			}
			
			searchResult("rating", rating);
			
		} else if (response.equals("3")) {
			System.out.println();
			System.out.println("Enter a director's name: ");
			releaseYear = scanner.nextLine().trim();
			if (releaseYear.isEmpty()) {
				System.out.println("Year must not be empty!");
				releaseYear = scanner.nextLine().trim();
			}
		} else {
			System.out.println("There seems to be an error. Please try again");
		}

	}
	
	/*
	 * Search for a movie -> new method 
	 * Enter title only
	 */
	public void search() {
		System.out.println();
		System.out.println("Enter a title: ");
		
		String name = scanner.nextLine().trim();

		while (name.isEmpty()) {
			System.out.println("Title must not be empty!");
			name = scanner.nextLine().trim();
		}
		
		searchResult("title", name);
	}
	
	public void searchResult(String attribute, String param) {
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT distinct title from Titles where " + attribute + "= '" + param + "'";
			
			statement.executeQuery(query);
			
			ResultSet result = statement.getResultSet();
			
			System.out.println();
			System.out.println("Here are your results: ");
			
			while (result.next()) {
				System.out.println(result.getNString("title"));
			}
				
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 		
	}
	
	/*
	 * Create a new user when user sign up
	 * Will return their ID so they can log in next time 
	 */
	public void signup() {
		
		// Take in user's required parameters
		
		System.out.println("Enter a name: ");
		String name = scanner.nextLine().trim();
		
		if (name.isEmpty()) {
			System.out.println("Name must not be empty!");
			name = scanner.nextLine().trim();
		}
		
		System.out.println("Enter a password: ");
		String password = scanner.nextLine().trim();
		
		if (password.isEmpty()) {
			System.out.println("Password must not be empty!");
			password = scanner.nextLine().trim();
		}
		
		System.out.println("Enter your age: ");
		
		int age = 0;
		
		// Handle age request
		// Should only be integer
		
		try {
			age = Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("Invalid age. Try again");
		}
		
		System.out.println("Enter your phone number: ");
		String phoneNumber = scanner.nextLine().trim();
		
		if (phoneNumber.isEmpty()) {
			System.out.println("Phone number must not be empty!");
			phoneNumber = scanner.nextLine().trim();
		}

		// Create a statement to connect to JDBC
		// Send SQL statement
		
		PreparedStatement statement = null;
		
		try {
		
			String query = "INSERT INTO User (name, age, password, phone_number)"
							+ " values (?,?,?,?)";
			
			statement = conn.prepareStatement(query, 
							 Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, name);
			statement.setInt(2, age);
			statement.setString(3, password);
			statement.setString(4, phoneNumber);
			
			statement.executeUpdate();
			
			ResultSet result = statement.getGeneratedKeys();
			
			if (result.next()) {
				
				// Return the user's ID
				
				System.out.println("You have successfully registered!");
				System.out.println("Your ID is : " + result.getInt(1) + "\n");
			} else {
				
				System.out.println("Registration failed. Please try again");
			}
			
		} 
		catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 
		
		memberlogin();
	}
	
}
