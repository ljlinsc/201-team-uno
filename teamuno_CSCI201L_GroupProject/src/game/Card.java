package game;

//	refer to: https://en.wikipedia.org/wiki/File:UNO_cards_deck.svg
//	to see a deck of UNO cards
	
public class Card {
//	type of cards: number card, Wild, Wild draw 4, skip, reverse, draw 2, 
	private String type; 
	
//	all cards except wild cards have an initial color. 
//	wild cards are assigned a value after they're played
	private String color;
	
//	all number cards have a value
	private int number = -1;
	
//	all cards have the following value:
	private boolean isDealt = false;
	private boolean isPlayed = false;

//	number cards constructor
	public Card(String type, String color, int number) {
		this.type = type;
		this.color = color;
		this.number = number;
	}
	
	public Card() {
	
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isDealt() {
		return isDealt;
	}
	public void setDealt(boolean isDealt) {
		this.isDealt = isDealt;
	}
	public boolean isPlayed() {
		return isPlayed;
	}
	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}
	
	
	
	
}
