import java.util.List;
import java.util.Scanner;

public class Notifications {

	private DataStorage data;
	private String account;

	public Notifications(DataStorage d, String id) {
		data = d;
		account = id;
	}

	public void notification() {

		Scanner sc = new Scanner(System.in);

		System.out.println("---- Notifications Menu ----");
		System.out.println("1) Check new messages");
		System.out.println("2) Check friend requests");
		System.out.println("Enter your choice:");
		String choice = sc.nextLine();

		// If user chooses option 1 => show new messages
		if (choice.equals("1")) {
			List<NotificationsRecord> messages = data.getMessages(account);
			if (messages.isEmpty()) {
				System.out.println("No new messages.");
			} else {
				for (NotificationsRecord msg : messages) {
					System.out.println("From: " + msg.getFromUser());
					System.out.println("Content: " + msg.getContent());
					System.out.print("Reply? (Y/N): ");
					String replyChoice = sc.nextLine();
					if (replyChoice.equalsIgnoreCase("Y")) {
						System.out.print("Enter your reply: ");
						String reply = sc.nextLine();
						data.sendMessage(account, msg.getFromUser(), reply);
					}
					data.markNotificationAsRead(msg.getNotificationId());
					System.out.println();
				}
			}
		}

		// If user chooses option 2 => show pending friend requests
		else if (choice.equals("2")) {
			List<NotificationsRecord> pendingList = data.getPendingNotifications(account);
			if (pendingList.isEmpty()) {
				System.out.println("No pending friend requests.");
			} else {
				System.out.println("---- Pending Friend Requests ----");
				for (NotificationsRecord record : pendingList) {
					System.out.println("Notification ID: " + record.getNotificationId());
					System.out.println("From User: " + record.getFromUser());
					System.out.println("Content: " + record.getContent());
					System.out.println("Status: " + record.getStatus());
					System.out.println("-----------------------------------");

					System.out.println("Would you like to Accept (A) or Reject (R) this friend request?");
					String friendChoice = sc.nextLine();

					if (friendChoice.equalsIgnoreCase("A")) {
						data.acceptFriendRequest(record.getNotificationId(), record.getFromUser(), record.getToUser());
						System.out.println("You have accepted the friend request from " + record.getFromUser());
					} else if (friendChoice.equalsIgnoreCase("R")) {
						data.rejectFriendRequest(record.getNotificationId());
						System.out.println("You have rejected the friend request from " + record.getFromUser());
					} else {
						System.out.println("Invalid choice. Skipping...");
					}
					System.out.println();
				}
			}
		} else {
			System.out.println("Invalid option.");
		}
	}


	public void sendRequest() {
		    Scanner sc = new Scanner(System.in);
		    System.out.println("Enter the friend's username to send a friend request:");
		    String user2 = sc.nextLine();

		    String fromuser = account;

		    if (data.isFriends(fromuser, user2)) {
		        System.out.println("You are already friends with " + user2 + ". No need to send a friend request.");
		        return;
		    }

		    String content = fromuser + " sent a friend request to " + user2;
		    String status = "pending";
		    String type = "Friend Request";
		    String datetime = dateTime.DateTime();

		    data.Notification(type, content, datetime, status, fromuser, user2);

		    System.out.println("Friend request sent to " + user2 + ". Status: Pending");


	}

}
