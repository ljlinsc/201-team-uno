package teamuno_CSCI201L_GroupProject;

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
	
	public Game(String id) {
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
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void start() {
		
		for (int i = 0; i < playerIDs.size(); i++)
		{
			Vector<UnoCard> hand = new Vector<UnoCard>(deck.drawCard(7));
			
		}
		
		UnoCard card = deck.drawCard();
		validColor = card.getColor();
		validValue = card.getValue();
		
		while (card.getValue() == UnoCard.Value.Wild || card.getValue() == UnoCard.Value.Wild_Four || card.getValue() == UnoCard.Value.DrawTwo) {
			stockPile.add(card);
			card = deck.drawCard();
		}

		if (card.getValue() == UnoCard.Value.Skip)
		{
			if (gameDirection)
			{
				currentPlayer = (currentPlayer + 1)%playerIDs.size();
			}
			else
			{
				if (currentPlayer == 0)
					currentPlayer = playerIDs.size() - 1;
				else
					currentPlayer--;
			}
		}
		
		if (card.getValue() == UnoCard.Value.Reverse)
		{
			gameDirection ^= true;
		}
		
		stockPile.add(card);
		running = true;
	}
	
	public UnoCard getTopCard() {
		return new UnoCard(validColor, validValue);
	}
	
	public String getTopCardImage() {
		return getTopCard().toString() + ".png";
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
	
	//TODO: Fully implement
	public String getPreviousPlayer(int i)
	{
		return "";
	}
	
	public Vector<UnoCard> getPlayerHand(String id)
	{
		int index = playerIDs.indexOf(id);
		return playerHand.get(index);
				
	}
	
	public int getPlayerHandSize(String id) {
		return getPlayerHand(id).size();
	}
	
	public UnoCard getPlayerCard(String id, int choice)
	{
		return getPlayerHand(id).get(choice);
	}
	
	public boolean hasEmptyHand(String id)
	{
		return getPlayerHand(id).isEmpty();
	}
	
	public boolean isValidCardPlay(UnoCard card) {
		return card.getColor() == validColor || card.getValue() == validValue;
	}
	
	public boolean checkPlayerTurn(String pid) {
		if (this.playerIDs.get(this.currentPlayer) != pid) {
			return false;
		}
		return true;
	}
	public String submitDraw(String pid) {
		
		if (deck.isEmpty()) {
			deck.replaceDeckWith(stockPile);
		}
		
		getPlayerHand(pid).add(deck.drawCard());
		if (!gameDirection) {
              currentPlayer = (currentPlayer + 1)%playerIDs.size();
		} else if(gameDirection) {
			if (currentPlayer == 0) {
				currentPlayer = playerIDs.size() - 1;
			} else {
				currentPlayer--;
			}
		}
		// Update user on their own info
		
		
		// Update all other users on info
		return "";
	}
	
	public void setCardColor(UnoCard.Color color) {
		validColor = color;
	}
	
	// Server Specific
	public void addSession(Session session) {
		sessions.add(session);
	}
	
	// Currently does not work with wild cards
	public String takeTurn(String userID, UnoCard.Color color, UnoCard.Value value) {
		String topCard = null;
		String nextPlayer = null;
		String requestSentBy = null;
		String message = null;
		String gameDirection = null;
		
		if (color != UnoCard.Color.Wild) {
			
		}
		else {
			this.validColor = color;
			this.validValue = value;
			
			if (UnoCard.Value.Reverse == value) {
				this.gameDirection ^= true;
			} else if (UnoCard.Value.Skip == value) {
				if (this.gameDirection)
				{
					currentPlayer = (currentPlayer + 1)%playerIDs.size();
				}
				else
				{
					if (currentPlayer == 0)
						currentPlayer = playerIDs.size() - 1;
					else
						currentPlayer--;
				}
			}
			
			
			
			if (!this.gameDirection) {
				gameDirection = "Foward";
			} else {
				gameDirection = "Backwards";
			}
			
			
			requestSentBy = userID;
			message = "Player " + userID + " put down card";// doesnt specify which one
			topCard = new UnoCard(validColor, validValue).toString();
			nextPlayer = this.playerIDs.get(currentPlayer);
		}
		
		return "{"
				+ "\"topCard\" : " + topCard + ","
				+ "\"nextPlayer\" : " + nextPlayer + ","
				+ "\"requestSentBy\" : " + requestSentBy + ","
				+ "\"message\" : " + message + ","
				+ "\"gameDirection\" : " + gameDirection
				+ "}";
		
	}
	
	// Currently does not work with wild cards
	public String uno(String userID, UnoCard.Color color, UnoCard.Value value) {
		if (this.getPlayerHand(userID).size() == 1) {
			return ""; // Sucess
		}
		
		return ""; // Error
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
				System.out.println("Game: Sending message to all Users, msg="+MessageToAllUsers);
			}
		} catch (ParseException e) {
			System.out.println("Game: Could not processRequest for " + json);
			e.printStackTrace();
		}
	}

	
	public String play(String action, String userID, String cardColor, String cardValue) {
		Action currentAction = new Action(action, userID, cardColor, cardValue);
		
		if (isValidCardPlay(new UnoCard(currentAction.getCardColor(), currentAction.getCardValue()))
				&& checkPlayerTurn(userID))
		{
			if (action == "draw") {
				return submitDraw(userID);
			} else if (action == "makeTurn") {
				return takeTurn(userID, currentAction.getCardColor(), currentAction.getCardValue());
			} else if (action == "uno") {
				return uno(userID, currentAction.getCardColor(), currentAction.getCardValue());
			}
		}
		else { // Could not process request
			User user = this.getUserByUsername(userID);
			if (user == null) {
				this.getUserByNickname(userID);
			}
			user.sendMessage("{"
					+ "\"error\" : true,"
					+ "\"message\" : \"There was an error processing your request\""
					+ "}");
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
		for(User u: this.players) {
			if(!u.isReady()) {
				return false;
			}
		}
		return true;
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
	public void setUserReady(String nickname) {
		User usr = this.getUserByNickname(nickname);
		usr.playerReady();
		
	}

	private void broadcastMessage(String message) {
		for(User u: this.players) {
			u.sendMessage(message);
		}
	}
	
	public void addUser(User user) {
		players.add(user);
		if (!user.isRegistered()) {
			playerIDs.add(user.getNickname());
		}
		else {
			playerIDs.add(user.getUsername());
		}
		playerHand.add(new Vector<UnoCard>());
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
		if (cardColor == "Blue") {
			return UnoCard.Color.Blue;
		} else if (cardColor == "Green") {
			return UnoCard.Color.Green;
		} else if (cardColor == "Red") {
			return UnoCard.Color.Red;
		} else if (cardColor == "Wild") {
			return UnoCard.Color.Wild;
		} else if (cardColor == "Yellow") {
			return UnoCard.Color.Yellow;
		}
		return null; // Error
	}
	
	public UnoCard.Value getCardValue() {
		if (cardValue == "1") {
			return UnoCard.Value.One;
		} else if (cardValue == "2") {
			return UnoCard.Value.Two;
		} else if (cardValue == "3") {
			return UnoCard.Value.Three;
		} else if (cardValue == "4") {
			return UnoCard.Value.Four;
		} else if (cardValue == "5") {
			return UnoCard.Value.Five;
		} else if (cardValue == "6") {
			return UnoCard.Value.Six;
		} else if (cardValue == "7") {
			return UnoCard.Value.Seven;
		} else if (cardValue == "8") {
			return UnoCard.Value.Eight;
		} else if (cardValue == "9") {
			return UnoCard.Value.Nine;
		} else if (cardValue == "Skip") {
			return UnoCard.Value.Skip;
		} else if (cardValue == "Reverse") {
			return UnoCard.Value.Reverse;
		} else if (cardValue == "Wild_Four") {
			return UnoCard.Value.Wild_Four;
		} else if (cardValue == "Wild") {
			return UnoCard.Value.Wild;
		}
		return null;//error
	}
}
