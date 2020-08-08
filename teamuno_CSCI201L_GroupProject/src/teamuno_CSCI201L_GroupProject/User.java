package teamuno_CSCI201L_GroupProject;

import java.io.IOException;
import java.util.List;

import javax.websocket.Session;

import game.Card;

public class User {
	private String username;
	private String nickname;
	private String roomID = null;
	private Session comm;
	private List<Card> player_hand;
	private boolean registered;
	private boolean isReady;
	
	public User(String username, String nickname, boolean registered) {
		this.username = username;
		this.nickname = nickname;
		this.registered = registered;
	}
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
		this.isReady = false;
	}
	public void setRoomID(String ID) {
		this.roomID = ID;
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
}
