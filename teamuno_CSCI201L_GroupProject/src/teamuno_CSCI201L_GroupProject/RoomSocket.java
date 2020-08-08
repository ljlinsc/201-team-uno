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
		System.out.println("Connection made!");
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println(message);
		
		String errorMessage = "{"
				+ "\"type\" : \"error\","
				+ "\"message\" : \"TESTTEST! already exists and is in the room\""
				+ "}";
		
		try {
			session.getBasicRemote().sendText(errorMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("could not send msg="+errorMessage);
			e.printStackTrace();
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
