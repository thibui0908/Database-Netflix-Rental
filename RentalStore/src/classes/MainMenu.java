package classes;

import java.util.Scanner;

public class MainMenu {

    /*
     * The main menu directing input
     * There are two types of users : Member and Admin
     * Member should be able to rent, browse, and pay
     * Admin should be able to look at member's info
     *  - both individually and holistically
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
        	System.out.println();
            System.out.println("Welcome to the Disney Rental Store!");
            System.out.println("Please choose an option to log in");
            System.out.println("\n");
            System.out.println("[1] Store member [2] Admin");

            String response = scanner.nextLine().trim();
            /*
             * Create a new member object
             * Redirect to the Member's class
             * Should contain all user requests
             */
            if (response.equals("1")) {
                Member m = new Member();
                m.memberlogin();
            }
            /*
             * Create a new admin object
             * Redirect to the Admin's class
             * Should contain all admin requests
             */
            else if (response.equals("2")) {
                Admin ad = new Admin();
                ad.adminlogin();
            }
            /*
             * Handles invalid requests
             */
            else if (response.length() > 1) {
                System.out.println("Please enter only the number option");
            } else {
                System.out.println("There seems to be an error. Please try again");
            }
        }
    }
}
