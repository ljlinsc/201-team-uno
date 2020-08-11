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
		Vector<UnoCard.Color> colors = new Vector<UnoCard.Color>();
		colors.add(UnoCard.Color.Blue);
		colors.add(UnoCard.Color.Green);
		colors.add(UnoCard.Color.Red);
		colors.add(UnoCard.Color.Yellow);
		
		for (int i = 0; i < colors.size(); i++) {
			UnoCard.Color color = colors.get(i);
			
			// Add 0 card
			cards.add(new UnoCard(color, UnoCard.Value.Zero));
			
			// Add cards 1-9
			UnoCard.Value one = UnoCard.Value.One;
			UnoCard.Value two = UnoCard.Value.Two;
			UnoCard.Value three = UnoCard.Value.Three;
			UnoCard.Value four = UnoCard.Value.Four;
			UnoCard.Value five = UnoCard.Value.Five;
			UnoCard.Value six = UnoCard.Value.Six;
			UnoCard.Value seven = UnoCard.Value.Seven;
			UnoCard.Value eight = UnoCard.Value.Eight;
			UnoCard.Value nine = UnoCard.Value.Nine;

			for (int j = 0; j < 2; j++) {
				cards.add(new UnoCard(color, one));
				cards.add(new UnoCard(color, two));
				cards.add(new UnoCard(color, three));
				cards.add(new UnoCard(color, four));
				cards.add(new UnoCard(color, five));
				cards.add(new UnoCard(color, six));
				cards.add(new UnoCard(color, seven));
				cards.add(new UnoCard(color, eight));
				cards.add(new UnoCard(color, nine));
			}
			
			for (int k = 0; k < 2; k++) {
				cards.add(new UnoCard(color, UnoCard.Value.DrawTwo));
				cards.add(new UnoCard(color, UnoCard.Value.Reverse));
				cards.add(new UnoCard(color, UnoCard.Value.Skip));

			}
			
			
		}
		
		
		UnoCard.Value[] values = new UnoCard.Value[] {UnoCard.Value.Wild, UnoCard.Value.Wild_Four};
		
		for (UnoCard.Value value : values) {
			for (int i = 0; i < 4; i++) {
				cards.add(new UnoCard(UnoCard.Color.Wild, value));
			}
		}
	}
	
	public void shuffle() {      
		int n = cards.size();
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < cards.size(); i++) {
			int randomValue = i + random.nextInt(n - i);
			swap(cards, i, randomValue);
		}
	}
	
	private void swap(Vector<UnoCard> a, int i, int change) {
		UnoCard helper = a.get(i);
		a.set(i, a.get(i));
		a.set(change, helper);
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
