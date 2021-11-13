package classes;
import java.util.Scanner;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.xdevapi.Result;


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
	
	public void userPortal() {
		System.out.println();
		System.out.println("Welcome back! What would you like to do today?");
		System.out.println("[1] Search   [2] Browse   [3] Rental   [4] Billing   [5] Log out");
		System.out.println();
		
		String response = scanner.nextLine().trim();
		
		if (response.equals("1")) {
			/*
			 * Search for a movie -> new method 
			 * Should be options of searching methods
			 * By name/ director/ rating / year
			 */
			System.out.println();
			System.out.println("How would you like to look up your show / movie?");
			System.out.println();
			System.out.println("[1] Director   [2] Title name   [3] Rating   [4] Year");
			
			response = scanner.nextLine().trim();
			
			String director, name, rating, releaseYear = "";
			
			if (response.equals("1")) {
				System.out.println();
				System.out.println("Enter a director's name: ");
				director = scanner.nextLine().trim();
				if (director.isEmpty()) {
					System.out.println("Name must not be empty!");
					director = scanner.nextLine().trim();
				}
				
			} else if (response.equals("2")) {
				System.out.println();
				System.out.println("Enter a title: ");
				name = scanner.nextLine().trim();
				if (name.isEmpty()) {
					System.out.println("Rating must not be empty!");
					name = scanner.nextLine().trim();
				}
			} else if  (response.equals("3")) {
				System.out.println();
				System.out.println("Enter a rating type: ");
				rating = scanner.nextLine().trim();
				if (rating.isEmpty()) {
					System.out.println("Rating must not be empty!");
					rating = scanner.nextLine().trim();
				}
			} else if (response.equals("4")) {
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
			
			
		} else if (response.equals("2")) {
			System.out.println();
			System.out.println("How would you like to browse?");
			System.out.println();
			System.out.println("[1] Director   [2] Title name   [3] Rating   [4] Year");
			
			response = scanner.nextLine().trim();
			
			String director, name, rating, releaseYear = "";
			
			if (response.equals("1")) {
				System.out.println();
				System.out.println("Enter a director's name: ");
				director = scanner.nextLine().trim();
				if (director.isEmpty()) {
					System.out.println("Name must not be empty!");
					director = scanner.nextLine().trim();
				}
				
			} else if (response.equals("2")) {
				System.out.println();
				System.out.println("Enter a title: ");
				name = scanner.nextLine().trim();
				if (name.isEmpty()) {
					System.out.println("Rating must not be empty!");
					name = scanner.nextLine().trim();
				}
			} else if  (response.equals("3")) {
				System.out.println();
				System.out.println("Enter a rating type: ");
				rating = scanner.nextLine().trim();
				if (rating.isEmpty()) {
					System.out.println("Rating must not be empty!");
					rating = scanner.nextLine().trim();
				}
				
				searchResult("rating", rating);
				
			} else if (response.equals("4")) {
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
			
			
		} else if (response.equals("3")) {
			
			/*
			 * Check current rentals for that user only
			 */
			
		} else if (response.equals("4")) {
			/*
			 * Handles billings and payments for users
			 */
			
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
				System.out.println("You have successfully registered!");
				System.out.println("Your ID is : " + result.getInt(1) + "\n");
			} else {
				
				System.out.println("Registration failed. Please try again");
			}
			
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 
		
		memberlogin();
	}
	
}
