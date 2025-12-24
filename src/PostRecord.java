// PostRecord.java
public class PostRecord {
	private int postID;
	private String userID;
	private String content;
	private String dateTime;
	private String hashtag;

	public PostRecord(int postID, String userID, String content, String dateTime, String hashtag) {
		this.postID = postID;
		this.userID = userID;
		this.content = content;
		this.dateTime = dateTime;
		this.hashtag = hashtag;
	}

	// Getters
	public int getPostID() {
		return postID;
	}

	public String getUserID() {
		return userID;
	}

	public String getContent() {
		return content;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getHashtag() {
		return hashtag;
	}
}
