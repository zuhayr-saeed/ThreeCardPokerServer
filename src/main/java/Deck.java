import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        initializeDeck();
    }

    private void initializeDeck() {
        String[] suits = {"H","D","S","C"};
        int[] values = {2,3,4,5,6,7,8,9,10,11,12,13,14};
        cards = new ArrayList<>();
        for (String suit : suits) {
            for (int val : values) {
                cards.add(new Card(val, suit.charAt(0)));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public ArrayList<Card> dealHand(int numberOfCards) {
        ArrayList<Card> hand = new ArrayList<>();
        for(int i=0; i < numberOfCards && !cards.isEmpty(); i++) {
            hand.add(cards.remove(0));
        }
        return hand;
    }

    public void reset() {
        cards.clear();
        initializeDeck();
    }
}
