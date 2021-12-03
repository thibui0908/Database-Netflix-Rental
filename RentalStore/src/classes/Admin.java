package classes;

import java.sql.*;
import java.util.Scanner;

public class Admin {

    Scanner scanner = new Scanner(System.in);
    String DB_URL = "jdbc:mysql://localhost:3306/disney";
    //  Database credentials
    String USERNAME = "root";
    String PASSWORD = "Monday2Tuesday3!";
    Connection conn = null;

    public void adminlogin() {
        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
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

            while (result.next()) {
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


            while (result.next()) {
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


    /*
     * Returns the maximum number of copies of movies.
     */
    public void getMaximumCopies() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT MAX(Copies) AS Copy_amount FROM titles";

            statement.executeQuery(query);

            ResultSet result = statement.getResultSet();

            System.out.println();


            while (result.next()) {
                String copies = result.getString("Copy_amount");
                System.out.print("The maximum number of copies is " + copies + ".");
                System.out.println();
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
    }

    /*
     * Returns the cheapest movies to rent.
     */
    public void getCheapest() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT title from Titles where price = (select MIN(price) from Titles)";

            statement.executeQuery(query);

            ResultSet result = statement.getResultSet();

            System.out.println();
            System.out.print("The cheapest movies to rent are: ");
            System.out.println();

            while (result.next()) {
                String title = result.getString("title");
                System.out.println(title);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
    }

    /*
     * Returns the average user's age.
     */
    public void getAverageAge() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT AVG(age) AS avg_age FROM User";

            statement.executeQuery(query);

            ResultSet result = statement.getResultSet();

            System.out.println();

            while (result.next()) {
                int age = result.getInt("avg_age");
                String avg_age = String.valueOf(age);
                System.out.print("The average user age is " + avg_age + ".");
                System.out.println();
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
    }

    /*
     * Returns all user ID who has either rentals video overdue or balance positive.
     */
    public void getOverdueOrPositiveBalanceUser() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT uID\n" +
                    "FROM  Rental \n" +
                    "where overdue <> FALSE\n" +
                    "UNION\n" +
                    "SELECT uID\n" +
                    "FROM Billing \n" +
                    "where balance > 0;\n";

            statement.executeQuery(query);

            ResultSet result = statement.getResultSet();

            System.out.println();

            System.out.println("Here are the uIDs: ");
            while (result.next()) {
                System.out.println(result.getNString("uID"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
    }


}
