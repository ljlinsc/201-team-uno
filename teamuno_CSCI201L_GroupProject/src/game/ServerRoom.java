package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import teamuno_CSCI201L_GroupProject.User;

public class ServerRoom {
	private List<User> players;
	private Deck deck;
	public String roomID;
	public Lock userLookup = new ReentrantLock();
	public ServerRoom(String roomID) {
		this.roomID = roomID;
		this.deck = new Deck();
		this.players = Collections.synchronizedList(new ArrayList<User>());
	}
	public void addUser(User user) {
		this.players.add(user);
	}
	public User getUserByUsername(String username) {
		this.userLookup.lock();
		for(User u: this.players) {
			if(username.equals(u.getUsername())) {
				this.userLookup.unlock();
				return u;
			}
		}
		this.userLookup.unlock();
		return null;
	}
	public User getUserByNickname(String nickname) {
		this.userLookup.lock();
		for(User u: this.players) {
			if(nickname.equals(u.getNickname())) {
				this.userLookup.unlock();
				return u;
			}
		}
		this.userLookup.unlock();
		return null;
	}
	public boolean hasUserByUsername(String username) {
		if(this.getUserByUsername(username) != null) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean hasUserByNickname(String nickname) {
		if(this.getUserByNickname(nickname) != null) {
			return true;
		}
		else {
			return false;
		}
	}
}
