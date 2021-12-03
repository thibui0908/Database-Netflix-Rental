package classes;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

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
        } else if (response.equals("3")) {
            return;
        } else if (response.length() > 1) {
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
    	
    	while (!logout) {
    		System.out.println("Welcome back! What would you like to do today?");
            System.out.println("[1] Browse   [2] Rental   [3] Billing   [4] Log out");

            String response = scanner.nextLine().trim();
            
            if (response.equals("1")) {
                browse();

            } else if (response.equals("2")) {
                rental();
            } else if (response.equals("3")) {
                Billing billing = new Billing(uID, name);
            } else if (response.equals("4")) {
                logout = true;
                return;
            } else if (response.length() > 1) {
                System.out.println("Please enter only the number option");
            } else {
                System.out.println("There seems to be an error. Please try again");
            }
    	}
    }

    public void browse() {
        System.out.println();
        System.out.println("Enter (without spaces) your filter attributes: ");
        System.out.println("[1] Title  [2] Rating  [3] Year   [4] Type    [5] Price");

        String response = scanner.nextLine().trim();
        String[] att = response.split("");
        
        String title, rating, release_year, type, price = "";
       
        ArrayList<String> attribute = new ArrayList<>();
        
        ArrayList<String> param = new ArrayList<>();
        		
        if (!response.isEmpty()) {
        	for (int i = 0; i < att.length; i++) {
        		if (att[i].equals("1")) {
        			System.out.println("Enter a title you'd like to search");
        			title = scanner.nextLine().trim();
        			attribute.add("title");
        			param.add(title);  			
        		} else if (att[i].equals("2")) {
        			System.out.println("Enter a rating you'd like to search");
        			rating = scanner.nextLine().trim();
        			attribute.add("rating");
        			param.add(rating);	
        		} else if (att[i].equals("3")) {
        			System.out.println("Enter a year you'd like to search");
        			release_year = scanner.nextLine().trim();
        			attribute.add("release_year");
        			param.add(release_year);
        		} else if (att[i].equals("4")) {
        			System.out.println("Enter a type you'd like to search");
        			type = scanner.nextLine().trim();
        			attribute.add("type");
        			param.add(type);
        		} else if (att[i].equals("5")) {
        			System.out.println("Enter a price range in the form of [min-max] you'd like to search");
        			price = scanner.nextLine().trim();
        			attribute.add("price");
        			param.add(price);
        		}
        	}
            
        	searchResult(attribute, param);
            
         } else {
        	 System.out.println("Please choose at least 1 attribute, try again");
         }
        return;
        
    }

    public void searchResult(ArrayList<String> attribute, ArrayList<String> param) {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT distinct title, release_year from Titles where ";
            
            String priceQuery = "";
            
            if (attribute.contains("price")) {
         
            	int index = attribute.indexOf("price");
            	String[] range = param.get(index).split("-");
            	String min = range[0];
            	String max = range[1];
            	
            	priceQuery += " and price in (select price from Titles where price <= " + max + " and price >=" + min + ")";
            }
            
            for (int i = 0; i < attribute.size(); i++) {
            	if (i == attribute.size() - 1) {
            		if (!attribute.get(i).equals("release_year")) {
            			query += attribute.get(i) + "= '" + param.get(i) + "'";
            		} else {
            			query += attribute.get(i) + "= " + param.get(i);
            		}
            		
            	} else {
            		if (!attribute.get(i).equals("release_year")) {
            			query += attribute.get(i) + "= '" + param.get(i) + "'" + " and ";
            		} else {
            			query += attribute.get(i) + "= " + param.get(i) + " and ";
            		}
            	}
            }
            
            query += priceQuery;
            
            //System.out.println(query);

            statement.executeQuery(query);

            ResultSet result = statement.getResultSet();

            System.out.println();
            System.out.println("Here are your results: ");

            while (result.next()) {
                System.out.println(result.getString("title") + " (" + result.getString("release_year") + ")");
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
        
        return;
    }

    /*
     * Insert a new user rental information
     * Enter title key words and ask user to chose which title want to rental and then insert rental table
     */
    public void rental() {
        try {
            System.out.println("Enter title: ");
            String title = scanner.nextLine().trim();

            Statement statement = conn.createStatement();


            String SearchTitlesQuery = "SELECT title FROM Titles WHERE title LIKE '%" + title + "%'";
            statement.executeQuery(SearchTitlesQuery);
            ResultSet titlesRs = statement.getResultSet();
            System.out.println("Here are the matched titles: ");
            while (titlesRs.next()) {
                System.out.println(titlesRs.getNString("title"));
            }
            System.out.println("Please choose one to rent: ");
            String selectedTitle = scanner.nextLine().trim();

            //System.out.println("Choose the title you want o rent: ");


            String findShowIDQuery = "SELECT show_id FROM Titles WHERE title = '" + selectedTitle + "'";


            statement.executeQuery(findShowIDQuery);

            ResultSet result = statement.getResultSet();

            String showId = "";
            while (result.next()) {
                showId = result.getNString("show_id");
                System.out.println("showId is: " + showId);
            }


            String checkoutDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            System.out.println("checkoutDate is: " + checkoutDate);
            int overDue = 0;

            String newRentalQuery = "INSERT INTO Rental VALUES (" +
                    uID + ", '" + showId + "', '" + checkoutDate + "', " + overDue + ")";
            System.out.println("newRentalQuery is: " + newRentalQuery);
//            Statement statement2 = conn.createStatement();
            statement.executeUpdate(newRentalQuery);

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

        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }

        memberlogin();
    }

}
