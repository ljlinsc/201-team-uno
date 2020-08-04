package teamuno_CSCI201L_GroupProject;

public class User {
	private String username;
	private String nickname;
	private boolean registered;
	
	public User(String username, String nickname, boolean registered) {
		this.setUsername(username);
		this.setNickname(nickname);
		this.setRegistered(registered);
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
