import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a deck of playing cards.
 */
public class Deck {
    private ArrayList<Card> cards;

    /**
     * Constructs a Deck with 52 standard playing cards.
     */
    public Deck() {
        initializeDeck();
    }

    /**
     * Initializes the deck with 52 standard playing cards.
     */
    private void initializeDeck() {
        String[] suits = {"H", "D", "S", "C"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}; // 11=J, 12=Q, 13=K, 14=A
        cards = new ArrayList<>();

        for (String suit : suits) {
            for (int value : values) {
                cards.add(new Card(value, suit.charAt(0)));
            }
        }
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Deals a hand of specified number of cards.
     *
     * @param numberOfCards The number of cards to deal.
     * @return A list of dealt Cards.
     */
    public ArrayList<Card> dealHand(int numberOfCards) {
        ArrayList<Card> hand = new ArrayList<>();
        for(int i=0; i < numberOfCards && !cards.isEmpty(); i++) {
            hand.add(cards.remove(0));
        }
        return hand;
    }

    /**
     * Resets the deck to a full 52-card deck.
     */
    public void reset() {
        cards.clear();
        initializeDeck();
    }
}