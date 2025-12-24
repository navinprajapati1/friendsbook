import java.util.Scanner;

public class CreateAccount {

	private DataStorage data;

	public CreateAccount(DataStorage d) {
		data = d;
	}

	public void createAccount() {
		 Scanner input = new Scanner(System.in);
	        String id;
	        String password;

	        // Validate Username
	        while (true) {
	            System.out.println("Please enter your username (3-10 chars, 1 letter, 1 digit, 1 special [#, ?, !, *]): ");
	            id = input.nextLine();

	            if (isValidInput(id)) {
	                break;
	            } else {
	                System.out.println(" Invalid username. Please try again.");
	            }
	        }

	        // Validate Password
	        while (true) {
				System.out.println("Please enter your password: ");
				password = input.nextLine();

				if (!password.equals(id)) {
	                break;
	            } else {
					System.out.println(" Password cannot be the same as your username.");
	            }
	        }

	        System.out.println("Please enter your Gender: ");
	        String gender = input.nextLine();

	        System.out.println("Please enter your School: ");
	        String school = input.nextLine();

	        System.out.println("\n Account created successfully!");
	        System.out.println("Username: " + id);
	        System.out.println("Gender: " + gender);
	        System.out.println("School: " + school);

			data.createAccount(id, password, gender, school);

	    }

		private boolean isValidInput(String str) {
	        if (str.length() < 3 || str.length() > 10) {
				return false;
			}

	        boolean hasLetter = str.matches(".*[a-zA-Z].*");
	        boolean hasDigit = str.matches(".*[0-9].*");
	        boolean hasSpecial = str.matches(".*[#!?*].*");

	        return hasLetter && hasDigit && hasSpecial;
	    }


	}
