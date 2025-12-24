import java.util.List;
import java.util.Scanner;

public class Post {
	private DataStorage data;
	private String account;

	public Post(DataStorage d, String userID) {
		data = d;
		account = userID;
	}

	// Called by the main menu
	public void createNewPost() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter post content:");
		String content = sc.nextLine();

		System.out.println("Enter a hashtag (optional):");
		String hashtag = sc.nextLine();


		String dt = dateTime.DateTime();
		data.createPost(account, content, hashtag, dt);
	}

	public void selectPost() {
		List<PostRecord> posts = data.getAllPostsForUser(account);
		if (posts.isEmpty()) {
			System.out.println("No posts found.");
			return;
		}

		System.out.println("---- Available Posts ----");
		int index = 1;
		for (PostRecord p : posts) {
			System.out.println(index + ") " + p.getContent() + "  " + p.getHashtag() + " | " + p.getDateTime()
					+ " | From user " + p.getUserID());
			index++;
		}


		Scanner sc = new Scanner(System.in);
		System.out.println("Select a post number to view/comments:");
		String choice = sc.nextLine();
		int selectedIndex;
		try {
			selectedIndex = Integer.parseInt(choice);
		} catch (NumberFormatException e) {
			System.out.println("Invalid selection.");
			return;
		}
		if (selectedIndex < 1 || selectedIndex > posts.size()) {
			System.out.println("Invalid selection.");
			return;
		}

		PostRecord chosen = posts.get(selectedIndex - 1);
		int postID = chosen.getPostID();

		List<CommentRecord> comments = data.getCommentsForPost(postID);
		if (comments.isEmpty()) {
			System.out.println("No comments yet.");
		} else {
			System.out.println("---- Comments ----");
			for (CommentRecord c : comments) {
				System.out.println(
						" Comment from  " + c.getUserID() + ": " + c.getContent() + " (at " + c.getDateTime() + ")");
			}
		}

		System.out.println("Enter 'C' to add comment, or press Enter to skip:");
		String cmd = sc.nextLine();
		if (cmd.equalsIgnoreCase("C")) {
			addComment(postID);
		}
	}

	private void addComment(int postID) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your comment (max 20 chars):");
		String comment = sc.nextLine();
		if (comment.length() > 20) {
			comment = comment.substring(0, 20);
		}
		String dt = dateTime.DateTime();
		data.createComment(postID, account, comment, dt);
	}
}
