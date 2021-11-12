package classes;
import java.util.Scanner;

public class MainMenu {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Welcome to the Disney Rental Store!");
			System.out.println("Please choose an option to log in");
			System.out.println("\n");
			System.out.println("[1] Store member [2] Admin");
			
			String response = scanner.nextLine().trim();
			if (response.equals("1")) {
				Member m = new Member();
				m.memberlogin();
			} else if (response.equals("2")) {
				adminlogin();
			} else if (response.length() > 1) {
				System.out.println("Please enter only the number option");
			} else {
				System.out.println("There seems to be an error. Please try again");
			}
		}
	}
	
	private static void adminlogin() {
		
	}
	
}
