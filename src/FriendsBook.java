import java.util.Scanner;

public class FriendsBook {

	public static void main(String[] args) {			// TODO Auto-generated method stub
			Scanner input = new Scanner(System.in);
			String selection = "";
			DataStorage data = new SQL_Database();
			
			while(!selection.equals("x"))  //while not x, keep displaying the menu
			{
				//display the menu
				System.out.println();
				System.out.println("Please make your selection: ");
				System.out.println("1: create a new account");
				System.out.println("2: login to your account");
				System.out.println("x: Finish");
				
				//get the selection from the user
				selection = input.nextLine();
				System.out.println();
				
				//based on the input, go to different function
				if(selection.equals("1"))
				{
					//open a new checking 
					new CreateAccount(data).createAccount();
				}
				else if (selection.equals("2")) {
					new LoginAccount(data).loginaccount();
				}

	}

}
}
