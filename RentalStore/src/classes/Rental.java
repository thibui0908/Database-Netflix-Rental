package classes;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Rental {

    private String uID;

    Scanner scanner = new Scanner(System.in);
    String DB_URL = "jdbc:mysql://localhost:3306/disney";
    //  Database credentials
    String USERNAME = "root";
    String PASSWORD = "Monday2Tuesday3!";
    Connection conn = null;

    public Rental(String uID) {
        this.uID = uID;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        rentalPortal();
    }

    /*
     * Handle user's rental requests
     */
    public void rentalPortal() {
        System.out.println();
        System.out.println("What would you like to do today?");
        System.out.println("[1] Rent   [2] Return a Film/Show   [3] Exit");
        System.out.println();

        String response = scanner.nextLine().trim();

        if (response.equals("1")) {
            createRental();
        } else if (response.equals("2")) {
            returnRental();
        } else if (response.equals("3")) {
            return;
        } else if (response.length() > 1) {
            System.out.println("Please enter only the number option");
        } else {
            System.out.println("There seems to be an error. Please try again");
        }
        System.out.println();
        return;
    }


    /*
     * Insert a new user rental information
     * Enter title key words and ask user to chose which title want to rental and then insert rental table
     */
    public void createRental() {
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
            //System.out.println("newRentalQuery is: " + newRentalQuery);
//            Statement statement2 = conn.createStatement();
            statement.executeUpdate(newRentalQuery);

            updateBillingAndPayment(selectedTitle, checkoutDate);
        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
    }


    /*
     * Return a film or video for the user
     */
    public void returnRental() {
        try {
            Statement statement = conn.createStatement();

            String SearchTitlesQuery = "SELECT title FROM Titles t JOIN Rental r ON t.show_id = r.show_id WHERE r.uID = " + uID;
            statement.executeQuery(SearchTitlesQuery);
            ResultSet titlesRs = statement.getResultSet();
            System.out.println("Here are the rented films/videos: ");
            while (titlesRs.next()) {
                System.out.println(titlesRs.getNString("title"));
            }
            System.out.println("Please choose one to return: ");
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


            String deleteRentalQuery = "DELETE FROM Rental WHERE show_id = '" + showId + "'";
            //System.out.println("deleteRentalQuery is: " + deleteRentalQuery);
            statement.executeUpdate(deleteRentalQuery);
            System.out.println("Thank you for returning the film/video.");
        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
    }

    /*
     * Update user billing
     */
    public void updateBillingAndPayment(String title, String checkoutDate) {
        try {
            Statement statement = conn.createStatement();

            String SearchPriceQuery = "SELECT price FROM Titles WHERE title = '" + title + "'";
            statement.executeQuery(SearchPriceQuery);
            ResultSet priceRs = statement.getResultSet();
            float price = 0;
            while (priceRs.next()) {
                price = priceRs.getFloat("price");
                //System.out.println("price is: " + price);
            }

            String updateBillingQuery = "UPDATE Billing SET balance = balance + " + String.valueOf(price) + " WHERE uID = '" + uID + "'";
            //System.out.println("updateBillingQuery is: " + updateBillingQuery);
            statement.executeUpdate(updateBillingQuery);

        } catch (SQLException e) {
            System.out.println("There seems to be an error. Please try again");
            System.out.println("System message: " + e.getMessage());
        }
    }
}
