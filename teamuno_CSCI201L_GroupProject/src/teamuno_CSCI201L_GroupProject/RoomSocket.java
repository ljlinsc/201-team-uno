package teamuno_CSCI201L_GroupProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

import org.json.simple.JSONObject;

import game.ServerRoom;
/*
 * Format of JSON Messages:
 * {
 * 		action : String,
 * 		username : String,
 * 		roomID: String
 * }
 **********************************************************************************************
 * action denotes one of a variety of actions:
 * 		action : "createRoom"
 * 		action : "joinRoom"
 * 		action : "makeTurn"
 **********************************************************************************************
 * Based on the action determines the server response:
 * "createRoom"
 * {
 * 		action : "createRoom",
 * 		nickname : String,
 * 		username : String,
 * 		roomID: NULL
 * }
 * This will create a new room for the user. Additionally, will send the user the userRoom file.
 **********************************************************************************************
 * "joinRoom"
 * {
 * 		action : "joinRoom",
 * 		nickname : String,
 * 		username : String,
 * 		roomID: String
 * }
 * This will simply add the user to the room while also sending the user the userRoom file.
 **********************************************************************************************
 * "makeTurn"
 * {
 * 		action : "makeTurn",
 * 		username : String,
 * 		nickname : String,
 * 		roomID: String,
 * 		card: Integer,
 * 		color: String,
 * }
 * This will make the user turn, and then broadcast to all other users the result of the action.
 **********************************************************************************************
 * Uno card notation format:
 * Regular Cards:
 * 		card: 1-9 (depending on value of card)
 * 		color: color of card
 * Reverse:
 * 		card: 10
 * 		color: color of card
 * Skip:
 * 		card: 11
 * 		color: color of card
 * Draw Two:
 * 		card: 12
 * 		color: color of card
 * Wild:
 * 		card: 13
 * 		color: users color choice
 * Wild Draw Four:
 * 		card: 14
 * 		color: users color choice
 */

@ServerEndpoint(value = "/RoomSocket")
public class RoomSocket extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static List<ServerRoom> rooms = Collections.synchronizedList(new ArrayList<ServerRoom>());
	@OnOpen
	public void open(Session session) {
		System.out.println("Connection made!");
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println(message);
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject)parser.parse(message);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(json != null) {
			String action = (String)json.get("action");
			String username = (String)json.get("username");
			String nickname = (String)json.get("nickname");
			if(action.equals("createRoom")) {
				if(!this.checkIfUserInRoom(username)) {
					String roomID = (String)json.get("roomID");
					ServerRoom created_room = new ServerRoom(roomID);
					User add_user = new User(username, nickname, session);
					created_room.addUser(add_user);
				}
				else {
					//Return error
				}
				
			}
			else if(action.equals("joinRoom")) {
				String roomID = (String)json.get("roomID");
				ServerRoom join_room = this.getRoomByID(roomID);
				User add_user = new User(username, nickname, session);	
				join_room.addUser(add_user);
			}
			else if (action.equals("makeTurn")) {
				String roomID = (String)json.get("roomID");
				ServerRoom join_room = this.getRoomByID(roomID);
			}
		}
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting!");
	}
	
	@OnError
	public void error(Throwable error) {
		System.out.println("Error!");
	}
	private boolean checkIfUserInRoom(String username) {
		if(username == null) {
			return false;
		}
		for(ServerRoom s: rooms) {
			if(s.hasUserByUsername(username)) {
				return true;
			}
		}
		return false;
	}
	private ServerRoom getRoomByID(String ID) {
		for(ServerRoom s: rooms) {
			if(s.roomID.equals(ID)) {
				return s;
			}
		}
		return null;
	}

}
