package teamuno_CSCI201L_GroupProject;

import java.io.IOException;
import java.util.List;

import javax.websocket.Session;

public class User {
	private String username; // Used as ID for Registered Users
	private String nickname; // Used as ID for Not Registered Users
	private String roomID = null;
	private Session comm;
	private boolean registered;
	private boolean isReady;
	
	// username will be used as the ID in the game (if registered)
	// otherwise, nickname (for guests)
	public User(String username, String nickname, boolean registered) {
		this.username = username;
		this.nickname = nickname;
		this.registered = registered;
		System.out.println("User: Created user with username="+username+" nickname="+nickname+" registered="+registered);
	}
	public User(String username, String nickname, Session comm) {
		this.setUsername(username);
		this.setNickname(nickname);
		if (username == null) {
			this.registered = false;
		}
		else {
			this.registered = true;
		}
		this.comm = comm;
		this.isReady = false;
		System.out.println("User: Created user with Session=comm username="+username+" nickname="+nickname+" registered="+registered);
	}
	public void setRoomID(String ID) {
		this.roomID = ID;
	}
	public String getUsername() {
		return username;
	}
	
	public Session getSession() {
		return comm;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	public boolean isReady() {
		return this.isReady;
	}
	public boolean playerReady() {
		return this.isReady = true;
	}
	public boolean notReady() {
		return this.isReady = false;
	}
	public void sendMessage(String message) {
		try {
			this.comm.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Setter Functions */
	public void setSession(Session session) {
		this.comm = session;
	}
}
