// CommentRecord.java
public class CommentRecord {
	private int commentID;
	private String userID;
	private int postID;
	private String content;
	private String dateTime;

	public CommentRecord(int commentID, String userID, int postID, String content, String dateTime) {
		this.commentID = commentID;
		this.userID = userID;
		this.postID = postID;
		this.content = content;
		this.dateTime = dateTime;
	}

	// Getters
	public int getCommentID() {
		return commentID;
	}

	public String getUserID() {
		return userID;
	}

	public int getPostID() {
		return postID;
	}

	public String getContent() {
		return content;
	}

	public String getDateTime() {
		return dateTime;
	}
}
