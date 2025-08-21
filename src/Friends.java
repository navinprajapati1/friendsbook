import java.util.List;
import java.util.Scanner;

public class Friends {

	private DataStorage data;
	private String id;
	private String friendname;

	public Friends(DataStorage d, String id) {

		data = d;
		this.id = id;

	}

	public void friends() {

		List<String> friends = data.getFriendsForUser(id);

		if (friends.isEmpty()) {
			System.out.println("You have no friends yet!");
		} else {
			System.out.println("Your friends:");
			for (String friendID : friends) {
				System.out.println(" - " + friendID);
			}
		}
	}

	public void sendMsg() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the friend's username to send a message:");
		String friend = sc.nextLine();
		System.out.println("Enter your message:");
		String content = sc.nextLine();

		data.sendMessage(id, friend, content);

	}

}
