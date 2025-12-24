
public class NotificationsRecord {

	private int notificationId;
	private String fromUser; // usually loginID1 in the DB
	private String toUser; // usually loginID2 in the DB
	private String content;
	private String status;

	// Constructor
	public NotificationsRecord(int notificationId, String fromUser, String toUser, String content, String status) {
		this.notificationId = notificationId;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.status = status;
	}

	// Getters (and setters if needed)
	public int getNotificationId() {
		return notificationId;
	}

	public String getFromUser() {
		return fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public String getContent() {
		return content;
	}

	public String getStatus() {
		return status;
	}
}
