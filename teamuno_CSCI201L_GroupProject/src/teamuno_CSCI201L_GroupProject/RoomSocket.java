package teamuno_CSCI201L_GroupProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
 * 		action : "uno"
 * 		action : "draw"
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
 * 		color: String,
 * 		value: String,
 * 		endTurn: boolean
 * }
 * This will make the user turn, and then broadcast to all other users the result of the action.
 **********************************************************************************************
 * "uno"
 * {
 * 		action : "uno",
 * 		username : String,
 * 		nickname : String,
 * 		roomID : String,
 * 		value: String,
 * 		color: String
 * }
 * "draw"
 * {
 * 		action : "draw",
 * 		username : String,
 * 		nickname : String,
 * 		roomID : String,
 * 		value: String,
 * 		color: String
 * }
 * 
 * Uno card notation format:
 * Draw Card:
 * 		card: 0
 * 		color: null // MAYBE REMOVE
 * Regular Cards:
 * 		card: "One", "Two", ..., "Nine" (depending on value of card)
 * 		color: "Red", "Blue", "Yellow"
 * 
 * Special Cards: 
 * Reverse:
 * 		card: "Reverse"
 * 		color: color of card
 * Skip:
 * 		card: "Skip"
 * 		color: color of card
 * Draw Two:
 * 		card: "DrawTwo"
 * 		color: color of card
 * Wild:
 * 		card: "Wild"
 * 		color: "Wild"
 * Wild Draw Four:
 * 		card: "Wild"
 * 		value: "Wild_Four"
 */

@ServerEndpoint(value = "/RoomSocket")
public class RoomSocket {
	private static HashMap<String, Game> rooms = new HashMap<String, Game>();
	
	@OnOpen
	public void open(Session session) {
		if (rooms.isEmpty()) {
			rooms.put("1x1x1x", new Game("1x1x1x"));
		}
		
		System.out.println("Connection made!");
		
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println(message);

		String cardData = "<div class=\"card\">\n" + 
				"				<div class=\"card-back card-face\">\n" + 
				"					<img class=\"uno\" src=\"IMG/Blue_Zero.png\">\n" + 
				"				</div>\n" + 
				"				<div class=\"card-front card-face\">\n" + 
				"				\n" + 
				"				</div>\n" + 
				"			</div>";
		cardData="Red_Zero.png";
		String addMessage = "{"
				+ "\"type\" : \"content-change\","
				+ "\"message\" : \"" + cardData + "\","
				+ "\"contentChangeType\" : \"addCard\""
				+ "}";
		
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject)parser.parse(message);
		} catch (ParseException e) {
			System.out.println("RoomSocket: Was unable to parse JSON in on Message " + message);
			e.printStackTrace();
		}
		//Check if message is empty
		if(json != null) {
			//Get action
			String action = (String)json.get("action");
			//Get username
			String username = (String)json.get("username");
			//Get nickname
			String nickname = (String)json.get("nickname");
			String roomID = (String)json.get("roomID");
			Game player_room = this.getRoomByID(roomID);

			
			//Creating room
			if(action.equals("createRoom")) {
				if(!rooms.containsKey(roomID) && !this.checkIfUserInRoom(username)) {
					Game created_room = new Game(roomID);
					User add_user = new User(username, nickname, session);
					created_room.addUser(add_user);
				}
				else {
					//Return error
					String errorMessage;
					if (rooms.containsKey(roomID) && this.checkIfUserInRoom(username)) {
						errorMessage = "{"
								+ "\"type\" : \"error\""
								+ "\"message\" : \"Room already exists and is in the room\""
								+ "}";
					}	
					else if (rooms.containsKey(roomID)) {
						errorMessage = "{"
								+ "\"type\" : \"error\""
								+ "\"message\" : \"Room already exists\""
								+ "}";
					} else {
						errorMessage = "{"
								+ "\"type\" : \"error\""
								+ "\"message\" : \"User already in roo\""
								+ "}";
					}
					try {
						session.getBasicRemote().sendText(errorMessage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("RoomSocket: Could not send message " + errorMessage);
						e.printStackTrace();
					}
				}
				
			}
			//Joining room
			else if(action.equals("joinRoom")) {
				roomID = (String)json.get("roomID");
				Game join_room = this.getRoomByID(roomID);
				// Cannot join game that is running
				if (join_room.isRunning()) {
					String error = "{"
							+ "\"type\" : \"error\","
							+ "\"message\" : \"Cannot join game, it already started\""
							+ "}";
					try {
						session.getBasicRemote().sendText(error);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("RoomSocket: Could not send message " + error);
						e.printStackTrace();
					}
				} 
				// Cannot join game with max num players
				else if (join_room.getUserCount() > 5) {
					String error = "{"
							+ "\"type\" : \"error\","
							+ "\"message\" : \"Game cannot have more than 5 players\""
							+ "}";
					try {
						session.getBasicRemote().sendText(error);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("RoomSocket: Could not send message " + error);
						e.printStackTrace();
					}
				}
				else {
					System.out.println("Adding user, username=" + username + " nickname="+nickname);
					User add_user = new User(username, nickname, session);	
					join_room.addUser(add_user);					
				}
			}
			else if(action.equals("ready")) {
				player_room = this.getRoomByID(roomID);
				if (player_room == null) {
					
				} else {
					if (username == null)
						player_room.setUserReady(nickname);
					else
						player_room.setUserReady(username);
				}
			}
			//Player taking turn
			else {
				roomID = (String)json.get("roomID");
				player_room = this.getRoomByID(roomID);
				if (player_room != null && player_room.isRunning()) {
					player_room.processRequest(message);					
				} else {
					String errorMessage = "{"
							+ "\"type\" : \"error\""
							+ "\"message\" : \"Game is not running\""
							+ "}";
				}
			}
		}
		
	
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting!");
	}
	
	@OnError
	public void error(Throwable error) {
		System.out.println("Error!" + error.getMessage());
		error.printStackTrace();
	}
	
	
	private boolean checkIfUserInRoom(String username) {
		if(username == null) {
			return false;
		}
		for(Game s: rooms.values()) {
			if(s.hasUserByUsername(username)) {
				return true;
			}
		}
		return false;
	}
	private Game getRoomByID(String ID) {
		if (rooms.containsKey(ID)) {
			return rooms.get(ID);
		}
		
		return null;
	}

}
