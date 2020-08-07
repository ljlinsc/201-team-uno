 package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import game.Card;
import game.Deck;

public class ServerThread implements Runnable{
	private String username;
	private Vector<Card> playerHand;
	private Deck wholeDeck; 
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private GameRoom gr;
	
	
	public ServerThread(String username, GameRoom gr, Socket s) {
		try {
			this.username = username;
			this.gr = gr;
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void playersTurn() {
		
	}
	public void startTurn() {
		Message m = new Message();
		m.message = "It is your turn now";
		m.turn = username;
		sendMessage(m);
	}
	
	/*
	 * send message to the player with specific info
	 * 
	 * @param message object 
	 */
	public void sendMessage(Message m) {
		try {
			oos.writeObject(m);
			oos.flush();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void drawCard() {
		// draw card only if the deck is not empty 
		if (!wholeDeck.deck_isEmpty()) {
			Card c = new Card();
			c = wholeDeck.addToPlayers();
			playerHand.add(c);
		}
	}
	
	public void playCard(Card c) {
		for (int i=0; i<playerHand.size(); i++) {
			// find the card and remove it 
			if (playerHand.get(i).equals(c)) {
				playerHand.remove(i);
				System.out.println("Card " + c + " has been played. ");
				break;
			}
			else 
				System.out.println("You have no cards left. You ");
		}
	}
	
	public void run() {
		
	}
	public String getUserName() {
		// TODO Auto-generated method stub
		return this.username;
	}
	
}
