package teamuno_CSCI201L_GroupProject;

import java.util.Vector;

public class Game {
	private int currentPlayer;
	private Vector<String> playerIDs;
	
	private UnoDeck deck;
	private Vector<Vector<UnoCard>> playerHand;
	private Vector<UnoCard> stockPile;
	
	boolean gameDirection;
	private UnoCard.Value validValue;
	private UnoCard.Color validColor;
	
	public Game(Vector<String> pids) {
		this.deck = new UnoDeck();
		deck.shuffle();
		this.stockPile = new Vector<UnoCard>();
		this.playerIDs = pids;
		this.gameDirection = false; // Clockwise
		this.currentPlayer = 0;
		playerHand = new Vector<Vector<UnoCard>>();
		
		
		for (int i = 0; i < pids.size(); i++)
		{
			Vector<UnoCard> hand = new Vector<UnoCard>(deck.drawCard(7));
			
		}
	}
	
	public void start() {
		UnoCard card = deck.drawCard();
		validColor = card.getColor();
		validValue = card.getValue();
		
		if (card.getValue() == UnoCard.Value.Wild || card.getValue() == UnoCard.Value.Wild_Four || card.getValue() == UnoCard.Value.DrawTwo) {
			start();
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
	
	public void checkPlayerTurn(String pid) throws IllegalArgumentException {
		if (this.playerIDs.get(this.currentPlayer) != pid) {
			throw new IllegalArgumentException("Not player " + pid + "'s turn + [playerID="+ pid + "]");
		}
	}
	public void submitDraw(String pid) throws IllegalArgumentException {
		checkPlayerTurn(pid);
		
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
	}
	
	public void setCardColor(UnoCard.Color color) {
		validColor = color;
	}
}
