package classes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin {
	
	Scanner scanner = new Scanner(System.in);
	String DB_URL = "jdbc:mysql://localhost:3306/disney";
	//  Database credentials
	String USERNAME = "root";
	String PASSWORD = "Monday2Tuesday3!";
	Connection conn = null;
	
	private String uID;
    private String name;
    private boolean logout = false;
	
	public void adminlogin() {
		try {
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Hi there! Select an option below:");
        System.out.println("[1] Sign in   [2] Exit");
        System.out.println();

        String response = scanner.nextLine().trim();

        if (response.equals("1")) {
            adminSignin();
        } else if (response.equals("2")) {
        	logout = true;
            return;
        } else if (response.length() > 1) {
            System.out.println("Please enter only the number option");
        } else {
            System.out.println("There seems to be an error. Please try again");
        }
	}
	/*
	 * Admin sign in: Only one administrator 
	 * admin ID : 1001
	 * password : 1234
	 * name: Admin McAdmin
	 */
	
	public void adminSignin() {
		System.out.println("Enter your member ID: ");
        String uID = scanner.nextLine().trim();

        if (uID.length() != 4) {
            System.out.println("Invalid user ID, please try again");
            uID = scanner.nextLine().trim();
        }

        System.out.println("Enter your password");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty, try again!");
            password = scanner.nextLine().trim();
        }

        try {
            CallableStatement statement = conn.prepareCall("{CALL adminLogin(?,?)}");

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
                adminPortal();
            } else {

                System.out.println("User cannot be found. Please try again");
            }

        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }

        adminlogin();
	}
	
	public void adminPortal() {
		 System.out.println("What would you like to do today?");
	        System.out.println("[1] Rental Information   [2] Top Rental  [3] Log out");

	        String response = scanner.nextLine().trim();

	        if (response.equals("1")) {
	            getUserRentalInfo();
	        } else if (response.equals("2")) {
	            getMostPopularMovies();
	        } else if (response.equals("3")) {
	            return;
	        }
	}
	
	/*
	 * Returns rental information for all users. 
	 */
	public void getUserRentalInfo() {
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT u.uID, u.name, u.age, u.phone_number, r.uID, r.check_out, r.overdue FROM User u LEFT OUTER JOIN Rental r ON u.uID = r.uID";
			
			statement.executeQuery(query);
	
			ResultSet result = statement.getResultSet();
			
			System.out.println();
			System.out.print("uID | name | age | phone_number | uID | check_out | overdue ");
			System.out.println();

			while(result.next()) {
				String uID = result.getString("u.uID");
				String name = result.getString("u.name");
				String age = result.getString("u.age");
				String phone_number = result.getString("u.phone_number");
				String check_out = result.getString("r.check_out");
				String overdue = result.getString("r.overdue");

				System.out.print(uID + " | " + name + " | " + age + " | " + phone_number + " | " + uID + " | " + check_out + " | " + overdue);
				System.out.println();
			}	
			System.out.println();
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 	
	}
	
	/*
	 * Returns most rented(popular) movie and how many copies of such movie was rented. 
	 */
	public void getMostPopularMovies() {
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT title, MAX(n) as max FROM (Select t.title, COUNT(r.show_id) AS n FROM Rental r JOIN Titles t ON r.show_id = t.show_id GROUP BY t.title) t GROUP BY title";
			
			statement.executeQuery(query);
	
			ResultSet result = statement.getResultSet();
			
			System.out.println();


			while(result.next()) {
				String title = result.getString("title");
				String max = result.getString("max");
				System.out.print("The most popular movie is '" + title + "'.");
				System.out.println();
				System.out.print("This movie was rented " + max + " time(s).");
				System.out.println();
			}	
			System.out.println();
		} catch (SQLException e) {
			System.out.println("There seems to be an error. Please try again");
			System.out.println("System message: " + e.getMessage());
		} 	
	}
}
