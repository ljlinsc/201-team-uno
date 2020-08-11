package teamuno_CSCI201L_GroupProject;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.Session;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * playerIDs VECTOR INDEX maps to playerHand, sessions index
 */
public class Game {
	private int currentPlayer;
	private Vector<String> playerIDs;
	private Vector<User> players;
	private String gameID;
	private boolean running;

	private UnoDeck deck;
	private Vector<Vector<UnoCard>> playerHand;
	private Vector<UnoCard> stockPile;

	boolean gameDirection;
	private UnoCard.Value validValue;
	private UnoCard.Color validColor;

	// Server Specific
	private Vector<Session> sessions;
	private Vector<Action> actions;
	public Lock userLookup;
	public Lock sendMessageLock = new ReentrantLock();

	public Game(String id) {
		System.out.println("Game (ID=" + id + ") creating new game");
		this.players = new Vector<User>();
		this.running = false;
		this.gameID = id;
		this.deck = new UnoDeck();
		deck.shuffle();
		this.stockPile = new Vector<UnoCard>();
		this.playerIDs = new Vector<String>();
		this.gameDirection = false; // Clockwise
		this.currentPlayer = 0;
		this.playerHand = new Vector<Vector<UnoCard>>();
		this.userLookup = new ReentrantLock();

		this.sessions = new Vector<Session>();
		System.out.println("Game (ID=" + id + ") sucessfully created new game");
	}

	public boolean isRunning() {
		return running;
	}

	public void start() {
		System.out.println("Game: starting game");
		for (int i = 0; i < playerIDs.size(); i++) {
			Vector<UnoCard> hand = new Vector<UnoCard>(deck.drawCard(5));
			playerHand.setElementAt(hand, i);
		}

		UnoCard card = deck.drawCard();
		validColor = card.getColor();
		validValue = card.getValue();

		while (card.getValue() == UnoCard.Value.Wild || card.getValue() == UnoCard.Value.Wild_Four
				|| card.getValue() == UnoCard.Value.DrawTwo) {
			stockPile.add(card);
			card = deck.drawCard();
			validColor = card.getColor();
			validValue = card.getValue();
		}

		if (card.getValue() == UnoCard.Value.Skip) {
			if (gameDirection) {
				currentPlayer = (currentPlayer + 1) % playerIDs.size();
			} else {
				if (currentPlayer == 0)
					currentPlayer = playerIDs.size() - 1;
				else
					currentPlayer--;
			}
		}

		if (card.getValue() == UnoCard.Value.Reverse) {
			gameDirection ^= true;
		}

		stockPile.add(card);
		running = true;

		String addTopCard = "{" + "\"type\" : \"content-change\"," + "\"contentChangeType\" : \"changeTopCard\","
				+ "\"message\" : \"" + stockPile.get(stockPile.size() - 1).toString() + "\"" + "}";
		sendMessageToAllUsers(addTopCard);
		for (int i = 0; i < playerIDs.size(); i++) {
			Vector<UnoCard> currHand = playerHand.get(i);
			for (UnoCard c : currHand) {
				String addCards = "{" + "\"type\" : \"content-change\"," + "\"contentChangeType\" : \"initCards\","
						+ "\"message\" : \"" + c.toString() + "\"" + "}";
				try {
					sessions.get(i).getBasicRemote().sendText(addCards);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public UnoCard getTopCard() {
		return new UnoCard(validColor, validValue);
	}

	public void sendMessageToAllUsers(String message) {
		for (Session s : sessions) {
			try {
				s.getBasicRemote().sendText(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void threadedBroadcastMessage(String message) {
		sendMessageLock.lock();
		for (Session s : sessions) {
			try {
				s.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sendMessageLock.unlock();
	}

	public String getTopCardImage() {
		return getTopCard().toString();
	}

	public boolean isGameOver() {
		for (String player : this.playerIDs) {
			if (hasEmptyHand(player)) {
				return true;
			}
		}
		return false;
	}

	public String getCurrentPlayer() {
		return this.playerIDs.get(this.currentPlayer);
	}

	public Vector<UnoCard> getCurrentPlayerHand() {
		return this.playerHand.get(this.currentPlayer);
	}

	// TODO: Fully implement
	public String getPreviousPlayer(int i) {
		return "";
	}

	public Vector<UnoCard> getPlayerHand(String id) {
		int index = playerIDs.indexOf(id);
		return playerHand.get(index);

	}

	public int getPlayerHandSize(String id) {
		return getPlayerHand(id).size();
	}

	public UnoCard getPlayerCard(String id, int choice) {
		return getPlayerHand(id).get(choice);
	}

	public boolean hasEmptyHand(String id) {
		return getPlayerHand(id).isEmpty();
	}

	public boolean isValidCardPlay(UnoCard card) {
		return card.getValue() == UnoCard.Value.Wild || card.getValue() == UnoCard.Value.Wild_Four ||
				card.getColor() == validColor || card.getValue() == validValue;
	}

	public boolean checkPlayerTurn(String pid) {
		if (this.playerIDs.get(this.currentPlayer).equals(pid)) {
			return true;
		}
		return false;
	}

	public String submitDraw(String pid) {

		if (deck.isEmpty()) {
			deck.replaceDeckWith(stockPile);
		}

		// CHeck if EXCEPTION THOWN
		UnoCard newCard = deck.drawCard();
		getPlayerHand(pid).add(newCard);

		// Go to next Player
		if (!gameDirection) {
			currentPlayer = (currentPlayer + 1) % playerIDs.size();
		} else if (gameDirection) {
			if (currentPlayer == 0) {
				currentPlayer = playerIDs.size() - 1;
			} else {
				currentPlayer--;
			}
		}
		// Update user on their own info (probs not)

		// Update all other users on info

		return "{" + "\"nextPlayer\" : \"" + playerIDs.get(currentPlayer) + "\"," + "\"requestSentBy\" : \"" + pid
				+ "\"," + "\"message\" : \"" + newCard.toString() + "\"," + "\"type\" : \"content-change\","
				+ "\"contentChangeType\" : \"drawCard\"" + "}";
	}

	public void setCardColor(UnoCard.Color color) {
		validColor = color;
	}

	// Server Specific
	public void addSession(Session session) {
		sessions.add(session);
	}
	// TODO: Check there are enough cards
	// TODO: Only send data to one player not alll
	public void drawCallBack(String userID, int n) {
		Vector<UnoCard> cardsToAdd = new Vector<UnoCard>();
		for (int i = 0; i < n; i++) {
			UnoCard toAdd = deck.drawCard();
			
			String message = "{"
			+ "\"nextPlayer\" : \"" + playerIDs.get(currentPlayer) + "\","
			+ "\"requestSentBy\" : \"" + userID + "\","
			+ "\"message\" : \"" + toAdd.toString() + "\","
			+ "\"type\" : \"content-change\","
			+ "\"contentChangeType\" : \"drawCard\""
			+ "}";
			
			try {
				sessions.get(playerIDs.indexOf(userID)).getBasicRemote().sendText(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}
	
	// TODO: Currently does not work with wild cards!!!
	public String takeTurn(String userID, UnoCard.Color color, UnoCard.Value value) {
		String topCard = null;
		String nextPlayer = null;
		String requestSentBy = null;
		String message = null;
		String gameDirection = null;
		String cardToRemove = null;
		
		System.out.println("takeTurn: "+ new UnoCard(color, value).toString());
		System.out.println("currentPlayer: " + playerIDs.get(currentPlayer) + " index="+currentPlayer);
		// Next Player
		if (!this.gameDirection) {
			if (currentPlayer == playerIDs.size()-1)
			{
				currentPlayer = 0;
			} else {
				currentPlayer++;
			}

		} else {
			if (currentPlayer == 0) {
				currentPlayer = playerIDs.size() - 1;
			} else {
				currentPlayer--;
			}
		}
		
		if (value == UnoCard.Value.Wild) {
			this.validColor = color;
			cardToRemove = new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild).toString();
			this.stockPile.add(new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild_Four));
			topCard = new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild).toString();
						
		} else if (value == UnoCard.Value.Wild_Four) {
			this.validColor = color;
			cardToRemove = new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild_Four).toString();
			this.stockPile.add(new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild));
			topCard = new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild_Four).toString();
			this.drawCallBack(playerIDs.get(currentPlayer),4);
						
		} else if (value == UnoCard.Value.DrawTwo) {
			this.validColor = color;
			this.validValue = value;
			this.stockPile.add(new UnoCard(color, value));
			cardToRemove = new UnoCard(color, value).toString();
			topCard = new UnoCard(validColor, validValue).toString();
			drawCallBack(playerIDs.get(currentPlayer), 2);
		} else {
			this.validColor = color;
			this.validValue = value;

			if (UnoCard.Value.Reverse == value) {
				this.gameDirection ^= true;
			} else if (UnoCard.Value.Skip == value) {
				
				if (!this.gameDirection) {
					currentPlayer = (currentPlayer + 1) % playerIDs.size();
				} else {
					if (currentPlayer == 0)
						currentPlayer = playerIDs.size() - 1;
					else
						currentPlayer--;
				}
			}
			
			// Adds placed card to stockpile
			stockPile.add(new UnoCard(color, value));
			cardToRemove = new UnoCard(color, value).toString();
			topCard = new UnoCard(validColor, validValue).toString();
		}
		
		System.out.println("nextPlayer: " + playerIDs.get(currentPlayer) + " index="+currentPlayer);
		
		// CallBack Information
		if (!this.gameDirection) {
			gameDirection = "Foward";
		} else {
			gameDirection = "Backwards";
		}
		
		
		requestSentBy = userID;
		message = "Player " + userID + " put down card";// doesnt specify which one
		nextPlayer = this.playerIDs.get(currentPlayer);
		
		return "{"
				+ "\"type\" : \"content-change\","
				+ "\"contentChangeType\" : \"takeTurnCallback\","
				+ "\"topCard\" : \"" + topCard + "\","
				+ "\"nextPlayer\" : \"" + nextPlayer + "\","
				+ "\"requestSentBy\" : \"" + requestSentBy + "\","
				+ "\"message\" : \"" + message + "\","
				+ "\"gameDirection\" : \"" + gameDirection + "\","
				+ "\"cardToRemoveID\" : \"" + cardToRemove + "\""
				+ "}";

	}

	// Currently does not work with wild cards
	public String uno(String userID, UnoCard.Color color, UnoCard.Value value) {
		if (this.getPlayerHand(userID).size() == 1) {
			String wonGame = "";
			return wonGame; // Sucess
		}
		String errorMessage = "{" + "\"type\" : \"error\"," + "\"message\" : \"You have "
				+ this.getPlayerHand(userID).size() + "cards left\"" + "}";
		// Send message to userplayerID
		int index = playerIDs.indexOf(userID);
		try {
			sessions.get(index).getBasicRemote().sendText(errorMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Game (id=" + gameID + ") could not send error message for uno to player=" + userID);
			e.printStackTrace();
		}

		return null; // Error
	}

	public void processRequest(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject event = (JSONObject) parser.parse(json);
			String action = (String) event.get("action");
			String username = (String) event.get("username");
			String nickname = (String) event.get("nickname");
			String cardColor = (String) event.get("cardColor");
			String cardValue = (String) event.get("cardValue");
			String userID = null;
			if (username == null) {
				userID = nickname;
			} else {
				userID = username;
			}

			String MessageToAllUsers = play(action, userID, cardColor, cardValue);
			if (MessageToAllUsers != null) {
				System.out.println("Game: Sending message to all Users, msg=" + MessageToAllUsers);

				// In the case of multithreading the messaging for turns
				MessageHandler handleTurnMessaging = new MessageHandler(this, MessageToAllUsers);
				handleTurnMessaging.start();
				// broadcastMessage(MessageToAllUsers);
			}
		} catch (ParseException e) {
			System.out.println("Game: Could not processRequest for " + json);
			e.printStackTrace();
		}
	}

	public String play(String action, String userID, String cardColor, String cardValue) {
		Action currentAction = new Action(action, userID, cardColor, cardValue);
		System.out.println("current player: " + playerIDs.get(currentPlayer));
		System.out.println("action: " + action);
		System.out.println("userID: " + userID);
		System.out.println("validplayer: " + checkPlayerTurn(userID));
//		System.out.println("vaildcard: " )
		// Needs to be current player's turn
		// TODO: checkPlayerTurn function faSiling
		if (checkPlayerTurn(userID)) {
			if (action.equals("draw") /* && checkPlayerTurn(userID) */) {
				return submitDraw(userID);
			} else if (isValidCardPlay(new UnoCard(currentAction.getCardColor(), currentAction.getCardValue()))) {
				if (action.equals("makeTurn")) {
					return takeTurn(userID, currentAction.getCardColor(), currentAction.getCardValue());
				} else if (action.equals("uno")) {
					return uno(userID, currentAction.getCardColor(), currentAction.getCardValue());
				}
			}
		} else { // Could not process request
			User user = this.getUserByUsername(userID);
			if (user == null) {
				this.getUserByNickname(userID);
			}
			user.sendMessage("{" + "\"type\" : \"error\","
					+ "\"message\" : \"There was an error processing your request\"" + "}");
			return null;
		}
		return null;
	}

	public int getUserCount() {
		return sessions.size();
	}

	public void run() {
		while (!isGameOver()) {
			while (!actions.isEmpty()) {

			}
		}
	}

	private boolean allReady() {
		for (User u : this.players) {
			if (!u.isReady()) {
				return false;
			}
		}
		return true;
	}

	public User getUserByNickname(String nickname) {
		this.userLookup.lock();
		for (User u : this.players) {
			if (nickname.equals(u.getNickname())) {
				this.userLookup.unlock();
				return u;
			}
		}
		this.userLookup.unlock();
		return null;
	}

	public boolean hasUserByUsername(String username) {
		if (this.getUserByUsername(username) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasUserByNickname(String nickname) {
		if (this.getUserByNickname(nickname) != null) {
			return true;
		} else {
			return false;
		}
	}

	public void setUserReady(String userID) {
		User usr = this.getUserByUsername(userID);
		if (usr == null) {
			usr = this.getUserByNickname(userID);
		}

		if (usr != null) {
			usr.playerReady();
			System.out.println("Game: Set user " + userID + " ready");
			if (allReady()) {
				start();
			}
		} else {
			System.out.println("Game: Could not find user by username or nickname in setUserReady(String userId");
		}
	}

	public void broadcastMessage(String message) {
		for (User u : this.players) {
			u.sendMessage(message);
		}
	}

	/*
	 * Used when joining game
	 */
	public void addUser(User user) {
		players.add(user);
		if (!user.isRegistered()) {
			playerIDs.add(user.getNickname());
		} else {
			playerIDs.add(user.getUsername());
		}
		playerHand.add(new Vector<UnoCard>());
		sessions.add(user.getSession());
		
		// Tell newly added User current player
		String currentPlayerInformation = "{" + 
				"		\"type\" : \"content-change\",\n" + 
				"		\"contentChangeType\" : \"currentPlayer\",\n" + 
				"		\"currentPlayer\" : \"" + playerIDs.get(0) + "\"" + 
				"	}";
		try {
			sessions.get(sessions.size()-1).getBasicRemote().sendText(currentPlayerInformation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Tell other users that new user was added
		String newUserID = user.getUsername();
		String newUserInformation = "{" + "		\"type\" : \"content-change\",\n"
				+ "		\"contentChangeType\" : \"newUser\",\n" + 
				"		\"newUserID\" : \"" + newUserID + "\"" + 
				"	}";
		// Add new User to everyone else
		for (int i = 0; i < sessions.size() - 1; i++) {
			try {
				sessions.get(i).getBasicRemote().sendText(newUserInformation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		// Add everyone else's information to user
		for (int i = 0; i < sessions.size()-1; i++) {
			String playersInformation = "{" + 
					"		\"type\" : \"content-change\",\n" + 
					"		\"contentChangeType\" : \"newUser\",\n" + 
					"		\"newUserID\" : \"" + playerIDs.get(i) + "\"" + 
					"	}";
			try {
				sessions.get(sessions.size()-1).getBasicRemote().sendText(playersInformation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public User getUserByUsername(String username) {
		this.userLookup.lock();
		for (User u : this.players) {
			if (username.equals(u.getUsername())) {
				this.userLookup.unlock();
				return u;
			}
		}
		this.userLookup.unlock();
		return null;
	}
}

class Action {
	private String action;
	private String userID;
	private String cardColor;
	private String cardValue;

	Action(String action, String userID, String cardColor, String cardValue) {
		this.action = action;
		this.userID = userID;
		this.cardColor = cardColor;
		this.cardValue = cardValue;
	}

	public String getAction() {
		return action;
	}

	public String getUserID() {
		return userID;
	}

	public UnoCard.Color getCardColor() {
		if (cardColor.equals("Blue")) {
			return UnoCard.Color.Blue;
		} else if (cardColor.equals("Green")) {
			return UnoCard.Color.Green;
		} else if (cardColor.equals("Red")) {
			return UnoCard.Color.Red;
		} else if (cardColor.equals("Wild")) {
			return UnoCard.Color.Wild;
		} else if (cardColor.equals("Yellow")) {
			return UnoCard.Color.Yellow;
		}
		return null; // Error
	}

	public UnoCard.Value getCardValue() {
		if (cardValue.equals("Zero")) {
			return UnoCard.Value.Zero;
		} else if (cardValue.equals("One")) {
			return UnoCard.Value.One;
		} else if (cardValue.equals("Two")) {
			return UnoCard.Value.Two;
		} else if (cardValue.equals("Three")) {
			return UnoCard.Value.Three;
		} else if (cardValue.equals("Four")) {
			return UnoCard.Value.Four;
		} else if (cardValue.equals("Five")) {
			return UnoCard.Value.Five;
		} else if (cardValue.equals("Six")) {
			return UnoCard.Value.Six;
		} else if (cardValue.equals("Seven")) {
			return UnoCard.Value.Seven;
		} else if (cardValue.equals("Eight")) {
			return UnoCard.Value.Eight;
		} else if (cardValue.equals("Nine")) {
			return UnoCard.Value.Nine;
		} else if (cardValue.equals("Skip")) {
			return UnoCard.Value.Skip;
		} else if (cardValue.equals("Reverse")) {
			return UnoCard.Value.Reverse;
		} else if (cardValue.equals("Wild_Four")) {
			return UnoCard.Value.Wild_Four;
		} else if (cardValue.equals("Wild")) {
			return UnoCard.Value.Wild;
		}
		return null;// error
	}
}
