package Server;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.websocket.Session;

/*
 * Game rooms that handles
 */
public class GameRoom implements Runnable{
	private Vector<ServerThread> Players = new Vector<>();
	private Queue<ServerThread> Turns =  new LinkedList<>();


	public GameRoom() {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public void initializeGame() {
		
	}
	public void startGame() {
		
	}
	public void broadcastMessage(Message m) {
		if (m.message != null) {
			for(ServerThread threads : Players) {
					threads.sendMessage(m);
			}
		}
	}
	public void broadcastTurn() {
		Message m = new Message();
		m.turn = Turns.poll().getUserName(); // get the first player in the turn queue
		m.message = "it is " + m.turn + "'s turn.";
	}
	
	public Vector<ServerThread> getPlayers() {
		return Players;
	}
	public void setPlayers(Vector<ServerThread> Players) {
		this.Players = Players;
	}

}
