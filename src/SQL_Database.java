
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQL_Database implements DataStorage {

	final String DATABASE_URL = "jdbc:mysql://cobmysql.uhcl.edu/prajapatin1961?useSSL=false";
	final String db_id = "prajapatin1961";
	final String db_psw = "2341961";

	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;

	@Override
	public void createAccount(String id, String password, String gender, String school) {
		try {
			// connect to the database
			connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
			// create a statement
			statement = connection.createStatement();

			// to get the account number
			resultSet = statement.executeQuery("Select * " + "from userprofile");

			// rolled back to here if anything wrong
			connection.setAutoCommit(false);

			int r = statement.executeUpdate("insert into userprofile values " + "('" + id + "', '" + gender + "', '"
					+ password + "', '" + school + "')");

			connection.commit();
			connection.setAutoCommit(true);

			// display msg
			System.out.println("*The user is created!*");
			System.out.println("*The user is " + id + "!*");
			System.out.println();

		} catch (SQLException e) {
			// handle the exceptions
			System.out.println("user creation failed");
			e.printStackTrace();
		} finally {
			// close the database
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public LoginAccount loginaccount(String id, String password) {
		try {

			// connect to the database
			connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
			// create statement
			statement = connection.createStatement();
			// search the accountID in the onlineAccount table
			resultSet = statement.executeQuery("Select * from userprofile " + "where loginID = '" + id + "'");

			if (resultSet.next()) {
				// the id is found, check the password
				if (password.equals(resultSet.getString(3))) {
					// password is good
					return new LoginAccount(resultSet.getString(1), resultSet.getString(3));
				} else {
					// password is not correct
					return null;
				}
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			// close the database
			try {
				connection.close();
				statement.close();
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void Notification(String type, String content, String datetime, String status, String fromuser,
			String user2) {
		
		try {
			// connect to the database
			connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
			// create a statement
			statement = connection.createStatement();

			// to get the account number
			resultSet = statement.executeQuery("Select * " + "from userprofile");

			// rolled back to here if anything wrong
			connection.setAutoCommit(false);

			int r = statement.executeUpdate(
					"insert into notification(type , content , dateTime ,status ,loginID1 ,loginID2) values ('" + type
							+ "', '" + content + "', '" + datetime + "', '" + status + "', '" + fromuser + "', '"
							+ user2 + "')");

			connection.commit();
			connection.setAutoCommit(true);

			// display msg
			if (type.equals("Friend Request")) {
				System.out.println("*Friend request sent*");
				System.out.println("*The user " + user2 + " has a pending request !*");
			}
			System.out.println();

		} catch (SQLException e) {
			// handle the exceptions
			System.out.println("user not found ");
			e.printStackTrace();
		} finally {
			// close the database
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<NotificationsRecord> getPendingNotifications(String account) {
		List<NotificationsRecord> pendingList = new ArrayList<>();

		// Open connection, do the SELECT, close everything
		try (Connection connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
				Statement statement = connection.createStatement()) {

			String sql = "SELECT notificationid, loginID1, loginID2, content, status " + "FROM notification "
					+ "WHERE loginID2 = '" + account + "' AND status = 'pending'";

			try (ResultSet rs = statement.executeQuery(sql)) {
				while (rs.next()) {
					int notifId = rs.getInt("notificationid");
					String fromUser = rs.getString("loginID1");
					String toUser = rs.getString("loginID2");
					String content = rs.getString("content");
					String status = rs.getString("status");

					// Build a NotificationRecord
					NotificationsRecord record = new NotificationsRecord(notifId, fromUser, toUser, content, status);
					pendingList.add(record);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Return the list
		return pendingList;
	}

	@Override
	public void acceptFriendRequest(int notificationId, String fromUser, String toUser) {
			// We'll do a small transaction
			try (Connection connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
					Statement statement = connection.createStatement()) {

				connection.setAutoCommit(false);

				// Insert into friends
				String insertFriends = "INSERT INTO friends (loginID1, loginID2) " + "VALUES ('" + fromUser + "', '"
						+ toUser
						+ "')";
				statement.executeUpdate(insertFriends);

				// Update notification
				String updateNotif = "UPDATE notification SET status='accepted' WHERE notificationid=" + notificationId;
				statement.executeUpdate(updateNotif);

				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


	@Override
	public void rejectFriendRequest(int notificationId) {
		try (Connection connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
				Statement statement = connection.createStatement()) {

			connection.setAutoCommit(false);

			String updateNotif = "UPDATE notification SET status='rejected' WHERE notificationid=" + notificationId;
			statement.executeUpdate(updateNotif);

			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getFriendsForUser(String id) {

		List<String> friendIDs = new ArrayList<>();

		String sql = "SELECT up.loginID " + "FROM friends f " + "JOIN userprofile up ON f.loginID2 = up.loginID "
				+ "WHERE f.loginID1 = '" + id + "' " + "UNION " + "SELECT up.loginID " + "FROM friends f "
				+ "JOIN userprofile up ON f.loginID1 = up.loginID " + "WHERE f.loginID2 = '" + id + "'";

		try (Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				String friendLoginID = rs.getString("loginID");
				friendIDs.add(friendLoginID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friendIDs;
	}

	@Override
	public boolean isFriends(String user1, String user2) {
		boolean result = false;

	        String sql = "SELECT * FROM friends "
					+ "WHERE (loginID1 = '" + user1 + "' AND loginID2 = '" + user2 + "') " + "   OR (loginID1 = '"
					+ user2 + "' AND loginID2 = '" + user1 + "')";

	        try (
	            Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
	            Statement stmt = conn.createStatement();
	            ResultSet rs   = stmt.executeQuery(sql);
	        ) {
	            if (rs.next()) {
	                result = true; // Found a row => they're friends
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    @Override
	    public void sendMessage(String fromUser, String toUser, String message) {

	        if (!isFriends(fromUser, toUser)) {
	            System.out.println("Error: You cannot send a message to " + toUser + " because they're not your friend.");
	            return;
	        }

	        String type = "Message";
	        String status = "unread"; // or whatever you want
			String datetime = dateTime.DateTime();

	        Notification(type, message, datetime, status, fromUser, toUser);
	        
	        System.out.println("Message sent to " + toUser);
		}

		@Override
		public List<NotificationsRecord> getMessages(String user) {
			List<NotificationsRecord> messages = new ArrayList<>();
			String sql = "SELECT notificationid, loginID1, loginID2, content, status " + "FROM notification "
					+ "WHERE loginID2 = '" + user + "' " + "  AND type = 'Message'" + "  AND status = 'unread'";

			try (Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
				while (rs.next()) {
					int notifId = rs.getInt("notificationid");
					String fromUser = rs.getString("loginID1");
					String toUser = rs.getString("loginID2");
					String content = rs.getString("content");
					String status = rs.getString("status");
					messages.add(new NotificationsRecord(notifId, fromUser, toUser, content, status));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return messages;
		}

		@Override
		public void markNotificationAsRead(int notificationId) {
			String sql = "UPDATE notification SET status='read' WHERE notificationid=" + notificationId;
			try (Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
					Statement stmt = conn.createStatement()) {
				conn.setAutoCommit(false);
				stmt.executeUpdate(sql);
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void createPost(String ID, String content, String hashtag, String dateTime) {
			String sql = "INSERT INTO post (loginID, content, datetime, hashtag) " + "VALUES ('" + ID + "', '"
					+ content + "', '" + dateTime + "', '" + hashtag + "')";

			try (Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
					Statement stmt = conn.createStatement();) {
				conn.setAutoCommit(false);
				stmt.executeUpdate(sql);
				conn.commit();

				System.out.println("Post created successfully by user: " + ID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	    @Override
	    public List<PostRecord> getAllPostsForUser(String userID) {
	        List<PostRecord> posts = new ArrayList<>();

	        String sql =
					"SELECT p.postID, p.loginID, p.content, p.datetime, p.hashtag " + "FROM post p "
							+ "JOIN friends f ON (p.loginID = f.loginID1 OR p.loginID = f.loginID2) "
							+ "WHERE (f.loginID1 = '" + userID + "' OR f.loginID2 = '" + userID + "') "
							+ "  AND p.loginID <> '" + userID + "'";

	        try (
	            Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
	            Statement stmt   = conn.createStatement();
	            ResultSet rs     = stmt.executeQuery(sql);
	        ) {
	            while (rs.next()) {
					int postID = rs.getInt("postID"); // or whatever your PK is named
					String uID = rs.getString("loginID");
	                String cont   = rs.getString("content");
	                String dt     = rs.getString("datetime");
	                String hash   = rs.getString("hashtag");

	                PostRecord record = new PostRecord(postID, uID, cont, dt, hash);
	                posts.add(record);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return posts;
	    }

	    @Override
	    public void createComment(int postID, String userID, String content, String dateTime) {
			String sql = "INSERT INTO comments (loginID, postID, content, datetime) "
	                   + "VALUES ('" + userID + "', " + postID + ", '" + content + "', '" + dateTime + "')";

	        try (
	            Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
	            Statement stmt   = conn.createStatement();
	        ) {
	            conn.setAutoCommit(false);
	            stmt.executeUpdate(sql);
	            conn.commit();

	            System.out.println("Comment added successfully by user: " + userID);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    public List<CommentRecord> getCommentsForPost(int postID) {
	        List<CommentRecord> comments = new ArrayList<>();
			String sql = "SELECT commentID, loginID, postID, content, datetime "
	                   + "FROM comments "
	                   + "WHERE postID = " + postID;

	        try (
	            Connection conn = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
	            Statement stmt   = conn.createStatement();
	            ResultSet rs     = stmt.executeQuery(sql);
	        ) {
	            while (rs.next()) {
					int cID = rs.getInt("commentID");
					String uID = rs.getString("loginID");
	                int pID   = rs.getInt("postID");
	                String cnt= rs.getString("content");
	                String dt = rs.getString("datetime");

	                CommentRecord rec = new CommentRecord(cID, uID, pID, cnt, dt);
	                comments.add(rec);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return comments;
	    }
	}


