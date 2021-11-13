package classes;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.mysql.cj.util.StringUtils;


public class Member {
	
	private String uID;
	private String name;
	private int age;
	private String phoneNumber;
	
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
		System.out.println("[1] Sign in   [2] Sign up [3] Exit");
		System.out.println();
		
		String response = scanner.nextLine().trim();
		
		if (response.equals("1")) {
			/*
			 * Handle for member log in 
			 * Take in userID and password
			 * 
			 */
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
		
			String query = "INSERT INTO User (name, age, password, phone_number) values (?,?,?,?)";
			
			
			
			statement = conn.prepareStatement(
											query, 
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
		
	}
	
}
