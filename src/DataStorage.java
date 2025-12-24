import java.util.List;

public interface DataStorage {

	public void createAccount(String id, String password, String gender, String school);

	public LoginAccount loginaccount(String id, String password);

	public void Notification(String type, String content, String datetime, String status, String fromuser,
			String user2);

	public List<NotificationsRecord> getPendingNotifications(String account);

	public void acceptFriendRequest(int notificationId, String fromUser, String toUser);

	public void rejectFriendRequest(int notificationId);

	public List<String> getFriendsForUser(String id);

	public void sendMessage(String id, String friend, String content);

	boolean isFriends(String user1, String user2);

	public List<NotificationsRecord> getMessages(String account);

	public void createPost(String account, String content, String hashtag, String dateTime);

	public List<PostRecord> getAllPostsForUser(String account);

	public void createComment(int postID, String account, String comment, String dateTimes);

	public List<CommentRecord> getCommentsForPost(int postID);

	public void markNotificationAsRead(int notificationId);

}
