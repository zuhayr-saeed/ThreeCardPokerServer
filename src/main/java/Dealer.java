import java.util.ArrayList;

public class Dealer {
    private Deck deck;
    private ArrayList<Card> hand;

    public Dealer() {
        deck = new Deck();
        hand = new ArrayList<>();
    }

    public void deal(Player playerOne, Player playerTwo) {
        deck.shuffle();
        hand = deck.dealHand(3);
        playerOne.setHand(deck.dealHand(3));
        if (playerTwo != null) {
            playerTwo.setHand(deck.dealHand(3));
        }
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void reset() {
        deck.reset();
        hand.clear();
    }
}
