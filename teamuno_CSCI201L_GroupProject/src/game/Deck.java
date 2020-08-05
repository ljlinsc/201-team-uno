package game;

import java.util.Collections;
import java.util.Vector;

// a deck has 108 cards in total
public class Deck {
	private Vector<Card> deck_vector; //contains all 108 cards
	private Vector<Card> activePile;
	private Vector<Card> discardPile;
	
//	in its constructor, the Deck class should initiate 108 Card.
	public Deck() {
		
//		4 "Wild" cards
		for (int i=0; i<4; i++) {
			Card c = new Card();
			c.setType("WILD");
			deck_vector.add(c);
		}
		
//		4 "Wild +4" cards
		for (int i=0; i<4; i++) {
			Card c = new Card();
			c.setType("WILD+4");
			deck_vector.add(c);
		}
		
		String[] colors = {"RED", "YELLOW", "GREEN", "BLUE"};

//		all NUMBER cards
		for (int i=0; i<10; i++) {
			if (i==0) {
//				number cards of number 0 (1 of each)
				for (int j=0; j<4; j++) {
					Card c = new Card("NUMBER", colors[j], 0);
					deck_vector.add(c);
				}
			}
			else {
//				number cards of number 1-9 (2 of each)
				for (int k=0; k<2; k++) {
					for (int j=0; j<4; j++) {
						Card c = new Card("NUMBER", colors[j], i);
						deck_vector.add(c);
					}
				}
			}
		}
		
//		all function cards
		// 8 SKIP cards
		for (int i=0; i<4; i++) {
			Card c = new Card();
			c.setType("SKIP");
			c.setColor(colors[i]);
			deck_vector.add(c);
			deck_vector.add(c);
		}
		// 8 REVERSE cards		
		for (int i=0; i<4; i++) {
			Card c = new Card();
			c.setType("REVERSE");
			c.setColor(colors[i]);
			deck_vector.add(c);
			deck_vector.add(c);
		}
		// 8 DRAW TWO cards	
		for (int i=0; i<4; i++) {
			Card c = new Card();
			c.setType("Draw+2");
			c.setColor(colors[i]);
			deck_vector.add(c);
			deck_vector.add(c);
		}
		
		Collections.shuffle(deck_vector);
		activePile = deck_vector;
	}
	
	
	
	public boolean deck_isEmpty() {
		return activePile.isEmpty();
	}

	public int numCardLeft() {
		return activePile.size();
	}
	
//	to call this function, first 
	public Card addToPlayers() {
		Card c = activePile.get(0);
		activePile.remove(0);
		return c;
	}
}
