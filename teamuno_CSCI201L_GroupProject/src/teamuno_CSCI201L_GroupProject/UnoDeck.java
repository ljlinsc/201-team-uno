package teamuno_CSCI201L_GroupProject;

import java.util.Random;
import java.util.Vector;

/**
 * The UnoDeck class. There are 108 total Uno Cards. There are four suits,
 * Red, Green, Yellow and Blue, each consisting of one 0 card, two 1 cards,
 * two 2s, 3s, 4s, 5d, 6s, 7s, 8s and 9s; two Draw Two cards; two Skip Cards,
 * and two Reverse cards. In addition there are four Wild Cards and Four Wild
 * Draw Four Cards.
 * @author Joshua
 *
 */
public class UnoDeck {
	private Vector<UnoCard> cards;
	private int cardsInDeck;
	
	public UnoDeck() {
		this.cards = new Vector<UnoCard>();
		this.cardsInDeck = 108;
		resetUnoDeck();
	}
	
	private void resetUnoDeck() {
		this.cards = new Vector<UnoCard>();
		UnoCard.Color[] colors = UnoCard.Color.values();
		for (int i = 0; i < colors.length - 1; i++) {
			UnoCard.Color color = colors[i];
			
			// Add 0 card
			cards.add(new UnoCard(color, UnoCard.Value.getValue(0)));
			
			// Add cards 1-9
			for (int j = 0; j < 10; j++) {
				cards.add(new UnoCard(color, UnoCard.Value.getValue(i+1)));
				cards.add(new UnoCard(color, UnoCard.Value.getValue(i+1)));
			}
			
			UnoCard.Value[] values = new UnoCard.Value[] {UnoCard.Value.DrawTwo, UnoCard.Value.Reverse, UnoCard.Value.Skip};
			for (UnoCard.Value value : values) {
				cards.add(new UnoCard(color, value));
				cards.add(new UnoCard(color, value));
			}
			
			
		}
		UnoCard.Value[] values = new UnoCard.Value[] {UnoCard.Value.Wild, UnoCard.Value.Wild_Four};
		
		for (UnoCard.Value value : values) {
			for (int i = 0; i < 4; i++) {
				cards.add(new UnoCard(UnoCard.Color.Wild, value));
			}
		}
		
		this.cardsInDeck = 108;
	}
	
	public void shuffle() {
		int n = cards.size();
		Random random = new Random();
		
		for (int i = 0; i < cards.size(); i++) {
			int randomValue = i + random.nextInt(n - 1);
			UnoCard randomCard = cards.get(randomValue);
			cards.set(randomValue, cards.get(i));
			cards.set(i, randomCard);
		}
	}
	
	public void replaceDeckWith(Vector<UnoCard> cards) {
		this.cards = cards;
	}
	
	public boolean isEmpty() {
		return this.cardsInDeck == 0;
	}
	
	public UnoCard drawCard() throws IllegalArgumentException {
		if (isEmpty()) {
			throw new IllegalArgumentException("Cannot draw card since there are no cards left");
		}
		return cards.get(cardsInDeck--);
	}
	
	public String topCardImage() throws IllegalArgumentException {
		if (isEmpty()) {
			throw new IllegalArgumentException("Cannot draw card image since there are no cards left");
		}
		
		return cards.get(cardsInDeck-1).toString() + ".png";
	}
	
	public Vector<UnoCard> drawCard(int n) throws IllegalArgumentException {
		if (n < 0) {
			throw new IllegalArgumentException("Must draw positive cards"); 
		}
		if (n > cardsInDeck) {
			throw new IllegalArgumentException("Cannot draw " + n + " cards since there are only " + cardsInDeck + " cards in the deck");
		}
		
		Vector<UnoCard> ret = new Vector<UnoCard>();
		
		for (int i = 0; i < n; i++) {
			ret.add(cards.get(--cardsInDeck));
		}
		return ret;
	}
}
