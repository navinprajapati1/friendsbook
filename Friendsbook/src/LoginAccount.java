import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class LoginAccount {

	private DataStorage data;
	private LoginAccount theLoginAccount;
	String id;
	String password;


	public LoginAccount(DataStorage d) {
		// TODO Auto-generated constructor stub
		data = d;
	}

	public LoginAccount(String d, String p) {
		id = d;
		password = p;
	}

	public void loginaccount() {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter your login ID");
		id = input.next();
		System.out.println("Please enter your  password");
		password = input.next();

		// data.loginaccount(id, password);

		theLoginAccount = data.loginaccount(id, password);
		if (theLoginAccount != null) {
			System.out.println();
			System.out.println("Hello " + id + ", welcome to your account");
			logindata(id);
		} else {
			System.out.println("The login failed");
			System.out.println();
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private void logindata(String id) {

		Scanner input = new Scanner(System.in);
		String selection = "";

		while (!selection.equals("x")) {
			System.out.println();
			System.out.println("Please make your selection: ");
			System.out.println("1: Select a post");
			System.out.println("2: Check notifications");
			System.out.println("3: Create a new post");
			System.out.println("4: Friends");
			System.out.println("5: Send a message to your friend");
			System.out.println("6: Send a friend request");
			System.out.println("7: See Hashtag in trends ");

			System.out.println("x: Finish");

			selection = input.nextLine();
			System.out.println();

			if (selection.equals("1")) {
				new Post(data, id).selectPost();
			} else if (selection.equals("2")) {
				new Notifications(data, id).notification();
			} else if (selection.equals("3")) {
				new Post(data, id).createNewPost();
			} else if (selection.equals("4")) {
				new Friends(data, id).friends();
			} else if (selection.equals("5")) {
				new Friends(data, id).sendMsg();
			} else if (selection.equals("6")) {
				new Notifications(data, id).sendRequest();
			} else if (selection.equals("7")) {
				Hashtag();
			}
		}

	}

	private void Hashtag() {
		 List<PostRecord> posts = data.getAllPostsForUser(id);
		    List<String> tags = new ArrayList<>();
		    for (PostRecord p : posts) {
		        String t = p.getHashtag();
		        if (t != null && !t.trim().isEmpty()) {
		            t = t.trim();
		            if (!tags.contains(t)) {
						tags.add(t);
					}
		        }
		    }
		    if (tags.isEmpty()) {
		        System.out.println("No hashtags available.");
		        return;
		    }

		    Collections.shuffle(tags);
		    System.out.println("Hashtags:");
		    int limit = Math.min(2, tags.size());
		    for (int i = 0; i < limit; i++) {
		        System.out.println((i+1) + ": " + tags.get(i));
		    }

		    Scanner sc = new Scanner(System.in);
		    System.out.println("Select 1 or 2, or type a new one:");
		    String choice = sc.nextLine().trim();
		    String selected = choice.equals("1") ? tags.get(0)
		                    : choice.equals("2") && tags.size() > 1 ? tags.get(1)
		                    : choice;

		    System.out.println("Posts with #" + selected + ":");
		    for (PostRecord p : posts) {
		        if (selected.equalsIgnoreCase(p.getHashtag())) {
		            System.out.println("- " 
		                + p.getContent() + " | " 
							+ p.getDateTime() + " | From user " 
		                + p.getUserID());
				}
		    }
		}
	}

