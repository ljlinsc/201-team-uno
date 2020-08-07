package teamuno_CSCI201L_GroupProject;

import javax.websocket.Session;

public class User {
	private String username;
	private String nickname;
	private Session comm;
	private boolean registered;
	
	public User(String username, String nickname, Session comm) {
		this.setUsername(username);
		this.setNickname(nickname);
		if (username== null) {
			this.registered = false;
		}
		else {
			this.registered = true;
		}
		this.comm = comm;
	}
	
	public String getUsername() {
		return username;
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
}
