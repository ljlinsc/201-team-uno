package Server;

import teamuno_CSCI201L_GroupProject.Game;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint(value="/game")
public class GameRoom {
	private static HashMap<String, Game> rooms = new HashMap<String, Game>();
	private static HashSet<String> GameRoomIDs = new HashSet<String>();
	private String id;
	//	private Queue<ServerThread> Turns =  new LinkedList<>();

	public GameRoom(String id) {
		this.id = id;
		if (!rooms.containsKey(id)) {
			rooms.put(id, new Game(id));
		}
	}
	
	public void initializeGame(String id) {
		
	}
	
	public void startGame(String id) {
		
	}
	
	public void broadcastMessage(Message m) {
		if (m.message != null) {
			for(Game threads : rooms.values()) {
				// threads.sendMessage(m);
			}
		}
	}
	
	public void broadcastTurn() {
		Message m = new Message();
		// m.turn = Turns.poll().getUserName(); // get the first player in the turn queue
		m.message = "it is " + m.turn + "'s turn.";
	}
	
	// Socket-Specific Messages
	@OnOpen
	public void open(Session session) {
		System.out.println("New Session for GameRoom="+id);
		
		// Add session to user
		rooms.get(id).addSession(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		JSONParser parser = new JSONParser();
		JSONObject parsedMessage = null;
		try {
			parsedMessage = (JSONObject) parser.parse(message);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (parsedMessage != null) {
			String action = null;
			String userID = null;
			String roomID = null;
			String cardColor = null;
			String cardValue = null;
			String nickname = null;
			
			action = (String) parsedMessage.get("action");
			userID = (String) parsedMessage.get("username");
			roomID  = (String) parsedMessage.get("roomID");
			cardColor = (String) parsedMessage.get("color");
			cardValue = (String) parsedMessage.get("card");
			nickname = (String) parsedMessage.get("nickname");
			
			Game game = rooms.get(roomID);
//			game.addAction(action, userID, cardColor, cardValue);
		}
	}

}
